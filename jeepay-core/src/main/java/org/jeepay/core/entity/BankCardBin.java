package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class BankCardBin implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * 通道类型代码
     *
     * @mbggenerated
     */
    private String ifTypeCode;

    /**
     * 卡bin
     *
     * @mbggenerated
     */
    private String cardBin;

    /**
     * 银行名称
     *
     * @mbggenerated
     */
    private String bankName;

    /**
     * 银行编码
     *
     * @mbggenerated
     */
    private String bankCode;

    /**
     * 卡名
     *
     * @mbggenerated
     */
    private String cardName;

    /**
     * 银行卡类型
     *
     * @mbggenerated
     */
    private String cardType;

    /**
     * 卡号长度
     *
     * @mbggenerated
     */
    private Integer cardLength;

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

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getCardLength() {
        return cardLength;
    }

    public void setCardLength(Integer cardLength) {
        this.cardLength = cardLength;
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
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", cardBin=").append(cardBin);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankCode=").append(bankCode);
        sb.append(", cardName=").append(cardName);
        sb.append(", cardType=").append(cardType);
        sb.append(", cardLength=").append(cardLength);
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
        BankCardBin other = (BankCardBin) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getCardBin() == null ? other.getCardBin() == null : this.getCardBin().equals(other.getCardBin()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankCode() == null ? other.getBankCode() == null : this.getBankCode().equals(other.getBankCode()))
            && (this.getCardName() == null ? other.getCardName() == null : this.getCardName().equals(other.getCardName()))
            && (this.getCardType() == null ? other.getCardType() == null : this.getCardType().equals(other.getCardType()))
            && (this.getCardLength() == null ? other.getCardLength() == null : this.getCardLength().equals(other.getCardLength()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getCardBin() == null) ? 0 : getCardBin().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankCode() == null) ? 0 : getBankCode().hashCode());
        result = prime * result + ((getCardName() == null) ? 0 : getCardName().hashCode());
        result = prime * result + ((getCardType() == null) ? 0 : getCardType().hashCode());
        result = prime * result + ((getCardLength() == null) ? 0 : getCardLength().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}