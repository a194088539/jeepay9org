package org.jeepay.pay.channel.bufpay;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.mq.BaseNotify4CashColl;
import org.jeepay.pay.util.EncryptUtils;
import org.jeepay.pay.util.HttpClientHelper;
import org.jeepay.pay.util.HttpsClientHelper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class BufPayNotifyService extends BasePayNotify {

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_BUF;
    }
    @Autowired
    private BaseNotify4CashColl baseNotify4CashColl;


    @Override
    public JSONObject doNotify(Object notifyData) {
    	
    	String payLoad =null;
    	
    	
        try {
        	payLoad= IOUtils.toString(((ServletRequest) notifyData).getReader());
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        //商户订单号
       	
    	JSONObject 	object=JSONObject.parseObject(payLoad);
    	
    	String traceno=object.getString("traceno");
    	
        System.out.println("商户订单号为===========>"+traceno);
    	
    	//格式化处理}

        //构建返回对象
        JSONObject retObj = buildRetObj();
        
        PayOrder payOrder;
        //修改订单状态
        
        //通过查询订单号找对订单对象
        payOrder= rpcCommonService.rpcPayOrderService.selectPayOrder(traceno);
        
        int updatePayOrderRows;
        
        updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), null, null);
        if (updatePayOrderRows != 1) {
            retObj.put("resResult", PayConstant.RETURN_ALIPAY_VALUE_FAIL);
            return retObj;
        }
        payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        
     //   payOrder
        //订单支付成功后，mq调用支付宝结算接口，进行资金归集操作。
        baseNotify4CashColl.doNotify(payOrder.getPayOrderId());
        System.out.println("====== 完成处理buf支付宝h5回调通知 ======");
        try {
			baseNotify4MchPay.doNotify(payOrder, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
        return retObj;
    }

    @Override
    public JSONObject doReturn(Object notifyData) {
        return super.doReturn(notifyData);
    }

    /*
    * 解析回调数据
    * */
    public Map buildNotifyData(HttpServletRequest request) {
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    /*
    * 验证回调数据
    * */
    @SuppressWarnings("unchecked")
	public boolean verifyAliPayParams(Map<String,Object> payContext){
    	
        Map<String,String> params = new HashMap<String,String>();
        params=(Map<String,String>)payContext.get("parameters");
        String transDate = params.get("transDate"); 		// 交易日期
        String transTime = params.get("transTime");		// 交易时间
        String merchno = params.get("merchno");		//商户号
        
        String amount = params.get("amount");		//交易金额
        String traceno = params.get("traceno");		//商户流水号
        String payType = params.get("payType");		//支付方式
        String status = params.get("status");		//交易状态
        String signature = params.get("signature");		//signature
        
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = traceno;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        //验证签名
        boolean verify_result = true;
        
        /**
        String md5Str="amount="+amount+
        		"&merchno="+merchno+
        		"&payType="+payType+
        		"&status="+status+
        		"&traceno="+traceno+
        		"&transDate="+transDate+
        		"&transTime="+transTime;
        
        String md5= EncryptUtils.encrypt(md5Str);
        
        
        if(signature.equals(md5)) {
        	
        	verify_result=true;
        }else {
        	
        	verify_result=false;
        	
        }
      **/
        
        if(!verify_result){
            errorMessage = "rsaCheckV1 failed.";
            payContext.put("retMsg", errorMessage);
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

    @Override
    public String getPayParam(PayOrder payOrder) {
        return super.getPayParam(payOrder);
    }

}
