package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class AgentAccountHistory implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 代理商ID
     *
     * @mbggenerated
     */
    private Long agentId;

    /**
     * 变动金额
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     * 账户余额
     *
     * @mbggenerated
     */
    private Long balance;

    /**
     * 资金变动方向,1-加款,2-减款
     *
     * @mbggenerated
     */
    private Byte fundDirection;

    /**
     * 业务类型,1-分润,2-提现,3-调账
     *
     * @mbggenerated
     */
    private Byte bizType;

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

    /**
     * 变更后账户余额
     *
     * @mbggenerated
     */
    private Long afterBalance;

    /**
     * 业务类目:10-余额,11-代付余额,12-冻结金额,13-保证金,20-支付,21-代付,22-线下充值,23-线上充值
     *
     * @mbggenerated
     */
    private String bizItem;

    /**
     * 平台订单号
     *
     * @mbggenerated
     */
    private String orderId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Byte getFundDirection() {
        return fundDirection;
    }

    public void setFundDirection(Byte fundDirection) {
        this.fundDirection = fundDirection;
    }

    public Byte getBizType() {
        return bizType;
    }

    public void setBizType(Byte bizType) {
        this.bizType = bizType;
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

    public Long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Long afterBalance) {
        this.afterBalance = afterBalance;
    }

    public String getBizItem() {
        return bizItem;
    }

    public void setBizItem(String bizItem) {
        this.bizItem = bizItem;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", agentId=").append(agentId);
        sb.append(", amount=").append(amount);
        sb.append(", balance=").append(balance);
        sb.append(", fundDirection=").append(fundDirection);
        sb.append(", bizType=").append(bizType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", afterBalance=").append(afterBalance);
        sb.append(", bizItem=").append(bizItem);
        sb.append(", orderId=").append(orderId);
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
        AgentAccountHistory other = (AgentAccountHistory) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getFundDirection() == null ? other.getFundDirection() == null : this.getFundDirection().equals(other.getFundDirection()))
            && (this.getBizType() == null ? other.getBizType() == null : this.getBizType().equals(other.getBizType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getAfterBalance() == null ? other.getAfterBalance() == null : this.getAfterBalance().equals(other.getAfterBalance()))
            && (this.getBizItem() == null ? other.getBizItem() == null : this.getBizItem().equals(other.getBizItem()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getFundDirection() == null) ? 0 : getFundDirection().hashCode());
        result = prime * result + ((getBizType() == null) ? 0 : getBizType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getAfterBalance() == null) ? 0 : getAfterBalance().hashCode());
        result = prime * result + ((getBizItem() == null) ? 0 : getBizItem().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        return result;
    }
}