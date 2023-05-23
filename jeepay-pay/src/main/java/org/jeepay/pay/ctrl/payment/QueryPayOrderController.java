package org.jeepay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchApp;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.PayOrderService;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 支付订单查询
 * @author aragom qq194088539
 * @date 2017-08-31
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
@RestController
public class QueryPayOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(QueryPayOrderController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询支付订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/pay/query_order")
    public String queryPayOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户查询支付订单请求 ######");
        String logPrefix = "【商户支付订单查询】";
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
            String mchOrderNo = po.getString("mchOrderNo"); 	// 商户订单号
            String payOrderId = po.getString("payOrderId"); 	// 支付订单号
            Boolean executeNotify = po.getBooleanValue("executeNotify");   // 是否执行回调
            PayOrder payOrder = payOrderService.query(mchId, payOrderId, mchOrderNo, executeNotify);
            _log.info("{}查询支付订单,结果:{}", logPrefix, payOrder);
            if (payOrder == null) {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付订单不存在", null, null));
            }
            Map<String, Object> map = buildRetMap(payOrder);
            _log.info("###### 商户查询订单处理完成 ######");
            return JEEPayUtil.makeRetData(map, payContext.getString("key"));
        }catch (Exception e) {
            _log.error(e, "");
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, null));
        }
    }

    /**
     * 构建返回Map
     * @param payOrder
     * @return
     */
    Map buildRetMap(PayOrder payOrder) {
        Map<String, Object> map = new HashedMap();
        map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        map.put("mchId", StrUtil.toString(payOrder.getMchId()));
        map.put("appId", StrUtil.toString(payOrder.getAppId()));
        map.put("productId", StrUtil.toString(payOrder.getProductId()));
        map.put("payOrderId", StrUtil.toString(payOrder.getPayOrderId()));
        map.put("mchOrderNo", StrUtil.toString(payOrder.getMchOrderNo()));
        map.put("amount", StrUtil.toString(payOrder.getAmount()));
        map.put("currency", StrUtil.toString(payOrder.getCurrency()));
        map.put("status", StrUtil.toString(payOrder.getStatus()));
        map.put("channelUser", StrUtil.toString(payOrder.getChannelUser()));
        map.put("channelOrderNo", StrUtil.toString(payOrder.getChannelOrderNo()));
        map.put("channelAttach", "".equals(StrUtil.toString(payOrder.getChannelAttach())) ? "" : JSONObject.parse(payOrder.getChannelAttach()));
        map.put("paySuccTime", "".equals(StrUtil.toString(payOrder.getPaySuccTime())) ? "" : payOrder.getPaySuccTime().getTime());
        return map;
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String appId = params.getString("appId");               // 应用ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String payOrderId = params.getString("payOrderId"); 	// 支付订单号

        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        if(StringUtils.isBlank(mchOrderNo) && StringUtils.isBlank(payOrderId)) {
            errorMessage = "request params[mchOrderNo or payOrderId] error.";
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
