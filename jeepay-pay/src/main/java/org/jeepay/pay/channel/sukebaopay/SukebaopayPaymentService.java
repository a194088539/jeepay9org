package org.jeepay.pay.channel.sukebaopay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.sukebaopay.util.MD5Utils;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
/**
 * 数科宝支付
 * <p>说明:</p>
 * <li></li>
 * @author aragom
 * @since 2018年11月1日下午3:40:31
 */
@Service
public class SukebaopayPaymentService extends BasePayment{
	private static final MyLog _log = MyLog.getLog(SukebaopayPaymentService.class);
    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay; 
	@Override
	public String getChannelName() {
		return SukebaopayConfig.CHANNEL_NAME;
	}

	@Override
	public JSONObject pay(PayOrder payOrder) {
		_log.info("======数科宝支付统一支付接口=======");
		// 组装参数
		SukebaopayConfig sukebaopayConfig = new SukebaopayConfig(getPayParam(payOrder));
		
		String channelId = payOrder.getChannelId();
		String payModel = channelId.substring(channelId.lastIndexOf("_")+1);
		String bankCode = channelId.substring(channelId.indexOf("_")+1,channelId.lastIndexOf("_"));
		
		//拼成原始签名串
		String MARK = "~|~";
		String orderTime = DateUtil.date2Str(new Date(),DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
		
		String initSign = sukebaopayConfig.getMchId()+MARK+sukebaopayConfig.getInterfaceVersion()+MARK+sukebaopayConfig.getSignType()+MARK+payOrder.getPayOrderId()+MARK+orderTime+MARK
				+AmountUtil.convertCent2Dollar(payOrder.getAmount() + "")+MARK+"1"+MARK+payConfig.getNotifyUrl(getChannelName())+MARK+payConfig.getReturnUrl(getChannelName())+MARK+bankCode+MARK
				+sukebaopayConfig.getNoticeType()+MARK+sukebaopayConfig.getServiceType();
	    
		String sign = MD5Utils.getMd5(initSign+MARK+sukebaopayConfig.getKey());
		Map<String, String> map = new HashMap<>();
		map.put("service_type", sukebaopayConfig.getServiceType());
		map.put("merchant_code", sukebaopayConfig.getMchId());
		map.put("interface_version", sukebaopayConfig.getInterfaceVersion());
		map.put("sign_type", sukebaopayConfig.getSignType());
		map.put("order_no", payOrder.getPayOrderId());
		map.put("order_time",orderTime);
		map.put("order_amount", AmountUtil.convertCent2Dollar(payOrder.getAmount() + ""));
		map.put("product_number", "1");
		map.put("notify_url", sukebaopayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName())));
		map.put("return_url", sukebaopayConfig.transformUrl(payConfig.getReturnUrl(getChannelName())));
		map.put("bank_code", bankCode);
		map.put("product_name", "");
		map.put("order_userid", "");
		map.put("order_info", "");
		map.put("notice_type", sukebaopayConfig.getNoticeType());
		map.put("pay_model", payModel);
		map.put("sign", sign);

		JSONObject retObj  = buildRetObj();
		try {
			String payUrl = sukebaopayConfig.getReqUrl() + "?" + JEEPayUtil.genUrlParams2(map);
			JSONObject payObj = new JSONObject();
			payObj.put("payUrl", payUrl);
            retObj.put("payParams", payObj);
			int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
			_log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "",
					result);
		} catch (Exception e) {
			retObj.put("errDes", "数科宝支付异常:"+e.getMessage());
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
		}
		return retObj;
	}
}
