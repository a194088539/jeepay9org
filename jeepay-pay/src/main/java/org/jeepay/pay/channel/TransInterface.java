package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.TransOrder;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
public interface TransInterface {

    /**
     * 发起转账(代付)
     * @param transOrder
     * @return
     */
    JSONObject trans(TransOrder transOrder);

    /**
     * 查询结果
     * @param transOrder
     * @return
     */
    JSONObject query(TransOrder transOrder);

    /**
     * 查询账户余额
     * @param payParam
     * @return
     */
    JSONObject balance(String payParam);

    /**
     * 如果上游通道对订单ID格式有特殊要求
     * 那么实现该接口按照上游通道ID格式生成
     * @return
     */
    String getOrderId(TransOrder transOrder);

}
