package org.jeepay.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.MySeq;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchAgentpayPassage;
import org.jeepay.core.entity.MchAgentpayRecord;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.service.IJeePayAgentpayService;
import org.jeepay.pay.service.AgentpayService;
import org.jeepay.pay.service.RpcCommonService;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: aragom
 * @date: 2018/10/2
 * @description:
 */
@Service(version = "1.0.0")
public class JeePayAgentpayServiceImpl implements IJeePayAgentpayService {

    private static final MyLog _log = MyLog.getLog(JeePayAgentpayServiceImpl.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private AgentpayService agentpayService;

    @Override
    public int applyAgentpay(MchInfo mchInfo, MchAgentpayRecord mchAgentpayRecord) {

        Long agentpayAmountL = mchAgentpayRecord.getAmount();
        String remark = mchAgentpayRecord.getRemark();

        List<MchAgentpayPassage> mchAgentpayPassageList = rpcCommonService.rpcMchAgentpayPassageService.selectAvailable(mchInfo.getMchId());
        if(CollectionUtils.isEmpty(mchAgentpayPassageList)) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_PASSAGE_NOT_EXIST);
        }
        // 从集合中取得第一个代付通道
        MchAgentpayPassage mchAgentpayPassage = mchAgentpayPassageList.get(0);

        // 判断单笔限额
        Long maxEveryAmount = mchAgentpayPassage.getMaxEveryAmount();
        if(maxEveryAmount != null && maxEveryAmount >= 0 && agentpayAmountL > maxEveryAmount) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_AMOUNT_MAX_LIMIT, "限额" + maxEveryAmount/100 + "元");
        }

        // 得到商户每笔代付手续费
        Long fee = null;
        if(mchAgentpayPassage.getMchFeeType() == 1) {
            fee = new BigDecimal(agentpayAmountL).multiply(mchAgentpayPassage.getMchFeeRate()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).longValue();
        }else if(mchAgentpayPassage.getMchFeeType() == 2) {
            fee = mchAgentpayPassage.getMchFeeEvery();
        }
        if(fee == null) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_FEE_ERROR);
        }
        // 设置
        mchAgentpayRecord.setFee(fee);                      // 代付手续费
        // 实际打款金额
        long remitAmount = agentpayAmountL;
        if(remitAmount <= 0) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_AMOUNT_ERROR);
        }
        mchAgentpayRecord.setRemitAmount(remitAmount);      // 打款金额
        mchAgentpayRecord.setSubAmount(remitAmount + fee);  // 扣减账户金额
        mchAgentpayRecord.setRemark(StringUtils.isBlank(remark) ? "代付:" + agentpayAmountL/100 + "元" : remark);
        mchAgentpayRecord.setPassageId(mchAgentpayPassage.getAgentpayPassageId());

        // 判断代付申请时间间隔
        if(agentpayService.isHasAgentpay(mchAgentpayRecord)) {
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "频繁代付,卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元");
        }

        agentpayService.setAgentpay2Redis(mchAgentpayRecord);

        int result = rpcCommonService.rpcMchAgentpayService.applyAgentpay(mchAgentpayRecord);
        if(result != 1) {
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
        // 发起转账
        agentpayService.excuteTrans(mchInfo, mchAgentpayRecord);

        return result;
    }

    @Override
    public JSONObject batchApplyAgentpay(MchInfo mchInfo, Integer totalApplyNum, Long totalApplyAmount, List<MchAgentpayRecord> applyMchAgentpayRecordList) {

        // 得到商户配置的代付通道
        List<MchAgentpayPassage> mchAgentpayPassageList = rpcCommonService.rpcMchAgentpayPassageService.selectAvailable(mchInfo.getMchId());
        if(CollectionUtils.isEmpty(mchAgentpayPassageList)) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_PASSAGE_NOT_EXIST);
        }
        MchAgentpayPassage mchAgentpayPassage = mchAgentpayPassageList.get(0);
        Long maxEveryAmount = mchAgentpayPassage.getMaxEveryAmount();
        String batchNo = MySeq.getUUID();   // 代付批次号
        // 代付记录
        List<MchAgentpayRecord> mchAgentpayRecordList = new LinkedList<>();
        // 代付笔数
        Integer totalNum = 0;
        // 代付总金额
        Long totalAmount= 0l;
        // 代付总手续费
        Long totalFee = 0l;
        for(MchAgentpayRecord mchAgentpayRecord : applyMchAgentpayRecordList) {
            String accountName = mchAgentpayRecord.getAccountName();
            String accountNo = mchAgentpayRecord.getAccountNo();
            Long agentpayAmount = mchAgentpayRecord.getAmount();

            if(agentpayAmount <= 0) {
                throw new ServiceException(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0");
            }
            // 判断单笔限额
            if(maxEveryAmount != null && maxEveryAmount >= 0 &&  agentpayAmount > maxEveryAmount) {
                throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_AMOUNT_MAX_LIMIT, "单笔限额" + maxEveryAmount/100 + "元");
            }
            mchAgentpayRecord.setAgentpayOrderId(MySeq.getAgentpay());
            mchAgentpayRecord.setBatchNo(batchNo);
            mchAgentpayRecord.setMchId(mchInfo.getMchId());
            mchAgentpayRecord.setMchType(mchInfo.getType());
            mchAgentpayRecord.setAccountName(accountName);      // 账户名
            mchAgentpayRecord.setAccountNo(accountNo);          // 账号
            mchAgentpayRecord.setAmount(agentpayAmount);        // 代付金额
            // 得到商户每笔代付手续费
            Long fee = getFee(mchAgentpayPassage, agentpayAmount);
            mchAgentpayRecord.setFee(fee);                         // 单笔代付手续费
            mchAgentpayRecord.setRemitAmount(agentpayAmount);      // 打款金额
            mchAgentpayRecord.setSubAmount(agentpayAmount + fee);  // 扣减账户金额
            if(StringUtils.isBlank(mchAgentpayRecord.getRemark())) mchAgentpayRecord.setRemark("代付:" + agentpayAmount/100 + "元");
            mchAgentpayRecord.setPassageId(mchAgentpayPassage.getAgentpayPassageId());
            totalNum++;
            totalAmount += agentpayAmount;
            totalFee += fee;
            mchAgentpayRecordList.add(mchAgentpayRecord);
        }
        // 如果账户记录为空
        if(mchAgentpayRecordList.size() == 0) {
            throw new ServiceException(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY);
        }
        if((mchAgentpayRecordList.size() > 1000)) {
            throw new ServiceException(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT, "最多1000条");
        }

        // 判断代付申请笔数是否一致
        if(totalNum.compareTo(totalApplyNum) != 0) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_NUM_NOT_EQUAL);
        }

        // 查看是否有重复代付申请记录
        MchAgentpayRecord mchAgentpayRecord = agentpayService.isRepeatAgentpay(mchAgentpayRecordList);
        if(mchAgentpayRecord != null) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "重复申请,卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元");
        }

        // 判断代付申请,是否都满足时间间隔
        for(MchAgentpayRecord record : mchAgentpayRecordList) {
            // 判断代付申请时间间隔
            if(agentpayService.isHasAgentpay(record)) {
                throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_INTERVAL_SHORT, "频繁代付,卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元");
            }
        }
        // 将每个申请设置到redis
        for(MchAgentpayRecord record : mchAgentpayRecordList) {
            agentpayService.setAgentpay2Redis(record);
        }

        // 批量提交代付
        int batchInertCount = rpcCommonService.rpcMchAgentpayService.applyAgentpayBatch(mchInfo.getMchId(), totalAmount + totalFee, mchAgentpayRecordList);

        // 提交批量任务
        agentpayService.asyncBatchTrans(mchInfo, mchAgentpayRecordList);

        JSONObject object= new JSONObject();
        object.put("totalNum", totalNum);
        object.put("totalAmount", totalAmount);
        object.put("totalFee", totalFee);
        object.put("batchInertCount", batchInertCount);
        return object;
    }

    /**
     * 得到手续费
     * @param mchAgentpayPassage
     * @param agentpayAmountL
     * @return
     */
    Long getFee(MchAgentpayPassage mchAgentpayPassage, Long agentpayAmountL) {
        return JEEPayUtil.getFee(mchAgentpayPassage.getMchFeeType(), mchAgentpayPassage.getMchFeeRate(), mchAgentpayPassage.getMchFeeEvery(), agentpayAmountL);
    }

}
