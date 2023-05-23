package org.jeepay.core.service;

import org.jeepay.core.entity.MchNotify;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/10/26
 * @description: 商户通知业务
 */
public interface IMchNotifyService {

    MchNotify findByOrderId(String orderId);

    List<MchNotify> select(int offset, int limit, MchNotify mchNotify);

    Integer count(MchNotify mchNotify);

    int insertSelectiveOnDuplicateKeyUpdate(String orderId, Long mchId, String appId, String mchOrderNo, String orderType, String notifyUrl);

    int updateMchNotifySuccess(String orderId, String result, byte notifyCount);

    int updateMchNotifyFail(String orderId, String result, byte notifyCount);

}
