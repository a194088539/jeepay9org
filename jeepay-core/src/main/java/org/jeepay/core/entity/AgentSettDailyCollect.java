package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class AgentSettDailyCollect implements Serializable {
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
     * 二级代理商利润,单位分
     *
     * @mbggenerated
     */
    private Long totalAgentProfit;

    /**
     * 一级代理商利润,单位分
     *
     * @mbggenerated
     */
    private Long totalParentAgentProfit;

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

    /**
     * 结算类目:20-支付,30-代付,31-线下充值,32-线上B2B充值,33-线上B2C充值
     *
     * @mbggenerated
     */
    private String collectItem;

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

    public Long getTotalAgentProfit() {
        return totalAgentProfit;
    }

    public void setTotalAgentProfit(Long totalAgentProfit) {
        this.totalAgentProfit = totalAgentProfit;
    }
    public Long getTotalParentAgentProfit() {
        return totalParentAgentProfit;
    }

    public void setTotalParentAgentProfit(Long totalParentAgentProfit) {
        this.totalParentAgentProfit = totalParentAgentProfit;
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

    public String getCollectItem() {
        return collectItem;
    }

    public void setCollectItem(String collectItem) {
        this.collectItem = collectItem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", agentId=").append(agentId);
        sb.append(", collectDate=").append(collectDate);
        sb.append(", collectType=").append(collectType);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", totalAgentProfit=").append(totalAgentProfit);
        sb.append(", totalParentAgentProfit=").append(totalParentAgentProfit);
        sb.append(", settStatus=").append(settStatus);
        sb.append(", remark=").append(remark);
        sb.append(", riskDay=").append(riskDay);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", collectItem=").append(collectItem);
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
        AgentSettDailyCollect other = (AgentSettDailyCollect) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getCollectDate() == null ? other.getCollectDate() == null : this.getCollectDate().equals(other.getCollectDate()))
            && (this.getCollectType() == null ? other.getCollectType() == null : this.getCollectType().equals(other.getCollectType()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getTotalCount() == null ? other.getTotalCount() == null : this.getTotalCount().equals(other.getTotalCount()))
            && (this.getTotalAgentProfit() == null ? other.getTotalAgentProfit() == null : this.getTotalAgentProfit().equals(other.getTotalAgentProfit()))
            && (this.getTotalParentAgentProfit() == null ? other.getTotalParentAgentProfit() == null : this.getTotalParentAgentProfit().equals(other.getTotalParentAgentProfit()))
            && (this.getSettStatus() == null ? other.getSettStatus() == null : this.getSettStatus().equals(other.getSettStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getRiskDay() == null ? other.getRiskDay() == null : this.getRiskDay().equals(other.getRiskDay()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCollectItem() == null ? other.getCollectItem() == null : this.getCollectItem().equals(other.getCollectItem()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getCollectDate() == null) ? 0 : getCollectDate().hashCode());
        result = prime * result + ((getCollectType() == null) ? 0 : getCollectType().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getTotalCount() == null) ? 0 : getTotalCount().hashCode());
        result = prime * result + ((getTotalAgentProfit() == null) ? 0 : getTotalAgentProfit().hashCode());
        result = prime * result + ((getTotalParentAgentProfit() == null) ? 0 : getTotalParentAgentProfit().hashCode());
        result = prime * result + ((getSettStatus() == null) ? 0 : getSettStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getRiskDay() == null) ? 0 : getRiskDay().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCollectItem() == null) ? 0 : getCollectItem().hashCode());
        return result;
    }
}