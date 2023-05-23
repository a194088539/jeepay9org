package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class MchBill implements Serializable {
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
     * 名称
     *
     * @mbggenerated
     */
    private String mchName;

    /**
     * 类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    private Byte mchType;

    /**
     * 账单时间
     *
     * @mbggenerated
     */
    private Date billDate;

    /**
     * 账单状态:0-初始,1-生成中,2-已生成,3-生成失败
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 账单地址
     *
     * @mbggenerated
     */
    private String billPath;

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

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getBillPath() {
        return billPath;
    }

    public void setBillPath(String billPath) {
        this.billPath = billPath;
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
        sb.append(", mchName=").append(mchName);
        sb.append(", mchType=").append(mchType);
        sb.append(", billDate=").append(billDate);
        sb.append(", status=").append(status);
        sb.append(", billPath=").append(billPath);
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
        MchBill other = (MchBill) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getMchName() == null ? other.getMchName() == null : this.getMchName().equals(other.getMchName()))
            && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
            && (this.getBillDate() == null ? other.getBillDate() == null : this.getBillDate().equals(other.getBillDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBillPath() == null ? other.getBillPath() == null : this.getBillPath().equals(other.getBillPath()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getMchName() == null) ? 0 : getMchName().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getBillDate() == null) ? 0 : getBillDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBillPath() == null) ? 0 : getBillPath().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}