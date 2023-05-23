package org.jeepay.pay.channel.duolabaopay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.RpcSignUtils;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.mq.Mq4PayQuery;
import org.jeepay.pay.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class DuolabaopayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(DuolabaopayPaymentService.class);
    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;
    @Autowired
    private Mq4PayQuery mq4PayQuery;

    @Override
    public String getChannelName() {
        return DuolabaopayConfig.CHANNEL_NAME;
    }

    /**
     * 支付
     *
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder) {
        return doPay(payOrder);
    }

    /**
     * 查询订单
     *
     * @param payOrder
     * @return
     */
    	@Override
    	public JSONObject query(PayOrder payOrder) {
    	    DuolabaopayConfig duolabaopayConfig = new DuolabaopayConfig(getPayParam(payOrder));
    	    JSONObject retObj = new JSONObject();
    	    String timestamp = System.currentTimeMillis()+"";
    	    String path = "/v1/customer/order/payresult";
    	    String reqUrl = duolabaopayConfig.getReqUrl() + path;
    	    reqUrl = reqUrl+"/"+duolabaopayConfig.getMchId()+"/"+duolabaopayConfig.getShopId()+"/"+payOrder.getPayOrderId();
    	    try {
    	        StringBuilder dist = new StringBuilder();
    	        dist.append("secretKey=").append(duolabaopayConfig.getPrivateKey()).append("&timestamp=").append(timestamp).append("&path=").append(path);
    	        String token = RpcSignUtils.sha1(dist.toString()).toUpperCase();
    	        Map<String, String> header = new HashMap<>(3);
    	        header.put("accessKey",duolabaopayConfig.getPublicKey());
    	        header.put("timestamp",timestamp);
    	        header.put("token",token);
    	        _log.info("哆啦宝支付查询,请求URL:{},请求header：{}", reqUrl,header);
    	        String result = HttpUtils.getInstance().get(reqUrl, header);
    	        _log.info("哆啦宝支付查询,响应结果:{}", result);
    	        if (StringUtils.isNotBlank(result)) {
    	            JSONObject resultObject = JSONObject.parseObject(result);
    	            String ss = resultObject.getString("status");
    	            if("SUCCESS".equals(ss)){
    	                retObj.put("status", "0");
    	            }else{
    	                retObj.put("status", "1");
    	            }
    	            retObj.put("transaction_id", payOrder.getPayOrderId());
    	            retObj.put("channelAttach", result);
    	            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
    	            return retObj;
    	        } else {
    	            retObj.put("errDes", "操作失败!");
    	            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
    	            return retObj;
    	        }

    	} catch (Exception e) {
    	        _log.error(e, "");
    	        retObj.put("errDes", "操作失败!");
    	        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
    	        return retObj;
    	    }
    	}

    	public JSONObject doPay(PayOrder payOrder) {
    	    String logPrefix = "【哆啦宝支付下单】";
    	    DuolabaopayConfig duolabaopayConfig = new DuolabaopayConfig(getPayParam(payOrder));
    	    JSONObject retObj = new JSONObject();
    	    //请求参数

    	    String customerNum = duolabaopayConfig.getMchId();       //商户id
    	    String shopNum = duolabaopayConfig.getShopId();       //商户id
    	    String requestNum = payOrder.getPayOrderId();//商户内部订单号
    	    String amount = AmountUtil.convertCent2Dollar(payOrder.getAmount().toString());             //订单总金额
    	    String callbackUrl = payConfig.getNotifyUrl(getChannelName());      //异步通知地址

    	    String timestamp = System.currentTimeMillis()+"";

    	    Map<String,String> param = new LinkedHashMap<>(6);
    	    param.put("customerNum",customerNum);
    	    param.put("shopNum",shopNum);
    	    param.put("machineNum",duolabaopayConfig.getMachineNum());
    	    param.put("requestNum",requestNum);
    	    param.put("amount",amount);
    	    param.put("source","API");
    	    param.put("callbackUrl",callbackUrl);

    	    String jsonParam = JSONObject.toJSONString(param);
    	    String path = "/v1/customer/order/payurl/create";
    	    String reqUrl = duolabaopayConfig.getReqUrl() + path;
    	    StringBuilder dist = new StringBuilder();
    	    dist.append("secretKey=").append(duolabaopayConfig.getPrivateKey())
    	            .append("&timestamp=").append(timestamp)
    	            .append("&path=").append(path)
    	            .append("&body=").append(jsonParam);
    	   String token = RpcSignUtils.sha1(dist.toString()).toUpperCase();
    	    Map<String, String> header = new HashMap<>(4);
    	    header.put("Content-Type","application/json");
    	    header.put("accessKey",duolabaopayConfig.getPublicKey());
    	    header.put("timestamp",timestamp);
    	    header.put("token",token);

    	try {
    	            _log.info("哆啦宝支付下单,请求URL:{},请求参数：{},请求header：{}", reqUrl,jsonParam,header);
    	            String result = HttpUtils.getInstance().post(reqUrl, jsonParam,header);
    	            _log.info("哆啦宝支付下单,请求URL:{},响应:{}", reqUrl, result);
    	            JSONObject resultObject = JSONObject.parseObject(result);
    	            if(!"success".equals(resultObject.getString("result"))){
    	                retObj.put("errDes", "操作失败!");
    	                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
    	                return retObj;
    	            }
    	            JSONObject dataObject = resultObject.getJSONObject("data");
    	            String codeUrl = dataObject.get("url").toString();
    	            String payOrderId = requestNum;
    	            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
    	            _log.info("###### 商户统一下单处理完成 ######");
    	            retObj.put("payOrderId", payOrderId);
    	            JSONObject payInfo = new JSONObject();
    	            // 二维码支付链接
    	            payInfo.put("codeUrl", codeUrl);
    	            payInfo.put("codeImgUrl", payConfig.getPayUrl() + "/qrcode_img_get?url=" + codeUrl + "&widht=200&height=200");
    	            payInfo.put("payMethod", PayConstant.PAY_METHOD_CODE_IMG);
    	            retObj.put("payParams", payInfo);
    	            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
    	            return retObj;
    	        } catch (Exception e) {
    	            _log.error(e, "");
    	            retObj.put("errDes", "操作失败!");
    	            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
    	            return retObj;
    	        }
    	    }
    	}
