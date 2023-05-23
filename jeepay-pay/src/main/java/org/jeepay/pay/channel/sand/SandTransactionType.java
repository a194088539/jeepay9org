package org.jeepay.pay.channel.sand;


import org.jeepay.common.bean.TransactionType;

public enum SandTransactionType implements TransactionType {
    SAND_H5("sand_h5","sandpay.trade.pay","00000008"),//H5快捷支付
    SAND_B2C("bank_pc","sandpay.trade.pay","00000007"),//B2C网关支付
    SAND_QR("sandpay.trade.precreate","00000012"), // 银联扫码
    SAND_JD_QR("sandpay.trade.precreate","00000012"), // 银联扫码包装的京东扫码
    AGENT_PAY("", "", "00000004"), // 代付
    SAND_QR_QUERY("sandpay.trade.query", "00000012"), // 银联二维码查单
    SAND_H5_QUERY("sandpay.trade.query", "00000008"), // 快捷支付查单
    SAND_B2C_QUERY("sandpay.trade.query", "00000007") // B2C支付查单
    ;

    /**
     * 支付模式
     */
    private String payMode;

    /**
     * 接口名称
     */
    private String method;

    /**
     * 产品编码
     */
    private String productId;

    SandTransactionType(String payMode, String method, String productId){
        this.payMode=payMode;
        this.method=method;
        this.productId=productId;
    }
    SandTransactionType(String method, String productId){
        this.method=method;
        this.productId=productId;
    }
    @Override
    public String getType() {
        return this.name();
    }

    @Override
    public String getMethod() {
        return method;
    }

    public String getPayMode() {
        return payMode;
    }


    public String getProductId() {
        return productId;
    }

}
