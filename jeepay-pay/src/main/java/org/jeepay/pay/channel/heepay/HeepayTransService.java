package org.jeepay.pay.channel.heepay;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.JsonUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.ecpsspay.EcpsspayPaymentService;
import org.jeepay.pay.channel.heepay.util.HttpUtil;
import org.jeepay.pay.channel.heepay.util.SmallTools;
import org.jeepay.pay.channel.heepay.util.Des.Des;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 汇付宝转账
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2018年11月30日下午6:02:59
 */
@Service
public class HeepayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(HeepayTransService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_HEEPAY;
    }
    @Override
    public JSONObject trans(TransOrder transOrder) {
        String logPrefix = "【汇付宝转账】";
        JSONObject retObj = buildRetObj();
        try {
        	HeepayConfig heepayConfig = new HeepayConfig(getTransParam(transOrder));
            String url = heepayConfig.getReqUrl();
            String result = null;
            String transOrderId = transOrder.getTransOrderId();
            retObj.put("transOrderId", transOrderId);
            try {
                String reqData = buildPayRequest(transOrder, heepayConfig);
                _log.info("{}请求数据:{}", logPrefix, reqData);
                result =  HttpUtil.sendPost(url,reqData);
                _log.info("{}响应结果:{}", logPrefix, result);

            } catch (Exception e) {
                _log.error(e, "转账失败");
            }
            if(StringUtils.isBlank(result)) {
                retObj.put("isSuccess", false);
            }
            JSONObject resultObj = JSON.parseObject(result);
            String respCode = resultObj.getString("retCode");
            if("1".equals(respCode)) {
                // 交易成功
                _log.info("{} >>> 转账成功", logPrefix);
                retObj.put("transOrderId", resultObj.getString("merchantBatchNo"));
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", resultObj.getString("merchantId"));
            }else {
                // 交易失败
                retObj.put("isSuccess", true);
            }
            return retObj;
        }catch (Exception e) {
            _log.error(e, "转账异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    public JSONObject query(TransOrder transOrder) {
        String logPrefix = "【汇付宝查询】";
        JSONObject retObj = buildRetObj();
        try{
        	HeepayConfig heepayConfig = new HeepayConfig(getTransParam(transOrder));
            String url = heepayConfig.getReqUrl();

            String result;
            String transOrderId = transOrder.getTransOrderId();
            result = HttpUtil.sendPost(url,buildQueryOrderRequest(transOrder, heepayConfig));
            _log.info("{}响应结果:{}", logPrefix, result);
            JSONObject rsJson = JsonUtil.getJSONObjectFromJson(result);
            if(!"1".equals(rsJson.get("retCode"))){
				retObj.put("errDes", "汇付宝查询失败:"+rsJson.get("retMsg").toString());
	            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
	            return retObj;
			}
            boolean ok = SmallTools.checkSign(result,heepayConfig.getSingKey());
            if(!ok){
				retObj.put("errDes", "汇付宝查询失败:验证签名失败");
	            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
	            return retObj;
			}
    		retObj.putAll(rsJson);
            retObj.put("isSuccess", true);
            retObj.put("transOrderId", transOrderId);
            return retObj;
        }catch (Exception e) {
            _log.error(e, "汇付宝异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    /**
     * 构建请求数据
     * <p>说明:</p>
     * <li></li>
     * @author DuanYong
     * @param transOrder
     * @param heepayConfig
     * @return
     * @since 2018年12月1日上午9:57:55
     */
    String buildPayRequest(TransOrder transOrder, HeepayConfig heepayConfig) {
    	//请求参数
        String merchantId = heepayConfig.getMchId();//商户号
        String merchantBatchNo = "pch"+SmallTools.getDate("yyyyMMddHHmmss");//商户批次号
        String batchAmount = new BigDecimal(AmountUtil.convertCent2Dollar(transOrder.getAmount()+""))+"";//付款总金额
        String batchNum = "1";//付款总笔数
        String intoAccountDay = "0";//到账日期，0=当日，1=次日
        String transferDetails = "";//付款详情，使用json格式
        String version = "2.0";//版本号
        String requestTime = SmallTools.getDate("yyyyMMddHHmmss");//请求时间，yyyyMMddHHmmss
        String notifyUrl = "";//本次交易异步通知URL
        String sign = "";//签名结果
        String key = heepayConfig.getSingKey();//商户密钥
        String deskey = key.substring(0,24);//商户密钥
        
    	//拼接transferDetails
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("merchantPayNo",transOrder.getTransOrderId());
        obj.put("bankId",transOrder.getBankCode());
        obj.put("publicFlag",transOrder.getAccountAttr());
        obj.put("bankcardNo",transOrder.getAccountNo());
        obj.put("ownerName",transOrder.getAccountName());
        obj.put("amount",batchAmount);
        obj.put("reason",transOrder.getRemarkInfo());
        obj.put("province",transOrder.getProvince());
        obj.put("city",transOrder.getCity());
        obj.put("bankName",transOrder.getBankName());
        array.add(obj);
       transferDetails = array.toString();
        _log.info("transferDetails：{}",transferDetails);
        transferDetails = Des.Encrypt3Des(transferDetails, deskey,"ToHex16");

        //拼接签名串
        String sign1 = "batchAmount="+batchAmount+
                "&batchNum="+batchNum+
                "&intoAccountDay="+intoAccountDay+
                "&merchantBatchNo="+merchantBatchNo+
                "&merchantId="+merchantId+
                "&notifyUrl="+notifyUrl+
                "&requestTime="+requestTime+
                "&transferDetails="+transferDetails+
                "&version="+version+
                "&key="+key;
        _log.info("签名参数：{}",sign1);
        //对签名参数进行MD5加密得到sign
        sign = SmallTools.MD5en(sign1);
        //拼接请求参数
        String parameter = "merchantId="+merchantId+
                "&merchantBatchNo="+merchantBatchNo+
                "&batchAmount="+batchAmount+
                "&batchNum="+batchNum+
                "&intoAccountDay="+intoAccountDay+
                "&transferDetails="+transferDetails+
                "&version="+version+
                "&requestTime="+requestTime+
                "&notifyUrl="+notifyUrl+
                "&sign="+sign;
        _log.info("请求参数：{}",parameter);
        return parameter;
    }

    /**
     *  构建查询字符串
     * <p>说明:</p>
     * <li></li>
     * @author DuanYong
     * @param transOrder
     * @param heepayConfig
     * @return
     * @since 2018年12月1日上午9:47:55
     */
    String buildQueryOrderRequest(TransOrder transOrder, HeepayConfig heepayConfig) {
    	 //请求参数
        String merchantId = heepayConfig.getMchId();//商户号
        String merchantBatchNo = "pch"+SmallTools.getDate("yyyyMMddHHmmss");//商户批次号
        String merchantPayNo = transOrder.getTransOrderId();
        String version = "2.0";//版本号
        String requestTime = SmallTools.getDate("yyyyMMddHHmmss");//请求时间，yyyyMMddHHmmss
        String sign = "";//签名结果
        String key = heepayConfig.getSingKey();//商户密钥

        //拼接签名串
        String sign1 = "merchantBatchNo="+merchantBatchNo+
                "&merchantId="+merchantId+
                "&merchantPayNo="+merchantPayNo+
                "&requestTime="+version+
                "&version="+version+
                "&key="+key;

        _log.info("签名参数：{}",sign1);
        //对签名参数进行MD5加密得到sign
        sign = SmallTools.MD5en(sign1);
        //拼接请求参数
        String parameter = "merchantId="+merchantId+
                "&merchantBatchNo="+merchantBatchNo+
                "&merchantPayNo="+merchantPayNo+
                "&version="+version+
                "&requestTime="+requestTime+
                "&sign="+sign;
        _log.info("请求参数：{}",parameter);
        return parameter;
    }
	@Override
	public JSONObject balance(String payParam) {
		// TODO Auto-generated method stub
		return null;
	}
}
