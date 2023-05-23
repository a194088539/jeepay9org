package org.jeepay.common.bean.outbuilder;


import org.jeepay.common.bean.MsgType;
import org.jeepay.common.bean.PayOutMessage;

/**
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2016-6-1 11:40:30
 *   </pre>
 */
public class PayJsonOutMessage extends PayOutMessage {

    public PayJsonOutMessage() {
        this.msgType = MsgType.json.name();
    }

    @Override
    public String toMessage() {
        return getContent();
    }


}
