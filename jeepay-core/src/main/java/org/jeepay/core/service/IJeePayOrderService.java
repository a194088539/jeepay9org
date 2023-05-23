package org.jeepay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.PayOrder;

public interface IJeePayOrderService {

    /**
     * 发起订单查询
     * @param payOrder
     * @return
     */
    JSONObject channelOrderQuery(PayOrder payOrder);

}
