package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MchAgentpayPassage implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private Long mchId;

    /**
     * 代付通道ID
     *
     * @mbggenerated
     */
    private Integer agentpayPassageId;

    /**
     * 代付通道账户ID
     *
     * @mbggenerated
     */
    private Integer agentpayPassageAccountId;

    /**
     * 手续费类型,1-百分比收费,2-固定收费
     *
     * @mbggenerated
     */
    private Byte mchFeeType;

    /**
     * 手续费费率,百分比
     *
     * @mbggenerated
     */
    private BigDecimal mchFeeRate;

    /**
     * 手续费每笔金额,单位分
     *
     * @mbggenerated
     */
    private Long mchFeeEvery;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 是否默认,0-否,1-是
     *
     * @mbggenerated
     */
    private Byte isDefault;

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
     * 单笔代付转出最大金额,单位分
     *
     * @mbggenerated
     */
    private Long maxEveryAmount;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Integer getAgentpayPassageId() {
        return agentpayPassageId;
    }

    public void setAgentpayPassageId(Integer agentpayPassageId) {
        this.agentpayPassageId = agentpayPassageId;
    }

    public Integer getAgentpayPassageAccountId() {
        return agentpayPassageAccountId;
    }

    public void setAgentpayPassageAccountId(Integer agentpayPassageAccountId) {
        this.agentpayPassageAccountId = agentpayPassageAccountId;
    }

    public Byte getMchFeeType() {
        return mchFeeType;
    }

    public void setMchFeeType(Byte mchFeeType) {
        this.mchFeeType = mchFeeType;
    }

    public BigDecimal getMchFeeRate() {
        return mchFeeRate;
    }

    public void setMchFeeRate(BigDecimal mchFeeRate) {
        this.mchFeeRate = mchFeeRate;
    }

    public Long getMchFeeEvery() {
        return mchFeeEvery;
    }

    public void setMchFeeEvery(Long mchFeeEvery) {
        this.mchFeeEvery = mchFeeEvery;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
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

    public Long getMaxEveryAmount() {
        return maxEveryAmount;
    }

    public void setMaxEveryAmount(Long maxEveryAmount) {
        this.maxEveryAmount = maxEveryAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mchId=").append(mchId);
        sb.append(", agentpayPassageId=").append(agentpayPassageId);
        sb.append(", agentpayPassageAccountId=").append(agentpayPassageAccountId);
        sb.append(", mchFeeType=").append(mchFeeType);
        sb.append(", mchFeeRate=").append(mchFeeRate);
        sb.append(", mchFeeEvery=").append(mchFeeEvery);
        sb.append(", status=").append(status);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", maxEveryAmount=").append(maxEveryAmount);
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
        MchAgentpayPassage other = (MchAgentpayPassage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getAgentpayPassageId() == null ? other.getAgentpayPassageId() == null : this.getAgentpayPassageId().equals(other.getAgentpayPassageId()))
            && (this.getAgentpayPassageAccountId() == null ? other.getAgentpayPassageAccountId() == null : this.getAgentpayPassageAccountId().equals(other.getAgentpayPassageAccountId()))
            && (this.getMchFeeType() == null ? other.getMchFeeType() == null : this.getMchFeeType().equals(other.getMchFeeType()))
            && (this.getMchFeeRate() == null ? other.getMchFeeRate() == null : this.getMchFeeRate().equals(other.getMchFeeRate()))
            && (this.getMchFeeEvery() == null ? other.getMchFeeEvery() == null : this.getMchFeeEvery().equals(other.getMchFeeEvery()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIsDefault() == null ? other.getIsDefault() == null : this.getIsDefault().equals(other.getIsDefault()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getMaxEveryAmount() == null ? other.getMaxEveryAmount() == null : this.getMaxEveryAmount().equals(other.getMaxEveryAmount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getAgentpayPassageId() == null) ? 0 : getAgentpayPassageId().hashCode());
        result = prime * result + ((getAgentpayPassageAccountId() == null) ? 0 : getAgentpayPassageAccountId().hashCode());
        result = prime * result + ((getMchFeeType() == null) ? 0 : getMchFeeType().hashCode());
        result = prime * result + ((getMchFeeRate() == null) ? 0 : getMchFeeRate().hashCode());
        result = prime * result + ((getMchFeeEvery() == null) ? 0 : getMchFeeEvery().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIsDefault() == null) ? 0 : getIsDefault().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getMaxEveryAmount() == null) ? 0 : getMaxEveryAmount().hashCode());
        return result;
    }
}