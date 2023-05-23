package org.jeepay.core.service;

import org.jeepay.core.entity.WxUser;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/9/8
 * @description: 微信用户
 */
public interface IWxUserService {

    int add(WxUser wxUser);

    int update(WxUser wxUser);

    int updateByRandomId(WxUser wxUser, String randomId);

    WxUser find(WxUser wxUser);

    WxUser findByUserId(Long userId);

    WxUser findByAccount(String account);

    List<WxUser> select(int offset, int limit, WxUser wxUser);

    List<WxUser> select4sync(int offset, int limit);

    Integer count(WxUser wxUser);

    /**
     * 得到一个有效的收款用户
     * @param dayMaxAmount
     * @param dayMaxNumber
     * @return
     */
    WxUser findByAvailable(String serverId, Long dayMaxAmount, Long dayMaxNumber);

    /**
     * 更新今日收款,今日订单数等数据
     * @param randomId
     * @param amount
     * @return
     */
    int updateDayByRandomId(String randomId, Long amount);

    /**
     * 初始化今日收款,今日订单数等数据
     * @return
     */
    int updateDayByInit();

}
