package org.jeepay.core.service;

import org.jeepay.core.entity.PayType;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/8/28
 * @description: 支付类型
 */
public interface IPayTypeService {

    int add(PayType payType);

    int update(PayType payType);

    PayType findByPayTypeCode(String payTypeCode);

    List<PayType> select(int offset, int limit, PayType payType);

    Integer count(PayType payType);

    List<PayType> selectAll(PayType payType);

    /**
     * 根据类型查询列表
     * @param type
     * @return
     */
    List<PayType> selectAllByType(String type);

}
