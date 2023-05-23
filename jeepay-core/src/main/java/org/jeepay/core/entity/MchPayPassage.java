package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MchPayPassage implements Serializable {
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
     * 产品ID
     *
     * @mbggenerated
     */
    private Integer productId;

    /**
     * 商户费率,百分比
     *
     * @mbggenerated
     */
    private BigDecimal mchRate;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 接口模式,1-单独,2-轮询
     *
     * @mbggenerated
     */
    private Byte ifMode;

    /**
     * 支付通道ID
     *
     * @mbggenerated
     */
    private Integer payPassageId;

    /**
     * 支付通道账户ID
     *
     * @mbggenerated
     */
    private Integer payPassageAccountId;

    /**
     * 轮询配置参数,json字符串
     *
     * @mbggenerated
     */
    private String pollParam;

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
     * 产品类型:1-收款,2-充值
     *
     * @mbggenerated
     */
    private Byte productType;

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getMchRate() {
        return mchRate;
    }

    public void setMchRate(BigDecimal mchRate) {
        this.mchRate = mchRate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIfMode() {
        return ifMode;
    }

    public void setIfMode(Byte ifMode) {
        this.ifMode = ifMode;
    }

    public Integer getPayPassageId() {
        return payPassageId;
    }

    public void setPayPassageId(Integer payPassageId) {
        this.payPassageId = payPassageId;
    }

    public Integer getPayPassageAccountId() {
        return payPassageAccountId;
    }

    public void setPayPassageAccountId(Integer payPassageAccountId) {
        this.payPassageAccountId = payPassageAccountId;
    }

    public String getPollParam() {
        return pollParam;
    }

    public void setPollParam(String pollParam) {
        this.pollParam = pollParam;
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

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mchId=").append(mchId);
        sb.append(", productId=").append(productId);
        sb.append(", mchRate=").append(mchRate);
        sb.append(", status=").append(status);
        sb.append(", ifMode=").append(ifMode);
        sb.append(", payPassageId=").append(payPassageId);
        sb.append(", payPassageAccountId=").append(payPassageAccountId);
        sb.append(", pollParam=").append(pollParam);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", productType=").append(productType);
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
        MchPayPassage other = (MchPayPassage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getMchRate() == null ? other.getMchRate() == null : this.getMchRate().equals(other.getMchRate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfMode() == null ? other.getIfMode() == null : this.getIfMode().equals(other.getIfMode()))
            && (this.getPayPassageId() == null ? other.getPayPassageId() == null : this.getPayPassageId().equals(other.getPayPassageId()))
            && (this.getPayPassageAccountId() == null ? other.getPayPassageAccountId() == null : this.getPayPassageAccountId().equals(other.getPayPassageAccountId()))
            && (this.getPollParam() == null ? other.getPollParam() == null : this.getPollParam().equals(other.getPollParam()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getProductType() == null ? other.getProductType() == null : this.getProductType().equals(other.getProductType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getMchRate() == null) ? 0 : getMchRate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIfMode() == null) ? 0 : getIfMode().hashCode());
        result = prime * result + ((getPayPassageId() == null) ? 0 : getPayPassageId().hashCode());
        result = prime * result + ((getPayPassageAccountId() == null) ? 0 : getPayPassageAccountId().hashCode());
        result = prime * result + ((getPollParam() == null) ? 0 : getPollParam().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getProductType() == null) ? 0 : getProductType().hashCode());
        return result;
    }
}