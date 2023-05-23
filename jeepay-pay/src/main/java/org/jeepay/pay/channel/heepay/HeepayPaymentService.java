package org.jeepay.pay.channel.heepay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.common.util.JsonUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.heepay.util.Disguiser;
import org.jeepay.pay.channel.heepay.util.HttpTools;
import org.jeepay.pay.channel.heepay.util.TimeUtil;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
/**
 * 汇付宝支付
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2018年11月30日下午3:02:55
 */
@Service
public class HeepayPaymentService extends BasePayment {

	private static final MyLog _log = MyLog.getLog(HeepayPaymentService.class);

	@Override
	public String getChannelName() {
		return PayConstant.CHANNEL_NAME_HEEPAY;
	}

	@Override
	public JSONObject pay(PayOrder payOrder) {
		_log.info("汇付宝支付接口");
		String channelId = payOrder.getChannelId();

        HeepayConfig heepayConfig = new HeepayConfig(getPayParam(payOrder));
        Map<String, String> maps = null;
        if(PayConstant.PAY_CHANNEL_HEEPAY_UNION_PC.equals(channelId)){
        	maps = populateUnionParamMap(payOrder, heepayConfig,"HY_B2CUNIONPC");
        }else if(PayConstant.PAY_CHANNEL_HEEPAY_UNION_WAP.equals(channelId)){
        	maps = populateUnionParamMap(payOrder, heepayConfig,"HY_B2CUNIONWAP");
        }else if(PayConstant.PAY_CHANNEL_HEEPAY_UNION_SCAN_CODE.equals(channelId)){
        	maps = populateUnionScanParamMap(payOrder, heepayConfig);
        }else if(PayConstant.PAY_CHANNEL_HEEPAY_QUICK_PC.equals(channelId)){
        	maps = populateQuickParamMap(payOrder, heepayConfig);
        }else{
        	return buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的汇付宝支付渠道[channelId="+channelId+"]");
        }
	   JSONObject retObj = new JSONObject();
	   retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
		try {
			if(PayConstant.PAY_CHANNEL_HEEPAY_UNION_SCAN_CODE.equals(channelId)){
				_log.info("请求数据：{}",maps);
				String jsonText = HttpTools.httpPostToJsonStr(heepayConfig.getReqUrl(),maps);
				_log.info("返回数据：{}",jsonText);
				JSONObject rsJson = JSONObject.parseObject(jsonText);//转换成json对象
				if(!"1".equals(rsJson.get("retCode"))){
					retObj.put("errDes", "汇付宝支付失败:"+rsJson.get("retMsg").toString());
		            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
		            return retObj;
				}
				//拼成原始签名串
				String source = "limitCount="+rsJson.get("limitCount")+
                "&merchantPreId="+rsJson.get("merchantPreId")+
                "&qrCode="+rsJson.get("qrCode")+
                "&qrExpireTime="+rsJson.get("qrExpireTime")+
                "&qrValidTime="+rsJson.get("qrValidTime")+
                "&retCode="+rsJson.get("retCode")+
                "&retMsg="+rsJson.get("retMsg")+
                "&key="+heepayConfig.getSingKey();
				_log.info("签名字符串 {}", source);
				String returnSign = maps.remove("sign");
				String notitysign = Disguiser.disguiseMD5(source);
				if (!returnSign.equals(notitysign)) {
					retObj.put("errDes", "汇付宝支付失败:返回数据验签失败");
		            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
					return retObj;
				}
				if(rsJson.get("qrCode") != null){
					JSONObject payObj = new JSONObject();
					payObj.put("codeUrl", rsJson.get("qrCode")); // 二维码支付链接
					payObj.put("codeImgUrl",payConfig.getPayUrl()+"/qrcode_img_get?url=" + rsJson.get("qrCode") + "&widht=200&height=200");
			        retObj.put("payParams", payObj);
				}
			}else{
				String payUrl = heepayConfig.getReqUrl() + "?" + JEEPayUtil.genUrlParams2(maps);
				JSONObject payObj = new JSONObject();
				payObj.put("payUrl", payUrl);
	            retObj.put("payParams", payObj);
			}
			int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
			_log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "",
					result);
		} catch (Exception e) {
			e.printStackTrace();
			retObj.put("errDes", "汇付宝支付异常:"+e.getMessage());
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
		}
        return retObj;
	}
    /**
     * 组装银联支付参数
     * <p>说明:</p>
     * <li></li>
     * @author DuanYong
     * @param payOrder
     * @param heepayConfig
     * @param trxType
     * @return
     * @since 2018年11月30日下午3:46:40
     */
	private Map<String, String> populateUnionParamMap(PayOrder payOrder, HeepayConfig heepayConfig,String trxType) {
		String merchantId = heepayConfig.getMchId();
		String merchantOrderNo = payOrder.getPayOrderId();
		String merchantUserId = heepayConfig.getMchId();
		String productCode = trxType;
		String payAmount = AmountUtil.convertCent2Dollar(payOrder.getAmount() + "");
		String notifyUrl = heepayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName()));
        String callBackUrl = heepayConfig.transformUrl(payConfig.getReturnUrl(getChannelName()));
        String description = payOrder.getBody();
		
		String source = "callBackUrl="+callBackUrl+
                "&description="+description+
                "&merchantId="+merchantId+
                "&merchantOrderNo="+merchantOrderNo+
                "&merchantUserId="+merchantUserId+
                "&notifyUrl="+notifyUrl+
                "&payAmount="+payAmount+
                "&key="+heepayConfig.getSingKey();
		_log.info("签名数据：{}",source);
		String generateSign = Disguiser.disguiseMD5(source);
		
		Map<String, String> maps = new HashMap<String, String>();	
		maps.put("merchantId",merchantId);
		maps.put("merchantOrderNo",merchantOrderNo);
		maps.put("merchantUserId",merchantUserId);
		maps.put("productCode",productCode);
		maps.put("payAmount",payAmount);
		maps.put("notifyUrl",notifyUrl);
		maps.put("callBackUrl",callBackUrl);
		maps.put("description",description);
		maps.put("sign",generateSign);
		maps.put("sign1",source);
		return maps;
	}
	/**
	 * 组装银联扫码参数
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @param payOrder
	 * @param heepayConfig
	 * @return
	 * @since 2018年11月30日下午4:24:02
	 */
	private Map<String, String> populateUnionScanParamMap(PayOrder payOrder, HeepayConfig heepayConfig) {
		String merchantId = heepayConfig.getMchId();
		String merchantOrderNo = payOrder.getPayOrderId();
		String merchantUserId = payOrder.getChannelUser();
		String payAmount = AmountUtil.convertCent2Dollar(payOrder.getAmount() + "");
		String notifyUrl = heepayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName()));
        String requestTime = TimeUtil.getCurDate("yyyyMMddHHmmss");
		String source = 
				"amount="+payAmount+
				"&invoiceSt="+1+
		        "&limitCardType="+
				"&limitCount="+0+
				"&merchantId="+merchantId+
				"&merchantPreId="+merchantOrderNo+
				"&notifyUrl="+notifyUrl+
				"&payeeComments="+
				"&qrValidTime="+
				"&requestTime="+requestTime+
				"&subMerCatCode="+
				"&subMerchantId="+
				"&subMerchantName="+
				"&version="+1.0+
                "&key="+heepayConfig.getSingKey();
		_log.info("签名数据：{}",source);
		String generateSign = Disguiser.disguiseMD5(source);
		
		Map<String, String> maps = new HashMap<String, String>();	
		maps.put("merchantId",merchantId);
		maps.put("subMerchantId","");
		maps.put("subMerchantName","");
		maps.put("subMerCatCode","");
		maps.put("merchantPreId",merchantOrderNo);
		maps.put("merchantUserId",merchantUserId);
		maps.put("amount",payAmount);
		maps.put("clientIp",payOrder.getClientIp());
		maps.put("requestTime",requestTime);
		maps.put("notifyUrl",notifyUrl);
		maps.put("limitCardType","");
		maps.put("qrValidTime","");
		maps.put("limitCount","0");
		maps.put("invoiceSt","1");
		maps.put("payeeComments","");
		maps.put("version","1.0");
		maps.put("sign",generateSign);
		maps.put("sign1",source);
		return maps;
	}
	/**
	 * 组装网银支付
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @param payOrder
	 * @param heepayConfig
	 * @return
	 * @since 2018年11月30日下午4:24:54
	 */
	private Map<String, String> populateQuickParamMap(PayOrder payOrder, HeepayConfig heepayConfig) {
		String merchantId = heepayConfig.getMchId();
		String merchantOrderNo = payOrder.getPayOrderId();
		String merchantUserId = payOrder.getChannelUser();
		String productCode = "HY_B2CEBANKPC";
		String payAmount = AmountUtil.convertCent2Dollar(payOrder.getAmount() + "");
		String notifyUrl = heepayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName()));
        String callBackUrl = heepayConfig.transformUrl(payConfig.getReturnUrl(getChannelName()));
        String description = payOrder.getBody();
        String version = "1.0";//版本号
		
		String source = "merchantId="+merchantId+
                "&merchantOrderNo="+merchantOrderNo+
                "&merchantUserId="+merchantUserId+
                "&notifyUrl="+notifyUrl+
                "&payAmount="+payAmount+
                "&productCode="+productCode+
                "&version="+version+
                "&key="+heepayConfig.getSingKey();
		_log.info("签名数据：{}",source);
		String generateSign = Disguiser.disguiseMD5(source);
		
		Map<String, String> maps = new HashMap<String, String>();	
		maps.put("merchantId",merchantId);
		maps.put("merchantOrderNo",merchantOrderNo);
		maps.put("merchantUserId",merchantUserId);
		maps.put("productCode",productCode);
		maps.put("payAmount",payAmount);
		maps.put("requestTime",TimeUtil.getCurDate("yyyyMMddHHmmss"));
		maps.put("version",version);
		maps.put("notifyUrl",notifyUrl);
		maps.put("callBackUrl",callBackUrl);
		maps.put("description",description);
		maps.put("clientIp",payOrder.getClientIp());
		maps.put("reqHyTime",System.currentTimeMillis()+"");
		maps.put("sign",generateSign);
		maps.put("onlineType","hard");
		maps.put("bankId","");
		maps.put("bankName","");
		maps.put("bankCardType","");
		maps.put("sign1",source);
		return maps;
	}

	@Override
	public JSONObject query(PayOrder payOrder) {
		// 组装参数
		HeepayConfig heepayConfig = new HeepayConfig(getPayParam(payOrder));
		JSONObject retObj = buildRetObj();
		String merchantId = heepayConfig.getMchId();
		String merchantOrderNo = payOrder.getPayOrderId();
        String requestTime = TimeUtil.getCurDate("yyyyMMddHHmmss");
        String currentPageNum = "1";
		String source = 
				"currentPageNum="+currentPageNum+
				"&merchantId="+merchantId+
				"&merchantPreId="+merchantOrderNo+
				"&requestTime="+requestTime+
				"&timeFrom="+
				"&timeTo="+
				"&version="+1.0+
                "&key="+heepayConfig.getSingKey();
		_log.info("签名数据：{}",source);
		String generateSign = Disguiser.disguiseMD5(source);
		
		Map<String, String> maps = new HashMap<String, String>();	
		maps.put("merchantId",merchantId);
		maps.put("merchantPreId",merchantOrderNo);
		maps.put("timeFrom","");
		maps.put("timeTo","");
		maps.put("requestTime",requestTime);
		maps.put("currentPageNum",currentPageNum);
		maps.put("version","1.0");
		maps.put("requestTime",requestTime);
		maps.put("sign",generateSign);
		maps.put("sign1",source);
		// 发起请求
		String result = "";
		try {
			result = HttpTools.httpPostToJsonStr(heepayConfig.getReqUrl(),maps);
			_log.info("返回数据：{}",result);
		} catch (Exception e) {
			_log.error(e, "");
		}
		if (StringUtils.isBlank(result)) {
			retObj.put("errDes", "返回数据为空");
			retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
			return retObj;
		}
		JSONObject jsonObj = JsonUtil.getJSONObjectFromJson(result);
		if(!"1".equals(jsonObj.get("retCode"))){
			retObj.put("errDes", "汇付宝查询:"+jsonObj.get("retMsg").toString());
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
		}
		//拼成原始签名串
		source = "currentPageNum="+jsonObj.get("currentPageNum")+
        "&merchantPreId="+jsonObj.get("merchantPreId")+
        "&retCode="+jsonObj.get("retCode")+
        "&retMsg="+jsonObj.get("retMsg")+
        "&timeFrom="+jsonObj.get("timeFrom")+
        "&timeTo="+jsonObj.get("timeTo")+
        "&totalPageNum="+jsonObj.get("totalPageNum")+
        "&transDetail="+jsonObj.get("transDetail")+
        "&key="+heepayConfig.getSingKey();
		_log.info("签名字符串 {}", source);
		String returnSign = jsonObj.get("sign")+"";
		String notitysign = Disguiser.disguiseMD5(source);
		if (!returnSign.equals(notitysign)) {
			retObj.put("errDes", "汇付宝查询:返回数据验签失败");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
			return retObj;
		}
		retObj.putAll(jsonObj);
		retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
		return retObj;
	}

}
