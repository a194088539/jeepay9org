package org.jeepay.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.service.IJeePayNotifyService;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.mq.Mq4MchAgentpayNotify;
import org.jeepay.pay.mq.Mq4MchPayNotify;
import org.jeepay.pay.service.RpcCommonService;

/**
 * @author: aragom
 * @date: 2018/5/29
 * @description:
 */
@Service(interfaceName = "org.jeepay.core.service.IJeePayNotifyService", version = "1.0.0", retries = -1)
public class JeePayNotifyServiceImpl implements IJeePayNotifyService {

    private static final MyLog _log = MyLog.getLog(JeePayNotifyServiceImpl.class);

    @Autowired
    private RpcCommonService rpcCommonService;

	@Autowired
	public BaseNotify4MchPay baseNotify4MchPay;
    
	/**
	 * 发送支付订单通知
	 * @param payOrderId
	 */
	public void executePayNotify(String payOrderId) {
		_log.info(">>>>>> 调取rpc补发支付通知,payOrderId：{}", payOrderId);
		PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
		baseNotify4MchPay.doNotifys(payOrder, true);
		_log.info(">>>>>> 调取rpc补发支付通知完成  <<<<<<");
	}



}
