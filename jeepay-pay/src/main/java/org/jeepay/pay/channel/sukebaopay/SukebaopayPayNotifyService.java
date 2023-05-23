package org.jeepay.pay.channel.sukebaopay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.sukebaopay.util.MD5Utils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
/**
 * 数科宝支付回调
 * <p>说明:</p>
 * <li></li>
 * @author aragom
 * @since 2018年11月1日下午4:55:54
 */
@Service
public class SukebaopayPayNotifyService extends BasePayNotify {

	private static final MyLog _log = MyLog.getLog(SukebaopayPayNotifyService.class);

	@Override
	public String getChannelName() {
		return SukebaopayConfig.CHANNEL_NAME;
	}

	@Override
	public JSONObject doNotify(Object notifyData) {
		String logPrefix = "【数科宝支付回调】";
		_log.info("====== 开始处理数科宝支付回调通知 ======");
		Map<?,?> params = null;
		if (notifyData instanceof Map) {
			params = (HashMap<?,?>) notifyData;
		} else if (notifyData instanceof HttpServletRequest) {
			params = buildNotifyData((HttpServletRequest) notifyData);
		}
		_log.info("{}请求数据:{}", logPrefix, params);
		String respString = PayConstant.RETURN_SUKEBAOPAY_VALUE_SUCCESS;
		// 构建返回对象
		JSONObject retObj = buildRetObj();
		if (params == null || params.isEmpty()) {
			retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
			return retObj;
		}
		try {
			PayOrder payOrder;
			Map<String, Object> payContext = new HashMap<>();
			payContext.put("parameters", params);
			if (!verifyDxPayParams(payContext)) {
				retObj.put(PayConstant.RESPONSE_RESULT, "验证数据没有通过");
				return retObj;
			}
			payOrder = (PayOrder) payContext.get("payOrder");
			// 处理订单
			byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
			if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
				int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(
						payOrder.getPayOrderId(), StrUtil.toString(params.get("order_no"), null));
				if (updatePayOrderRows != 1) {
					_log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(),
							PayConstant.PAY_STATUS_SUCCESS);
					retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
					return retObj;
				}
				_log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(),
						PayConstant.PAY_STATUS_SUCCESS);
				payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
			}
			// 业务系统后端通知
			baseNotify4MchPay.doNotify(payOrder, true);
			_log.info("====== 完成处理数科宝支付回调通知 ======");
			respString = PayConstant.RETURN_VALUE_SUCCESS;
		} catch (Exception e) {
			_log.error(e, logPrefix + "处理异常");
		}
		retObj.put(PayConstant.RESPONSE_RESULT, respString);
		return retObj;
	}

	/**
	 * 解析回调请求的数据
	 * 
	 * @param request
	 * @return
	 */
	public Map<?,?> buildNotifyData(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>();
		Map<?,?> requestParams = request.getParameterMap();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		return params;

	}

	/**
	 * 验证电信支付通知参数
	 * <p>
	 * 说明:
	 * </p>
	 * <li></li>
	 * 
	 * @author aragom
	 * @param payContext
	 * @return
	 * @since 2018年10月12日下午1:34:05
	 */
	public boolean verifyDxPayParams(Map<String, Object> payContext) {
		Map<String, String> params = (Map<String, String>) payContext.get("parameters");
		// 校验结果是否成功
		String returncode = params.get("order_status");
		if (!"success".equals(returncode)) {
			_log.error("returncode={}", returncode);
			payContext.put("retMsg", "notify data failed");
			return false;
		}

		String out_trade_no = params.get("order_no"); // 商户订单号
		String total_amount = params.get("order_amount"); // 支付金额
		if (StringUtils.isEmpty(out_trade_no)) {
			_log.error("Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
			payContext.put("retMsg", "out_trade_no is empty");
			return false;
		}
		if (StringUtils.isEmpty(total_amount)) {
			_log.error("Notify parameter total_amount is empty. total_amount={}", total_amount);
			payContext.put("retMsg", "total_amount is empty");
			return false;
		}
		String errorMessage;
		// 查询payOrder记录
		String payOrderId = out_trade_no;
		PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
		if (payOrder == null) {
			_log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
			payContext.put("retMsg", "Can't found payOrder");
			return false;
		}

		SukebaopayConfig sukebaopayConfig = new SukebaopayConfig(getPayParam(payOrder));
		// 验证签名
		try {
			String returnSign = params.remove("sign");
			String MARK = "~|~";
			//拼成原始签名串
			String initSign = sukebaopayConfig.getMchId()+MARK+sukebaopayConfig.getInterfaceVersion()+MARK+params.get("order_no")+MARK+params.get("trade_no")+MARK+params.get("order_amount")+MARK
			+params.get("product_number")+MARK+params.get("order_success_time")+MARK+params.get("order_time")+MARK+params.get("order_status")+MARK+params.get("bank_code");
			String notitysign = MD5Utils.getMd5(initSign+MARK+sukebaopayConfig.getKey());
			if (!returnSign.equals(notitysign)) {
				errorMessage = "check sign failed.";
				_log.error("Notify parameter {}", errorMessage);
				payContext.put("retMsg", errorMessage);
				return false;
			}
		} catch (Exception e) {
			_log.error(e, "");
		}

		// 核对金额
		long outPayAmt = new BigDecimal(total_amount).multiply(new BigDecimal(100)).longValue();
		long dbPayAmt = payOrder.getAmount().longValue();
		if (dbPayAmt != outPayAmt) {
			_log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}",
					total_amount, payOrderId);
			payContext.put("retMsg", "");
			return false;
		}
		payContext.put("payOrder", payOrder);
		return true;
	}
}
