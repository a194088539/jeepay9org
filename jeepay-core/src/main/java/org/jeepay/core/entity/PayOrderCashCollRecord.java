package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayOrderCashCollRecord implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 支付订单号
     *
     * @mbggenerated
     */
    private String payOrderId;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    private String channelOrderNo;

    /**
     * 请求序列号（上送渠道序列）
     *
     * @mbggenerated
     */
    private String requestNo;

    /**
     * 分账收入方姓名快照
     *
     * @mbggenerated
     */
    private String transInUserName;

    /**
     * 分账收入方账号快照
     *
     * @mbggenerated
     */
    private String transInUserAccount;

    /**
     * 分账收入方用户ID 快照(支付宝PID)
     *
     * @mbggenerated
     */
    private String transInUserId;

    /**
     * 分账比例快照,百分比
     *
     * @mbggenerated
     */
    private BigDecimal transInPercentage;

    /**
     * 分账计算金额
     *
     * @mbggenerated
     */
    private Long transInAmount;

    /**
     * 状态,0-失败 ,1-成功
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 信息备注
     *
     * @mbggenerated
     */
    private String remark;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getTransInUserName() {
        return transInUserName;
    }

    public void setTransInUserName(String transInUserName) {
        this.transInUserName = transInUserName;
    }

    public String getTransInUserAccount() {
        return transInUserAccount;
    }

    public void setTransInUserAccount(String transInUserAccount) {
        this.transInUserAccount = transInUserAccount;
    }

    public String getTransInUserId() {
        return transInUserId;
    }

    public void setTransInUserId(String transInUserId) {
        this.transInUserId = transInUserId;
    }

    public BigDecimal getTransInPercentage() {
        return transInPercentage;
    }

    public void setTransInPercentage(BigDecimal transInPercentage) {
        this.transInPercentage = transInPercentage;
    }

    public Long getTransInAmount() {
        return transInAmount;
    }

    public void setTransInAmount(Long transInAmount) {
        this.transInAmount = transInAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        sb.append(", id=").append(id);
        sb.append(", payOrderId=").append(payOrderId);
        sb.append(", channelOrderNo=").append(channelOrderNo);
        sb.append(", requestNo=").append(requestNo);
        sb.append(", transInUserName=").append(transInUserName);
        sb.append(", transInUserAccount=").append(transInUserAccount);
        sb.append(", transInUserId=").append(transInUserId);
        sb.append(", transInPercentage=").append(transInPercentage);
        sb.append(", transInAmount=").append(transInAmount);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
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
        PayOrderCashCollRecord other = (PayOrderCashCollRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getPayOrderId() == null ? other.getPayOrderId() == null : this.getPayOrderId().equals(other.getPayOrderId()))
                && (this.getChannelOrderNo() == null ? other.getChannelOrderNo() == null : this.getChannelOrderNo().equals(other.getChannelOrderNo()))
                && (this.getRequestNo() == null ? other.getRequestNo() == null : this.getRequestNo().equals(other.getRequestNo()))
                && (this.getTransInUserName() == null ? other.getTransInUserName() == null : this.getTransInUserName().equals(other.getTransInUserName()))
                && (this.getTransInUserAccount() == null ? other.getTransInUserAccount() == null : this.getTransInUserAccount().equals(other.getTransInUserAccount()))
                && (this.getTransInUserId() == null ? other.getTransInUserId() == null : this.getTransInUserId().equals(other.getTransInUserId()))
                && (this.getTransInPercentage() == null ? other.getTransInPercentage() == null : this.getTransInPercentage().equals(other.getTransInPercentage()))
                && (this.getTransInAmount() == null ? other.getTransInAmount() == null : this.getTransInAmount().equals(other.getTransInAmount()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPayOrderId() == null) ? 0 : getPayOrderId().hashCode());
        result = prime * result + ((getChannelOrderNo() == null) ? 0 : getChannelOrderNo().hashCode());
        result = prime * result + ((getRequestNo() == null) ? 0 : getRequestNo().hashCode());
        result = prime * result + ((getTransInUserName() == null) ? 0 : getTransInUserName().hashCode());
        result = prime * result + ((getTransInUserAccount() == null) ? 0 : getTransInUserAccount().hashCode());
        result = prime * result + ((getTransInUserId() == null) ? 0 : getTransInUserId().hashCode());
        result = prime * result + ((getTransInPercentage() == null) ? 0 : getTransInPercentage().hashCode());
        result = prime * result + ((getTransInAmount() == null) ? 0 : getTransInAmount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}