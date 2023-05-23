package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class MchQrCode implements Serializable {
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
     * 应用ID
     *
     * @mbggenerated
     */
    private String appId;

    /**
     * 状态,启用(1),停止(0)
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 支付渠道,json格式[wx,alipay]
     *
     * @mbggenerated
     */
    private String channels;

    private String codeName;

    /**
     * 最小金额,单位分
     *
     * @mbggenerated
     */
    private Long minAmount;

    /**
     * 最大金额,单位分
     *
     * @mbggenerated
     */
    private Long maxAmount;

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Long minAmount) {
        this.minAmount = minAmount;
    }

    public Long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Long maxAmount) {
        this.maxAmount = maxAmount;
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
        sb.append(", appId=").append(appId);
        sb.append(", status=").append(status);
        sb.append(", channels=").append(channels);
        sb.append(", codeName=").append(codeName);
        sb.append(", minAmount=").append(minAmount);
        sb.append(", maxAmount=").append(maxAmount);
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
        MchQrCode other = (MchQrCode) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getChannels() == null ? other.getChannels() == null : this.getChannels().equals(other.getChannels()))
            && (this.getCodeName() == null ? other.getCodeName() == null : this.getCodeName().equals(other.getCodeName()))
            && (this.getMinAmount() == null ? other.getMinAmount() == null : this.getMinAmount().equals(other.getMinAmount()))
            && (this.getMaxAmount() == null ? other.getMaxAmount() == null : this.getMaxAmount().equals(other.getMaxAmount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getChannels() == null) ? 0 : getChannels().hashCode());
        result = prime * result + ((getCodeName() == null) ? 0 : getCodeName().hashCode());
        result = prime * result + ((getMinAmount() == null) ? 0 : getMinAmount().hashCode());
        result = prime * result + ((getMaxAmount() == null) ? 0 : getMaxAmount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}