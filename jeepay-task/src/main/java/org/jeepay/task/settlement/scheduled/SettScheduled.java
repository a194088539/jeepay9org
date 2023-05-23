package org.jeepay.task.settlement.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.DateUtils;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.task.settlement.service.AgentSettService;
import org.jeepay.task.settlement.service.MchSettService;

import java.util.Date;

/**
 * @author: aragom
 * @date: 17/12/3
 * @description:
 */
@Component
public class SettScheduled {

    @Autowired
    private MchSettService mchSettService;

    @Autowired
    private AgentSettService agentSettService;

    private static final MyLog _log = MyLog.getLog(SettScheduled.class);

    /**
     * 商户结算
     * 代理商结算逻辑:此处任务每天执行一次,对前一天商户未结算记录进行汇总结算.这里没用到商户风险预存期,默认都为0.对于商户d0结算,是在业务中实时处理的.
     */
    //@Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次(测试时开启)
    @Scheduled(cron="0 5 0 ? * *")  // 每日零点五分执行
    public void mchSettDailyCollectTask() {
        //String collDate = DateUtils.getCurrentTimeStr("yyyy-MM-dd");
        // 得到昨天日期
        Date billDate = DateUtil.addDay(new Date(), -1);
        String collDate = DateUtil.date2Str(billDate, DateUtil.FORMAT_YYYY_MM_DD);
        _log.info("执行商户({})待结算汇总数据,开始...", collDate);
        mchSettService.buildSettDailyCollect(collDate);
        _log.info("执行商户({})待结算汇总数据,结束。", collDate);
    }

    /**
     * 代理商结算
     * 代理商结算逻辑:代理商每分钟跑批一次,查询风险预存期内未结算的记录,然后更新结算状态增加代理商资金记录,如果d0结算那么风险预存期应为0
     */
    @Scheduled(cron="0 0/1 * * * ?")   //每分钟执行一次,代理商
    //@Scheduled(cron="0 15 0 ? * *")  // 每日零点十五分执行
    public void agentSettDailyCollectTask() {
        // 当天结算
        String collDate = DateUtils.getCurrentTimeStr("yyyy-MM-dd");
        // 得到昨天日期
        //Date billDate = DateUtil.addDay(new Date(), -1);
        //String collDate = DateUtil.date2Str(billDate, DateUtil.FORMAT_YYYY_MM_DD);
        _log.info("执行代理商({})待结算汇总数据,开始...", collDate);
        agentSettService.buildSettDailyCollect(collDate);
        _log.info("执行代理商({})待结算汇总数据,结束。", collDate);
    }

}
