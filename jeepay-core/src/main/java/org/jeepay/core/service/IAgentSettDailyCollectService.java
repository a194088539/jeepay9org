package org.jeepay.core.service;

import org.jeepay.core.entity.AgentSettDailyCollect;

/**
 * @author: aragom
 * @date: 17/12/4
 * @description: 代理商结算汇总
 */
public interface IAgentSettDailyCollectService {

    int add(AgentSettDailyCollect record);

    /**
     * 处理结算汇总,保证事务
     * (1) 增加代理商结算汇总记录
     * (2) 更新商户账户资金流水中的结算状态
     * (3) 更新代理商账户可结算金额
     * @param collDate
     * @param record
     */
    void handleSettDailyCollect(String collDate, AgentSettDailyCollect record, Byte bizType, String bizItem);

}
