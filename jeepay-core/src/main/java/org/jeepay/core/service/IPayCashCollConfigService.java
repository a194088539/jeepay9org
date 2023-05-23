package org.jeepay.core.service;

import org.jeepay.core.entity.PayCashCollConfig;

import java.util.List;

public interface IPayCashCollConfigService {


    List<PayCashCollConfig> select(int offset, int limit, PayCashCollConfig config) ;

    List<PayCashCollConfig> selectAll(PayCashCollConfig config);

    Integer count(PayCashCollConfig config) ;

    PayCashCollConfig findById(Integer id);

    int add(PayCashCollConfig config);

    int update(PayCashCollConfig config);



}
