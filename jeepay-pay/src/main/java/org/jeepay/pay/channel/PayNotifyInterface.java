package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.entity.PayOrder;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
public interface PayNotifyInterface {

    JSONObject doNotify(Object notifyData);

    JSONObject doReturn(Object notifyData);

}
