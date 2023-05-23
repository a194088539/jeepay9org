package org.jeepay.pay.mq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.util.MyLog;

@Component
public class BaseNotify4CashColl {

	private static final MyLog _log = MyLog.getLog(BaseNotify4CashColl.class);

	@Autowired
	private Mq4CashColl mq4CashColl;

	/**
	 * 处理资金结算
	 */
	public void doNotify(String payOrderId) {
		try {
			JSONObject json = new JSONObject();
			json.put("payOrderId", payOrderId);
			mq4CashColl.send(json.toString());
		} catch (Exception e) {
			_log.error(e, "payOrderId={},sendMessage error.", payOrderId);
		}
	}

}
