package org.jeepay.task.settlement.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchAccount;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.MchSettDailyCollect;
import org.jeepay.task.common.service.RpcCommonService;

import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/3
 * @description: 商户结算
 */
@Component
public class MchSettService {

    private static final MyLog _log = MyLog.getLog(MchSettService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 生成商户结算汇总数据
     */
    public void buildSettDailyCollect(String collDate) {
        int pageIndex = 1;
        int limit = 100;
        boolean flag = true;
        // 循环查询所有商户账户
        while (flag) {
            List<MchAccount> mchAccountList = rpcCommonService.rpcMchAccountService.listAll((pageIndex - 1) * limit, limit);
            for(MchAccount mchAccount : mchAccountList) {
                MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchAccount.getMchId());
                long mchId = mchInfo.getMchId();
                if(mchInfo == null || mchInfo.getStatus() != MchConstant.PUB_YES) {
                    _log.warn("商户信息[{}]不存在或状态不可用", mchId);
                    continue;
                }
                if(mchAccount.getStatus() != MchConstant.PUB_YES) {
                    _log.warn("商户账户[{}]不存在或状态不可用", mchId);
                    continue;
                }
                // 执行结算
                long startTime = System.currentTimeMillis();
                dailySettCollect(mchInfo, mchAccount, collDate);
                _log.info("商户[{}]风险预存期[{}天]结算完成,耗时:{} ms", mchId, mchInfo.getRiskDay(), System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(mchAccountList) || mchAccountList.size() < limit) {
                flag = false;
            }
        }

    }

    public void dailySettCollect(MchInfo mchInfo, MchAccount mchAccount, String collDate) {
        int riskDay = mchInfo.getRiskDay(); // 风险预存期
        Map map = rpcCommonService.rpcMchAccountHistoryService.selectSettDailyCollect4Mch(mchAccount.getMchId(), collDate, MchConstant.FUND_DIRECTION_ADD, riskDay);
        if(map != null) {
            // 订单金额
            Long totalOrderAmount = Long.parseLong(map.get("totalOrderAmount").toString());
            // 商户入账
            Long totalMchIncome = Long.parseLong(map.get("totalMchIncome").toString());
            // 平台利润
            Long totalPlatProfit = Long.parseLong(map.get("totalPlatProfit").toString());
            // 代理商利润
            Long totalAgentProfit = Long.parseLong(map.get("totalAgentProfit").toString());
            // 渠道成本
            Long totalChannelCost = Long.parseLong(map.get("totalChannelCost").toString());
            // 交易数
            Integer totalCount = Integer.parseInt(map.get("totalCount").toString());
            MchSettDailyCollect mchSettDailyCollect = new MchSettDailyCollect();
            mchSettDailyCollect.setMchId(mchAccount.getMchId());
            mchSettDailyCollect.setRiskDay(riskDay);
            mchSettDailyCollect.setCollectDate(DateUtil.str2date(collDate, "yyyy-MM-dd"));
            mchSettDailyCollect.setCollectType(MchConstant.COLLECT_TYPE_NORMAL);
            mchSettDailyCollect.setSettStatus(MchConstant.PUB_YES);
            mchSettDailyCollect.setTotalAmount(totalOrderAmount);
            mchSettDailyCollect.setTotalMchIncome(totalMchIncome);
            mchSettDailyCollect.setTotalAgentProfit(totalAgentProfit);
            mchSettDailyCollect.setTotalPlatProfit(totalPlatProfit);
            mchSettDailyCollect.setTotalChannelCost(totalChannelCost);
            mchSettDailyCollect.setTotalCount(totalCount);
            mchSettDailyCollect.setRemark("");
            rpcCommonService.rpcMchSettDailyCollectService.handleSettDailyCollect(collDate, mchSettDailyCollect);

        }
    }

}
