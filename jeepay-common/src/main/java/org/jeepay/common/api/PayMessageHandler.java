package org.jeepay.common.api;

import org.jeepay.common.bean.PayMessage;
import org.jeepay.common.bean.PayOutMessage;
import org.jeepay.common.exception.PayErrorException;

import java.util.Map;


/**
 * 处理支付回调消息的处理器接口
 *
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016-6-1 11:40:30
 *
 *
 *     source Daniel Qian
 *  </pre>
 */
public interface PayMessageHandler {

    /**
     * 处理支付回调消息的处理器接口
     * @param payMessage 支付消息
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService 支付服务
     * @return xml,text格式的消息，如果在异步规则里处理的话，可以返回null
     * @throws PayErrorException 支付错误异常
     */
    PayOutMessage handle(PayMessage payMessage,
                         Map<String, Object> context,
                         PayService payService
    ) throws PayErrorException;

}
