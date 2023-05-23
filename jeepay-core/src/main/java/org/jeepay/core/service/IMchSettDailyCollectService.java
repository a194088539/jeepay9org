package org.jeepay.core.service;

import org.jeepay.core.entity.MchSettDailyCollect;

/**
 * @author: aragom
 * @date: 17/12/4
 * @description:
 */
public interface IMchSettDailyCollectService {

    int add(MchSettDailyCollect record);

    /**
     * 处理结算汇总,保证事务
     * (1) 增加结算汇总记录
     * (2) 更新账户资金流水中的结算状态
     * (3) 更新账户可结算金额
     * @param collDate
     * @param record
     */
    void handleSettDailyCollect(String collDate, MchSettDailyCollect record);

}
