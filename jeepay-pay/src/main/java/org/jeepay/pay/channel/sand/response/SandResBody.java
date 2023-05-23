package org.jeepay.pay.channel.sand.response;

public class SandResBody {

    private String orderCode;

    private String totalAmount;

    private String credential;

    private String tradeNo;

    private String buyerPayAmount;

    private String discAmount;

    private String payTime;

    private String clearDate;

    private String extend;

    private String qrCode;

    private String merchExtendParams;

    private String mid;

    private String orderStatus;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String traceNo) {
        this.tradeNo = traceNo;
    }

    public String getBuyerPayAmount() {
        return buyerPayAmount;
    }

    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount;
    }

    public String getDiscAmount() {
        return discAmount;
    }

    public void setDiscAmount(String discAmount) {
        this.discAmount = discAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getClearDate() {
        return clearDate;
    }

    public void setClearDate(String clearDate) {
        this.clearDate = clearDate;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getMerchExtendParams() {
        return merchExtendParams;
    }

    public void setMerchExtendParams(String merchExtendParams) {
        this.merchExtendParams = merchExtendParams;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
