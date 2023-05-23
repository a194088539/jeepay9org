package org.jeepay.core.service;

import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.MchAccount;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/04/27
 * @description: 代理商账户接口
 */
public interface IAgentAccountService {

    List<AgentAccount> listAll(int offset, int limit);

    AgentAccount findByAgentId(Long agentId);

    int updateSettAmount(long agentId, long totalAmount);

    /**
     * 给代理商户加款
     * @param agentId
     * @param amount
     * @param bizType
     */
    void creditToAccount(Long agentId, Long amount, Byte bizType);

    /**
     * 给代理商户加款
     * @param agentId
     * @param amount
     * @param bizType
     */
    void creditToAccount(Long agentId, Long amount, Byte bizType, String bizItem, String orderId);

    /**
     * 给代理商账户减款款
     * @param agentId
     * @param amount
     * @param bizType
     */
    void debitToAccount(Long agentId, Long amount, Byte bizType);

    /**
     * 给代理商账户减款款
     * @param agentId
     * @param amount
     * @param bizType
     */
    void debitToAccount(Long agentId, Long amount, Byte bizType, String bizItem);

    /**
     * 给代理商账户冻结金额(增加用户不可用金额)
     * @param agentId
     * @param freezeAmount
     */
    void freezeAmount(Long agentId, Long freezeAmount);

    /**
     * 结算成功给代理商解冻金额并减款(减少账户余额,减少账户不可用金额,减少账户可结算金额)
     * @param agentId
     * @param amount
     * @param requestNo
     * @param bizType
     */
    void unFreezeAmount(Long agentId, Long amount, String requestNo, Byte bizType);

    /** 结算失败：解冻 **/
    /**
     * 结算失败给代理商账户解冻金额(减少账户不可用金额)
     * @param agentId
     * @param amount
     */
    void unFreezeSettAmount(Long agentId, Long amount);

    /**
     * 给代理商账户加钱
     * @param agentId
     * @param amount
     */
    void credit2Account(Long agentId, Byte bizType, Long amount);

    /**
     * 给代理商账户加钱
     * @param agentId
     * @param amount
     */
    void credit2Account(Long agentId, Byte bizType, Long amount, String bizItem);

    /**
     * 给代理商账户减钱
     * @param agentId
     * @param amount
     */
    void debit2Account(Long agentId, Byte bizType, Long amount);

    /**
     * 给代理商账户减钱
     * @param agentId
     * @param amount
     */
    void debit2Account(Long agentId, Byte bizType, Long amount, String bizItem);

    public BigDecimal sumAgentBalance(AgentAccount record);

}
