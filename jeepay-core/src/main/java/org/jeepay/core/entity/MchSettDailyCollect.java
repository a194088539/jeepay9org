package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class MchSettDailyCollect implements Serializable {
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
     * 汇总日期
     *
     * @mbggenerated
     */
    private Date collectDate;

    /**
     * 汇总类型:1-存入/减少汇总,2-临时汇总,3-遗留汇总
     *
     * @mbggenerated
     */
    private Byte collectType;

    /**
     * 交易金额
     *
     * @mbggenerated
     */
    private Long totalAmount;

    /**
     * 交易总笔数
     *
     * @mbggenerated
     */
    private Integer totalCount;

    /**
     * 商户入账,单位分
     *
     * @mbggenerated
     */
    private Long totalMchIncome;

    /**
     * 代理商利润,单位分
     *
     * @mbggenerated
     */
    private Long totalAgentProfit;

    /**
     * 平台利润,单位分
     *
     * @mbggenerated
     */
    private Long totalPlatProfit;

    /**
     * 渠道成本,单位分
     *
     * @mbggenerated
     */
    private Long totalChannelCost;

    /**
     * 结算状态,0-未结算,1-已结算
     *
     * @mbggenerated
     */
    private Byte settStatus;

    /**
     * 备注
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * 风险预存期天数
     *
     * @mbggenerated
     */
    private Integer riskDay;

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

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }

    public Byte getCollectType() {
        return collectType;
    }

    public void setCollectType(Byte collectType) {
        this.collectType = collectType;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalMchIncome() {
        return totalMchIncome;
    }

    public void setTotalMchIncome(Long totalMchIncome) {
        this.totalMchIncome = totalMchIncome;
    }

    public Long getTotalAgentProfit() {
        return totalAgentProfit;
    }

    public void setTotalAgentProfit(Long totalAgentProfit) {
        this.totalAgentProfit = totalAgentProfit;
    }

    public Long getTotalPlatProfit() {
        return totalPlatProfit;
    }

    public void setTotalPlatProfit(Long totalPlatProfit) {
        this.totalPlatProfit = totalPlatProfit;
    }

    public Long getTotalChannelCost() {
        return totalChannelCost;
    }

    public void setTotalChannelCost(Long totalChannelCost) {
        this.totalChannelCost = totalChannelCost;
    }

    public Byte getSettStatus() {
        return settStatus;
    }

    public void setSettStatus(Byte settStatus) {
        this.settStatus = settStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRiskDay() {
        return riskDay;
    }

    public void setRiskDay(Integer riskDay) {
        this.riskDay = riskDay;
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
        sb.append(", mchId=").append(mchId);
        sb.append(", collectDate=").append(collectDate);
        sb.append(", collectType=").append(collectType);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", totalMchIncome=").append(totalMchIncome);
        sb.append(", totalAgentProfit=").append(totalAgentProfit);
        sb.append(", totalPlatProfit=").append(totalPlatProfit);
        sb.append(", totalChannelCost=").append(totalChannelCost);
        sb.append(", settStatus=").append(settStatus);
        sb.append(", remark=").append(remark);
        sb.append(", riskDay=").append(riskDay);
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
        MchSettDailyCollect other = (MchSettDailyCollect) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getCollectDate() == null ? other.getCollectDate() == null : this.getCollectDate().equals(other.getCollectDate()))
            && (this.getCollectType() == null ? other.getCollectType() == null : this.getCollectType().equals(other.getCollectType()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getTotalCount() == null ? other.getTotalCount() == null : this.getTotalCount().equals(other.getTotalCount()))
            && (this.getTotalMchIncome() == null ? other.getTotalMchIncome() == null : this.getTotalMchIncome().equals(other.getTotalMchIncome()))
            && (this.getTotalAgentProfit() == null ? other.getTotalAgentProfit() == null : this.getTotalAgentProfit().equals(other.getTotalAgentProfit()))
            && (this.getTotalPlatProfit() == null ? other.getTotalPlatProfit() == null : this.getTotalPlatProfit().equals(other.getTotalPlatProfit()))
            && (this.getTotalChannelCost() == null ? other.getTotalChannelCost() == null : this.getTotalChannelCost().equals(other.getTotalChannelCost()))
            && (this.getSettStatus() == null ? other.getSettStatus() == null : this.getSettStatus().equals(other.getSettStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getRiskDay() == null ? other.getRiskDay() == null : this.getRiskDay().equals(other.getRiskDay()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getCollectDate() == null) ? 0 : getCollectDate().hashCode());
        result = prime * result + ((getCollectType() == null) ? 0 : getCollectType().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getTotalCount() == null) ? 0 : getTotalCount().hashCode());
        result = prime * result + ((getTotalMchIncome() == null) ? 0 : getTotalMchIncome().hashCode());
        result = prime * result + ((getTotalAgentProfit() == null) ? 0 : getTotalAgentProfit().hashCode());
        result = prime * result + ((getTotalPlatProfit() == null) ? 0 : getTotalPlatProfit().hashCode());
        result = prime * result + ((getTotalChannelCost() == null) ? 0 : getTotalChannelCost().hashCode());
        result = prime * result + ((getSettStatus() == null) ? 0 : getSettStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getRiskDay() == null) ? 0 : getRiskDay().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}