package org.jeepay.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.*;
import org.jeepay.core.entity.MchAgentpayRecord;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.mq.BaseNotify4MchAgentpay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: aragom
 * @date: 18/10/02
 * @description: 代付业务
 */
@Service
public class AgentpayService {

    private static final MyLog _log = MyLog.getLog(AgentpayService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private TransOrderService transOrderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public BaseNotify4MchAgentpay baseNotify4MchAgentpay;

    private static final long agentpayTimeout = 1; // 同一账号和金额的代付申请间隔时间10分钟

    @Value("${config.agentpayNotifyUrl}")
    protected String agentpayNotifyUrl;

    static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    /**
     * 执行转账
     * @param mchInfo
     * @param mchAgentpayRecord
     * @return
     */
    public void excuteTrans(MchInfo mchInfo, MchAgentpayRecord mchAgentpayRecord) {
        // 1. 更新为处理中
        int result = rpcCommonService.rpcMchAgentpayService.updateStatus4Ing(mchAgentpayRecord.getAgentpayOrderId(), null);
        if(result != 1) return;

        // 2. 向支付中心发起转账申请
        JSONObject resultObj = createTransOrder(mchInfo, mchAgentpayRecord);
        if(resultObj == null) {
            _log.info("发起转账失败.agentpayOrderId={}", mchAgentpayRecord.getAgentpayOrderId());
            // 更新为代付失败
            rpcCommonService.rpcMchAgentpayService.updateStatus4Fail(mchAgentpayRecord.getAgentpayOrderId(), null, null);
            deleteKey(mchAgentpayRecord);
            return;
        }
        // 3. 处理转账结果
        String transOrderId = resultObj.getString("transOrderId");
        String  transStatusStr = resultObj.getString("status");
        handleAgentpayResult(mchAgentpayRecord, transOrderId, transStatusStr);
    }

    /**
     * 异步批量转账
     * @param mchInfo
     * @param mchAgentpayRecordList
     */
    public void asyncBatchTrans(MchInfo mchInfo, List<MchAgentpayRecord> mchAgentpayRecordList) {
        scheduledThreadPool.execute(new Runnable() {
            public void run() {
                for(MchAgentpayRecord mchAgentpayRecord : mchAgentpayRecordList) {
                    excuteTrans(mchInfo, mchAgentpayRecord);
                }
            }
        });
    }

    /**
     * 处理代付结果
     * @param mchAgentpayRecord
     * @param transOrderId
     * @param transStatusStr
     */
    public int handleAgentpayResult(MchAgentpayRecord mchAgentpayRecord, String transOrderId, String transStatusStr) {
        Byte transStatus = Byte.parseByte(transStatusStr);
        int result;
        if(PayConstant.REFUND_STATUS_FAIL == transStatus) {  // 明确转账失败
            // 更新为代付失败
            TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
            result = rpcCommonService.rpcMchAgentpayService.updateStatus4Fail(mchAgentpayRecord.getAgentpayOrderId(), transOrderId, StrUtil.toString(transOrder.getChannelErrMsg(), ""));
            deleteKey(mchAgentpayRecord);
            if(result == 1 && mchAgentpayRecord.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_API
                    && StringUtils.isNotBlank(mchAgentpayRecord.getNotifyUrl())) {
                mchAgentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_FAIL);   // 状态为失败
                baseNotify4MchAgentpay.doNotify(mchAgentpayRecord, true);
            }
        }else if(PayConstant.TRANS_STATUS_SUCCESS == transStatus || PayConstant.TRANS_STATUS_COMPLETE == transStatus) { // 明确转账成功
            // 更新为代付成功
            result = rpcCommonService.rpcMchAgentpayService.updateStatus4Success(mchAgentpayRecord.getAgentpayOrderId(), transOrderId, mchAgentpayRecord.getPassageId());
            if(result == 1 && mchAgentpayRecord.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_API
                    && StringUtils.isNotBlank(mchAgentpayRecord.getNotifyUrl())) {
                mchAgentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_SUCCESS);   // 状态为成功
                baseNotify4MchAgentpay.doNotify(mchAgentpayRecord, true);
            }
        }else {
            result = rpcCommonService.rpcMchAgentpayService.updateTrans(mchAgentpayRecord.getAgentpayOrderId(), transOrderId, null);
        }
        return result;
    }

    /**
     * 创建转账订单
     * @param mchInfo
     * @param mchAgentpayRecord
     * @return
     */
    public JSONObject createTransOrder(MchInfo mchInfo, MchAgentpayRecord mchAgentpayRecord) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchInfo.getMchId());                      // 商户ID
        paramMap.put("mchTransNo", mchAgentpayRecord.getAgentpayOrderId());      // 商户代付单号
        paramMap.put("channelId", mchAgentpayRecord.getChannelId());    // 代付渠道
        paramMap.put("passageId", mchAgentpayRecord.getPassageId());    // 支付通道,平台账户需要传递
        paramMap.put("amount", mchAgentpayRecord.getRemitAmount());     // 转账金额,单位分
        paramMap.put("currency", "cny");                                // 币种, cny-人民币
        paramMap.put("clientIp", IPUtility.getLocalIP());               // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                                  // 设备
        paramMap.put("notifyUrl", agentpayNotifyUrl);                   // 异步回调URL
        paramMap.put("param1", "");                                     // 扩展参数1
        paramMap.put("param2", "");                                     // 扩展参数2
        paramMap.put("remarkInfo", mchAgentpayRecord.getRemark());
        paramMap.put("accountName", mchAgentpayRecord.getAccountName());
        paramMap.put("accountNo", mchAgentpayRecord.getAccountNo());
        paramMap.put("province", mchAgentpayRecord.getProvince());
        paramMap.put("city", mchAgentpayRecord.getCity());
        paramMap.put("bankName", mchAgentpayRecord.getBankName());
        paramMap.put("bankNumber", mchAgentpayRecord.getBankNumber());
        String reqSign = PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey());
        paramMap.put("sign", reqSign);   // 签名
        _log.info("[trans_req]{}", paramMap);
        String result = transOrderService.createTransOrder(paramMap);
        _log.info("[trans_res]{}", result);
        JSONObject retObj = JSON.parseObject(result);
        if(JEEPayUtil.isSuccess(retObj)) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retObj, mchInfo.getPrivateKey(), "sign");
            String retSign = (String) retObj.get("sign");
            if(checkSign.equals(retSign)) return retObj;
            _log.info("验签失败:retSign={},checkSign={}", retSign, checkSign);
        }
        return null;
    }

    /**
     * 查询代付订单
     * @param mchId
     * @param agentpayOrderId
     * @param mchOrderNo
     * @param executeNotify
     * @return
     */
    public MchAgentpayRecord query(Long mchId, String agentpayOrderId, String mchOrderNo, boolean executeNotify) {
        MchAgentpayRecord mchAgentpayRecord;
        if(StringUtils.isNotBlank(agentpayOrderId)) {
            mchAgentpayRecord = rpcCommonService.rpcMchAgentpayService.findByMchIdAndAgentpayOrderId(mchId, agentpayOrderId);
        }else {
            mchAgentpayRecord = rpcCommonService.rpcMchAgentpayService.findByMchIdAndMchOrderNo(mchId, mchOrderNo);
        }
        if(mchAgentpayRecord == null) return null;

        if(executeNotify && (PayConstant.AGENTPAY_STATUS_SUCCESS == mchAgentpayRecord.getStatus() || PayConstant.AGENTPAY_STATUS_FAIL == mchAgentpayRecord.getStatus())) {
            baseNotify4MchAgentpay.doNotify(mchAgentpayRecord, false);
            _log.info("业务查单完成,并再次发送业务代付通知.发送完成");
        }
        return mchAgentpayRecord;
    }

    /**
     * 判断代付申请在10分钟内是否发起过,如果发起过那么返回true,否则返回false,并将写入redis,设置超时时间10分钟
     * @param mchAgentpayRecord
     * @return
     */
    public boolean isHasAgentpay(MchAgentpayRecord mchAgentpayRecord) {
        String key = "mch_agentpay_" + mchAgentpayRecord.getMchId() + "_" + mchAgentpayRecord.getAccountNo() + "_" + mchAgentpayRecord.getAmount();
        // 判断redis中是否有该笔转账
        if(stringRedisTemplate.hasKey(key)) {
            _log.info("[Redis]存在代付申请key={}", key);
            return true;
        }
        return false;
    }

    /**
     * 将代付申请设置在redis,并设置超时时间
     * @param mchAgentpayRecord
     */
    public void setAgentpay2Redis(MchAgentpayRecord mchAgentpayRecord) {
        String key = "mch_agentpay_" + mchAgentpayRecord.getMchId() + "_" + mchAgentpayRecord.getAccountNo() + "_" + mchAgentpayRecord.getAmount();
        // 向redis里存入数据并设置超时时间
        stringRedisTemplate.opsForValue().set(key, mchAgentpayRecord.getAccountNo(), agentpayTimeout * 60, TimeUnit.SECONDS);
        _log.info("[Redis]设置代付申请key={},timeout={}分", key, agentpayTimeout);
    }

    /**
     * 删除代付申请间隔key
     * 当代付失败时,需要清除该key
     * @param mchAgentpayRecord
     */
    public void deleteKey(MchAgentpayRecord mchAgentpayRecord) {
        // 删除key
        String key = "mch_agentpay_" + mchAgentpayRecord.getMchId() + "_" + mchAgentpayRecord.getAccountNo() + "_" + mchAgentpayRecord.getAmount();
        stringRedisTemplate.delete(key);
        _log.info("[Redis]删除代付申请key={}", key);
    }

    /**
     * 判断是否有重复的代付申请(卡号+金额),只要有重复即返回
     * @param mchAgentpayRecordList
     * @return
     */
    public MchAgentpayRecord isRepeatAgentpay(List<MchAgentpayRecord> mchAgentpayRecordList) {
        Map<String, MchAgentpayRecord> map = new HashMap<>();
        if(mchAgentpayRecordList == null || mchAgentpayRecordList.size() <= 1) return null;
        for(MchAgentpayRecord record : mchAgentpayRecordList) {
            String key = record.getAccountNo() + record.getAmount();
            if(map.get(key) != null) {
                return map.get(key);
            }
            map.put(key, record);
        }
        return null;
    }
}
