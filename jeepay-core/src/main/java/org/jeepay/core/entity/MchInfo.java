package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MchInfo implements Serializable {
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
    private String name;

    /**
     * 用户登录名
     *
     * @mbggenerated
     */
    private String userName;

    /**
     * 用户真实姓名
     *
     * @mbggenerated
     */
    private String realName;

    /**
     * 类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    private Byte type;

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
     * 手机号
     *
     * @mbggenerated
     */
    private Long mobile;

    /**
     * 邮箱
     *
     * @mbggenerated
     */
    private String email;

    /**
     * 密码
     *
     * @mbggenerated
     */
    private String password;

    /**
     * 支付密码,默认abc123456
     *
     * @mbggenerated
     */
    private String payPassword;

    /**
     * 角色
     *
     * @mbggenerated
     */
    private String role;

    /**
     * 商户费率,百分比
     *
     * @mbggenerated
     */
    private BigDecimal mchRate;

    /**
     * 私钥
     *
     * @mbggenerated
     */
    private String privateKey;

    /**
     * 通讯地址
     *
     * @mbggenerated
     */
    private String address;

    /**
     * 账户属性:0-对私,1-对公,默认对私
     *
     * @mbggenerated
     */
    private Byte accountAttr;

    /**
     * 账户类型:1-银行卡转账,2-微信转账,3-支付宝转账
     *
     * @mbggenerated
     */
    private Byte accountType;

    /**
     * 开户行名称
     *
     * @mbggenerated
     */
    private String bankName;

    /**
     * 开户网点名称
     *
     * @mbggenerated
     */
    private String bankNetName;

    /**
     * 账户名
     *
     * @mbggenerated
     */
    private String accountName;

    /**
     * 账户号
     *
     * @mbggenerated
     */
    private String accountNo;

    /**
     * 开户行所在省
     *
     * @mbggenerated
     */
    private String province;

    /**
     * 开户行所在市
     *
     * @mbggenerated
     */
    private String city;

    /**
     * 手续费类型,1-百分比收费,2-固定收费
     *
     * @mbggenerated
     */
    private Byte feeType;

    /**
     * 手续费百分比值
     *
     * @mbggenerated
     */
    private BigDecimal feeRate;

    /**
     * 手续费等级值,使用json存储每一等级信息
     *
     * @mbggenerated
     */
    private String feeLevel;

    /**
     * 风险预存期
     *
     * @mbggenerated
     */
    private Integer riskDay;

    /**
     * 最大提现金额,单位分
     *
     * @mbggenerated
     */
    private Long maxDrawAmount;

    /**
     * 最小提现金额,单位分
     *
     * @mbggenerated
     */
    private Long minDrawAmount;

    /**
     * 每日提现次数
     *
     * @mbggenerated
     */
    private Integer dayDrawTimes;

    /**
     * 结算类型,0-手动提现,1-自动结算
     *
     * @mbggenerated
     */
    private Byte settType;

    /**
     * 结算方式,1-D0到账,2-D1到账,3-T0到账,4-T1到账
     *
     * @mbggenerated
     */
    private Byte settMode;

    /**
     * 商户状态,-1-待审核,0-停止使用,1-使用中
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 备注
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * 最后一次重置密码时间
     *
     * @mbggenerated
     */
    private Date lastPasswordResetTime;

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
     * 标记
     *
     * @mbggenerated
     */
    private String tag;

    /**
     * 登录安全类型,1-仅登录密码验证,2-登录密码+谷歌组合验证
     *
     * @mbggenerated
     */
    private Byte loginSecurityType;

    /**
     * 支付安全类型,0-无需验证,1-仅支付密码验证,2-仅谷歌验证,3-支付密码+谷歌组合验证
     *
     * @mbggenerated
     */
    private Byte paySecurityType;

    /**
     * 绑定谷歌验证状态,0-未绑定,1-已绑定
     *
     * @mbggenerated
     */
    private Byte googleAuthStatus;

    /**
     * 谷歌验证密钥
     *
     * @mbggenerated
     */
    private String googleAuthSecretKey;

    /**
     * 身份证号
     *
     * @mbggenerated
     */
    private String idCard;

    /**
     * QQ号码
     *
     * @mbggenerated
     */
    private String qq;

    /**
     * 网站名称
     *
     * @mbggenerated
     */
    private String siteName;

    /**
     * 网站地址
     *
     * @mbggenerated
     */
    private String siteUrl;

    /**
     * 登录白名单,半角逗号分隔
     *
     * @mbggenerated
     */
    private String loginWhiteIp;

    /**
     * 登录黑名单,半角逗号分隔
     *
     * @mbggenerated
     */
    private String loginBlackIp;

    /**
     * 支付白名单,半角逗号分隔
     *
     * @mbggenerated
     */
    private String payWhiteIp;

    /**
     * 支付黑名单,半角逗号分隔
     *
     * @mbggenerated
     */
    private String payBlackIp;

    /**
     * 代付白名单,半角逗号分隔
     *
     * @mbggenerated
     */
    private String agentpayWhiteIp;

    /**
     * 代付黑名单,半角逗号分隔
     *
     * @mbggenerated
     */
    private String agentpayBlackIp;

    /**
     * 结算配置模式,1-继承系统,2-自定义
     *
     * @mbggenerated
     */
    private Byte settConfigMode;

    /**
     * 提现开关:0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte drawFlag;

    /**
     * 每周周几允许提现,数字表示,多个逗号分隔
     *
     * @mbggenerated
     */
    private String allowDrawWeekDay;

    /**
     * 每天提现开始时间
     *
     * @mbggenerated
     */
    private String drawDayStartTime;

    /**
     * 每天提现结束时间
     *
     * @mbggenerated
     */
    private String drawDayEndTime;

    /**
     * 每天提现最大金额,单位分
     *
     * @mbggenerated
     */
    private Long drawMaxDayAmount;

    /**
     * 每笔提现手续费上限,单位分
     *
     * @mbggenerated
     */
    private Long drawFeeLimit;

    /**
     * 最后一次登录IP
     *
     * @mbggenerated
     */
    private String lastLoginIp;

    /**
     * 最后一次登录时间
     *
     * @mbggenerated
     */
    private Date lastLoginTime;

    /**
     * 线下充值费率,百分比
     *
     * @mbggenerated
     */
    private BigDecimal offRechargeRate;

    private static final long serialVersionUID = 1L;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getMchRate() {
        return mchRate;
    }

    public void setMchRate(BigDecimal mchRate) {
        this.mchRate = mchRate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Byte getAccountAttr() {
        return accountAttr;
    }

    public void setAccountAttr(Byte accountAttr) {
        this.accountAttr = accountAttr;
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNetName() {
        return bankNetName;
    }

    public void setBankNetName(String bankNetName) {
        this.bankNetName = bankNetName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Byte getFeeType() {
        return feeType;
    }

    public void setFeeType(Byte feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public String getFeeLevel() {
        return feeLevel;
    }

    public void setFeeLevel(String feeLevel) {
        this.feeLevel = feeLevel;
    }

    public Integer getRiskDay() {
        return riskDay;
    }

    public void setRiskDay(Integer riskDay) {
        this.riskDay = riskDay;
    }

    public Long getMaxDrawAmount() {
        return maxDrawAmount;
    }

    public void setMaxDrawAmount(Long maxDrawAmount) {
        this.maxDrawAmount = maxDrawAmount;
    }

    public Long getMinDrawAmount() {
        return minDrawAmount;
    }

    public void setMinDrawAmount(Long minDrawAmount) {
        this.minDrawAmount = minDrawAmount;
    }

    public Integer getDayDrawTimes() {
        return dayDrawTimes;
    }

    public void setDayDrawTimes(Integer dayDrawTimes) {
        this.dayDrawTimes = dayDrawTimes;
    }

    public Byte getSettType() {
        return settType;
    }

    public void setSettType(Byte settType) {
        this.settType = settType;
    }

    public Byte getSettMode() {
        return settMode;
    }

    public void setSettMode(Byte settMode) {
        this.settMode = settMode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getLastPasswordResetTime() {
        return lastPasswordResetTime;
    }

    public void setLastPasswordResetTime(Date lastPasswordResetTime) {
        this.lastPasswordResetTime = lastPasswordResetTime;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Byte getLoginSecurityType() {
        return loginSecurityType;
    }

    public void setLoginSecurityType(Byte loginSecurityType) {
        this.loginSecurityType = loginSecurityType;
    }

    public Byte getPaySecurityType() {
        return paySecurityType;
    }

    public void setPaySecurityType(Byte paySecurityType) {
        this.paySecurityType = paySecurityType;
    }

    public Byte getGoogleAuthStatus() {
        return googleAuthStatus;
    }

    public void setGoogleAuthStatus(Byte googleAuthStatus) {
        this.googleAuthStatus = googleAuthStatus;
    }

    public String getGoogleAuthSecretKey() {
        return googleAuthSecretKey;
    }

    public void setGoogleAuthSecretKey(String googleAuthSecretKey) {
        this.googleAuthSecretKey = googleAuthSecretKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getLoginWhiteIp() {
        return loginWhiteIp;
    }

    public void setLoginWhiteIp(String loginWhiteIp) {
        this.loginWhiteIp = loginWhiteIp;
    }

    public String getLoginBlackIp() {
        return loginBlackIp;
    }

    public void setLoginBlackIp(String loginBlackIp) {
        this.loginBlackIp = loginBlackIp;
    }

    public String getPayWhiteIp() {
        return payWhiteIp;
    }

    public void setPayWhiteIp(String payWhiteIp) {
        this.payWhiteIp = payWhiteIp;
    }

    public String getPayBlackIp() {
        return payBlackIp;
    }

    public void setPayBlackIp(String payBlackIp) {
        this.payBlackIp = payBlackIp;
    }

    public String getAgentpayWhiteIp() {
        return agentpayWhiteIp;
    }

    public void setAgentpayWhiteIp(String agentpayWhiteIp) {
        this.agentpayWhiteIp = agentpayWhiteIp;
    }

    public String getAgentpayBlackIp() {
        return agentpayBlackIp;
    }

    public void setAgentpayBlackIp(String agentpayBlackIp) {
        this.agentpayBlackIp = agentpayBlackIp;
    }

    public Byte getSettConfigMode() {
        return settConfigMode;
    }

    public void setSettConfigMode(Byte settConfigMode) {
        this.settConfigMode = settConfigMode;
    }

    public Byte getDrawFlag() {
        return drawFlag;
    }

    public void setDrawFlag(Byte drawFlag) {
        this.drawFlag = drawFlag;
    }

    public String getAllowDrawWeekDay() {
        return allowDrawWeekDay;
    }

    public void setAllowDrawWeekDay(String allowDrawWeekDay) {
        this.allowDrawWeekDay = allowDrawWeekDay;
    }

    public String getDrawDayStartTime() {
        return drawDayStartTime;
    }

    public void setDrawDayStartTime(String drawDayStartTime) {
        this.drawDayStartTime = drawDayStartTime;
    }

    public String getDrawDayEndTime() {
        return drawDayEndTime;
    }

    public void setDrawDayEndTime(String drawDayEndTime) {
        this.drawDayEndTime = drawDayEndTime;
    }

    public Long getDrawMaxDayAmount() {
        return drawMaxDayAmount;
    }

    public void setDrawMaxDayAmount(Long drawMaxDayAmount) {
        this.drawMaxDayAmount = drawMaxDayAmount;
    }

    public Long getDrawFeeLimit() {
        return drawFeeLimit;
    }

    public void setDrawFeeLimit(Long drawFeeLimit) {
        this.drawFeeLimit = drawFeeLimit;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public BigDecimal getOffRechargeRate() {
        return offRechargeRate;
    }

    public void setOffRechargeRate(BigDecimal offRechargeRate) {
        this.offRechargeRate = offRechargeRate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mchId=").append(mchId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", agentId=").append(agentId);
        sb.append(", parentAgentId=").append(parentAgentId);
        sb.append(", mobile=").append(mobile);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", payPassword=").append(payPassword);
        sb.append(", role=").append(role);
        sb.append(", mchRate=").append(mchRate);
        sb.append(", privateKey=").append(privateKey);
        sb.append(", address=").append(address);
        sb.append(", accountAttr=").append(accountAttr);
        sb.append(", accountType=").append(accountType);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNetName=").append(bankNetName);
        sb.append(", accountName=").append(accountName);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", feeType=").append(feeType);
        sb.append(", feeRate=").append(feeRate);
        sb.append(", feeLevel=").append(feeLevel);
        sb.append(", riskDay=").append(riskDay);
        sb.append(", maxDrawAmount=").append(maxDrawAmount);
        sb.append(", minDrawAmount=").append(minDrawAmount);
        sb.append(", dayDrawTimes=").append(dayDrawTimes);
        sb.append(", settType=").append(settType);
        sb.append(", settMode=").append(settMode);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", lastPasswordResetTime=").append(lastPasswordResetTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", tag=").append(tag);
        sb.append(", loginSecurityType=").append(loginSecurityType);
        sb.append(", paySecurityType=").append(paySecurityType);
        sb.append(", googleAuthStatus=").append(googleAuthStatus);
        sb.append(", googleAuthSecretKey=").append(googleAuthSecretKey);
        sb.append(", userName=").append(userName);
        sb.append(", realName=").append(realName);
        sb.append(", idCard=").append(idCard);
        sb.append(", qq=").append(qq);
        sb.append(", siteName=").append(siteName);
        sb.append(", siteUrl=").append(siteUrl);
        sb.append(", loginWhiteIp=").append(loginWhiteIp);
        sb.append(", loginBlackIp=").append(loginBlackIp);
        sb.append(", payWhiteIp=").append(payWhiteIp);
        sb.append(", payBlackIp=").append(payBlackIp);
        sb.append(", agentpayWhiteIp=").append(agentpayWhiteIp);
        sb.append(", agentpayBlackIp=").append(agentpayBlackIp);
        sb.append(", settConfigMode=").append(settConfigMode);
        sb.append(", drawFlag=").append(drawFlag);
        sb.append(", allowDrawWeekDay=").append(allowDrawWeekDay);
        sb.append(", drawDayStartTime=").append(drawDayStartTime);
        sb.append(", drawDayEndTime=").append(drawDayEndTime);
        sb.append(", drawMaxDayAmount=").append(drawMaxDayAmount);
        sb.append(", drawFeeLimit=").append(drawFeeLimit);
        sb.append(", lastLoginIp=").append(lastLoginIp);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", offRechargeRate=").append(offRechargeRate);
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
        MchInfo other = (MchInfo) that;
        return (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getParentAgentId() == null ? other.getParentAgentId() == null : this.getParentAgentId().equals(other.getParentAgentId()))
            && (this.getMobile() == null ? other.getMobile() == null : this.getMobile().equals(other.getMobile()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getIdCard() == null ? other.getIdCard() == null : this.getIdCard().equals(other.getIdCard()))
            && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
            && (this.getSiteName() == null ? other.getSiteName() == null : this.getSiteName().equals(other.getSiteName()))
            && (this.getSiteUrl() == null ? other.getSiteUrl() == null : this.getSiteUrl().equals(other.getSiteUrl()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getPayPassword() == null ? other.getPayPassword() == null : this.getPayPassword().equals(other.getPayPassword()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getMchRate() == null ? other.getMchRate() == null : this.getMchRate().equals(other.getMchRate()))
            && (this.getPrivateKey() == null ? other.getPrivateKey() == null : this.getPrivateKey().equals(other.getPrivateKey()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getAccountAttr() == null ? other.getAccountAttr() == null : this.getAccountAttr().equals(other.getAccountAttr()))
            && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankNetName() == null ? other.getBankNetName() == null : this.getBankNetName().equals(other.getBankNetName()))
            && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
            && (this.getAccountNo() == null ? other.getAccountNo() == null : this.getAccountNo().equals(other.getAccountNo()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getSettConfigMode() == null ? other.getSettConfigMode() == null : this.getSettConfigMode().equals(other.getSettConfigMode()))
            && (this.getDrawFlag() == null ? other.getDrawFlag() == null : this.getDrawFlag().equals(other.getDrawFlag()))
            && (this.getAllowDrawWeekDay() == null ? other.getAllowDrawWeekDay() == null : this.getAllowDrawWeekDay().equals(other.getAllowDrawWeekDay()))
            && (this.getDrawDayStartTime() == null ? other.getDrawDayStartTime() == null : this.getDrawDayStartTime().equals(other.getDrawDayStartTime()))
            && (this.getDrawDayEndTime() == null ? other.getDrawDayEndTime() == null : this.getDrawDayEndTime().equals(other.getDrawDayEndTime()))
            && (this.getDrawMaxDayAmount() == null ? other.getDrawMaxDayAmount() == null : this.getDrawMaxDayAmount().equals(other.getDrawMaxDayAmount()))
            && (this.getFeeType() == null ? other.getFeeType() == null : this.getFeeType().equals(other.getFeeType()))
            && (this.getFeeRate() == null ? other.getFeeRate() == null : this.getFeeRate().equals(other.getFeeRate()))
            && (this.getFeeLevel() == null ? other.getFeeLevel() == null : this.getFeeLevel().equals(other.getFeeLevel()))
            && (this.getDrawFeeLimit() == null ? other.getDrawFeeLimit() == null : this.getDrawFeeLimit().equals(other.getDrawFeeLimit()))
            && (this.getRiskDay() == null ? other.getRiskDay() == null : this.getRiskDay().equals(other.getRiskDay()))
            && (this.getMaxDrawAmount() == null ? other.getMaxDrawAmount() == null : this.getMaxDrawAmount().equals(other.getMaxDrawAmount()))
            && (this.getMinDrawAmount() == null ? other.getMinDrawAmount() == null : this.getMinDrawAmount().equals(other.getMinDrawAmount()))
            && (this.getDayDrawTimes() == null ? other.getDayDrawTimes() == null : this.getDayDrawTimes().equals(other.getDayDrawTimes()))
            && (this.getSettType() == null ? other.getSettType() == null : this.getSettType().equals(other.getSettType()))
            && (this.getSettMode() == null ? other.getSettMode() == null : this.getSettMode().equals(other.getSettMode()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getLoginSecurityType() == null ? other.getLoginSecurityType() == null : this.getLoginSecurityType().equals(other.getLoginSecurityType()))
            && (this.getPaySecurityType() == null ? other.getPaySecurityType() == null : this.getPaySecurityType().equals(other.getPaySecurityType()))
            && (this.getGoogleAuthStatus() == null ? other.getGoogleAuthStatus() == null : this.getGoogleAuthStatus().equals(other.getGoogleAuthStatus()))
            && (this.getGoogleAuthSecretKey() == null ? other.getGoogleAuthSecretKey() == null : this.getGoogleAuthSecretKey().equals(other.getGoogleAuthSecretKey()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
            && (this.getLastLoginIp() == null ? other.getLastLoginIp() == null : this.getLastLoginIp().equals(other.getLastLoginIp()))
            && (this.getLastLoginTime() == null ? other.getLastLoginTime() == null : this.getLastLoginTime().equals(other.getLastLoginTime()))
            && (this.getLastPasswordResetTime() == null ? other.getLastPasswordResetTime() == null : this.getLastPasswordResetTime().equals(other.getLastPasswordResetTime()))
            && (this.getLoginWhiteIp() == null ? other.getLoginWhiteIp() == null : this.getLoginWhiteIp().equals(other.getLoginWhiteIp()))
            && (this.getLoginBlackIp() == null ? other.getLoginBlackIp() == null : this.getLoginBlackIp().equals(other.getLoginBlackIp()))
            && (this.getPayWhiteIp() == null ? other.getPayWhiteIp() == null : this.getPayWhiteIp().equals(other.getPayWhiteIp()))
            && (this.getPayBlackIp() == null ? other.getPayBlackIp() == null : this.getPayBlackIp().equals(other.getPayBlackIp()))
            && (this.getAgentpayWhiteIp() == null ? other.getAgentpayWhiteIp() == null : this.getAgentpayWhiteIp().equals(other.getAgentpayWhiteIp()))
            && (this.getAgentpayBlackIp() == null ? other.getAgentpayBlackIp() == null : this.getAgentpayBlackIp().equals(other.getAgentpayBlackIp()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getOffRechargeRate() == null ? other.getOffRechargeRate() == null : this.getOffRechargeRate().equals(other.getOffRechargeRate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getParentAgentId() == null) ? 0 : getParentAgentId().hashCode());
        result = prime * result + ((getMobile() == null) ? 0 : getMobile().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getPayPassword() == null) ? 0 : getPayPassword().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getMchRate() == null) ? 0 : getMchRate().hashCode());
        result = prime * result + ((getPrivateKey() == null) ? 0 : getPrivateKey().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getAccountAttr() == null) ? 0 : getAccountAttr().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankNetName() == null) ? 0 : getBankNetName().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getAccountNo() == null) ? 0 : getAccountNo().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getFeeType() == null) ? 0 : getFeeType().hashCode());
        result = prime * result + ((getFeeRate() == null) ? 0 : getFeeRate().hashCode());
        result = prime * result + ((getFeeLevel() == null) ? 0 : getFeeLevel().hashCode());
        result = prime * result + ((getRiskDay() == null) ? 0 : getRiskDay().hashCode());
        result = prime * result + ((getMaxDrawAmount() == null) ? 0 : getMaxDrawAmount().hashCode());
        result = prime * result + ((getMinDrawAmount() == null) ? 0 : getMinDrawAmount().hashCode());
        result = prime * result + ((getDayDrawTimes() == null) ? 0 : getDayDrawTimes().hashCode());
        result = prime * result + ((getSettType() == null) ? 0 : getSettType().hashCode());
        result = prime * result + ((getSettMode() == null) ? 0 : getSettMode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getLastPasswordResetTime() == null) ? 0 : getLastPasswordResetTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getLoginSecurityType() == null) ? 0 : getLoginSecurityType().hashCode());
        result = prime * result + ((getPaySecurityType() == null) ? 0 : getPaySecurityType().hashCode());
        result = prime * result + ((getGoogleAuthStatus() == null) ? 0 : getGoogleAuthStatus().hashCode());
        result = prime * result + ((getGoogleAuthSecretKey() == null) ? 0 : getGoogleAuthSecretKey().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getIdCard() == null) ? 0 : getIdCard().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getSiteName() == null) ? 0 : getSiteName().hashCode());
        result = prime * result + ((getSiteUrl() == null) ? 0 : getSiteUrl().hashCode());
        result = prime * result + ((getLoginWhiteIp() == null) ? 0 : getLoginWhiteIp().hashCode());
        result = prime * result + ((getLoginBlackIp() == null) ? 0 : getLoginBlackIp().hashCode());
        result = prime * result + ((getPayWhiteIp() == null) ? 0 : getPayWhiteIp().hashCode());
        result = prime * result + ((getPayBlackIp() == null) ? 0 : getPayBlackIp().hashCode());
        result = prime * result + ((getAgentpayWhiteIp() == null) ? 0 : getAgentpayWhiteIp().hashCode());
        result = prime * result + ((getAgentpayBlackIp() == null) ? 0 : getAgentpayBlackIp().hashCode());
        result = prime * result + ((getSettConfigMode() == null) ? 0 : getSettConfigMode().hashCode());
        result = prime * result + ((getDrawFlag() == null) ? 0 : getDrawFlag().hashCode());
        result = prime * result + ((getAllowDrawWeekDay() == null) ? 0 : getAllowDrawWeekDay().hashCode());
        result = prime * result + ((getDrawDayStartTime() == null) ? 0 : getDrawDayStartTime().hashCode());
        result = prime * result + ((getDrawDayEndTime() == null) ? 0 : getDrawDayEndTime().hashCode());
        result = prime * result + ((getDrawMaxDayAmount() == null) ? 0 : getDrawMaxDayAmount().hashCode());
        result = prime * result + ((getDrawFeeLimit() == null) ? 0 : getDrawFeeLimit().hashCode());
        result = prime * result + ((getLastLoginIp() == null) ? 0 : getLastLoginIp().hashCode());
        result = prime * result + ((getLastLoginTime() == null) ? 0 : getLastLoginTime().hashCode());
        result = prime * result + ((getOffRechargeRate() == null) ? 0 : getOffRechargeRate().hashCode());
        return result;
    }
}