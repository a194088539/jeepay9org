package org.jeepay.core.service;

import org.jeepay.core.entity.AgentPassage;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/5/3
 * @description: 代理商支付通道
 */
public interface IAgentPassageService {

    int add(AgentPassage agentPassage);

    int update(AgentPassage agentPassage);

    int update(AgentPassage updateAgentPassage, AgentPassage queryAgentPassage);

    AgentPassage findById(Integer id);

    AgentPassage findByAgentIdAndProductId(Long agentId, Integer productId);

    List<AgentPassage> select(int offset, int limit, AgentPassage agentPassage);

    Integer count(AgentPassage agentPassage);

    List<AgentPassage> selectAll(AgentPassage agentPassage);

    /**
     * 根据代理商ID查询所有代理商通道列表
     * @param agentId
     * @return
     */
    List<AgentPassage> selectAllByAgentId(Long agentId);

}
