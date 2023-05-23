package org.jeepay.pay.channel.yinyingtong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.shengfutong.SftpayConfig;
import org.jeepay.pay.channel.swiftpay.util.MD5;
import org.jeepay.pay.channel.yinyingtong.utils.YytSignUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class YytpayTransService extends BaseTrans {
    private final static String logPrefix = "【银盈通】";

    @Override
    public String getChannelName() {
        return YytpayConfig.CHANNEL_NAME;
    }

    long realtime=System.currentTimeMillis();

    SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");

    String time = sdf1.format(Calendar.getInstance().getTimeInMillis());

    /**
     * 代付订单
     *
     * @param transOrder
     * @return
     */
    @Override
    public JSONObject trans(TransOrder transOrder) {
        YytpayConfig yytpayConfig = new YytpayConfig(getTransParam(transOrder));
        Map<String, Object> parameters = buildRequstParam(yytpayConfig,transOrder);
        HashMap<String, Object> signMap = new HashMap<>();
        signMap.put("aid", "8a179b8c67bcf908016a256877414641");
        signMap.put("api_id", "eb_trans@agent_for_paying");
        signMap.put("app_key", "amnogienoqnimaieo|m2|20190416");
        signMap.put("timestamp", realtime);
        signMap.put("nonce", time);
        _log.info("{}申请代付签名参数：{}", logPrefix, signMap.toString());
        String data = JSON.toJSONString(parameters);

        String data_sign = YytSignUtils.getSignature(new String[] { data });

        //签名
        String signature = YytSignUtils.getSignature(signMap.get("aid").toString() ,null, signMap.get("api_id").toString(), signMap.get("app_key").toString(), signMap.get("timestamp").toString().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", ""), signMap.get("nonce").toString() ,  data_sign );

        //请求地址
        String URL = yytpayConfig.getRequestUrl() + "?aid="+signMap.get("aid").toString()+"&api_id="+signMap.get("api_id").toString()+"&signature="+signature+"&timestamp="+realtime+"&nonce="+time+"&method="+"POST";

        _log.info("{}申请代付请求参数：{}", logPrefix, parameters.toString());
//        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
//        JSONObject origin = requestTemplate.postForObject(URL, parameters, JSONObject.class);
        String result = YytSignUtils.httpurlconnectionpost(URL, data);
        _log.info("{}申请代付响应参数：{}", logPrefix, result);
        JSONObject origin = JSON.parseObject(result);
        JSONObject retObj = buildRetObj();
        retObj.put("isSuccess", false);
        retObj.put("transOrderId", transOrder.getTransOrderId());
        if (StringUtils.equals(YytpayConfig.REP_SUCCESS, origin.getString("op_ret_code"))) {
            retObj.put("channelOrderNo", origin.getString("transaction_id"));
            retObj.put("isSuccess", true);
            // 1. 处理中 2. 成功 3. 失败
            retObj.put("status", 1);
            return retObj;
        }
        // 1. 处理中 2. 成功 3. 失败
        retObj.put("status", 3);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, "下单失败[" + origin.getString("msg") + "]");
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        return retObj;
    }

    /**
     * 余额查询接口
     *
     * @param payParam
     * @return
     */
    @Override
    public JSONObject balance(String payParam) {
        return null;
    }

    /**
     * 订单查询接口
     *
     * @param transOrder
     * @return
     */
    @Override
    public JSONObject query(TransOrder transOrder) {
        return null;
    }

    /**
     * 构建实时代付请求参数
     *
     * @return
     */
    private Map<String, Object> buildRequstParam(YytpayConfig yytpayConfig,TransOrder transOrder) {
        Map<String, Object> parameters = new HashMap<>();
        //全局参数
        parameters.put("login_token","");//登陆令牌
        parameters.put("req_no",UUID.randomUUID().toString().replaceAll("-", ""));//请求流水号
        parameters.put("app_code",yytpayConfig.getPartnerId());//应用号
        parameters.put("app_version","0.0.1");//应用版本
        parameters.put("service_code",yytpayConfig.getPublicKey());//服务号
        parameters.put("plat_form","03");//平台

        //接口业务参数
        parameters.put("merchant_number",yytpayConfig.getMchId());//商户号
        parameters.put("order_number",transOrder.getTransOrderId());//商家原始订单号
        parameters.put("wallet_id",yytpayConfig.getPayProduct());//付款钱包id
        parameters.put("asset_id", yytpayConfig.getPayMode());//付款资产id(电子账户资产ID)
        parameters.put("business_type", "1");//业务类型 	暂时支持1、代付到个人储蓄卡；
        parameters.put("money_model", "1");//资金模式	支持：0 = T0-垫资 ,1 = T1-预存,2 = 混合模式
        parameters.put("source", "0");//source	支持：0 = API接口,1 = 分账宝,2 = 企业版账户
        parameters.put("password_type", "02");//付款方密码类型 支持：0 = API接口,1 = 分账宝,2 = 企业版账户
        parameters.put("encrypt_type", "02");//付款方加密类型	02-MD5;03-沙海密码控件；04-微通新城；05-微通新城H5

        String pass = MD5Util.string2MD5(yytpayConfig.getPrivateKey());
        parameters.put("pay_password", pass);//付款方加密类型	美付宝钱包支付密码，传入md5加密后的密文

        parameters.put("customer_type", "01");//收款客户类型	01-个人；02-企业
        parameters.put("customer_name", transOrder.getAccountName());// 收款客户姓名 客户姓名作为必要的校验数据项
        parameters.put("currency", yytpayConfig.CURRENCY);//币种
        parameters.put("amount", AmountUtil.convertCent2Dollar(String.valueOf(transOrder.getAmount())));//代付金额
        parameters.put("async_notification_addr", payConfig.getNotifyTransUrl(getChannelName()));//异步通知地址
        parameters.put("account_number", transOrder.getAccountNo());//异步通知地址
        parameters.put("issue_bank_name", transOrder.getBankName());//收款银行卡发卡行名称

        return parameters;
    }

}
