package org.jeepay.manage.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.*;
import org.jeepay.guava.DefaultGuavaCacheManager;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sett")
public class SettController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(SettController.class);

    /**
     * 结算列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SettRecord settRecord = getObject(param, SettRecord.class);
        int count = rpcCommonService.rpcSettRecordService.count(settRecord, getQueryObj(param));
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), settRecord, getQueryObj(param));
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 审核列表查询
     * @return
     */
    @RequestMapping("/audit_list")
    @ResponseBody
    public ResponseEntity<?> auditList(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SettRecord settRecord = getObject(param, SettRecord.class);
        List<Byte> settStatusList = new LinkedList<>();
        settStatusList.add(MchConstant.SETT_STATUS_AUDIT_ING); // 审核中
        int count = rpcCommonService.rpcSettRecordService.count(settStatusList, settRecord, getQueryObj(param));
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), settStatusList, settRecord, getQueryObj(param));
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 打款列表查询
     * @return
     */
    @RequestMapping("/remit_list")
    @ResponseBody
    public ResponseEntity<?> remitList(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SettRecord settRecord = getObject(param, SettRecord.class);
        List<Byte> settStatusList = new LinkedList<>();
        settStatusList.add(MchConstant.SETT_STATUS_AUDIT_OK);   // 已审核
        settStatusList.add(MchConstant.SETT_STATUS_REMIT_ING);  // 打款中
        int count = rpcCommonService.rpcSettRecordService.count(settStatusList, settRecord, getQueryObj(param));
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), settStatusList, settRecord, getQueryObj(param));
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 结算查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(settRecord));
    }

    /**
     * 审核
     * @return
     */
    @RequestMapping("/audit")
    @ResponseBody
    @MethodLog( remark = "结算审核" )
    public ResponseEntity<?> audit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        Byte status = getByteRequired(param, "status");
        String remark = getString(param, "remark");
        // 状态必须为审核通过或审核不通过
        if(status.byteValue() != MchConstant.SETT_STATUS_AUDIT_OK &&
                status.byteValue() != MchConstant.SETT_STATUS_AUDIT_NOT) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_STATUS_ERROR));
        }
        try {
            int count = rpcCommonService.rpcSettRecordService.auditSett(id, status, remark);
            if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 打款(改状态)
     * @return
     */
    @RequestMapping("/remit")
    @ResponseBody
    @MethodLog( remark = "结算打款" )
    public ResponseEntity<?> remit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        Byte status = getByteRequired(param, "status");
        String remark = getString(param, "remark");
        String remitRemark = getString(param, "remitRemark");
        // 状态必须为打款中|打款成功|打款失败
        if(status.byteValue() != MchConstant.SETT_STATUS_REMIT_ING
                && status.byteValue() != MchConstant.SETT_STATUS_REMIT_SUCCESS
                && status.byteValue() != MchConstant.SETT_STATUS_REMIT_FAIL) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_STATUS_ERROR));
        }
        try {
            int count = rpcCommonService.rpcSettRecordService.remit(id, status, remark, remitRemark, null, null);
            if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 打款(转账+改状态)
     * @return
     */
    @RequestMapping("/remit2")
    @ResponseBody
    @MethodLog( remark = "结算打款+转账" )
    public ResponseEntity<?> remit2(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        String remark = getString(param, "remark");
        String remitRemark = getString(param, "remitRemark");

        Object cacheVal = DefaultGuavaCacheManager.get(id.toString());

        if(!ObjectUtils.isEmpty(cacheVal)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_FREQUENT_OPERATION));
        }
        DefaultGuavaCacheManager.put(id.toString(), id);

        Integer agentpayPassageId = getIntegerRequired(param, "agentpayPassageId");
        Integer agentpayPassageAccountId = getIntegerRequired(param, "agentpayPassageAccountId");

        // 判断代付通道
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null || agentpayPassage.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_AGENTPAY_PASSAGE_NOT_EXIST));
        }

        // 判断代付通道子账户
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(agentpayPassageAccountId);
        if(agentpayPassageAccount == null || agentpayPassageAccount.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_AGENTPAY_PASSAGE_ACCOUNT_NOT_EXIST));
        }

        // 查询结算记录
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.findById(id);

        TransOrder transOrder = new TransOrder();
        transOrder.setMchId(settRecord.getInfoId());
        transOrder.setMchType(MchConstant.MCH_TYPE_PLATFORM);
        transOrder.setMchTransNo(settRecord.getSettOrderId());
        transOrder.setChannelType(agentpayPassage.getIfTypeCode());
        transOrder.setChannelId(agentpayPassage.getIfCode());
        transOrder.setPassageId(agentpayPassage.getId());
        transOrder.setPassageAccountId(agentpayPassageAccount.getId());
        transOrder.setAmount(settRecord.getRemitAmount());
        transOrder.setAccountAttr(settRecord.getAccountAttr());
        transOrder.setAccountType(settRecord.getAccountType());
        transOrder.setAccountName(settRecord.getAccountName());
        transOrder.setAccountNo(settRecord.getAccountNo());
        transOrder.setProvince(settRecord.getProvince());
        transOrder.setCity(settRecord.getCity());
        transOrder.setBankName(settRecord.getBankName());
        transOrder.setChannelMchId(agentpayPassageAccount.getPassageMchId());
        transOrder.setNotifyUrl(mainConfig.getSettNotifyUrl());
        transOrder.setRemarkInfo(settRecord.getBankNetName());
        BigDecimal channelRate = null;  // 渠道费率
        Long channelFeeEvery = null;    // 渠道每笔费用
        Long channelCost = null;        // 渠道成本
        if(agentpayPassage.getFeeType() == 1) {   // 百分比收费
            channelRate = agentpayPassage.getFeeRate();
            channelCost = new BigDecimal(settRecord.getRemitAmount()).multiply(channelRate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).longValue();
        }else if(agentpayPassage.getFeeType() == 2) { // 固定收费
            channelFeeEvery = agentpayPassage.getFeeEvery();
            channelCost = channelFeeEvery;
        }

        transOrder.setChannelRate(channelRate);
        transOrder.setChannelFeeEvery(channelFeeEvery);
        transOrder.setChannelCost(channelCost);

        // 先更新为打款中
        int count = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_ING, remark, remitRemark, null, null);
        if(count != 1) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        // 调用转账接口,进行实时转账
        String transStr;
        try {
            String transOrderId = rpcCommonService.rpcJeePayTransService.executeTrans(transOrder);
            transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
            if(transOrder == null) {
                return ResponseEntity.ok(BizResponse.build("转账失败,调用支付中心异常"));
            }

            Byte transStatus = transOrder.getStatus();
            if(transStatus == null || PayConstant.TRANS_RESULT_FAIL == transStatus) {  // 明确转账失败
                // 更新打款失败
                transStr = "转账失败";
                count = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_FAIL, remark, remitRemark, transOrderId, transOrder.getChannelErrMsg());
            }else if(PayConstant.TRANS_STATUS_SUCCESS == transStatus || PayConstant.TRANS_STATUS_COMPLETE == transStatus) { // 明确转成成功
                transStr = "转账成功";
                // 更新打款成功
                count = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_SUCCESS, remark, remitRemark, transOrderId, null);
            }else if(PayConstant.TRANS_STATUS_TRANING == transStatus) {
                transStr = "转账处理中";
                count = rpcCommonService.rpcSettRecordService.updateTrans(id, transOrderId, null);
            }else {
                transStr = "转账处理中";
                count = rpcCommonService.rpcSettRecordService.updateTrans(id, transOrderId, null);
            }
            if(count != 1) {
                return ResponseEntity.ok(BizResponse.build(transStr + ",操作账户失败"));
            }
        }catch (Exception e) {
            transStr = "转账异常[" + e.getMessage() + "]";
            _log.error(e, "");
        }
        return ResponseEntity.ok(BizResponse.build(transStr));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long infoId = getLong(param, "infoId");
        String settOrderId = getString(param, "settOrderId");
        String accountName = getString(param, "accountName");
        Byte settStatus = getByte(param, "settStatus");

        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcSettRecordService.count4All(infoId, accountName, settOrderId, settStatus, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

    /**
     * 批量账户记录查询
     * @return
     */
    @RequestMapping("/batch_record_list")
    @ResponseBody
    public ResponseEntity<?> batchRecordList(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long settRecordId = getLongRequired(param, "id");   // 结算记录ID
        MchSettBatchRecord mchSettBatchRecord = new MchSettBatchRecord();
        mchSettBatchRecord.setSettRecordId(settRecordId);
        int count = rpcCommonService.rpcMchSettBatchRecordService.count(mchSettBatchRecord);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchSettBatchRecord> mchSettBatchRecordList = rpcCommonService.rpcMchSettBatchRecordService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), mchSettBatchRecord);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchSettBatchRecordList, count));
    }

}
