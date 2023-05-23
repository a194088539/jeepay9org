package org.jeepay.pay.channel.heepay;

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
import org.jeepay.pay.channel.heepay.util.Disguiser;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
/**
 * 汇付宝支付回调
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2018年11月30日下午4:57:12
 */
@Service
public class HeepayPayNotifyService extends BasePayNotify {

	private static final MyLog _log = MyLog.getLog(HeepayPayNotifyService.class);

	@Override
	public String getChannelName() {
		return PayConstant.CHANNEL_NAME_HEEPAY;
	}

	@Override
	public JSONObject doNotify(Object notifyData) {
		String logPrefix = "【汇付宝支付回调】";
		_log.info("====== 开始处理汇付宝支付回调通知 ======");
		Map<?,?> params = null;
		if (notifyData instanceof Map) {
			params = (HashMap<?,?>) notifyData;
		} else if (notifyData instanceof HttpServletRequest) {
			params = buildNotifyData((HttpServletRequest) notifyData);
		}
		_log.info("{}请求数据:{}", logPrefix, params);
		String respString = PayConstant.RETURN_VALUE_SUCCESS;
		// 构建返回对象
		JSONObject retObj = buildRetObj();
		if (params == null || params.isEmpty()) {
			retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
			return retObj;
		}
		try {
			//根据返回参数判断
		    //包含merchantBatchNo为转账，包含merchantPreId为银联扫码，包含successAmount 为银联支付，网银支付
			PayOrder payOrder = null;
			if(params.containsKey("merchantBatchNo")){//转账通知
				String out_trade_no = params.get("merchantBatchNo").toString(); // 商户批次号
				//TODO
			}else if(params.containsKey("merchantPreId")){//银联扫码通知
				_log.info("银联扫码通知");
				String out_trade_no = params.get("merchantPreId").toString(); // 商户的交易号
				payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(out_trade_no);
				if (payOrder == null) {
					_log.error("Can't found payOrder form db. payOrderId={}, ", out_trade_no);
					retObj.put(PayConstant.RESPONSE_RESULT, "未找到订单："+out_trade_no);
					return retObj;
				}
				Map<String, Object> payContext = new HashMap<>();
				payContext.put("parameters", params);
				if (!verifyUnionScanParams(payContext,payOrder)) {
					retObj.put(PayConstant.RESPONSE_RESULT, "验证数据没有通过");
					return retObj;
				}
			}else if(params.containsKey("successAmount")){//银联支付，网银支付
				_log.info("银联支付，网银支付通知");
				String out_trade_no = params.get("merchantOrderNo").toString(); //商户的交易号
				payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(out_trade_no);
				if (payOrder == null) {
					_log.error("Can't found payOrder form db. payOrderId={}, ", out_trade_no);
					retObj.put(PayConstant.RESPONSE_RESULT, "未找到订单："+out_trade_no);
					return retObj;
				}
				Map<String, Object> payContext = new HashMap<>();
				payContext.put("parameters", params);
				if (!verifyParams(payContext,payOrder)) {
					retObj.put(PayConstant.RESPONSE_RESULT, "验证数据没有通过");
					return retObj;
				}
			}else{
				retObj.put(PayConstant.RESPONSE_RESULT, "不支持的通知类型");
				return retObj;
			}
			
			if (payOrder == null) {
				_log.error("Can't found payOrder form db");
				retObj.put(PayConstant.RESPONSE_RESULT, "未找到订单");
				return retObj;
			}
			
			// 处理订单
			byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
			if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
				int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(
						payOrder.getPayOrderId(), StrUtil.toString(payOrder.getPayOrderId(), null));
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
			_log.info("====== 完成处理汇付宝支付回调通知 ======");
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
	 * 验证支付通知参数
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @param payContext
	 * @return
	 * @since 2018年11月9日下午10:31:52
	 */
	public boolean verifyParams(Map<String, Object> payContext,PayOrder payOrder) {
		Map<String, String> params = (Map<String, String>) payContext.get("parameters");
		// 校验结果是否成功
		String returncode = params.get("result");
		if (!"1000".equals(returncode)) {
			_log.error("returncode={}", returncode);
			payContext.put("retMsg", "notify data failed");
			return false;
		}

		String out_trade_no = params.get("merchantOrderNo"); // 商户订单号
		String total_amount = params.get("payAmount"); // 支付金额
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
		

		HeepayConfig heepayConfig = new HeepayConfig(getPayParam(payOrder));
		// 验证签名
		try {
			String returnSign = params.remove("sign");
			//拼成原始签名串
			String source = 
					"merchantId="+params.get("merchantId")+
					"&merchantOrderNo="+params.get("merchantOrderNo")+
			        "&payAmount="+params.get("payAmount")+
					"&result="+params.get("result")+
					"&successAmount="+params.get("successAmount")+
					"&transNo="+params.get("transNo")+
					"&version="+params.get("version")+
	                "&key="+heepayConfig.getSingKey();
			_log.info("签名字符串 {}", source);
			String notitysign = Disguiser.disguiseMD5(source);
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
	public boolean verifyUnionScanParams(Map<String, Object> payContext,PayOrder payOrder) {
		Map<String, String> params = (Map<String, String>) payContext.get("parameters");
		// 校验结果是否成功
		String returncode = params.get("result");
		if (!"1000".equals(returncode)) {
			_log.error("returncode={}", returncode);
			payContext.put("retMsg", "notify data failed");
			return false;
		}

		String out_trade_no = params.get("merchantPreId"); // 商户订单号
		String total_amount = params.get("amount"); // 支付金额
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
		

		HeepayConfig heepayConfig = new HeepayConfig(getPayParam(payOrder));
		// 验证签名
		try {
			String returnSign = params.remove("sign");
			//拼成原始签名串
			String source = 
					"amount="+params.get("amount")+
					"&bankCardType="+params.get("bankCardType")+
			        "&bankSerialNo="+params.get("bankSerialNo")+
					"&invoiceInfo="+params.get("invoiceInfo")+
					"&merchantId="+params.get("merchantId")+
					"&merchantPreId="+params.get("merchantPreId")+
					"&payerComments="+params.get("payerComments")+
					"&result="+params.get("result")+
					"&transNo="+params.get("transNo")+
	                "&key="+heepayConfig.getSingKey();
			_log.info("签名字符串 {}", source);
			String notitysign = Disguiser.disguiseMD5(source);
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
