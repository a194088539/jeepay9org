package org.jeepay.pay.channel.hanyinpay;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.redpay.RedpayConfig;
import org.jeepay.pay.channel.redpay.RedpayPayNotifyService;
import org.jeepay.pay.channel.swiftpay.util.XmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class HanyinpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(RedpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return HanyinConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理"+getChannelName()+"支付回调】";
        _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = new JSONObject();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String resString = XmlUtils.parseRequst(req);
        _log.info("{}异步通知内容：{}", logPrefix, resString);

        if(resString != null && !"".equals(resString)) {
            Map<String, String> params = null;
            try {
                params = XmlUtils.toMap(resString.getBytes(), "utf-8");
            } catch (Exception e) {
                _log.error(e, logPrefix + "处理异常");
            }
            payContext.put("parameters", params);
            if (!verifyPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_SWIFTPAY_VALUE_FAIL);
                return retObj;
            }

            payOrder = (PayOrder) payContext.get("payOrder");

            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotifys(payOrder, true);
            _log.info("====== 完成处理"+getChannelName()+"支付回调通知 ======");
            // 返回上游信息
            retObj.put(PayConstant.RESPONSE_RESULT, HanyinConfig.RESPONSE_RESULT_SUCCESS);
            return retObj;
        }
        return null;
    }

    public boolean verifyPayParams(Map<String, Object> payContext) {
        JSONObject params = (JSONObject) payContext.get("parameters");
        // 校验结果是否成功
        String orderNo = params.getString("orderNo");            // 订单号
        String transAmt = params.getString("transAmt");          // 支付金额
        String serialId = params.getString("serialId");         // 平台产生的交易流水号
        String type = params.getString("type");                 // 支付类型
        String signature = params.getString("signature"); 		         // 签名

        // 查询payOrder记录
        String payOrderId = orderNo;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        RedpayConfig redpayConfig = new RedpayConfig(getPayParam(payOrder));
        // 验证签名
        StringBuffer sb = new StringBuffer();
        String signValue = MD5Util.string2MD5(sb.toString());
        if(!signature.equals(signValue)) {
            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = Long.valueOf(transAmt);
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", transAmt, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
