package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class MchTradeOrder implements Serializable {
    /**
     * 交易单号
     *
     * @mbggenerated
     */
    private String tradeOrderId;

    /**
     * 交易类型:1-收款,2-充值
     *
     * @mbggenerated
     */
    private Byte tradeType;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private Long mchId;

    /**
     * 应用ID
     *
     * @mbggenerated
     */
    private String appId;

    /**
     * 客户端IP
     *
     * @mbggenerated
     */
    private String clientIp;

    /**
     * 设备
     *
     * @mbggenerated
     */
    private String device;

    /**
     * 商品ID
     *
     * @mbggenerated
     */
    private String goodsId;

    /**
     * 商品标题
     *
     * @mbggenerated
     */
    private String subject;

    /**
     * 商品描述信息
     *
     * @mbggenerated
     */
    private String body;

    /**
     * 交易金额,单位分
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     * 商户入账,单位分
     *
     * @mbggenerated
     */
    private Long mchIncome;

    /**
     * 用户ID
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * 支付渠道用户ID(微信openID或支付宝账号等第三方支付账号)
     *
     * @mbggenerated
     */
    private String channelUserId;

    /**
     * 订单状态,生成(0),处理中(1),成功(2),失败(-1)
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 支付订单号
     *
     * @mbggenerated
     */
    private String payOrderId;

    /**
     * 产品ID
     *
     * @mbggenerated
     */
    private String productId;

    /**
     * 产品类型
     *
     * @mbggenerated
     */
    private Byte productType;

    /**
     * 交易成功时间
     *
     * @mbggenerated
     */
    private Date tradeSuccTime;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

    public Byte getTradeType() {
        return tradeType;
    }

    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getMchIncome() {
        return mchIncome;
    }

    public void setMchIncome(Long mchIncome) {
        this.mchIncome = mchIncome;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    public Date getTradeSuccTime() {
        return tradeSuccTime;
    }

    public void setTradeSuccTime(Date tradeSuccTime) {
        this.tradeSuccTime = tradeSuccTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tradeOrderId=").append(tradeOrderId);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", mchId=").append(mchId);
        sb.append(", appId=").append(appId);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", device=").append(device);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", subject=").append(subject);
        sb.append(", body=").append(body);
        sb.append(", amount=").append(amount);
        sb.append(", mchIncome=").append(mchIncome);
        sb.append(", userId=").append(userId);
        sb.append(", channelUserId=").append(channelUserId);
        sb.append(", status=").append(status);
        sb.append(", payOrderId=").append(payOrderId);
        sb.append(", productId=").append(productId);
        sb.append(", productType=").append(productType);
        sb.append(", tradeSuccTime=").append(tradeSuccTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MchTradeOrder other = (MchTradeOrder) that;
        return (this.getTradeOrderId() == null ? other.getTradeOrderId() == null : this.getTradeOrderId().equals(other.getTradeOrderId()))
            && (this.getTradeType() == null ? other.getTradeType() == null : this.getTradeType().equals(other.getTradeType()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
            && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
            && (this.getGoodsId() == null ? other.getGoodsId() == null : this.getGoodsId().equals(other.getGoodsId()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getMchIncome() == null ? other.getMchIncome() == null : this.getMchIncome().equals(other.getMchIncome()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getChannelUserId() == null ? other.getChannelUserId() == null : this.getChannelUserId().equals(other.getChannelUserId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPayOrderId() == null ? other.getPayOrderId() == null : this.getPayOrderId().equals(other.getPayOrderId()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getProductType() == null ? other.getProductType() == null : this.getProductType().equals(other.getProductType()))
            && (this.getTradeSuccTime() == null ? other.getTradeSuccTime() == null : this.getTradeSuccTime().equals(other.getTradeSuccTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTradeOrderId() == null) ? 0 : getTradeOrderId().hashCode());
        result = prime * result + ((getTradeType() == null) ? 0 : getTradeType().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getGoodsId() == null) ? 0 : getGoodsId().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getMchIncome() == null) ? 0 : getMchIncome().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getChannelUserId() == null) ? 0 : getChannelUserId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPayOrderId() == null) ? 0 : getPayOrderId().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getProductType() == null) ? 0 : getProductType().hashCode());
        result = prime * result + ((getTradeSuccTime() == null) ? 0 : getTradeSuccTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}