package org.jeepay.core.service;

import org.jeepay.core.entity.PayOrderCashCollRecord;

import java.util.List;

public interface IPayOrderCashCollRecordService {

    int add(PayOrderCashCollRecord record);

    List<PayOrderCashCollRecord> select(int offset, int limit, PayOrderCashCollRecord record);

    Integer count(PayOrderCashCollRecord record);

}
