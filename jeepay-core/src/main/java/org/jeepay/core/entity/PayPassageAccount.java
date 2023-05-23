package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayPassageAccount implements Serializable {
    /**
     * 账户ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 账户名称
     *
     * @mbggenerated
     */
    private String accountName;

    /**
     * 支付通道ID
     *
     * @mbggenerated
     */
    private Integer payPassageId;

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
     * 账户配置参数,json字符串
     *
     * @mbggenerated
     */
    private String param;

    /**
     * 账户状态,0-停止,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 轮询权重
     *
     * @mbggenerated
     */
    private Integer pollWeight;

    /**
     * 通道商户ID
     *
     * @mbggenerated
     */
    private String passageMchId;

    /**
     * 通道费率百分比
     *
     * @mbggenerated
     */
    private BigDecimal passageRate;

    /**
     * 风控模式,1-继承,2-自定义
     *
     * @mbggenerated
     */
    private Byte riskMode;

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
     * 资金归集开关,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte cashCollStatus;

    /**
     * 资金归集配置,1-继承全局配置,2-自定义
     *
     * @mbggenerated
     */
    private Byte cashCollMode;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getPayPassageId() {
        return payPassageId;
    }

    public void setPayPassageId(Integer payPassageId) {
        this.payPassageId = payPassageId;
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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getPollWeight() {
        return pollWeight;
    }

    public void setPollWeight(Integer pollWeight) {
        this.pollWeight = pollWeight;
    }

    public String getPassageMchId() {
        return passageMchId;
    }

    public void setPassageMchId(String passageMchId) {
        this.passageMchId = passageMchId;
    }

    public BigDecimal getPassageRate() {
        return passageRate;
    }

    public void setPassageRate(BigDecimal passageRate) {
        this.passageRate = passageRate;
    }

    public Byte getRiskMode() {
        return riskMode;
    }

    public void setRiskMode(Byte riskMode) {
        this.riskMode = riskMode;
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

    public Byte getCashCollStatus() {
        return cashCollStatus;
    }

    public void setCashCollStatus(Byte cashCollStatus) {
        this.cashCollStatus = cashCollStatus;
    }

    public Byte getCashCollMode() {
        return cashCollMode;
    }

    public void setCashCollMode(Byte cashCollMode) {
        this.cashCollMode = cashCollMode;
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
        sb.append(", accountName=").append(accountName);
        sb.append(", payPassageId=").append(payPassageId);
        sb.append(", ifCode=").append(ifCode);
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", param=").append(param);
        sb.append(", status=").append(status);
        sb.append(", pollWeight=").append(pollWeight);
        sb.append(", passageMchId=").append(passageMchId);
        sb.append(", passageRate=").append(passageRate);
        sb.append(", riskMode=").append(riskMode);
        sb.append(", maxDayAmount=").append(maxDayAmount);
        sb.append(", maxEveryAmount=").append(maxEveryAmount);
        sb.append(", minEveryAmount=").append(minEveryAmount);
        sb.append(", fixedAmount=").append(fixedAmount);
        sb.append(", tradeStartTime=").append(tradeStartTime);
        sb.append(", tradeEndTime=").append(tradeEndTime);
        sb.append(", riskStatus=").append(riskStatus);
        sb.append(", cashCollStatus=").append(cashCollStatus);
        sb.append(", cashCollMode=").append(cashCollMode);
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
        PayPassageAccount other = (PayPassageAccount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
            && (this.getPayPassageId() == null ? other.getPayPassageId() == null : this.getPayPassageId().equals(other.getPayPassageId()))
            && (this.getIfCode() == null ? other.getIfCode() == null : this.getIfCode().equals(other.getIfCode()))
            && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getParam() == null ? other.getParam() == null : this.getParam().equals(other.getParam()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPollWeight() == null ? other.getPollWeight() == null : this.getPollWeight().equals(other.getPollWeight()))
            && (this.getPassageMchId() == null ? other.getPassageMchId() == null : this.getPassageMchId().equals(other.getPassageMchId()))
            && (this.getPassageRate() == null ? other.getPassageRate() == null : this.getPassageRate().equals(other.getPassageRate()))
            && (this.getRiskMode() == null ? other.getRiskMode() == null : this.getRiskMode().equals(other.getRiskMode()))
            && (this.getMaxDayAmount() == null ? other.getMaxDayAmount() == null : this.getMaxDayAmount().equals(other.getMaxDayAmount()))
            && (this.getMaxEveryAmount() == null ? other.getMaxEveryAmount() == null : this.getMaxEveryAmount().equals(other.getMaxEveryAmount()))
            && (this.getMinEveryAmount() == null ? other.getMinEveryAmount() == null : this.getMinEveryAmount().equals(other.getMinEveryAmount()))
            && (this.getFixedAmount() == null ? other.getFixedAmount() == null : this.getFixedAmount().equals(other.getFixedAmount()))
            && (this.getTradeStartTime() == null ? other.getTradeStartTime() == null : this.getTradeStartTime().equals(other.getTradeStartTime()))
            && (this.getTradeEndTime() == null ? other.getTradeEndTime() == null : this.getTradeEndTime().equals(other.getTradeEndTime()))
            && (this.getRiskStatus() == null ? other.getRiskStatus() == null : this.getRiskStatus().equals(other.getRiskStatus()))
            && (this.getCashCollStatus() == null ? other.getCashCollStatus() == null : this.getCashCollStatus().equals(other.getCashCollStatus()))
            && (this.getCashCollMode() == null ? other.getCashCollMode() == null : this.getCashCollMode().equals(other.getCashCollMode()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getPayPassageId() == null) ? 0 : getPayPassageId().hashCode());
        result = prime * result + ((getIfCode() == null) ? 0 : getIfCode().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getParam() == null) ? 0 : getParam().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPollWeight() == null) ? 0 : getPollWeight().hashCode());
        result = prime * result + ((getPassageMchId() == null) ? 0 : getPassageMchId().hashCode());
        result = prime * result + ((getPassageRate() == null) ? 0 : getPassageRate().hashCode());
        result = prime * result + ((getRiskMode() == null) ? 0 : getRiskMode().hashCode());
        result = prime * result + ((getMaxDayAmount() == null) ? 0 : getMaxDayAmount().hashCode());
        result = prime * result + ((getMaxEveryAmount() == null) ? 0 : getMaxEveryAmount().hashCode());
        result = prime * result + ((getMinEveryAmount() == null) ? 0 : getMinEveryAmount().hashCode());
        result = prime * result + ((getFixedAmount() == null) ? 0 : getFixedAmount().hashCode());
        result = prime * result + ((getTradeStartTime() == null) ? 0 : getTradeStartTime().hashCode());
        result = prime * result + ((getTradeEndTime() == null) ? 0 : getTradeEndTime().hashCode());
        result = prime * result + ((getRiskStatus() == null) ? 0 : getRiskStatus().hashCode());
        result = prime * result + ((getCashCollStatus() == null) ? 0 : getCashCollStatus().hashCode());
        result = prime * result + ((getCashCollMode() == null) ? 0 : getCashCollMode().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}