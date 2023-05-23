package org.jeepay.pay.channel.bufpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.util.EncryptUtils;
import org.jeepay.pay.util.HttpClientHelper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
@Service
public class BufPaymentService extends BasePayment {

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_BUF;
    }

    @Override
    public JSONObject pay(PayOrder payOrder) {
    	
    	Map<String,String> map=new HashMap<String, String>();
    	
        Double price=payOrder.getAmount()/100D;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String strPrice = decimalFormat.format(price);//返回字符串
    	map.put("amount",strPrice);
    	map.put("goodsName",payOrder.getSubject());
    	map.put("merchno","860000013000533");
        map.put("payType","1");
        map.put("traceno",payOrder.getPayOrderId());
    	map.put("notifyUrl","http://103.100.208.231:53020/notify/buf/notify_res.htm");
    	
    	String signStr="amount="+map.get("amount")+"&goodsName="+map.get("goodsName")+"&merchno="+map.get("merchno")+"&notifyUrl="+map.get("notifyUrl")+"&payType="+map.get("payType")+"&traceno="+map.get("traceno")+"&268439a8749a44e0bd63ace9f159e8b8";
    	
    	String sign= EncryptUtils.encrypt(signStr);
    	
    	map.put("signature",sign.toUpperCase());
    	
    	Map<String,String> headers=new HashMap<String, String>();
    	
    	headers.put("Content-Type", "application/json;charset=UTF-8");
    	
    	String s1 = HttpClientHelper.postJsonFile1("http://39.106.2.109:8181/wapPay.jhtml", map,headers);
    	
    	JSONObject jsonObject=JSONObject.parseObject(s1);
    	
    	String respCode=jsonObject.getString("respCode");
    	
    	//成功
    	if(respCode.equals("10")) {
    		
    		rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
    		jsonObject.put(PayConstant.RETURN_PARAM_RETCODE,PayConstant.RETURN_VALUE_SUCCESS);
    		return jsonObject;
    	}

    	return jsonObject;

    }
    
  
    
    public static void main(String [] args) {
    	/**
    	String str="{\"amount\":\"1000.00\",\"merchno\":\"860000013000528\",\"payType\":\"1\",\"signature\":\"EB9E559B261EB0B3019CE311FADAC5BA\",\"status\":\"1\",\"traceno\":\"48974ssjasdfk4d123f5sd7558441545\",\"transDate\":\"2020-03-27\",\"transTime\":\"11:50:42\"}";
    	
    	JSONObject 	object=JSONObject.parseObject(str);
    	
    	String traceno=object.getString("traceno");
    	
    	System.out.println(traceno);
    	
    	   **/
    	
    	BufPaymentService bufPaymentService=new BufPaymentService();
    	
    	PayOrder payOrder=new PayOrder();
    	
	    	payOrder.setAmount(50000L);
	    	payOrder.setSubject("测试商品");
	    	payOrder.setPayOrderId("202003270001");
	    	
    	JSONObject 	object=bufPaymentService.pay(payOrder);
    	
    	String respCode=object.getString("respCode");
    	
    
    	System.out.println("test");
 
    	
    }
   
    
    @Override
    public JSONObject close(PayOrder payOrder) {
    	return null;
    }
    
    
}
