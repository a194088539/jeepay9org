package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.Date;

public class AgentAgentpayPassage implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 代理商ID
     *
     * @mbggenerated
     */
    private Long agentId;

    /**
     * 代付通道ID
     *
     * @mbggenerated
     */
    private Integer agentpayPassageId;

    /**
     * 手续费每笔金额,单位分
     *
     * @mbggenerated
     */
    private Long feeEvery;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    private Byte status;

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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Integer getAgentpayPassageId() {
        return agentpayPassageId;
    }

    public void setAgentpayPassageId(Integer agentpayPassageId) {
        this.agentpayPassageId = agentpayPassageId;
    }

    public Long getFeeEvery() {
        return feeEvery;
    }

    public void setFeeEvery(Long feeEvery) {
        this.feeEvery = feeEvery;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
        sb.append(", agentId=").append(agentId);
        sb.append(", agentpayPassageId=").append(agentpayPassageId);
        sb.append(", feeEvery=").append(feeEvery);
        sb.append(", status=").append(status);
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
        AgentAgentpayPassage other = (AgentAgentpayPassage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getAgentpayPassageId() == null ? other.getAgentpayPassageId() == null : this.getAgentpayPassageId().equals(other.getAgentpayPassageId()))
            && (this.getFeeEvery() == null ? other.getFeeEvery() == null : this.getFeeEvery().equals(other.getFeeEvery()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getAgentpayPassageId() == null) ? 0 : getAgentpayPassageId().hashCode());
        result = prime * result + ((getFeeEvery() == null) ? 0 : getFeeEvery().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}