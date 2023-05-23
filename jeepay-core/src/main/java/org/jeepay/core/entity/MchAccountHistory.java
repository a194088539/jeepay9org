package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class MchAccountHistory implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private Long mchId;

    /**
     * 变动金额
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     * 变更前账户余额
     *
     * @mbggenerated
     */
    private Long balance;

    /**
     * 变更后账户余额
     *
     * @mbggenerated
     */
    private Long afterBalance;

    /**
     * 代理商ID
     *
     * @mbggenerated
     */
    private Long agentId;

    /**
     * 一级代理商ID
     *
     * @mbggenerated
     */
    private Long parentAgentId;

    /**
     * 订单金额
     *
     * @mbggenerated
     */
    private Long orderAmount;

    /**
     * 手续费
     *
     * @mbggenerated
     */
    private Long fee;

    /**
     * 代理商利润,单位分
     *
     * @mbggenerated
     */
    private Long agentProfit;

    /**
     * 父级代理商利润
     *
     * @mbggenerated
     */
    private Long parentAgentProfit;

    /**
     * 平台利润,单位分
     *
     * @mbggenerated
     */
    private Long platProfit;

    /**
     * 渠道成本,单位分
     *
     * @mbggenerated
     */
    private Long channelCost;

    /**
     * 资金变动方向,1-加款,2-减款
     *
     * @mbggenerated
     */
    private Byte fundDirection;

    /**
     * 是否允许结算,1-允许,0-不允许
     *
     * @mbggenerated
     */
    private Byte isAllowSett;

    /**
     * 商户结算状态,1-已完成,0-未完成
     *
     * @mbggenerated
     */
    private Byte mchSettStatus;

    /**
     * 代理商结算状态,1-已完成,0-未完成
     *
     * @mbggenerated
     */
    private Byte agentSettStatus;

    /**
     * 父级代理商结算状态,1-已完成,0-未完成
     *
     * @mbggenerated
     */
    private Byte parentAgentSettStatus;

    /**
     * 平台订单号
     *
     * @mbggenerated
     */
    private String orderId;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    private String channelOrderNo;

    /**
     * 业务类型,1-支付,2-提现,3-调账,4-充值,5-差错处理,6-代付
     *
     * @mbggenerated
     */
    private Byte bizType;

    /**
     * 风险预存期
     *
     * @mbggenerated
     */
    private Integer riskDay;

    /**
     * 备注
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

    /**
     * 业务类目:10-余额,11-代付余额,12-冻结金额,13-保证金,20-支付,21-代付,22-线下充值,23-线上充值
     *
     * @mbggenerated
     */
    private String bizItem;

    /**
     * 代理商风险预存期
     *
     * @mbggenerated
     */
    private Integer agentRiskDay;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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

    public Long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Long afterBalance) {
        this.afterBalance = afterBalance;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(Long parentAgentId) {
        this.parentAgentId = parentAgentId;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getAgentProfit() {
        return agentProfit;
    }

    public void setAgentProfit(Long agentProfit) {
        this.agentProfit = agentProfit;
    }

    public Long getParentAgentProfit() {
        return parentAgentProfit;
    }

    public void setParentAgentProfit(Long parentAgentProfit) {
        this.parentAgentProfit = parentAgentProfit;
    }

    public Long getPlatProfit() {
        return platProfit;
    }

    public void setPlatProfit(Long platProfit) {
        this.platProfit = platProfit;
    }

    public Long getChannelCost() {
        return channelCost;
    }

    public void setChannelCost(Long channelCost) {
        this.channelCost = channelCost;
    }

    public Byte getFundDirection() {
        return fundDirection;
    }

    public void setFundDirection(Byte fundDirection) {
        this.fundDirection = fundDirection;
    }

    public Byte getIsAllowSett() {
        return isAllowSett;
    }

    public void setIsAllowSett(Byte isAllowSett) {
        this.isAllowSett = isAllowSett;
    }

    public Byte getMchSettStatus() {
        return mchSettStatus;
    }

    public void setMchSettStatus(Byte mchSettStatus) {
        this.mchSettStatus = mchSettStatus;
    }

    public Byte getAgentSettStatus() {
        return agentSettStatus;
    }

    public void setAgentSettStatus(Byte agentSettStatus) {
        this.agentSettStatus = agentSettStatus;
    }

    public Byte getParentAgentSettStatus() {
        return parentAgentSettStatus;
    }

    public void setParentAgentSettStatus(Byte parentAgentSettStatus) {
        this.parentAgentSettStatus = parentAgentSettStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public Byte getBizType() {
        return bizType;
    }

    public void setBizType(Byte bizType) {
        this.bizType = bizType;
    }

    public Integer getRiskDay() {
        return riskDay;
    }

    public void setRiskDay(Integer riskDay) {
        this.riskDay = riskDay;
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

    public String getBizItem() {
        return bizItem;
    }

    public void setBizItem(String bizItem) {
        this.bizItem = bizItem;
    }

    public Integer getAgentRiskDay() {
        return agentRiskDay;
    }

    public void setAgentRiskDay(Integer agentRiskDay) {
        this.agentRiskDay = agentRiskDay;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mchId=").append(mchId);
        sb.append(", amount=").append(amount);
        sb.append(", balance=").append(balance);
        sb.append(", afterBalance=").append(afterBalance);
        sb.append(", agentId=").append(agentId);
        sb.append(", parentAgentId=").append(parentAgentId);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", fee=").append(fee);
        sb.append(", agentProfit=").append(agentProfit);
        sb.append(", parentAgentProfit=").append(parentAgentProfit);
        sb.append(", platProfit=").append(platProfit);
        sb.append(", channelCost=").append(channelCost);
        sb.append(", fundDirection=").append(fundDirection);
        sb.append(", isAllowSett=").append(isAllowSett);
        sb.append(", mchSettStatus=").append(mchSettStatus);
        sb.append(", agentSettStatus=").append(agentSettStatus);
        sb.append(", parentAgentSettStatus=").append(parentAgentSettStatus);
        sb.append(", orderId=").append(orderId);
        sb.append(", channelOrderNo=").append(channelOrderNo);
        sb.append(", bizType=").append(bizType);
        sb.append(", riskDay=").append(riskDay);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", bizItem=").append(bizItem);
        sb.append(", agentRiskDay=").append(agentRiskDay);
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
        MchAccountHistory other = (MchAccountHistory) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getAfterBalance() == null ? other.getAfterBalance() == null : this.getAfterBalance().equals(other.getAfterBalance()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getParentAgentId() == null ? other.getParentAgentId() == null : this.getParentAgentId().equals(other.getParentAgentId()))
            && (this.getOrderAmount() == null ? other.getOrderAmount() == null : this.getOrderAmount().equals(other.getOrderAmount()))
            && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
            && (this.getAgentProfit() == null ? other.getAgentProfit() == null : this.getAgentProfit().equals(other.getAgentProfit()))
            && (this.getParentAgentProfit() == null ? other.getParentAgentProfit() == null : this.getParentAgentProfit().equals(other.getParentAgentProfit()))
            && (this.getPlatProfit() == null ? other.getPlatProfit() == null : this.getPlatProfit().equals(other.getPlatProfit()))
            && (this.getChannelCost() == null ? other.getChannelCost() == null : this.getChannelCost().equals(other.getChannelCost()))
            && (this.getFundDirection() == null ? other.getFundDirection() == null : this.getFundDirection().equals(other.getFundDirection()))
            && (this.getIsAllowSett() == null ? other.getIsAllowSett() == null : this.getIsAllowSett().equals(other.getIsAllowSett()))
            && (this.getMchSettStatus() == null ? other.getMchSettStatus() == null : this.getMchSettStatus().equals(other.getMchSettStatus()))
            && (this.getAgentSettStatus() == null ? other.getAgentSettStatus() == null : this.getAgentSettStatus().equals(other.getAgentSettStatus()))
            && (this.getParentAgentSettStatus() == null ? other.getParentAgentSettStatus() == null : this.getParentAgentSettStatus().equals(other.getParentAgentSettStatus()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getChannelOrderNo() == null ? other.getChannelOrderNo() == null : this.getChannelOrderNo().equals(other.getChannelOrderNo()))
            && (this.getBizType() == null ? other.getBizType() == null : this.getBizType().equals(other.getBizType()))
            && (this.getRiskDay() == null ? other.getRiskDay() == null : this.getRiskDay().equals(other.getRiskDay()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getBizItem() == null ? other.getBizItem() == null : this.getBizItem().equals(other.getBizItem()))
            && (this.getAgentRiskDay() == null ? other.getAgentRiskDay() == null : this.getAgentRiskDay().equals(other.getAgentRiskDay()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getAfterBalance() == null) ? 0 : getAfterBalance().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getParentAgentId() == null) ? 0 : getParentAgentId().hashCode());
        result = prime * result + ((getOrderAmount() == null) ? 0 : getOrderAmount().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getAgentProfit() == null) ? 0 : getAgentProfit().hashCode());
        result = prime * result + ((getParentAgentProfit() == null) ? 0 : getParentAgentProfit().hashCode());
        result = prime * result + ((getPlatProfit() == null) ? 0 : getPlatProfit().hashCode());
        result = prime * result + ((getChannelCost() == null) ? 0 : getChannelCost().hashCode());
        result = prime * result + ((getFundDirection() == null) ? 0 : getFundDirection().hashCode());
        result = prime * result + ((getIsAllowSett() == null) ? 0 : getIsAllowSett().hashCode());
        result = prime * result + ((getMchSettStatus() == null) ? 0 : getMchSettStatus().hashCode());
        result = prime * result + ((getAgentSettStatus() == null) ? 0 : getAgentSettStatus().hashCode());
        result = prime * result + ((getParentAgentSettStatus() == null) ? 0 : getParentAgentSettStatus().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getChannelOrderNo() == null) ? 0 : getChannelOrderNo().hashCode());
        result = prime * result + ((getBizType() == null) ? 0 : getBizType().hashCode());
        result = prime * result + ((getRiskDay() == null) ? 0 : getRiskDay().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getBizItem() == null) ? 0 : getBizItem().hashCode());
        result = prime * result + ((getAgentRiskDay() == null) ? 0 : getAgentRiskDay().hashCode());
        return result;
    }
}