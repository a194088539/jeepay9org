package org.jeepay.pay.mq;


import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.pay.service.RpcCommonService;

import javax.jms.*;

/**
 * @Description: 商户通知MQ统一处理
 * @author aragom qq194088539
 * @date 2017-10-31
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
@Component
public class Mq4MchNotify {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(Mq4MchNotify.class);

    public void send(Queue queue, String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(queue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(Queue queue, String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(queue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    public String httpPost(String url) {
        return JEEPayUtil.call4Post(url);
    }

}
