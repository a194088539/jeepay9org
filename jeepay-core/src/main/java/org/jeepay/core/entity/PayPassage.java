package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayPassage implements Serializable {
    /**
     * 支付通道ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 通道名称
     *
     * @mbggenerated
     */
    private String passageName;

    /**
     * 接口代码
     *
     * @mbggenerated
     */
    private String ifCode;

    /**
     * 接口类型代码
     *
     * @mbggenerated
     */
    private String ifTypeCode;

    /**
     * 支付类型
     *
     * @mbggenerated
     */
    private String payType;

    /**
     * 通道状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 通道费率百分比
     *
     * @mbggenerated
     */
    private BigDecimal passageRate;

    /**
     * 当天交易金额,单位分
     *
     * @mbggenerated
     */
    private Long maxDayAmount;

    /**
     * 单笔最大金额,单位分
     *
     * @mbggenerated
     */
    private Long maxEveryAmount;

    /**
     * 单笔最小金额,单位分
     *
     * @mbggenerated
     */
    private Long minEveryAmount;

    /**
     * 固定金额
     *
     * @mbggenerated
     */
    private String fixedAmount;

    /**
     * 交易开始时间
     *
     * @mbggenerated
     */
    private String tradeStartTime;

    /**
     * 交易结束时间
     *
     * @mbggenerated
     */
    private String tradeEndTime;

    /**
     * 风控状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte riskStatus;

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

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassageName() {
        return passageName;
    }

    public void setPassageName(String passageName) {
        this.passageName = passageName;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigDecimal getPassageRate() {
        return passageRate;
    }

    public void setPassageRate(BigDecimal passageRate) {
        this.passageRate = passageRate;
    }

    public Long getMaxDayAmount() {
        return maxDayAmount;
    }

    public void setMaxDayAmount(Long maxDayAmount) {
        this.maxDayAmount = maxDayAmount;
    }

    public Long getMaxEveryAmount() {
        return maxEveryAmount;
    }

    public void setMaxEveryAmount(Long maxEveryAmount) {
        this.maxEveryAmount = maxEveryAmount;
    }

    public Long getMinEveryAmount() {
        return minEveryAmount;
    }

    public void setMinEveryAmount(Long minEveryAmount) {
        this.minEveryAmount = minEveryAmount;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getTradeStartTime() {
        return tradeStartTime;
    }

    public void setTradeStartTime(String tradeStartTime) {
        this.tradeStartTime = tradeStartTime;
    }

    public String getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(String tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }

    public Byte getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Byte riskStatus) {
        this.riskStatus = riskStatus;
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
        sb.append(", passageName=").append(passageName);
        sb.append(", ifCode=").append(ifCode);
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", payType=").append(payType);
        sb.append(", status=").append(status);
        sb.append(", passageRate=").append(passageRate);
        sb.append(", maxDayAmount=").append(maxDayAmount);
        sb.append(", maxEveryAmount=").append(maxEveryAmount);
        sb.append(", minEveryAmount=").append(minEveryAmount);
        sb.append(", fixedAmount=").append(fixedAmount);
        sb.append(", tradeStartTime=").append(tradeStartTime);
        sb.append(", tradeEndTime=").append(tradeEndTime);
        sb.append(", riskStatus=").append(riskStatus);
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
        PayPassage other = (PayPassage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPassageName() == null ? other.getPassageName() == null : this.getPassageName().equals(other.getPassageName()))
            && (this.getIfCode() == null ? other.getIfCode() == null : this.getIfCode().equals(other.getIfCode()))
            && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getPayType() == null ? other.getPayType() == null : this.getPayType().equals(other.getPayType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPassageRate() == null ? other.getPassageRate() == null : this.getPassageRate().equals(other.getPassageRate()))
            && (this.getMaxDayAmount() == null ? other.getMaxDayAmount() == null : this.getMaxDayAmount().equals(other.getMaxDayAmount()))
            && (this.getMaxEveryAmount() == null ? other.getMaxEveryAmount() == null : this.getMaxEveryAmount().equals(other.getMaxEveryAmount()))
            && (this.getMinEveryAmount() == null ? other.getMinEveryAmount() == null : this.getMinEveryAmount().equals(other.getMinEveryAmount()))
            && (this.getFixedAmount() == null ? other.getFixedAmount() == null : this.getFixedAmount().equals(other.getFixedAmount()))
            && (this.getTradeStartTime() == null ? other.getTradeStartTime() == null : this.getTradeStartTime().equals(other.getTradeStartTime()))
            && (this.getTradeEndTime() == null ? other.getTradeEndTime() == null : this.getTradeEndTime().equals(other.getTradeEndTime()))
            && (this.getRiskStatus() == null ? other.getRiskStatus() == null : this.getRiskStatus().equals(other.getRiskStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPassageName() == null) ? 0 : getPassageName().hashCode());
        result = prime * result + ((getIfCode() == null) ? 0 : getIfCode().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getPayType() == null) ? 0 : getPayType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPassageRate() == null) ? 0 : getPassageRate().hashCode());
        result = prime * result + ((getMaxDayAmount() == null) ? 0 : getMaxDayAmount().hashCode());
        result = prime * result + ((getMaxEveryAmount() == null) ? 0 : getMaxEveryAmount().hashCode());
        result = prime * result + ((getMinEveryAmount() == null) ? 0 : getMinEveryAmount().hashCode());
        result = prime * result + ((getFixedAmount() == null) ? 0 : getFixedAmount().hashCode());
        result = prime * result + ((getTradeStartTime() == null) ? 0 : getTradeStartTime().hashCode());
        result = prime * result + ((getTradeEndTime() == null) ? 0 : getTradeEndTime().hashCode());
        result = prime * result + ((getRiskStatus() == null) ? 0 : getRiskStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}