package org.jeepay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.SettRecord;

import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/19
 * @description: 结算记录
 */
public interface ISettRecordService {

    /**
     * 申请结算
     * @param infoType
     * @param infoId
     * @param settAmount
     * @return
     */
    int applySett(Byte infoType, Long infoId, Long settAmount, String bankName, String bankNetName, String accountName, String accountNo, String province, String city, String thirdOrderId);

    /**
     * 查询结算列表
     * @param offset
     * @param limit
     * @param settRecord
     * @return
     */
    List<SettRecord> select(int offset, int limit, SettRecord settRecord, JSONObject queryObj);

    /**
     * 查询个数
     * @param settRecord
     * @return
     */
    int count(SettRecord settRecord, JSONObject queryObj);

    /**
     * 审核结算
     * @param id
     * @return
     */
    int auditSett(Long id, Byte status, String remark);

    /**
     * 查询结算记录
     * @param settRecord
     * @return
     */
    SettRecord find(SettRecord settRecord);

    /**
     * 查询结算记录
     * @param id
     * @return
     */
    SettRecord findById(Long id);

    SettRecord findByTransOrderId(String transOrderId);

    SettRecord findBySettOrderId(String settOrderId);

    SettRecord findBySettThirdOrderId(String settThirdOrderId);

    SettRecord findBySettThirdOrderIdOrOrderId(Long mchId,String settThirdOrderId,String orderId);

    /**
     * 打款
     * @param id
     * @return
     */
    int remit(Long id, Byte status, String remark, String remitRemark, String transOrderId, String transMsg);

    /**
     * 更新转账信息
     * @param id
     * @param transOrderId
     * @param transMsg
     * @return
     */
    int updateTrans(Long id, String transOrderId, String transMsg);

    List<SettRecord> select(int offset, int limit, List<Byte> settStatusList, SettRecord settRecord, JSONObject queryObj);

    int count(List<Byte> settStatusList, SettRecord settRecord, JSONObject queryObj);

    /**
     * 结算统计
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Map count4Sett(String createTimeStart, String createTimeEnd);

    /**
     * 是否允许申请提现,如果不可以返回提示消息
     * @param drawFlag
     * @param allowDrawWeekDay
     * @param drawDayStartTime
     * @param drawDayEndTime
     * @return
     */
    String isAllowApply(Byte drawFlag, String allowDrawWeekDay, String drawDayStartTime, String drawDayEndTime);

    /**
     * 统计结算记录数据
     * @param infoId
     * @param accountName
     * @param settOrderId
     * @param settStatus
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Map count4All(Long infoId, String accountName, String settOrderId, Byte settStatus, String createTimeStart, String createTimeEnd);
}
