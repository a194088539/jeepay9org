package org.jeepay.pay.channel.shengfutong;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.jeepay.common.bean.Bank;
import org.jeepay.common.http.HttpConfigStorage;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package org.jeepay.pay.channel.shengfutong
 * @Class: SftpayTransService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/8 16:51
 * @Version
 **/
@Component
public class SftpayTransService extends BaseTrans {

    private final static String logPrefix = "【盛付通】";

    private final static String AGENT_PAY_REQUEST_STR = "/Payment_Dfpay_add.html";
    private final static String AGENT_PAY_QUERY_REQUEST_STR = "/Payment_Dfpay_query.html";
    private final static String AGENT_PAY_BALANCE_STR = "/balance.do";


    @Override
    public String getChannelName() {
        return SftpayConfig.CHANNEL_NAME;
    }

    /**
     * 代付订单
     *
     * @param transOrder
     * @return
     */
    @Override
    public JSONObject trans(TransOrder transOrder) {
        JSONObject retObj = buildRetObj();
        SftpayConfig sftpayConfig = new SftpayConfig(getTransParam(transOrder));
        Map<String, Object> parameters = buildRequstParam(sftpayConfig, transOrder);
        _log.info("{}申请代付请求参数：{}", logPrefix, parameters.toString());
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        httpConfigStorage.setMaxTotal(5);
        httpConfigStorage.setDefaultMaxPerRoute(3);
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(httpConfigStorage);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(sftpayConfig.getRequestUrl(), AGENT_PAY_REQUEST_STR), parameters, JSONObject.class);
        _log.info("{}申请代付响应参数：{}", logPrefix, origin.toJSONString());

        retObj.put("isSuccess", false);
        retObj.put("transOrderId", transOrder.getTransOrderId());
        if (StringUtils.equals(SftpayConfig.RESPONSE_RESULT_SUCCESS, origin.getString("status"))) {
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
        SftpayConfig sftpayConfig = new SftpayConfig(payParam);
        JSONObject parameters = new JSONObject();
        parameters.put("sign", getSign(sftpayConfig, parameters));
        _log.info("{}代付余额查询请求参数：{}", logPrefix, parameters.toJSONString());
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(sftpayConfig.getRequestUrl(), AGENT_PAY_BALANCE_STR), parameters, JSONObject.class);
        _log.info("{}代付余额查询响应参数：{}", logPrefix, origin.toJSONString());
        JSONObject retObj = buildRetObj();
        if (StringUtils.equals(SftpayConfig.RETURN_VALUE_SUCCESS, origin.getString("code"))) {
            JSONObject bizContext = origin.getJSONObject("data");
            String balance = bizContext.getString("balance");
            retObj.put("cashBalance", balance);
            retObj.put("payBalance", balance);
            return retObj;
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("msg"));
        return retObj;
    }

    /**
     * 订单查询接口
     *
     * @param transOrder
     * @return
     */
    @Override
    public JSONObject query(TransOrder transOrder) {
        SftpayConfig sftpayConfig = new SftpayConfig(getTransParam(transOrder));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("out_trade_no", transOrder.getTransOrderId());
        parameters.put("mchid", sftpayConfig.getAppId());
        parameters.put("pay_md5sign", getSign(sftpayConfig, parameters));
        _log.info("{}代付查询请求参数：{}", logPrefix, parameters.toString());
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        JSONObject origin = requestTemplate.postForObject(getReqUrl(sftpayConfig.getRequestUrl(), AGENT_PAY_QUERY_REQUEST_STR), parameters, JSONObject.class);
        _log.info("{}代付查询响应参数：{}", logPrefix, origin.toJSONString());
        JSONObject retObj = buildRetObj();
        if (StringUtils.equals(SftpayConfig.RESPONSE_RESULT_SUCCESS, origin.getString("status"))) {
            retObj.put("isSuccess", true);
            retObj.put("transOrderId", transOrder.getTransOrderId());
            retObj.putAll(origin);
            /**
             *  status状态说明 1 => 成功 2 => 失败 3 => 处理中 4 => 待处理 5 => 审核驳回 6 => 待审核 7=> 交易不存在 8 => 未知状态
             */
            Integer status = origin.getInteger("refCode");
            if (status == 1) {
                retObj.put("status", 2); // 2表示成功
            } else if (status == 2 || status == 5 || status == 7 || status == 8) {
                retObj.put("status", 3); // 3表示失败
            } else {
                retObj.put("status", 1); // 1处理中
            }
            retObj.put("channelOrderNo", origin.getString("transaction_id"));
            retObj.put("channelObj", origin);
            return retObj;
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, origin.getString("msg"));
        retObj.put("channelErrCode", origin.getString("status"));
        retObj.put("isSuccess", false);
        return retObj;
    }

    /**
     * 构建实时代付请求参数
     *
     * @return
     */
    private Map<String, Object> buildRequstParam(SftpayConfig sftpayConfig, TransOrder transOrder) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mchid", sftpayConfig.getAppId());
        parameters.put("out_trade_no", transOrder.getTransOrderId());
        parameters.put("money", AmountUtil.convertCent2DollarShort(String.valueOf(transOrder.getAmount())));
        parameters.put("bankname", transOrder.getBankName());
        parameters.put("subbranch", transOrder.getBankName());
        parameters.put("accountname", transOrder.getAccountName());
        parameters.put("cardnumber", transOrder.getAccountNo());
        parameters.put("province", transOrder.getProvince());
        parameters.put("city", transOrder.getCity());
        parameters.put("pay_md5sign", getSign(sftpayConfig, parameters));
        return parameters;
    }

    private String getSign(SftpayConfig sftpayConfig, Map<String, Object> parameters) {
        String signTxt = SignUtils.parameterText(parameters);
        String sign = SignUtils.MD5.createSign(signTxt, "&key=" + sftpayConfig.getPrivateKey(), SignUtils.CHARSET_UTF8);
        return sign.toUpperCase();
    }

}
