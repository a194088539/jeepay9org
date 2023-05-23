package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class PayOrder implements Serializable {
    /**
     * 支付订单号
     *
     * @mbggenerated
     */
    private String payOrderId;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private Long mchId;

    /**
     * 商户名字
     *
     * @mbggenerated
     */
    private String mchName;

    /**
     * 商户类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    private Byte mchType;

    /**
     * 商户费率
     *
     * @mbggenerated
     */
    private BigDecimal mchRate;

    /**
     * 商户入账,单位分
     *
     * @mbggenerated
     */
    private Long mchIncome;

    /**
     * 应用ID
     *
     * @mbggenerated
     */
    private String appId;

    /**
     * 商户订单号
     *
     * @mbggenerated
     */
    private String mchOrderNo;

    /**
     * 代理商ID
     *
     * @mbggenerated
     */
    private Long agentId;

    /**
     * 一级代理商ID
     *
     * @mbggenerated
     */
    private Long parentAgentId;

    /**
     * 代理商费率
     *
     * @mbggenerated
     */
    private BigDecimal agentRate;

    /**
     * 一级代理商费率
     *
     * @mbggenerated
     */
    private BigDecimal parentAgentRate;

    /**
     * 代理商利润,单位分
     *
     * @mbggenerated
     */
    private Long agentProfit;

    /**
     * 一级代理商利润
     *
     * @mbggenerated
     */
    private Long parentAgentProfit;

    /**
     * 支付产品ID
     *
     * @mbggenerated
     */
    private Integer productId;

    /**
     * 通道ID
     *
     * @mbggenerated
     */
    private Integer passageId;

    /**
     * 通道账户ID
     *
     * @mbggenerated
     */
    private Integer passageAccountId;

    /**
     * 渠道类型,对接支付接口类型代码
     *
     * @mbggenerated
     */
    private String channelType;

    /**
     * 渠道类型
     *
     * @mbggenerated
     */
    private String channelName;

    /**
     * 渠道ID,对应支付接口代码
     *
     * @mbggenerated
     */
    private String channelId;

    /**
     * 支付金额,单位分
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     * 三位货币代码,人民币:cny
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * 支付状态,0-订单生成,1-支付中,2-支付成功,3-业务处理完成,4-已退款
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 客户端IP
     *
     * @mbggenerated
     */
    private String clientIp;

    /**
     * 设备
     *
     * @mbggenerated
     */
    private String device;

    /**
     * 商品标题
     *
     * @mbggenerated
     */
    private String subject;

    /**
     * 商品描述信息
     *
     * @mbggenerated
     */
    private String body;

    /**
     * 特定渠道发起额外参数
     *
     * @mbggenerated
     */
    private String extra;

    /**
     * 渠道用户标识,如微信openId,支付宝账号
     *
     * @mbggenerated
     */
    private String channelUser;

    /**
     * 渠道商户ID
     *
     * @mbggenerated
     */
    private String channelMchId;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    private String channelOrderNo;

    /**
     * 渠道数据包
     *
     * @mbggenerated
     */
    private String channelAttach;

    /**
     * 平台利润,单位分
     *
     * @mbggenerated
     */
    private Long platProfit;

    /**
     * 渠道费率
     *
     * @mbggenerated
     */
    private BigDecimal channelRate;

    /**
     * 渠道成本,单位分
     *
     * @mbggenerated
     */
    private Long channelCost;

    /**
     * 是否退款,0-未退款,1-退款
     *
     * @mbggenerated
     */
    private Byte isRefund;

    /**
     * 退款次数
     *
     * @mbggenerated
     */
    private Integer refundTimes;

    /**
     * 成功退款金额,单位分
     *
     * @mbggenerated
     */
    private Long successRefundAmount;

    /**
     * 渠道支付错误码
     *
     * @mbggenerated
     */
    private String errCode;

    /**
     * 渠道支付错误描述
     *
     * @mbggenerated
     */
    private String errMsg;

    /**
     * 扩展参数1
     *
     * @mbggenerated
     */
    private String param1;

    /**
     * 扩展参数2
     *
     * @mbggenerated
     */
    private String param2;

    /**
     * 通知地址
     *
     * @mbggenerated
     */
    private String notifyUrl;

    /**
     * 跳转地址
     *
     * @mbggenerated
     */
    private String returnUrl;

    /**
     * 订单失效时间
     *
     * @mbggenerated
     */
    private Date expireTime;

    /**
     * 订单支付成功时间
     *
     * @mbggenerated
     */
    private Date paySuccTime;

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
    
   private JSONObject params;
   
    private Byte productType;

    private static final long serialVersionUID = 1L;

    public PayOrder() {
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
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

    public BigDecimal getMchRate() {
        return mchRate;
    }

    public void setMchRate(BigDecimal mchRate) {
        this.mchRate = mchRate;
    }

    public Long getMchIncome() {
        return mchIncome;
    }

    public void setMchIncome(Long mchIncome) {
        this.mchIncome = mchIncome;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(Long parentAgentId) {
        this.parentAgentId = parentAgentId;
    }

    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    public BigDecimal getParentAgentRate() {
        return parentAgentRate;
    }

    public void setParentAgentRate(BigDecimal parentAgentRate) {
        this.parentAgentRate = parentAgentRate;
    }

    public Long getAgentProfit() {
        return agentProfit;
    }

    public void setAgentProfit(Long agentProfit) {
        this.agentProfit = agentProfit;
    }

    public Long getParentAgentProfit() {
        return parentAgentProfit;
    }

    public void setParentAgentProfit(Long parentAgentProfit) {
        this.parentAgentProfit = parentAgentProfit;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPassageId() {
        return passageId;
    }

    public void setPassageId(Integer passageId) {
        this.passageId = passageId;
    }

    public Integer getPassageAccountId() {
        return passageAccountId;
    }

    public void setPassageAccountId(Integer passageAccountId) {
        this.passageAccountId = passageAccountId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public String getChannelMchId() {
        return channelMchId;
    }

    public void setChannelMchId(String channelMchId) {
        this.channelMchId = channelMchId;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getChannelAttach() {
        return channelAttach;
    }

    public void setChannelAttach(String channelAttach) {
        this.channelAttach = channelAttach;
    }

    public Long getPlatProfit() {
        return platProfit;
    }

    public void setPlatProfit(Long platProfit) {
        this.platProfit = platProfit;
    }

    public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public Long getChannelCost() {
        return channelCost;
    }

    public void setChannelCost(Long channelCost) {
        this.channelCost = channelCost;
    }

    public Byte getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Byte isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getRefundTimes() {
        return refundTimes;
    }

    public void setRefundTimes(Integer refundTimes) {
        this.refundTimes = refundTimes;
    }

    public Long getSuccessRefundAmount() {
        return successRefundAmount;
    }

    public void setSuccessRefundAmount(Long successRefundAmount) {
        this.successRefundAmount = successRefundAmount;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(Date paySuccTime) {
        this.paySuccTime = paySuccTime;
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
    
    public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", payOrderId=").append(payOrderId);
        sb.append(", mchId=").append(mchId);
        sb.append(", mchName=").append(mchName);
        sb.append(", mchType=").append(mchType);
        sb.append(", mchRate=").append(mchRate);
        sb.append(", mchIncome=").append(mchIncome);
        sb.append(", appId=").append(appId);
        sb.append(", mchOrderNo=").append(mchOrderNo);
        sb.append(", agentId=").append(agentId);
        sb.append(", parentAgentId=").append(parentAgentId);
        sb.append(", agentRate=").append(agentRate);
        sb.append(", parentAgentRate=").append(parentAgentRate);
        sb.append(", agentProfit=").append(agentProfit);
        sb.append(", parentAgentProfit=").append(parentAgentProfit);
        sb.append(", productId=").append(productId);
        sb.append(", passageId=").append(passageId);
        sb.append(", passageAccountId=").append(passageAccountId);
        sb.append(", channelType=").append(channelType);
        sb.append(", channelName=").append(channelName);
        sb.append(", channelId=").append(channelId);
        sb.append(", amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append(", status=").append(status);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", device=").append(device);
        sb.append(", subject=").append(subject);
        sb.append(", body=").append(body);
        sb.append(", extra=").append(extra);
        sb.append(", channelUser=").append(channelUser);
        sb.append(", channelMchId=").append(channelMchId);
        sb.append(", channelOrderNo=").append(channelOrderNo);
        sb.append(", channelAttach=").append(channelAttach);
        sb.append(", platProfit=").append(platProfit);
        sb.append(", channelRate=").append(channelRate);
        sb.append(", channelCost=").append(channelCost);
        sb.append(", isRefund=").append(isRefund);
        sb.append(", refundTimes=").append(refundTimes);
        sb.append(", successRefundAmount=").append(successRefundAmount);
        sb.append(", errCode=").append(errCode);
        sb.append(", errMsg=").append(errMsg);
        sb.append(", param1=").append(param1);
        sb.append(", param2=").append(param2);
        sb.append(", notifyUrl=").append(notifyUrl);
        sb.append(", returnUrl=").append(returnUrl);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", paySuccTime=").append(paySuccTime);
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
        PayOrder other = (PayOrder) that;
        return (this.getPayOrderId() == null ? other.getPayOrderId() == null : this.getPayOrderId().equals(other.getPayOrderId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getMchName() == null ? other.getMchName() == null : this.getMchName().equals(other.getMchName()))
            && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
            && (this.getMchRate() == null ? other.getMchRate() == null : this.getMchRate().equals(other.getMchRate()))
            && (this.getMchIncome() == null ? other.getMchIncome() == null : this.getMchIncome().equals(other.getMchIncome()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getMchOrderNo() == null ? other.getMchOrderNo() == null : this.getMchOrderNo().equals(other.getMchOrderNo()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getParentAgentId() == null ? other.getParentAgentId() == null : this.getParentAgentId().equals(other.getParentAgentId()))
            && (this.getAgentRate() == null ? other.getAgentRate() == null : this.getAgentRate().equals(other.getAgentRate()))
            && (this.getParentAgentRate() == null ? other.getParentAgentRate() == null : this.getParentAgentRate().equals(other.getParentAgentRate()))
            && (this.getAgentProfit() == null ? other.getAgentProfit() == null : this.getAgentProfit().equals(other.getAgentProfit()))
            && (this.getParentAgentProfit() == null ? other.getParentAgentProfit() == null : this.getParentAgentProfit().equals(other.getParentAgentProfit()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getPassageId() == null ? other.getPassageId() == null : this.getPassageId().equals(other.getPassageId()))
            && (this.getPassageAccountId() == null ? other.getPassageAccountId() == null : this.getPassageAccountId().equals(other.getPassageAccountId()))
            && (this.getChannelType() == null ? other.getChannelType() == null : this.getChannelType().equals(other.getChannelType()))
            && (this.getChannelName() == null ? other.getChannelName() == null : this.getChannelName().equals(other.getChannelName()))
            && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
            && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
            && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()))
            && (this.getChannelUser() == null ? other.getChannelUser() == null : this.getChannelUser().equals(other.getChannelUser()))
            && (this.getChannelMchId() == null ? other.getChannelMchId() == null : this.getChannelMchId().equals(other.getChannelMchId()))
            && (this.getChannelOrderNo() == null ? other.getChannelOrderNo() == null : this.getChannelOrderNo().equals(other.getChannelOrderNo()))
            && (this.getChannelAttach() == null ? other.getChannelAttach() == null : this.getChannelAttach().equals(other.getChannelAttach()))
            && (this.getPlatProfit() == null ? other.getPlatProfit() == null : this.getPlatProfit().equals(other.getPlatProfit()))
            && (this.getChannelRate() == null ? other.getChannelRate() == null : this.getChannelRate().equals(other.getChannelRate()))
            && (this.getChannelCost() == null ? other.getChannelCost() == null : this.getChannelCost().equals(other.getChannelCost()))
            && (this.getIsRefund() == null ? other.getIsRefund() == null : this.getIsRefund().equals(other.getIsRefund()))
            && (this.getRefundTimes() == null ? other.getRefundTimes() == null : this.getRefundTimes().equals(other.getRefundTimes()))
            && (this.getSuccessRefundAmount() == null ? other.getSuccessRefundAmount() == null : this.getSuccessRefundAmount().equals(other.getSuccessRefundAmount()))
            && (this.getErrCode() == null ? other.getErrCode() == null : this.getErrCode().equals(other.getErrCode()))
            && (this.getErrMsg() == null ? other.getErrMsg() == null : this.getErrMsg().equals(other.getErrMsg()))
            && (this.getParam1() == null ? other.getParam1() == null : this.getParam1().equals(other.getParam1()))
            && (this.getParam2() == null ? other.getParam2() == null : this.getParam2().equals(other.getParam2()))
            && (this.getNotifyUrl() == null ? other.getNotifyUrl() == null : this.getNotifyUrl().equals(other.getNotifyUrl()))
            && (this.getReturnUrl() == null ? other.getReturnUrl() == null : this.getReturnUrl().equals(other.getReturnUrl()))
            && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
            && (this.getPaySuccTime() == null ? other.getPaySuccTime() == null : this.getPaySuccTime().equals(other.getPaySuccTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getProductType() == null ? other.getProductType() == null : this.getProductType().equals(other.getProductType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPayOrderId() == null) ? 0 : getPayOrderId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getMchName() == null) ? 0 : getMchName().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getMchRate() == null) ? 0 : getMchRate().hashCode());
        result = prime * result + ((getMchIncome() == null) ? 0 : getMchIncome().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getMchOrderNo() == null) ? 0 : getMchOrderNo().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getParentAgentId() == null) ? 0 : getParentAgentId().hashCode());
        result = prime * result + ((getAgentRate() == null) ? 0 : getAgentRate().hashCode());
        result = prime * result + ((getParentAgentRate() == null) ? 0 : getParentAgentRate().hashCode());
        result = prime * result + ((getAgentProfit() == null) ? 0 : getAgentProfit().hashCode());
        result = prime * result + ((getParentAgentProfit() == null) ? 0 : getParentAgentProfit().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getPassageId() == null) ? 0 : getPassageId().hashCode());
        result = prime * result + ((getPassageAccountId() == null) ? 0 : getPassageAccountId().hashCode());
        result = prime * result + ((getChannelType() == null) ? 0 : getChannelType().hashCode());
        result = prime * result + ((getChannelName() == null) ? 0 : getChannelName().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        result = prime * result + ((getChannelUser() == null) ? 0 : getChannelUser().hashCode());
        result = prime * result + ((getChannelMchId() == null) ? 0 : getChannelMchId().hashCode());
        result = prime * result + ((getChannelOrderNo() == null) ? 0 : getChannelOrderNo().hashCode());
        result = prime * result + ((getChannelAttach() == null) ? 0 : getChannelAttach().hashCode());
        result = prime * result + ((getPlatProfit() == null) ? 0 : getPlatProfit().hashCode());
        result = prime * result + ((getChannelRate() == null) ? 0 : getChannelRate().hashCode());
        result = prime * result + ((getChannelCost() == null) ? 0 : getChannelCost().hashCode());
        result = prime * result + ((getIsRefund() == null) ? 0 : getIsRefund().hashCode());
        result = prime * result + ((getRefundTimes() == null) ? 0 : getRefundTimes().hashCode());
        result = prime * result + ((getSuccessRefundAmount() == null) ? 0 : getSuccessRefundAmount().hashCode());
        result = prime * result + ((getErrCode() == null) ? 0 : getErrCode().hashCode());
        result = prime * result + ((getErrMsg() == null) ? 0 : getErrMsg().hashCode());
        result = prime * result + ((getParam1() == null) ? 0 : getParam1().hashCode());
        result = prime * result + ((getParam2() == null) ? 0 : getParam2().hashCode());
        result = prime * result + ((getNotifyUrl() == null) ? 0 : getNotifyUrl().hashCode());
        result = prime * result + ((getReturnUrl() == null) ? 0 : getReturnUrl().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        result = prime * result + ((getPaySuccTime() == null) ? 0 : getPaySuccTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getProductType() == null) ? 0 : getProductType().hashCode());
        return result;
    }
}