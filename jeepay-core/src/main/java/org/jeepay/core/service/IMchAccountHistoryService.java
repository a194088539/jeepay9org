package org.jeepay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.MchAccountHistory;

import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/4
 * @description: 商户账户流水记录
 */
public interface IMchAccountHistoryService {

    Map selectSettDailyCollect4Mch(Long mchId, String collDate, byte fundDirection, int riskDay);

    void updateCompleteSett4Mch(Long mchId, String collDate, int riskDay);

    Map selectSettDailyCollect4Agent(Long agentId, String collDate, byte fundDirection, int riskDay, Byte bizType, String bizItem);

    void updateCompleteSett4Agent(Long agentId, String collDate, int riskDay, Byte bizType, String bizItem);

    /**
     * 查询二级代理商在风险预存期内未结算的记录
     * @param agentId
     * @param collDate
     * @return
     */
    List<MchAccountHistory> selectNotSettCollect4Agent2(Long agentId, String collDate);

    /**
     * 查询一级代理商在风险预存期内未结算的记录
     * @param parentAgentId
     * @param collDate
     * @return
     */
    List<MchAccountHistory> selectNotSettCollect4Agent1(Long parentAgentId, String collDate);
    /**
     * 更新一级代理商结算状态,并增加代理商账户资金历史记录
     * @param mchAccountHistory
     */
    void updateCompleteSett4Agent1(MchAccountHistory mchAccountHistory);

    /**
     * 更新二级代理商结算状态,并增加代理商账户资金历史记录
     * @param mchAccountHistory
     */
    void updateCompleteSett4Agent2(MchAccountHistory mchAccountHistory);

    List<MchAccountHistory> select(Long mchId, int offset, int limit, MchAccountHistory mchAccountHistory, JSONObject queryObj);

    int count(Long mchId, MchAccountHistory mchAccountHistory, JSONObject queryObj);

    List<MchAccountHistory> select(int offset, int limit, MchAccountHistory mchAccountHistory, JSONObject queryObj);

    int count(MchAccountHistory mchAccountHistory, JSONObject queryObj);

    MchAccountHistory findById(Long id);

    MchAccountHistory findById(Long mchId, Long id);

    MchAccountHistory findByOrderId(String orderId);

    Map count4Data(Byte bizType);

	Map count4Data2(Long mchId, Long agentId, String orderId, Byte bizType, String createTimeStart,
			String createTimeEnd);

	List<Map> count4AgentTop(Long agentId, String bizType, String createTimeStart, String createTimeEnd);


}
