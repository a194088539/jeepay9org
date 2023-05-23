package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: aragom
 * @date: 18/08/16
 * @description: 转账通知接口
 */
public interface TransNotifyInterface {

    JSONObject doNotify(Object notifyData);

}
