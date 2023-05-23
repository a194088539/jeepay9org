package org.jeepay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WxUser implements Serializable {
    /**
     * 用户ID
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     * 微信ID
     *
     * @mbggenerated
     */
    private String wxId;

    /**
     * 登录账号
     *
     * @mbggenerated
     */
    private String account;

    /**
     * 密码
     *
     * @mbggenerated
     */
    private String password;

    /**
     * 昵称
     *
     * @mbggenerated
     */
    private String nickName;

    /**
     * 服务器ID
     *
     * @mbggenerated
     */
    private String serverId;

    /**
     * 随机ID
     *
     * @mbggenerated
     */
    private String randomId;

    /**
     * 日收款金额,单位分
     *
     * @mbggenerated
     */
    private Long dayInAmount;

    /**
     * 日收款笔数
     *
     * @mbggenerated
     */
    private Long dayInNumber;

    /**
     * 权重
     *
     * @mbggenerated
     */
    private BigDecimal weight;

    /**
     * 收款状态,0:停止收款,1:可以收款,2:正在收款
     *
     * @mbggenerated
     */
    private Byte inStatus;

    /**
     * 正在付款用户信息
     *
     * @mbggenerated
     */
    private String startPayUser;

    /**
     * 开始支付时间
     *
     * @mbggenerated
     */
    private Date startPayTime;

    /**
     * 登录状态,与微信服务端一致.-1:未登录,0:等待扫码登录,1:已扫码,未确认,2:已扫码,已确认,等待登录,3:已登录
     *
     * @mbggenerated
     */
    private Integer loginStatus;

    /**
     * 登录结果
     *
     * @mbggenerated
     */
    private String loginResult;

    /**
     * 登录同步时间
     *
     * @mbggenerated
     */
    private Date loginSyncTime;

    /**
     * 今日数据更新时间
     *
     * @mbggenerated
     */
    private Date dayUpdateTime;

    /**
     * 最后一次收款时间
     *
     * @mbggenerated
     */
    private Date lastInTime;

    /**
     * 用户状态,0:停止使用,1:可以使用
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
     * 配置信息
     *
     * @mbggenerated
     */
    private String settings;

    /**
     * 微信数据
     *
     * @mbggenerated
     */
    private String wxDat;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    public Long getDayInAmount() {
        return dayInAmount;
    }

    public void setDayInAmount(Long dayInAmount) {
        this.dayInAmount = dayInAmount;
    }

    public Long getDayInNumber() {
        return dayInNumber;
    }

    public void setDayInNumber(Long dayInNumber) {
        this.dayInNumber = dayInNumber;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Byte getInStatus() {
        return inStatus;
    }

    public void setInStatus(Byte inStatus) {
        this.inStatus = inStatus;
    }

    public String getStartPayUser() {
        return startPayUser;
    }

    public void setStartPayUser(String startPayUser) {
        this.startPayUser = startPayUser;
    }

    public Date getStartPayTime() {
        return startPayTime;
    }

    public void setStartPayTime(Date startPayTime) {
        this.startPayTime = startPayTime;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public Date getLoginSyncTime() {
        return loginSyncTime;
    }

    public void setLoginSyncTime(Date loginSyncTime) {
        this.loginSyncTime = loginSyncTime;
    }

    public Date getDayUpdateTime() {
        return dayUpdateTime;
    }

    public void setDayUpdateTime(Date dayUpdateTime) {
        this.dayUpdateTime = dayUpdateTime;
    }

    public Date getLastInTime() {
        return lastInTime;
    }

    public void setLastInTime(Date lastInTime) {
        this.lastInTime = lastInTime;
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

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getWxDat() {
        return wxDat;
    }

    public void setWxDat(String wxDat) {
        this.wxDat = wxDat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", wxId=").append(wxId);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", nickName=").append(nickName);
        sb.append(", serverId=").append(serverId);
        sb.append(", randomId=").append(randomId);
        sb.append(", dayInAmount=").append(dayInAmount);
        sb.append(", dayInNumber=").append(dayInNumber);
        sb.append(", weight=").append(weight);
        sb.append(", inStatus=").append(inStatus);
        sb.append(", startPayUser=").append(startPayUser);
        sb.append(", startPayTime=").append(startPayTime);
        sb.append(", loginStatus=").append(loginStatus);
        sb.append(", loginResult=").append(loginResult);
        sb.append(", loginSyncTime=").append(loginSyncTime);
        sb.append(", dayUpdateTime=").append(dayUpdateTime);
        sb.append(", lastInTime=").append(lastInTime);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", settings=").append(settings);
        sb.append(", wxDat=").append(wxDat);
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
        WxUser other = (WxUser) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getWxId() == null ? other.getWxId() == null : this.getWxId().equals(other.getWxId()))
            && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getServerId() == null ? other.getServerId() == null : this.getServerId().equals(other.getServerId()))
            && (this.getRandomId() == null ? other.getRandomId() == null : this.getRandomId().equals(other.getRandomId()))
            && (this.getDayInAmount() == null ? other.getDayInAmount() == null : this.getDayInAmount().equals(other.getDayInAmount()))
            && (this.getDayInNumber() == null ? other.getDayInNumber() == null : this.getDayInNumber().equals(other.getDayInNumber()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getInStatus() == null ? other.getInStatus() == null : this.getInStatus().equals(other.getInStatus()))
            && (this.getStartPayUser() == null ? other.getStartPayUser() == null : this.getStartPayUser().equals(other.getStartPayUser()))
            && (this.getStartPayTime() == null ? other.getStartPayTime() == null : this.getStartPayTime().equals(other.getStartPayTime()))
            && (this.getLoginStatus() == null ? other.getLoginStatus() == null : this.getLoginStatus().equals(other.getLoginStatus()))
            && (this.getLoginResult() == null ? other.getLoginResult() == null : this.getLoginResult().equals(other.getLoginResult()))
            && (this.getLoginSyncTime() == null ? other.getLoginSyncTime() == null : this.getLoginSyncTime().equals(other.getLoginSyncTime()))
            && (this.getDayUpdateTime() == null ? other.getDayUpdateTime() == null : this.getDayUpdateTime().equals(other.getDayUpdateTime()))
            && (this.getLastInTime() == null ? other.getLastInTime() == null : this.getLastInTime().equals(other.getLastInTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSettings() == null ? other.getSettings() == null : this.getSettings().equals(other.getSettings()))
            && (this.getWxDat() == null ? other.getWxDat() == null : this.getWxDat().equals(other.getWxDat()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getWxId() == null) ? 0 : getWxId().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getServerId() == null) ? 0 : getServerId().hashCode());
        result = prime * result + ((getRandomId() == null) ? 0 : getRandomId().hashCode());
        result = prime * result + ((getDayInAmount() == null) ? 0 : getDayInAmount().hashCode());
        result = prime * result + ((getDayInNumber() == null) ? 0 : getDayInNumber().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getInStatus() == null) ? 0 : getInStatus().hashCode());
        result = prime * result + ((getStartPayUser() == null) ? 0 : getStartPayUser().hashCode());
        result = prime * result + ((getStartPayTime() == null) ? 0 : getStartPayTime().hashCode());
        result = prime * result + ((getLoginStatus() == null) ? 0 : getLoginStatus().hashCode());
        result = prime * result + ((getLoginResult() == null) ? 0 : getLoginResult().hashCode());
        result = prime * result + ((getLoginSyncTime() == null) ? 0 : getLoginSyncTime().hashCode());
        result = prime * result + ((getDayUpdateTime() == null) ? 0 : getDayUpdateTime().hashCode());
        result = prime * result + ((getLastInTime() == null) ? 0 : getLastInTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSettings() == null) ? 0 : getSettings().hashCode());
        result = prime * result + ((getWxDat() == null) ? 0 : getWxDat().hashCode());
        return result;
    }
}