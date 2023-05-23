package org.jeepay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.TransOrder;

/**
 * @author: aragom
 * @date: 2018/5/29
 * @description:
 */
public interface IJeePayTransService {

    /**
     * 发起转账
     * @param transOrder
     * @return
     */
    String executeTrans(TransOrder transOrder);

    /**
     * 查询余额
     * @param channelType
     * @param payParam
     * @return
     */
    JSONObject queryBalance(String channelType, String payParam);

    /**
     * 查询余额
     * @param transOrderId
     * @return
     */
    JSONObject queryTrans(String transOrderId);

}
