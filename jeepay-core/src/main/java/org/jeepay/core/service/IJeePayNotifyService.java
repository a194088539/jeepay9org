package org.jeepay.core.service;

/**
 * @author: aragom
 * @date: 2018/5/29
 * @description:
 */
public interface IJeePayNotifyService {

	/**
	 * 发送支付订单通知
	 * @param payOrderId
	 */
	void executePayNotify(String payOrderId);

}
