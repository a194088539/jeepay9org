package org.jeepay.core.service;

import org.jeepay.core.entity.AgentAgentpayPassage;
import org.jeepay.core.entity.AgentPassage;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/8/30
 * @description: 代理商代付通道
 */
public interface IAgentAgentpayPassageService {

    int add(AgentAgentpayPassage agentAgentpayPassage);

    int update(AgentAgentpayPassage agentAgentpayPassage);

    int update(AgentAgentpayPassage updateAgentAgentpayPassage, AgentAgentpayPassage queryAgentAgentpayPassage);

    AgentAgentpayPassage findById(Integer id);

    AgentAgentpayPassage findByAgentIdAndPassageId(Long agentId, Integer passageId);

    List<AgentAgentpayPassage> select(int offset, int limit, AgentAgentpayPassage agentAgentpayPassage);

    Integer count(AgentAgentpayPassage agentAgentpayPassage);

    List<AgentAgentpayPassage> selectAll(AgentAgentpayPassage agentAgentpayPassage);

    /**
     * 根据代理商ID查询所有代理商代付通道列表
     * @param agentId
     * @return
     */
    List<AgentAgentpayPassage> selectAllByAgentId(Long agentId);

}
