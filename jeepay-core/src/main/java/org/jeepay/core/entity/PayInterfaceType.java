package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class PayInterfaceType implements Serializable {
    /**
     * 接口类型代码
     *
     * @mbggenerated
     */
    private String ifTypeCode;

    /**
     * 接口类型名称
     *
     * @mbggenerated
     */
    private String ifTypeName;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 接口配置定义描述,json字符串
     *
     * @mbggenerated
     */
    private String param;

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

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }

    public String getIfTypeName() {
        return ifTypeName;
    }

    public void setIfTypeName(String ifTypeName) {
        this.ifTypeName = ifTypeName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
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
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", ifTypeName=").append(ifTypeName);
        sb.append(", status=").append(status);
        sb.append(", param=").append(param);
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
        PayInterfaceType other = (PayInterfaceType) that;
        return (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getIfTypeName() == null ? other.getIfTypeName() == null : this.getIfTypeName().equals(other.getIfTypeName()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getParam() == null ? other.getParam() == null : this.getParam().equals(other.getParam()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getIfTypeName() == null) ? 0 : getIfTypeName().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getParam() == null) ? 0 : getParam().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}