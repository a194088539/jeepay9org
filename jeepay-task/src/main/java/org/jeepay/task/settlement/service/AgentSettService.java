package org.jeepay.task.settlement.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.MchAccountHistory;
import org.jeepay.task.common.service.RpcCommonService;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/05/05
 * @description: 代理商结算
 */
@Component
public class AgentSettService {

    private static final MyLog _log = MyLog.getLog(AgentSettService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 生成代理商结算汇总数据
     */
    public void buildSettDailyCollect(String collDate) {
        int pageIndex = 1;
        int limit = 100;
        boolean flag = true;
        // 循环查询所有代理商账户
        while (flag) {
            List<AgentAccount> agentAccountList = rpcCommonService.rpcAgentAccountService.listAll((pageIndex - 1) * limit, limit);
            for(AgentAccount agentAccount : agentAccountList) {
                AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentAccount.getAgentId());
                long agentId = agentInfo.getAgentId();
                if(agentInfo == null || agentInfo.getStatus() != MchConstant.PUB_YES) {
                    _log.warn("代理商信息[{}]不存在或状态不可用", agentId);
                    continue;
                }
                if(agentAccount.getStatus() != MchConstant.PUB_YES) {
                    _log.warn("代理商账户[{}]不存在或状态不可用", agentId);
                    continue;
                }
                // 进行结算处理
                long startTime = System.currentTimeMillis();
                int count;
                if (agentInfo.getParentAgentId() == 0 && agentInfo.getAgentLevel() == 1) {
                    count = dailySettCollect1(agentId, collDate);
                    count += dailySettCollect2(agentId, collDate);
                }else{
                    count = dailySettCollect2(agentId, collDate);
                }
                _log.info("代理商[{}]结算完成,记录:{},耗时:{} ms", agentId, count, System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(agentAccountList) || agentAccountList.size() < limit) {
                flag = false;
            }
        }

    }

    /**
     * 处理一级代理商结算
     * @param parentAgentId
     * @param collDate
     * @return
     */
    public int dailySettCollect1(Long parentAgentId, String collDate) {
        // 查询待结算记录
        List<MchAccountHistory> mchAccountHistoryList = rpcCommonService.rpcMchAccountHistoryService.selectNotSettCollect4Agent1(parentAgentId, collDate);
        if(mchAccountHistoryList == null) return 0;

        // 事务:
        // 插入代理商资金历史记录
        // 更改代理商结算状态
        for(MchAccountHistory mchAccountHistory : mchAccountHistoryList) {
            rpcCommonService.rpcMchAccountHistoryService.updateCompleteSett4Agent1(mchAccountHistory);
        }
        return mchAccountHistoryList.size();
    }

    /**
     * 处理二级代理商结算
     * @param agentId
     * @param collDate
     * @return
     */
    public int dailySettCollect2(Long agentId, String collDate) {
        // 查询待结算记录
        List<MchAccountHistory> mchAccountHistoryList = rpcCommonService.rpcMchAccountHistoryService.selectNotSettCollect4Agent2(agentId, collDate);
        if(mchAccountHistoryList == null) return 0;

        // 事务:
        // 插入代理商资金历史记录
        // 更改代理商结算状态
        for(MchAccountHistory mchAccountHistory : mchAccountHistoryList) {
            rpcCommonService.rpcMchAccountHistoryService.updateCompleteSett4Agent2(mchAccountHistory);
        }
        return mchAccountHistoryList.size();
    }

}
