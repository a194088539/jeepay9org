package org.jeepay.pay.channel.redpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.MySeq;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.mq.BaseNotify4MchPay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: aragom
 * @date: 19/1/20
 * @description: 转银行卡通道支付接口
 */
@Service
public class RedpayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(RedpayPaymentService.class);

    private static AtomicLong pay_seq = new AtomicLong(0L);

    @Override
    public String getChannelName() {
        return RedpayConfig.CHANNEL_NAME;
    }

    @Override
    public String getOrderId(PayOrder payOrder) {
        SimpleDateFormat fm = new SimpleDateFormat("yyMMddHHmmss");
        return String.format("P%sR%04d", fm.format(new Date()), pay_seq.getAndIncrement() % 10000);
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case RedpayConfig.CHANNEL_NAME_ALIPAY_PC :
                retObj = doTfPay4Alipay(payOrder, "pc");
                break;
            case RedpayConfig.CHANNEL_NAME_ALIPAY_H5 :
                retObj = doTfPay4Alipay(payOrder, "wap");
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject query(PayOrder payOrder) {
        // 上游没有查询接口，直接返回
        JSONObject retObj = buildRetObj();
        retObj.put("status", 1);    // 支付中
        return retObj;
    }

    /**
     * 支付宝支付(跳转页面,显示二维码)
     * @param payOrder
     * @param type
     * @return
     */
    public JSONObject doTfPay4Alipay(PayOrder payOrder, String type) {
        RedpayConfig redpayConfig = new RedpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        // 根据子账号配置得到转卡信息，拼接支付宝转账银行卡URL
        String alipayUserId = redpayConfig.getAlipayUserId();     // 支付宝用户ID
        String alipayAccount = redpayConfig.getAlipayAccount();   // 支付宝账号
        String amt = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");
        try {
            String toUrl = payConfig.getPayUrl() + "/%s/alipay_red.htm?alipayUserId=%s&alipayAccount=%s&amount=%s&payOrderId=%s";
            toUrl = String.format(toUrl, RedpayConfig.CHANNEL_NAME, alipayUserId, alipayAccount, amt, payOrder.getPayOrderId());
            _log.info("[{}]生成红包URL={}", getChannelName(), toUrl);
            int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},result={}", getChannelName(), payOrder.getPayOrderId(), updateCount);

            String codeImgUrl = payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(toUrl) + "&widht=200&height=200";
            StringBuffer payForm = new StringBuffer();
            String toPayUrl = payConfig.getPayUrl() + "/" + RedpayConfig.CHANNEL_NAME + "/pay_" + type + ".htm";
            Boolean autoJump = redpayConfig.getAutoJump();
            payForm.append("<form style=\"display: none\" action=\""+toPayUrl+"\" method=\"post\">");
            payForm.append("<input name=\"mchOrderNo\" value=\""+payOrder.getMchOrderNo()+"\" >");
            payForm.append("<input name=\"payOrderId\" value=\""+payOrder.getPayOrderId()+"\" >");
            payForm.append("<input name=\"amount\" value=\""+payOrder.getAmount()+"\" >");
            payForm.append("<input name=\"codeImgUrl\" value=\""+codeImgUrl+"\" >");
            payForm.append("<input name=\"codeUrl\" value=\""+toUrl+"\" >");
            payForm.append("<input name=\"autoJump\" value=\""+autoJump+"\" >");
            payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
            payForm.append("</form>");
            payForm.append("<script>document.forms[0].submit();</script>");

            // 支付链接地址
            retObj.put("payOrderId", payOrder.getPayOrderId()); // 设置支付订单ID
            JSONObject payParams = new JSONObject();
            payParams.put("payUrl", payForm);
            String payJumpUrl = toPayUrl + "?mchOrderNo=" + payOrder.getMchOrderNo() + "&payOrderId=" + payOrder.getPayOrderId() +
                    "&amount=" + payOrder.getAmount() + "&codeImgUrl=" + URLEncoder.encode(codeImgUrl) + "&codeUrl=" + URLEncoder.encode(toUrl) +
                    "&autoJump=" + autoJump;
            payParams.put("payJumpUrl", payJumpUrl);
            payParams.put("payMethod", PayConstant.PAY_METHOD_FORM_JUMP);
            retObj.put("payParams", payParams);
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            return retObj;
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }

    public static void main(String[] args) {

        String cardNo = "6230200015683472";
        cardNo = cardNo.substring(0, 6) + "****" + cardNo.substring(cardNo.length()-4);
        System.out.println(cardNo);

    }

}
