package org.jeepay.pay.channel.tcpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.mq.BaseNotify4MchPay;

import java.net.URL;
import java.net.URLEncoder;

/**
 * @author: aragom
 * @date: 19/1/20
 * @description: 转银行卡通道支付接口
 */
@Service
public class TcpayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(TcpayPaymentService.class);

    @Override
    public String getChannelName() {
        return TcpayConfig.CHANNEL_NAME;
    }

    @Override
    public Long getAmount(PayOrder payOrder) {
        return rpcCommonService.rpcPayOrderService.getAvailableAmount(payOrder, TcpayConfig.PAY_ORDER_TIME_OUT, TcpayConfig.PAY_AMOUNT_INCR_RANGE, TcpayConfig.PAY_AMOUNT_INCR_STEP);
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
            case TcpayConfig.CHANNEL_NAME_ALIPAY_PC :
                retObj = doTfPay4Alipay(payOrder, "pc");
                break;
            case TcpayConfig.CHANNEL_NAME_ALIPAY_WAP :
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
        TcpayConfig tcpayConfig = new TcpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        // 根据子账号配置得到转卡信息，拼接支付宝转账银行卡URL
        String cardNo = tcpayConfig.getCardNo();            // 银行卡号
        String bankAccount = tcpayConfig.getBankAccount();  // 银行账号姓名
        String bankName = tcpayConfig.getBankName();        // 银行名称
        String bankMark = tcpayConfig.getBankMark();        // 银行代码
        String cardIndex = tcpayConfig.getCardIndex();      // 卡ID
        try {
            // 支付宝转卡URL：https://www.alipay.com/?appId=09999988&actionType=toCard&sourceId=bill&cardNo=6230200015683472&bankAccount=丁志伟&money=0.8&amount=0.8&bankMark=HXBANK&bankName=华夏银行
            // 支付宝转卡URL(隐藏卡号)：https://www.alipay.com/?appId=09999988&actionType=toCard&sourceId=bill&cardNo=6230200015683472&bankAccount=丁志伟&money=0.8&amount=0.8&bankMark=HXBANK&bankName=华夏银行&cardIndex=1901211891833778390&cardNoHidden=true&cardChannel=HISTORY_CARD&orderSource=from
//alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill&cardNo=621799***0341990&bankAccount=廖期云&amount=299.97&bankMark=PSBC&bankName=中国民生银行&cardIndex=1904291176817883909&cardNoHidden=true&cardChannel=HISTORY_CARD&orderSource=from
            String tcUrl = "alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill&cardNo=%s&bankAccount=%s&amount=%s&bankMark=%s&bankName=%s&cardIndex=%s&cardNoHidden=true&cardChannel=HISTORY_CARD&orderSource=from";
            String amt = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");
            tcUrl = String.format(tcUrl ,
                    getHideCardNo(cardNo), bankAccount, amt,  bankMark, bankName, cardIndex);
//            if(StringUtils.isNotEmpty(cardIndex)) {
//                // 隐藏卡号
//                tcUrl = String.format(tcUrl + "&cardIndex=%s&cardNoHidden=true&cardChannel=HISTORY_CARD&orderSource=from",
//                        getHideCardNo(cardNo), bankAccount, amt, amt, bankMark, bankName, cardIndex);
//            }else {
//                tcUrl = String.format(tcUrl,
//                        cardNo, bankAccount, amt, amt, bankMark, bankName);
//            }
         //   int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateParam1(payOrder.getPayOrderId(),amt+"/"+alipayUid+"/"+alipayName+"/"+alipayAccount);

            _log.info("[{}]生成支付宝转银行卡URL={}", getChannelName(), tcUrl);
            int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},result={}", getChannelName(), payOrder.getPayOrderId(), updateCount);

            String codeImgUrl = payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(tcUrl) + "&widht=200&height=200";
            StringBuffer payForm = new StringBuffer();
            String toPayUrl = payConfig.getPayUrl() + "/"+TcpayConfig.CHANNEL_NAME+"/pay_"+type+".htm";
            payForm.append("<form style=\"display: none\" action=\""+toPayUrl+"\" method=\"post\">");
            payForm.append("<input name=\"mchOrderNo\" value=\""+payOrder.getMchOrderNo()+"\" >");
            payForm.append("<input name=\"payOrderId\" value=\""+payOrder.getPayOrderId()+"\" >");
            payForm.append("<input name=\"amount\" value=\""+payOrder.getAmount()+"\" >");
            payForm.append("<input name=\"codeImgUrl\" value=\""+codeImgUrl+"\" >");
            payForm.append("<input name=\"codeUrl\" value=\""+tcUrl+"\" >");
            payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
            payForm.append("</form>");
            payForm.append("<script>document.forms[0].submit();</script>");

            // 支付链接地址
            retObj.put("payOrderId", payOrder.getPayOrderId()); // 设置支付订单ID
            JSONObject payParams = new JSONObject();
            payParams.put("payUrl", payForm);
            String payJumpUrl = toPayUrl + "?mchOrderNo=" + payOrder.getMchOrderNo() + "&payOrderId=" + payOrder.getPayOrderId() +
                    "&amount=" + payOrder.getAmount();
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

    /**
     * 获取隐藏卡号，保留前6位+"****"+后4位
     * @param cardNo
     * @return
     */
    private String getHideCardNo(String cardNo) {
        return cardNo.substring(0, 6) + "****" + cardNo.substring(cardNo.length()-4);
    }

    public static void main(String[] args) {

        String cardNo = "6230200015683472";
        cardNo = cardNo.substring(0, 6) + "****" + cardNo.substring(cardNo.length()-4);
        System.out.println(cardNo);

    }

}
