package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class AgentAccount implements Serializable {
    /**
     * 代理商ID
     *
     * @mbggenerated
     */
    private Long agentId;

    /**
     * 账户余额
     *
     * @mbggenerated
     */
    private Long balance;

    /**
     * 不可用余额
     *
     * @mbggenerated
     */
    private Long unBalance;

    /**
     * 保证金
     *
     * @mbggenerated
     */
    private Long securityMoney;

    /**
     * 总收益
     *
     * @mbggenerated
     */
    private Long totalIncome;

    /**
     * 总支出
     *
     * @mbggenerated
     */
    private Long totalExpend;

    /**
     * 今日收益
     *
     * @mbggenerated
     */
    private Long todayIncome;

    /**
     * 今日支出
     *
     * @mbggenerated
     */
    private Long todayExpend;

    /**
     * 可结算金额
     *
     * @mbggenerated
     */
    private Long settAmount;

    /**
     * 账户状态,1-可用,0-停止使用
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 账户修改时间
     *
     * @mbggenerated
     */
    private Date accountUpdateTime;

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
     * 获取可用余额
     *
     * @return
     */
    public Long getAvailableBalance() {
        return this.balance - this.unBalance;
    }

    /**
     * 获取实际可结算金额
     *
     * @return
     */
    public Long getAvailableSettAmount() {
        Long subSettAmount = this.settAmount - this.unBalance;
        if (getAvailableBalance().compareTo(subSettAmount) == -1) {
            return getAvailableBalance();
        }
        return subSettAmount;
    }

    /**
     * 验证可用余额是否足够
     *
     * @param amount
     * @return
     */
    public boolean availableBalanceIsEnough(Long amount) {
        return this.getAvailableBalance().longValue() >= amount;
    }

    private static final long serialVersionUID = 1L;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getUnBalance() {
        return unBalance;
    }

    public void setUnBalance(Long unBalance) {
        this.unBalance = unBalance;
    }

    public Long getSecurityMoney() {
        return securityMoney;
    }

    public void setSecurityMoney(Long securityMoney) {
        this.securityMoney = securityMoney;
    }

    public Long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Long getTotalExpend() {
        return totalExpend;
    }

    public void setTotalExpend(Long totalExpend) {
        this.totalExpend = totalExpend;
    }

    public Long getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(Long todayIncome) {
        this.todayIncome = todayIncome;
    }

    public Long getTodayExpend() {
        return todayExpend;
    }

    public void setTodayExpend(Long todayExpend) {
        this.todayExpend = todayExpend;
    }

    public Long getSettAmount() {
        return settAmount;
    }

    public void setSettAmount(Long settAmount) {
        this.settAmount = settAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getAccountUpdateTime() {
        return accountUpdateTime;
    }

    public void setAccountUpdateTime(Date accountUpdateTime) {
        this.accountUpdateTime = accountUpdateTime;
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
        sb.append(", agentId=").append(agentId);
        sb.append(", balance=").append(balance);
        sb.append(", unBalance=").append(unBalance);
        sb.append(", securityMoney=").append(securityMoney);
        sb.append(", totalIncome=").append(totalIncome);
        sb.append(", totalExpend=").append(totalExpend);
        sb.append(", todayIncome=").append(todayIncome);
        sb.append(", todayExpend=").append(todayExpend);
        sb.append(", settAmount=").append(settAmount);
        sb.append(", status=").append(status);
        sb.append(", accountUpdateTime=").append(accountUpdateTime);
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
        AgentAccount other = (AgentAccount) that;
        return (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getUnBalance() == null ? other.getUnBalance() == null : this.getUnBalance().equals(other.getUnBalance()))
            && (this.getSecurityMoney() == null ? other.getSecurityMoney() == null : this.getSecurityMoney().equals(other.getSecurityMoney()))
            && (this.getTotalIncome() == null ? other.getTotalIncome() == null : this.getTotalIncome().equals(other.getTotalIncome()))
            && (this.getTotalExpend() == null ? other.getTotalExpend() == null : this.getTotalExpend().equals(other.getTotalExpend()))
            && (this.getTodayIncome() == null ? other.getTodayIncome() == null : this.getTodayIncome().equals(other.getTodayIncome()))
            && (this.getTodayExpend() == null ? other.getTodayExpend() == null : this.getTodayExpend().equals(other.getTodayExpend()))
            && (this.getSettAmount() == null ? other.getSettAmount() == null : this.getSettAmount().equals(other.getSettAmount()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAccountUpdateTime() == null ? other.getAccountUpdateTime() == null : this.getAccountUpdateTime().equals(other.getAccountUpdateTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getUnBalance() == null) ? 0 : getUnBalance().hashCode());
        result = prime * result + ((getSecurityMoney() == null) ? 0 : getSecurityMoney().hashCode());
        result = prime * result + ((getTotalIncome() == null) ? 0 : getTotalIncome().hashCode());
        result = prime * result + ((getTotalExpend() == null) ? 0 : getTotalExpend().hashCode());
        result = prime * result + ((getTodayIncome() == null) ? 0 : getTodayIncome().hashCode());
        result = prime * result + ((getTodayExpend() == null) ? 0 : getTodayExpend().hashCode());
        result = prime * result + ((getSettAmount() == null) ? 0 : getSettAmount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAccountUpdateTime() == null) ? 0 : getAccountUpdateTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}