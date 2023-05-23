package org.jeepay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchApp;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.RefundOrder;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.mq.BaseNotify4MchRefund;
import org.jeepay.pay.mq.Mq4MchPayNotify;
import org.jeepay.pay.mq.Mq4MchRefundNotify;
import org.jeepay.pay.service.PayOrderService;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 退款订单查询
 * @author aragom qq194088539
 * @date 2014-04-16
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
@RestController
public class QueryRefundOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(QueryRefundOrderController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private BaseNotify4MchRefund baseNotify4MchRefund;

    /**
     * 查询退款订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/refund/query_order")
    public String queryPayOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户查询退款订单请求 ######");
        String logPrefix = "【商户退款订单查询】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject payContext = new JSONObject();
            // 验证参数有效性
            String errorMessage = validateParams(po, payContext);
            if (!"success".equalsIgnoreCase(errorMessage)) {
                _log.warn(errorMessage);
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, errorMessage, null, null));
            }
            _log.debug("请求参数及签名校验通过");
            Long mchId = po.getLong("mchId"); 			        // 商户ID
            String mchRefundNo = po.getString("mchRefundNo"); 	    // 商户退款单号
            String refundOrderId = po.getString("refundOrderId"); 	// 退款单号
            Boolean executeNotify = po.getBooleanValue("executeNotify");   // 是否执行回调

            RefundOrder refundOrder = null;
            if(StringUtils.isNotBlank(mchRefundNo)) {
                refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndMchRefundNo(mchId, mchRefundNo);
            }else if(StringUtils.isNotBlank(refundOrderId)) {
                refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndRefundOrderId(mchId, refundOrderId);
            }
            _log.info("{}查询退款订单,结果:{}", logPrefix, refundOrder);
            if (refundOrder == null) {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "退款订单不存在", null, null));
            }
            if(executeNotify &&
                    StringUtils.isNotBlank(refundOrder.getNotifyUrl()) &&
                    (PayConstant.REFUND_RESULT_SUCCESS == refundOrder.getStatus() || PayConstant.REFUND_STATUS_COMPLETE == refundOrder.getStatus()) ) {
                baseNotify4MchRefund.doNotify(refundOrder, false);
                _log.info("业务查单完成,并再次发送业务退款通知.发送完成");
            }

            Map<String, Object> map = buildRetMap(refundOrder);
            _log.info("###### 商户查询订单处理完成 ######");
            return JEEPayUtil.makeRetData(map, payContext.getString("key"));
        }catch (Exception e) {
            _log.error(e, "");
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, null));
        }
    }

    /**
     * 构建返回Map
     * @param refundOrder
     * @return
     */
    Map buildRetMap(RefundOrder refundOrder) {
        Map<String, Object> map = new HashedMap();
        map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        map.put("mchId", StrUtil.toString(refundOrder.getMchId()));
        map.put("appId", StrUtil.toString(refundOrder.getAppId()));
        map.put("mchRefundNo", StrUtil.toString(refundOrder.getMchRefundNo()));
        map.put("refundAmount", StrUtil.toString(refundOrder.getRefundAmount()));
        map.put("status", StrUtil.toString(refundOrder.getStatus()));
        map.put("channelOrderNo", StrUtil.toString(refundOrder.getChannelOrderNo()));
        map.put("refundSuccTime", "".equals(StrUtil.toString(refundOrder.getRefundSuccTime())) ? "" : refundOrder.getRefundSuccTime().getTime());
        return map;
    }

    /**
     * 验证请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String appId = params.getString("appId");               // 应用ID
        String mchRefundNo = params.getString("mchRefundNo"); 	    // 商户退款单号
        String refundOrderId = params.getString("refundOrderId"); 	// 退款订单号

        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);
        if(StringUtils.isBlank(mchRefundNo) && StringUtils.isBlank(refundOrderId)) {
            errorMessage = "request params[mchRefundNo or refundOrderId] error.";
            return errorMessage;
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "request params[sign] error.";
            return errorMessage;
        }

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "Can't found mchInfo[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "mchInfo not available [mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        // 查询应用信息
        if(StringUtils.isNotBlank(appId)) {
            MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchIdL, appId);
            if(mchApp == null) {
                errorMessage = "Can't found app[appId="+appId+"] record in db.";
                return errorMessage;
            }
            if(mchApp.getStatus() != MchConstant.PUB_YES) {
                errorMessage = "app not available [appId="+appId+"] record in db.";
                return errorMessage;
            }
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "key is null[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        payContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = JEEPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "Verify XX pay sign failed.";
            return errorMessage;
        }

        return "success";
    }

}
