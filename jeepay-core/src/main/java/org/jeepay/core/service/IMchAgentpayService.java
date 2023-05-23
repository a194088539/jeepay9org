package org.jeepay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.MchAgentpayRecord;
import org.jeepay.core.entity.MchSettRecord;

import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/04/21
 * @description: 商户代付操作
 */
public interface IMchAgentpayService {

    /**
     * 申请代付
     * @param mchAgentpayRecord
     * @return
     */
    int applyAgentpay(MchAgentpayRecord mchAgentpayRecord);

    /**
     * 批量申请代付
     * @param mchId
     * @param agentpayAmount
     * @param mchAgentpayRecordList
     * @return
     */
    int applyAgentpayBatch(Long mchId, Long agentpayAmount, List<MchAgentpayRecord> mchAgentpayRecordList);

    /**
     * 更新代付状态为处理中
     * @param agentpayOrderId
     * @param transOrderId
     * @return
     */
    int updateStatus4Ing(String agentpayOrderId, String transOrderId);

    /**
     * 更新代付状态为成功
     * @param agentpayOrderId
     * @return
     */
    int updateStatus4Success(String agentpayOrderId, String transOrderId, Integer agentpayPassageId);

    /**
     * 更新代付状态为失败
     * @param agentpayOrderId
     * @return
     */
    int updateStatus4Fail(String agentpayOrderId, String transOrderId, String transMsg);

    /**
     * 查询代付列表
     * @param offset
     * @param limit
     * @param mchAgentpayRecord
     * @return
     */
    List<MchAgentpayRecord> select(int offset, int limit, MchAgentpayRecord mchAgentpayRecord, JSONObject queryObj);

    /**
     * 更新转账信息
     * @param transOrderId
     * @param transMsg
     * @return
     */
    int updateTrans(String agentpayOrderId, String transOrderId, String transMsg);

    /**
     * 查询个数
     * @param mchAgentpayRecord
     * @return
     */
    int count(MchAgentpayRecord mchAgentpayRecord, JSONObject queryObj);

    /**
     * 查询代付记录
     * @param mchAgentpayRecord
     * @return
     */
    MchAgentpayRecord find(MchAgentpayRecord mchAgentpayRecord);

    MchAgentpayRecord findByTransOrderId(String transOrderId);

    MchAgentpayRecord findByAgentpayOrderId(String agentpayOrderId);

    MchAgentpayRecord findByMchIdAndAgentpayOrderId(Long mchId, String agentpayOrderId);

    MchAgentpayRecord findByMchIdAndMchOrderNo(Long mchId, String mchOrderNo);

    List<MchAgentpayRecord> select(int offset, int limit, List<Byte> statusList, MchAgentpayRecord mchAgentpayRecord);

    int count(List<Byte> statusList, MchAgentpayRecord mchAgentpayRecord);

    Map count4All(Long mchId, String accountName, String agentpayOrderId, String transOrderId, Byte status, Byte agentpayChannel, String createTimeStart, String createTimeEnd);

}
