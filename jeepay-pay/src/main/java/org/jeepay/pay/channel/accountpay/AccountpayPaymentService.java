package org.jeepay.pay.channel.accountpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.mq.BaseNotify4MchPay;

/**
 * @author: aragom
 * @date: 2018/4/5
 * @description: 账户支付
 */
@Service
public class AccountpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(AccountpayPaymentService.class);

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ACCOUNTPAY;
    }

    @Override
    public JSONObject pay(PayOrder payOrder) {
        String logPrefix = "【账户支付】";
        JSONObject retObj = new JSONObject();
        // 将订单更改为支付中
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);

        String userId = payOrder.getChannelUser();
        Long mchId = payOrder.getMchId();
        Long amount = payOrder.getAmount();
        // 扣账户余额
        result = rpcCommonService.rpcUserAccountService.userAccountRollOut(mchId, userId, amount);
        _log.info("{}userId={},mchId={},amount={},结果:{}", logPrefix, userId, mchId, amount, result);

        // 扣款成功
        if(result == 1){
            result = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
            _log.info("[{}]更新订单状态为支付成功:payOrderId={},result={}", getChannelName(), payOrder.getPayOrderId(), result);
            retObj.put(PayConstant.RESULT_PARAM_RESCODE, PayConstant.RESULT_VALUE_SUCCESS);
            // 发送商户支付成功通知
            if (result == 1) {
                // 通知业务系统
                try {
					baseNotify4MchPay.doNotify(payOrder, true);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }else {
            // 明确为失败,可修改订单状态为失败
            retObj.put(PayConstant.RESULT_PARAM_RESCODE, PayConstant.RESULT_VALUE_FAIL);
            retObj.put("errDes", "扣余额失败");
            retObj.put("errCode", "");
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        return retObj;

    }

    @Override
    public JSONObject query(PayOrder payOrder) {
        return null;
    }

    @Override
    public JSONObject close(PayOrder payOrder) {
        return null;
    }
}
