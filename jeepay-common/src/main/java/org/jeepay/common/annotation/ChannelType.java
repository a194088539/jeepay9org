package org.jeepay.common.annotation;

/**
 * 渠道类型
 */
public enum ChannelType {
    /**
     * 支付宝
     */
    ALIPAY("ALIPAY"),
    /**
     * 富友支付
     */
    FUIOU("FUIOU"),
    /**
     * payoneer支付
     */
    PAYONEER("PAYONEER"),
    /**
     * paypal支付
     */
    PAYPAL("PAYPAL"),
    /**
     * 银联支付
     */
    UNION("UNION"),
    /**
     * 微信支付
     */
    WECHAT("WECHAT"),
    /**
     * 友店微信支付
     */
    YOUDIAN("YOUDIAN"),
    /**
     * 先锋云个码
     */
    XFY("XFY"),
    /**
     * 杉德支付
     */
    SAND("SAND")
    ;

    private String value;

    ChannelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
