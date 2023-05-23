package org.jeepay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.PayEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.MySeq;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchApp;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.RefundOrder;
import org.jeepay.pay.channel.RefundInterface;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.PayOrderService;
import org.jeepay.pay.service.RefundOrderService;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 退款
 * @author aragom qq194088539
 * @date 2017-10-30
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
@RestController
public class RefundOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(RefundOrderController.class);

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 统一退款接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/refund/create_order")
    public String payOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户统一退款请求 ######");
        String logPrefix = "【商户统一退款】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject refundContext = new JSONObject();
            RefundOrder refundOrder = null;
            // 验证参数有效性
            Object object = validateParams(po, refundContext);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
            }
            if (object instanceof RefundOrder) refundOrder = (RefundOrder) object;
            if(refundOrder == null) return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心退款失败", null, null));
            int result = rpcCommonService.rpcRefundOrderService.createRefundOrder(refundOrder);
            _log.info("{}创建退款订单,结果:{}", logPrefix, result);
            if(result != 1) {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建退款订单失败", null, null));
            }

            Map<String, Object> map = JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            String refundOrderId = refundOrder.getRefundOrderId();
            String channelType = refundContext.getString("channelType");

            // 是否异步退款
            boolean async = refundContext.getBooleanValue("async");

            if(async) {
                // 发送异步退款消息
                refundOrderService.sendRefundNotify(refundOrderId, channelType);
                _log.info("{}发送退款任务完成,refundOrderId={}", logPrefix, refundOrderId);
                // 返回的参数
                map.put("refundOrderId", refundOrder.getRefundOrderId());
                map.put("mchId", refundOrder.getMchId());
                map.put("appId", refundOrder.getAppId());
                map.put("refundAmount", refundOrder.getRefundAmount());
                map.put("channelId", refundOrder.getChannelId());
                return JEEPayUtil.makeRetData(map, refundContext.getString("key"));
            }else {

                // 修改转账状态为退款中
                result = rpcCommonService.rpcRefundOrderService.updateStatus4Ing(refundOrderId, "");
                if(result != 1) {
                    _log.warn("更改退款为退款中({})失败,不能退款.refundOrderId={}", PayConstant.REFUND_STATUS_REFUNDING, refundOrderId);
                    return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心退款失败", null, PayEnum.ERR_0120));
                }

                JSONObject retObj;
                try{
                    RefundInterface refundInterface = (RefundInterface) SpringUtil.getBean(channelType + "RefundService");
                    retObj = refundInterface.refund(refundOrder);
                }catch (BeansException e) {
                    _log.warn("不支持的退款渠道,停止退款处理.refundOrderId={},channelType={}", refundOrderId, channelType);
                    return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心退款失败", null, PayEnum.ERR_0119));
                }

                if(PayConstant.retIsSuccess(retObj) && retObj.getBooleanValue("isSuccess")) {
                    // 更新退款状态为成功
                    // TODO 考虑事务内完成
                    String channelOrderNo = retObj.getString("channelOrderNo");
                    result = rpcCommonService.rpcRefundOrderService.updateStatus4Success(refundOrderId, channelOrderNo);
                    _log.info("更新退款订单状态为成功({}),refundOrderId={},返回结果:{}", PayConstant.REFUND_STATUS_SUCCESS, refundOrderId, result);

                    PayOrder updatePayOrder = new PayOrder();
                    updatePayOrder.setPayOrderId(refundOrder.getPayOrderId());
                    updatePayOrder.setStatus(PayConstant.PAY_STATUS_REFUND);    // 状态修改为已退款
                    updatePayOrder.setIsRefund(MchConstant.PUB_YES);
                    updatePayOrder.setRefundTimes(1);
                    updatePayOrder.setSuccessRefundAmount(refundOrder.getRefundAmount());
                    result = rpcCommonService.rpcPayOrderService.updateByPayOrderId(refundOrder.getPayOrderId(), updatePayOrder);
                    _log.info("更新支付订单退款信息,payOrderId={},返回结果:{}", refundOrder.getPayOrderId(), result);
                }else {
                    // 更新退款状态为失败
                    String channelErrCode = retObj.getString("channelErrCode");
                    String channelErrMsg = retObj.getString("channelErrMsg");
                    result = rpcCommonService.rpcRefundOrderService.updateStatus4Fail(refundOrderId, channelErrCode, channelErrMsg);
                    _log.info("更新退款订单状态为失败({}),refundOrderId={},返回结果:{}", PayConstant.REFUND_STATUS_FAIL, refundOrderId, result);
                }

                RefundOrder order = rpcCommonService.rpcRefundOrderService.findByRefundOrderId(refundOrderId);
                // 返回的参数
                map.put("mchId", order.getMchId());
                map.put("appId", order.getAppId());
                map.put("refundOrderId", order.getRefundOrderId());
                map.put("mchRefundNo", order.getMchRefundNo());
                map.put("refundAmount", order.getRefundAmount());
                map.put("status", order.getStatus());
                map.put("channelOrderNo", order.getChannelOrderNo());
                map.put("refundSuccTime", order.getRefundSuccTime() == null ? "" : order.getRefundSuccTime().getTime());
                return JEEPayUtil.makeRetData(map, refundContext.getString("key"));
            }
        }catch (Exception e) {
            _log.error(e, "");
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, null));
        }
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject refundContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String appId = params.getString("appId");               // 应用ID
        String payOrderId = params.getString("payOrderId");     // 支付订单号
        String mchOrderNo = params.getString("mchOrderNo");     // 商户支付单号
        String mchRefundNo = params.getString("mchRefundNo"); 	// 商户退款单号
        String amount = params.getString("amount"); 		    // 退款金额（单位分）
        String currency = params.getString("currency");         // 币种
        String clientIp = params.getString("clientIp");	        // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String notifyUrl = params.getString("notifyUrl"); 		// 退款结果回调URL,如果填写则退款结果会通过该url通知
        String sign = params.getString("sign"); 				// 签名
        String channelUser = params.getString("channelUser");	// 渠道用户标识,如微信openId,支付宝账号
        String userName = params.getString("userName");	        // 用户姓名
        String remarkInfo = params.getString("remarkInfo");	    // 备注
        // 验证请求参数有效性（必选项）
        if(!NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        Long mchIdL = Long.parseLong(mchId);
        if(StringUtils.isBlank(payOrderId) && StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "request params[payOrderId,mchOrderNo] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(mchRefundNo)) {
            errorMessage = "request params[mchRefundNo] error.";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "request params[amount] error.";
            return errorMessage;
        }
        Long refundAmount = Long.parseLong(amount); // 退款金额
        if(StringUtils.isBlank(currency)) {
            errorMessage = "request params[currency] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(channelUser)) {
            errorMessage = "request params[channelUser] error.";
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
        refundContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = JEEPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "Verify XX refund sign failed.";
            return errorMessage;
        }

        // 验证支付订单是否存在
        PayOrder payOrder = payOrderService.query(Long.parseLong(mchId), payOrderId, mchOrderNo);
        if(payOrder == null) {
            errorMessage = "payOrder is not exist.";
            return errorMessage;
        }
        // 订单必须是成功或处理完成状态才可以退款
        if(payOrder.getStatus() != PayConstant.PAY_STATUS_SUCCESS && payOrder.getStatus() != PayConstant.PAY_STATUS_COMPLETE) {
            errorMessage = "payOrder can not refund.";
            return errorMessage;
        }

        refundContext.put("channelType", payOrder.getChannelType());
        // 如果已经退款,不能再次发起
        if(MchConstant.PUB_YES == payOrder.getIsRefund() &&
                (payOrder.getSuccessRefundAmount() != null && payOrder.getSuccessRefundAmount() == payOrder.getAmount())) {
            errorMessage = "payOrder already refunds.";
            return errorMessage;
        }

        String channelPayOrderNo = payOrder.getChannelOrderNo();    // 渠道测支付单号
        Long payAmount = payOrder.getAmount();  // 全额退款
        if(refundAmount.longValue() != payAmount) {
            errorMessage = "amount not equals payAmount";
            return errorMessage;
        }

        // 如果通知地址不为空,则为异步退款
        if(StringUtils.isNotBlank(notifyUrl)){
            refundContext.put("async", true);
        }

        // 验证参数通过,返回退款对象
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setRefundOrderId(MySeq.getRefund());
        refundOrder.setPayOrderId(payOrderId);
        refundOrder.setChannelPayOrderNo(channelPayOrderNo);
        refundOrder.setMchId(mchIdL);
        refundOrder.setMchType(mchInfo.getType());
        refundOrder.setAppId(appId);
        refundOrder.setMchRefundNo(mchRefundNo);
        refundOrder.setChannelType(payOrder.getChannelType());
        refundOrder.setChannelId(payOrder.getChannelId());
        refundOrder.setPassageId(payOrder.getPassageId());
        refundOrder.setPayAmount(payAmount);
        refundOrder.setRefundAmount(refundAmount);
        refundOrder.setCurrency(currency);
        refundOrder.setClientIp(clientIp);
        refundOrder.setDevice(device);
        refundOrder.setChannelUser(channelUser);
        refundOrder.setUserName(userName);
        refundOrder.setRemarkInfo(remarkInfo);
        refundOrder.setExtra(extra);
        refundOrder.setChannelMchId(payOrder.getChannelMchId());
        refundOrder.setParam1(param1);
        refundOrder.setParam2(param2);
        refundOrder.setNotifyUrl(notifyUrl);
        return refundOrder;
    }

}
