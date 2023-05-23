package org.jeepay.pay.channel.gepay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.PayDigestUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description: 个付通支付回调
 */
@Service
public class GepayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(GepayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return GepayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理个付通支付回调】";
        _log.info("====== 开始处理个付通支付回调通知 ======");
        HttpServletRequest request = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = GepayConfig.RETURN_VALUE_FAIL;
        try {
            JSONObject params = new JSONObject();
            params.put("userId", request.getParameter("userId"));
            params.put("money", request.getParameter("money"));
            params.put("outTradeNo", request.getParameter("outTradeNo"));
            params.put("sign", request.getParameter("sign"));
            payContext.put("parameters", params);
            if(!verifyPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, GepayConfig.RETURN_VALUE_FAIL);
                return retObj;
            }
            payOrder = (PayOrder) payContext.get("payOrder");
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理个付通支付回调通知 ======");
            respString = GepayConfig.RETURN_VALUE_SUCCESS;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, respString);
        return retObj;
    }

    /**
     * 验证个付通支付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, Object> payContext) {
        JSONObject params = (JSONObject) payContext.get("parameters");
        // 校验结果是否成功
        String userId = params.getString("userId");             // 商户ID
        String money = params.getString("money");               // 支付金额
        String outTradeNo = params.getString("outTradeNo");		// 商户订单号
        String sign = params.getString("sign"); 		        // 签名

        // 查询payOrder记录
        String payOrderId = outTradeNo;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        GepayConfig gepayConfig = new GepayConfig(getPayParam(payOrder));
        // 验证签名
        Map signMap = new HashMap<>();
        signMap.put("userId", userId);
        signMap.put("money", money);
        signMap.put("outTradeNo", outTradeNo);
        String signValue = PayDigestUtil.getSign(signMap, gepayConfig.getKey());
        if(!sign.equals(signValue)) {
            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = new BigDecimal(money).multiply(new BigDecimal(100)).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", money, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
