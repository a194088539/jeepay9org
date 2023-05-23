package org.jeepay.common.bean;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * 转账订单
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2018/1/31
 * </pre>
 */
public class TransferOrder {

    /**
     * 转账订单单号
     */
    private String outNo;

    /**
     * 收款方账户, 用户openid
     */
    private String  payeeAccount ;

    /**
     * 转账金额
     */
    private BigDecimal amount ;

    /**
     * 付款人名称
     */
    private String payerName;
    /**
     * 收款人银行卡开户行
     */
    private String payeeBranchName;

    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 收款人银行卡对应手机号
     */
    private String payeePhone;
    /**
     * 收款人身份证号码
     */
    private String payeeIdNum;
    /**
     * 备注
     */
    private String remark;

    /**
     * 收款开户行
     */
    private Bank bank;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行卡所属省份
     */
    private String bankProvince;
    /**
     * 银行卡所属城市
     */
    private String bankCity;
    /**
     * 币种
     */
    private CurType curType;
    /**
     * 转账类型，收款方账户类型，比如支付宝账户或者银行卡
     */
    private TransferType transferType;
    /**
     * 商户发起时间 格式：yyyymmddhhMMss
     */
    private String tranTime;
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 请求ip
     */
    private String spbillIp;

    /**
     * 结算周期 0：d0 1：t1
     */
    private String cycle;
    /**
     * 联行卡号
     */
    private String bankUnionid;

    private JSONObject ext;

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeBranchName() {
        return payeeBranchName;
    }

    public void setPayeeBranchName(String payeeBranchName) {
        this.payeeBranchName = payeeBranchName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeePhone() {
        return payeePhone;
    }

    public void setPayeePhone(String payeePhone) {
        this.payeePhone = payeePhone;
    }

    public String getPayeeIdNum() {
        return payeeIdNum;
    }

    public void setPayeeIdNum(String payeeIdNum) {
        this.payeeIdNum = payeeIdNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public CurType getCurType() {
        return curType;
    }

    public void setCurType(CurType curType) {
        this.curType = curType;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSpbillIp() {
        return spbillIp;
    }

    public void setSpbillIp(String spbillIp) {
        this.spbillIp = spbillIp;
    }

    public JSONObject getExt() {
        return ext;
    }

    public void setExt(JSONObject ext) {
        this.ext = ext;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getBankUnionid() {
        return bankUnionid;
    }

    public void setBankUnionid(String bankUnionid) {
        this.bankUnionid = bankUnionid;
    }
}

