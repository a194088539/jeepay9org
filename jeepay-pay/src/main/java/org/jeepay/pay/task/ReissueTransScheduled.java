package org.jeepay.pay.task;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.DateUtils;
import org.jeepay.core.common.util.IPUtility;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.TransInterface;
import org.jeepay.pay.mq.BaseNotify4MchTrans;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

import java.util.Date;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/09/09
 * @description: 转账补单任务
 */
@Component
public class ReissueTransScheduled extends ReissuceBase {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(ReissueTransScheduled.class);

    @Autowired
    public BaseNotify4MchTrans baseNotify4MchTrans;

    /**
     * 转账订单批量补单任务
     */
    @Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    public void transReissueTask() {
        String logPrefix = "【转账补单】";
        // 代付补单开关
        if(!isExcuteReissueTask(reissueTransSwitch, reissueTransIP)) {
            _log.info("{}当前机器不执行补单任务", logPrefix);
            return;
        }
        int pageIndex = 1;
        int limit = 100;
        TransOrder queryTransOrder = new TransOrder();
        queryTransOrder.setStatus(PayConstant.TRANS_STATUS_TRANING);
        // 查询需要处理的订单
        Date createTimeStart = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);  // 24小时
        Date createTimeEnd = new Date(System.currentTimeMillis() - 1 * 60 * 1000);          // 1分钟
        boolean flag = true;
        _log.info("{}开始查询需要处理的转账订单,查询订单{}<=创建时间<={}", logPrefix, DateUtils.getTimeStr(createTimeStart, "yyyy-MM-dd HH:mm:ss"), DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有
        while (flag) {
            List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select((pageIndex - 1) * limit, limit, queryTransOrder, createTimeStart, createTimeEnd);
            _log.info("{}查询需要处理的转账订单,共:{}条", logPrefix, transOrderList == null ? 0 : transOrderList.size());
            for(TransOrder transOrder : transOrderList) {
                long startTime = System.currentTimeMillis();
                String transOrderId = transOrder.getTransOrderId();
                _log.info("{}开始处理transOrderId={}", logPrefix, transOrderId);
                // 渠道类型
                String channelType = transOrder.getChannelType();
                JSONObject retObj;
                try{
                    TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
                    retObj = transInterface.query(transOrder);
                }catch (BeansException e) {
                    _log.warn("{}不支持的转账渠道,停止处理.transOrderId={},channelType={}", logPrefix, transOrderId, channelType);
                    continue;
                }
                if(retObj == null) {
                    _log.warn("{}查询上游返回空,停止处理.transOrderId={},channelType={}", logPrefix, transOrderId, channelType);
                    continue;
                }
                // 查询成功
                if(JEEPayUtil.isSuccess(retObj)) {
                    // 1-处理中 2-成功 3-失败
                    int status = retObj.getInteger("status");
                    if(status == 2) {
                        String channelOrderNo = retObj.getString("channelOrderNo");
                        int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                        _log.info("{}更新转账订单状态为成功({}),transOrderId={},返回结果:{}", logPrefix, PayConstant.TRANS_STATUS_SUCCESS, transOrderId, updateTransOrderRows);
                        if (updateTransOrderRows == 1) {
                            transOrder.setStatus(PayConstant.TRANS_STATUS_SUCCESS);
                            // 通知业务系统
                            transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
                            baseNotify4MchTrans.doNotify(transOrder, true);
                        }
                    }else if(status == 3) {
                        String channelOrderNo = retObj.getString("channelOrderNo");
                        String channelErrCode = retObj.getString("channelErrCode");
                        String channelErrMsg = retObj.getString("channelErrMsg");
                        int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg, channelOrderNo);
                        _log.info("{}更新转账订单状态为失败({}),transOrderId={},返回结果:{}", logPrefix, PayConstant.TRANS_STATUS_FAIL, transOrderId, updateTransOrderRows);
                        if (updateTransOrderRows == 1) {
                            transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
                            // 通知业务系统
                            baseNotify4MchTrans.doNotify(transOrder, true);
                        }
                    }
                }
                _log.info("{}处理完毕transOrderId={}.耗时:{} ms", logPrefix, transOrderId, System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(transOrderList) || transOrderList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}本次查询处理完成,", logPrefix);
    }

}
