package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayCashCollConfig implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 分账收入方姓名
     *
     * @mbggenerated
     */
    private String transInUserName;

    /**
     * 分账收入方账号
     *
     * @mbggenerated
     */
    private String transInUserAccount;

    /**
     * 分账收入方用户ID (支付宝PID)
     *
     * @mbggenerated
     */
    private String transInUserId;

    /**
     * 分账比例,百分比
     *
     * @mbggenerated
     */
    private BigDecimal transInPercentage;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 所属通道子账户ID, 系统全局配置为0
     *
     * @mbggenerated
     */
    private Integer belongPayAccountId;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getBelongPayAccountId() {
        return belongPayAccountId;
    }

    public void setBelongPayAccountId(Integer belongPayAccountId) {
        this.belongPayAccountId = belongPayAccountId;
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
        sb.append(", transInUserName=").append(transInUserName);
        sb.append(", transInUserAccount=").append(transInUserAccount);
        sb.append(", transInUserId=").append(transInUserId);
        sb.append(", transInPercentage=").append(transInPercentage);
        sb.append(", status=").append(status);
        sb.append(", belongPayAccountId=").append(belongPayAccountId);
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
        PayCashCollConfig other = (PayCashCollConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTransInUserName() == null ? other.getTransInUserName() == null : this.getTransInUserName().equals(other.getTransInUserName()))
                && (this.getTransInUserAccount() == null ? other.getTransInUserAccount() == null : this.getTransInUserAccount().equals(other.getTransInUserAccount()))
                && (this.getTransInUserId() == null ? other.getTransInUserId() == null : this.getTransInUserId().equals(other.getTransInUserId()))
                && (this.getTransInPercentage() == null ? other.getTransInPercentage() == null : this.getTransInPercentage().equals(other.getTransInPercentage()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getBelongPayAccountId() == null ? other.getBelongPayAccountId() == null : this.getBelongPayAccountId().equals(other.getBelongPayAccountId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTransInUserName() == null) ? 0 : getTransInUserName().hashCode());
        result = prime * result + ((getTransInUserAccount() == null) ? 0 : getTransInUserAccount().hashCode());
        result = prime * result + ((getTransInUserId() == null) ? 0 : getTransInUserId().hashCode());
        result = prime * result + ((getTransInPercentage() == null) ? 0 : getTransInPercentage().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBelongPayAccountId() == null) ? 0 : getBelongPayAccountId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}