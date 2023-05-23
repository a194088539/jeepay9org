package org.jeepay.core.service;

import org.jeepay.core.entity.AgentpayPassageAccount;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/5/3
 * @description: 代付通道账户
 */
public interface IAgentpayPassageAccountService {

    int add(AgentpayPassageAccount agentpayPassageAccount);

    int update(AgentpayPassageAccount agentpayPassageAccount);

    AgentpayPassageAccount findById(Integer id);

    List<AgentpayPassageAccount> select(int offset, int limit, AgentpayPassageAccount agentpayPassageAccount);

    Integer count(AgentpayPassageAccount agentpayPassageAccount);

    List<AgentpayPassageAccount> selectAll(AgentpayPassageAccount agentpayPassageAccount);

    /**
     * 根据代付通道ID,查询所有代付通道账户列表
     * @param agentpayPassageId
     * @return
     */
    List<AgentpayPassageAccount> selectAllByPassageId(Integer agentpayPassageId);

}
