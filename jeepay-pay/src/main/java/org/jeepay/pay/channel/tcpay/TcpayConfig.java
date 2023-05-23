package org.jeepay.pay.channel.tcpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: aragom
 * @date: 19/1/20
 * @description: 转卡支付通道（如支付宝转银行卡）
 */
@Component
public class TcpayConfig {

    public static final String CHANNEL_NAME = "tcpay";
    public static final String CHANNEL_NAME_ALIPAY_PC = CHANNEL_NAME + "_alipay_pc";
    public static final String CHANNEL_NAME_ALIPAY_WAP = CHANNEL_NAME + "_alipay_wap";
    public static final String RETURN_VALUE_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";
    public static final Long PAY_ORDER_TIME_OUT = 30 * 60l;        // 订单超时时间，单位秒
    public static final Long PAY_AMOUNT_INCR_RANGE = 20l;         // 订单金额增加的范围
    public static final Long PAY_AMOUNT_INCR_STEP = 1L;           // 订单金额增加的步长
    public static final String SIGN_KEY = "ABCD1234567890";       // 签名key
    public static final String RESPONSE_RESULT_SUCCESS = "success"; // 返回上游成功
    public static final String RESPONSE_RESULT_FAIL = "fail";       // 返回上游失败

    // 银行卡号
    private String cardNo;
    // 银行账户名
    private String bankAccount;
    // 银行名称
    private String bankName;
    // 银行代码
    private String bankMark;
    // 支付宝卡ID,隐藏卡号使用
    private String cardIndex;

    public TcpayConfig(){}

    public TcpayConfig(String payParam) {
        Assert.notNull(payParam, "init tcpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.cardNo = object.getString("cardNo");
        this.bankAccount = object.getString("bankAccount");
        this.bankName = object.getString("bankName");
        this.bankMark = object.getString("bankMark");
        this.cardIndex = object.getString("cardIndex");
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankMark() {
        return bankMark;
    }

    public void setBankMark(String bankMark) {
        this.bankMark = bankMark;
    }

    public String getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(String cardIndex) {
        this.cardIndex = cardIndex;
    }
}
