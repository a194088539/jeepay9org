package org.jeepay.core.service;

import org.jeepay.core.entity.AgentInfo;

import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/04/27
 * @description: 代理商接口
 */
public interface IAgentInfoService {

    int add(AgentInfo agentInfo);

    int update(AgentInfo agentInfo);

    AgentInfo find(AgentInfo agentInfo);

    AgentInfo findByLoginName(String loginName);

    AgentInfo findByAgentId(Long agentId);

    AgentInfo findByUserName(String userName);

    AgentInfo findByMobile(Long mobile);

    AgentInfo findByEmail(String email);

    List<AgentInfo> select(int offset, int limit, AgentInfo agentInfo);

    Integer count(AgentInfo agentInfo);

    Map count4Agent();

    AgentInfo findByParentAgentId(Long parentAgentId);

    /**
     * 重新构建代理商信息
     * @param info
     */
    AgentInfo reBuildAgentInfoSettConfig(AgentInfo info);

    /**
     * 得到代理商风险预存期
     * @param agentId
     * @return
     */
    int getRiskDay(Long agentId);

    List<AgentInfo> selectAll(AgentInfo agentInfo);
}
