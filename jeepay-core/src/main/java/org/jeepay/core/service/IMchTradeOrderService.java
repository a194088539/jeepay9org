package org.jeepay.core.service;

import org.jeepay.core.entity.MchTradeOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/21
 * @description:
 */
public interface IMchTradeOrderService {

    List<MchTradeOrder> select(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd);

    int count(MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd);

    int add(MchTradeOrder mchTradeOrder);

    MchTradeOrder findByTradeOrderId(String tradeOrderId);

    MchTradeOrder findByMchIdAndTradeOrderId(Long mchId, String tradeOrderId);

    MchTradeOrder findByMchIdAndPayOrderId(Long mchId, String payOrderId);

    int updateStatus4Ing(String tradeOrderId);

    int updateStatus4Success(String tradeOrderId, Long income);

    int updateStatus4Success(String tradeOrderId, String payOrderId, Long income);

    int updateStatus4Fail(String tradeOrderId);

    int update(MchTradeOrder mchTradeOrder);

    Map count4All(Long mchId, String tradeOrderId, String payOrderId, Byte tradeType, Byte status, String createTimeStart, String createTimeEnd);

}
