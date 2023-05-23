package org.jeepay.mch.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.MchSettBatchRecord;
import org.jeepay.core.entity.SettRecord;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/sett")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class SettController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(SettController.class);

    /**
     * 是否允许申请提现
     * @return
     */
    @RequestMapping("/is_allow_apply")
    @ResponseBody
    public ResponseEntity<?> allowApply(HttpServletRequest request) {
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
        // 重新设置商户结算属性
        mchInfo = rpcCommonService.rpcMchInfoService.reBuildMchInfoSettConfig(mchInfo);
        // 判断是否允许提现,返回提示内容
        String applyTip = rpcCommonService.rpcSettRecordService.isAllowApply(mchInfo.getDrawFlag(),
                mchInfo.getAllowDrawWeekDay(), mchInfo.getDrawDayStartTime(), mchInfo.getDrawDayEndTime());
        return ResponseEntity.ok(BizResponse.build(applyTip));
    }

    /**
     * 申请结算
     * @return
     */
    @RequestMapping("/apply")
    @ResponseBody
    @MethodLog( remark = "申请结算" )
    public ResponseEntity<?> apply(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String settAmount = getStringRequired(param, "settAmount");         // 前端填写的为元,可以为小数点2位
        Long settAmountL = new BigDecimal(settAmount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        if(settAmountL <= 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR));
        }

        String bankName  = getStringRequired(param, "bankName");        // 银行名称
        String bankNetName  = getStringRequired(param, "bankNetName");  // 网点名称
        String accountName  = getStringRequired(param, "accountName");  // 账户名称
        String accountNo  = getStringRequired(param, "accountNo");      // 银行卡号
        String province = getStringRequired(param, "province");
        String city = getStringRequired(param, "city");

        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
        BizResponse bizResponse = verifyPay(mchInfo, param);
        if(bizResponse != null) return ResponseEntity.ok(bizResponse);
        // 发起提现申请
        try {
            int count = rpcCommonService.rpcSettRecordService.applySett(MchConstant.SETT_INFO_TYPE_MCH, getUser().getId(),
                    settAmountL, bankName, bankNetName, accountName, accountNo, province, city ,null);
            if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }catch (ServiceException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
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
        mchSettBatchRecord.setMchId(getUser().getId());
        mchSettBatchRecord.setSettRecordId(settRecordId);
        int count = rpcCommonService.rpcMchSettBatchRecordService.count(mchSettBatchRecord);
        if(count == 0) ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchSettBatchRecord> mchSettBatchRecordList = rpcCommonService.rpcMchSettBatchRecordService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), mchSettBatchRecord);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchSettBatchRecordList, count));
    }

    /**
     * 结算列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SettRecord settRecord = getObject(param, SettRecord.class);
        if(settRecord == null) settRecord = new SettRecord();
        settRecord.setInfoType(MchConstant.SETT_INFO_TYPE_MCH);
        settRecord.setInfoId(getUser().getId());
        int count = rpcCommonService.rpcSettRecordService.count(settRecord, getQueryObj(param));
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), settRecord, getQueryObj(param));
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
        SettRecord querySettRecord = new SettRecord();
        querySettRecord.setInfoType(MchConstant.SETT_INFO_TYPE_MCH);
        querySettRecord.setInfoId(getUser().getId());
        querySettRecord.setId(id);
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.find(querySettRecord);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(settRecord));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getUser().getId();
        String settOrderId = getString(param, "settOrderId");
        String accountName = getString(param, "accountName");
        Byte settStatus = getByte(param, "settStatus");

        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcSettRecordService.count4All(mchId, accountName, settOrderId, settStatus, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }
}
