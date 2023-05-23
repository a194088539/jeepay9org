package org.jeepay.task.reconciliation.channel.jdpay;

import org.springframework.stereotype.Service;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.task.reconciliation.channel.BaseBill;

/**
 * @author: aragom
 * @date: 18/1/19
 * @description:
 */
@Service
public class JdpayBillService extends BaseBill {

    private static final MyLog _log = MyLog.getLog(JdpayBillService.class);

    @Override
    public String getChannelName() {
        return null;
    }
}
