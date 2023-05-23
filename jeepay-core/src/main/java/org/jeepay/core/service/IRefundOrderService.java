package org.jeepay.core.service;

import org.jeepay.core.entity.RefundOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/10/26
 * @description: 退款业务
 */
public interface IRefundOrderService {

    RefundOrder findByRefundOrderId(String refundOrderId);

    RefundOrder find(RefundOrder refundOrder);

    List<RefundOrder> select(int offset, int limit, RefundOrder refundOrder, Date createTimeStart, Date createTimeEnd);

    Integer count(RefundOrder refundOrder, Date createTimeStart, Date createTimeEnd);

    RefundOrder selectRefundOrder(String refundOrderId);

    RefundOrder selectByMchIdAndRefundOrderId(Long mchId, String refundOrderId);

    RefundOrder selectByMchIdAndMchRefundNo(Long mchId, String mchRefundNo);

    int createRefundOrder(RefundOrder refundOrder);

    int updateStatus4Ing(String refundOrderId, String channelOrderNo);

    int updateStatus4Success(String refundOrderId);

    int updateStatus4Success(String refundOrderId, String channelOrderNo);

    int updateStatus4Complete(String refundOrderId);

    int updateStatus4Fail(String refundOrderId, String channelErrCode, String channelErrMsg);

}
