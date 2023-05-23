package org.jeepay.pay.channel.sand;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.unify.AbstractPaymentConfig;
import org.jeepay.common.util.DateUtils;
import org.jeepay.common.util.FormUrlDecoder;
import org.jeepay.common.util.HtmlUtils;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.CertDescriptor;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.sign.encrypt.Base64;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.sand.response.SandAgentpayResBody;
import org.jeepay.pay.channel.sand.utils.CryptoUtil;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @Package org.jeepay.pay.channel.sand
 * @Class: SandTransService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/12 10:49
 * @Version
 **/
@Component
public class SandTransService extends BaseTrans {
    private final static String logPrefix = "【杉德支付代付】";

    private static final String CILRSA = "RSA/ECB/PKCS1Padding";

    private  static final String CILAES = "AES/ECB/PKCS5Padding";

    private static final String AES = "AES";

    @Override
    public String getChannelName() {
        return SandConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject trans(TransOrder transOrder) {
        _log.info("{}订单信息：{}", logPrefix, JSON.toJSONString(transOrder));
        SandConfig sandConfig = new SandConfig(getTransParam(transOrder));
        CertDescriptor certDescriptor = new CertDescriptor();
        JSONObject bizParameters = getAgentpayParameters(sandConfig, transOrder);
        _log.info("{}请求参数：{}", logPrefix, bizParameters.toJSONString());
        Map<String, Object> requestBody = buildAgentpayFinalBody(sandConfig, certDescriptor, bizParameters, SandConfig.AGENT_PAY);
        _log.info("{}最终请求报文：{}", logPrefix, JSON.toJSONString(requestBody));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(sandConfig.getRequestUrl(), SandConfig.AGENT_APPLAY_URL),
                requestBody, String.class);
        // sand原始响应报文解密后的数据
        JSONObject parseResult = handleResponse(sandConfig, certDescriptor, origin);
        SandAgentpayResBody sandAgentpayResBody = JSONObject.toJavaObject(parseResult, SandAgentpayResBody.class);
        JSONObject retObj = buildRetObj();
        if(StringUtils.equals(sandAgentpayResBody.getRespCode(), SandConfig.AGENG_RETURN_VALUE_SUCCESS)) {
            rpcCommonService.rpcTransOrderService.updateTransParam2(transOrder.getTransOrderId(), sandAgentpayResBody.getTranTime());
            retObj.put("isSuccess", true);
            retObj.put("transOrderId", transOrder.getTransOrderId());
            retObj.put("channelOrderNo", sandAgentpayResBody.getSandSerial());
            retObj.putAll(parseResult);
            // 杉德结果状态：0-成功 1-失败 2-处理中
            if(StringUtils.equals(sandAgentpayResBody.getResultFlag(), "0")) {
                retObj.put("status", 2); // 2表示成功
            } else if (StringUtils.equals(sandAgentpayResBody.getResultFlag(), "1")) {
                retObj.put("status", 3); // 3表示失败
            } else {
                retObj.put("status", 1); // 1处理中
            }
            return retObj;
        }

        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, sandAgentpayResBody.getRespCode());
        retObj.put("channelErrCode", sandAgentpayResBody.getRespDesc());
        retObj.put("isSuccess", false);
        return retObj;
    }

    @Override
    public JSONObject query(TransOrder transOrder) {
        SandConfig sandConfig = new SandConfig(getTransParam(transOrder));
        CertDescriptor certDescriptor = new CertDescriptor();
        JSONObject bizParameters = getAgentpayQueryParameters(sandConfig, transOrder);
        _log.info("{}查询请求参数：{}", logPrefix, bizParameters.toJSONString());
        Map<String, Object> requestBody = buildAgentpayFinalBody(sandConfig, certDescriptor, bizParameters, SandConfig.ORDER_QUERY);
        _log.info("{}查询最终请求报文：{}", logPrefix, JSON.toJSONString(requestBody));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(sandConfig.getRequestUrl(), SandConfig.AGENT_ORDER_QUERY_URL),
                requestBody, String.class);
        // sand原始响应报文解密后的数据
        JSONObject parseResult = handleResponse(sandConfig, certDescriptor, origin);
        SandAgentpayResBody sandAgentpayResBody = JSONObject.toJavaObject(parseResult, SandAgentpayResBody.class);

        JSONObject retObj = buildRetObj();
        if(StringUtils.equals(sandAgentpayResBody.getRespCode(), SandConfig.AGENG_RETURN_VALUE_SUCCESS)) {
            retObj.put("isSuccess", true);
            retObj.put("transOrderId", transOrder.getTransOrderId());
            retObj.put("channelObj", parseResult);
            // 杉德结果状态：0-成功 1-失败 2-处理中
            if(StringUtils.equals(sandAgentpayResBody.getResultFlag(), "0")) {
                retObj.put("status", 2); // 2表示成功
            } else if (StringUtils.equals(sandAgentpayResBody.getResultFlag(), "1")) {
                retObj.put("status", 3); // 3表示失败
            } else {
                retObj.put("status", 1); // 1处理中
            }
            return retObj;
        }

        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, String.format("code:%s,desc:%s", sandAgentpayResBody.getRespCode(), sandAgentpayResBody.getRespDesc()));
        retObj.put("channelErrCode", sandAgentpayResBody.getRespDesc());
        retObj.put("isSuccess", false);
        return retObj;
    }

    @Override
    public JSONObject balance(String payParam) {
        SandConfig sandConfig = new SandConfig(payParam);
        CertDescriptor certDescriptor = new CertDescriptor();

        JSONObject bizParameters = getAgentpayQueryBalanceParameters(sandConfig);
        _log.info("{}余额查询请求参数：{}", logPrefix, bizParameters.toJSONString());
        Map<String, Object> requestBody = buildAgentpayFinalBody(sandConfig, certDescriptor, bizParameters, SandConfig.MER_BALANCE_QUERY);
        _log.info("{}余额查询最终请求报文：{}", logPrefix, JSON.toJSONString(requestBody));
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(sandConfig.getRequestUrl(), SandConfig.AGENT_BALANCE_QUERY_URL),
                requestBody, String.class);

        // sand原始响应报文解密后的数据
        JSONObject parseResult = handleResponse(sandConfig, certDescriptor, origin);
        SandAgentpayResBody sandAgentpayResBody = JSONObject.toJavaObject(parseResult, SandAgentpayResBody.class);

        JSONObject retObj = buildRetObj();
        if(StringUtils.equals(sandAgentpayResBody.getRespCode(), SandConfig.AGENG_RETURN_VALUE_SUCCESS)) {
            String balance = sandAgentpayResBody.getBalance();
            String creditAmt= StringUtils.isNotBlank(sandAgentpayResBody.getCreditAmt()) ? sandAgentpayResBody.getCreditAmt() : "0";
            if(balance.startsWith("+")) {
                balance = balance.substring(1, balance.length());
            }
            if(creditAmt.startsWith("+")) {
                creditAmt = creditAmt.substring(1, creditAmt.length());
            }
            retObj.put("cashBalance", Util.conversionYuanAmount(Long.valueOf(balance)));
            retObj.put("payBalance", Util.conversionYuanAmount(Long.valueOf(creditAmt)));
            return retObj;
        }
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        retObj.put(PayConstant.RESULT_PARAM_ERRDES, String.format("code:%s,desc:%s", sandAgentpayResBody.getRespCode(), sandAgentpayResBody.getRespDesc()));
        return retObj;
    }

    /**
     * 格式化金额让其符合Sand上传格式
     * @param price
     * @return
     */
    private String foramtPrice(long price) {
        return String.format("%012d",price);
    }

    private PublicKey getPublicKey(AbstractPaymentConfig config, CertDescriptor certDescriptor) {
        certDescriptor.initPublicCert(config.getPublicStorePath());
        PublicKey publicKey = certDescriptor.getPublicCert().getPublicKey();
        return publicKey;
    }

    private PrivateKey getPrivateKey(AbstractPaymentConfig config, CertDescriptor certDescriptor) {
        certDescriptor.initPrivateSignCert(config.getPrivateStorePath(), config.getPrivateStorePathPwd(), "PKCS12");
        return certDescriptor.getSignCertPrivateKey(config.getPrivateStorePathPwd());
    }

    /**
     * RSA数字签名
     * @return
     */
    private String getSign(AbstractPaymentConfig config, String requestBody) {
        CertDescriptor certDescriptor = new CertDescriptor();
        certDescriptor.initPrivateSignCert(config.getPrivateStorePath(), config.getPrivateStorePathPwd(), "PKCS12");
        String key = Base64.encode(certDescriptor.getSignCertPrivateKey(config.getPrivateStorePathPwd()).getEncoded());
        String signValue = SignUtils.RSA.createSign(requestBody, key, SignUtils.CHARSET_UTF8);
        return signValue;
    }

    /**
     * 处理同步请求响应报文
     * @param origin 请求返回的同步原始报文
     * @return
     */
    private JSONObject handleResponse(AbstractPaymentConfig config, CertDescriptor certDescriptor, String origin) {
        _log.info("{}同步响应原始报文：{}", logPrefix, origin);
        origin = HtmlUtils.decodeURI(origin);
        _log.info("{}同步响应URL解码后报文：{}", logPrefix, origin);
        // 将key=value&key1=value1形式的参数解析成map键值对
        Map<String, String> responseInfo = FormUrlDecoder.getMap(origin);
        JSONObject resJson = JSONObject.parseObject(JSON.toJSONString(responseInfo));
        _log.info("{}响应参数转换JSON：{}", logPrefix, resJson.toJSONString());
        String retEncryptKey = resJson.getString("encryptKey");
        String retEncryptData = resJson.getString("encryptData");
        String retSign = resJson.getString("sign");
        byte[] decodeBase64KeyBytes = new byte[0];
        try {
            decodeBase64KeyBytes = Base64.decode(retEncryptKey);

            byte[] merchantAESKeyBytes = CryptoUtil.RSADecrypt(
                    decodeBase64KeyBytes, getPrivateKey(config, certDescriptor), 2048, 11, CILRSA);

            byte[] decodeBase64DataBytes = Base64.decode(retEncryptData);

            byte[] respDataBytes = CryptoUtil.AESDecrypt(decodeBase64DataBytes,
                    merchantAESKeyBytes, AES, CILAES, null);

            String respData = new String(respDataBytes, SignUtils.CHARSET_UTF8);
            _log.info("{}响应报文解密：{}", logPrefix, respData);
            return JSONObject.parseObject(respData);
        } catch (Exception e) {
            _log.error(e, e.getMessage());
            throw new ServiceException(RetEnum.RET_COMM_RES_PARSE_ERROR);
        }
    }

    /**
     * 代付请求报文
     * @param config
     * @param transOrder
     * @return
     */
    private JSONObject getAgentpayParameters(AbstractPaymentConfig config, TransOrder transOrder) {
        JSONObject transferMap = new JSONObject();
        transferMap.put("version", SandConfig.AP_PARAMS_VERSION_VAL);
        transferMap.put("productId", SandTransactionType.AGENT_PAY.getProductId());
        transferMap.put("tranTime", DateUtils.YYYYMMDDHHMMSS.format(new Date()));
        transferMap.put("orderCode", transOrder.getTransOrderId());
        transferMap.put("tranAmt", foramtPrice(transOrder.getAmount()));
        // 币种，固定填写156
        transferMap.put("currencyCode", SandConfig.AP_PARAM_CURRENCY_VAL);
        //账户属性     0-对私   1-对公
        transferMap.put("accAttr", transOrder.getAccountAttr());
        //账号类型      3-公司账户  4-银行卡
        transferMap.put("accType", SandConfig.AP_PARAM_ACC_TYPE_VAL);
        transferMap.put("accNo", transOrder.getAccountNo());
        transferMap.put("accName", transOrder.getAccountName());
        transferMap.put("remark", transOrder.getRemarkInfo());
        transferMap.put("notifyUrl", payConfig.getNotifyTransUrl(getChannelName()));
        transferMap.put("bankName", transOrder.getBankName());
        return  transferMap;
    }

    /**
     * 代付查询业务请求报文
     * @param config
     * @param transOrder
     * @return
     */
    private JSONObject getAgentpayQueryParameters(AbstractPaymentConfig config, TransOrder transOrder) {
        JSONObject tquery = new JSONObject();
        tquery.put("version", SandConfig.AP_PARAMS_VERSION_VAL);
        tquery.put("productId", SandTransactionType.AGENT_PAY.getProductId());
        tquery.put("tranTime", DateUtils.YYYYMMDDHHMMSS.format(new Date())); // DateUtils.YYYYMMDDHHMMSS.format(transOrder.getCreateTime())
        tquery.put("orderCode", transOrder.getTransOrderId());
        return tquery;
    }

    private JSONObject getAgentpayQueryBalanceParameters(AbstractPaymentConfig config) {
        JSONObject tquery = new JSONObject();
        tquery.put("version", SandConfig.AP_PARAMS_VERSION_VAL);
        tquery.put("productId", SandTransactionType.AGENT_PAY.getProductId());
        String currTime = DateUtils.YYYYMMDDHHMMSS.format(new Date());
        tquery.put("tranTime", currTime);
        tquery.put("orderCode", currTime);
        tquery.put("channelType", "");
        tquery.put("extend", "");
        return tquery;
    }

    /**
     * 构建代付最终请求参数body，包括请求加密等
     * @param config
     * @param bizParameters
     * @param transCode 交易码, 如RTPM表示实时代付
     * @return
     */
    private Map<String, Object> buildAgentpayFinalBody(AbstractPaymentConfig config, CertDescriptor certDescriptor, JSONObject bizParameters, String transCode) {
        Map<String, Object> finalBody = Maps.newHashMap();
        try {
            //设置随机加密串
            String aesKey = "1223456789101112";//SignUtils.randomStr();
            byte[] aesKeyBytes = aesKey.getBytes(SignUtils.CHARSET_UTF8);
            PublicKey publicKey = getPublicKey(config, certDescriptor);

            // 使用商户公钥对随机加密传进行RSA加密
            String encryptKey = Base64.encode(CryptoUtil.RSAEncrypt(aesKeyBytes, publicKey, 2048, 11, CILRSA));
            // 对请求的业务参数作AES加密
            String bizParametersJsonStr = bizParameters.toJSONString();
            byte [] bizBytes = bizParametersJsonStr.getBytes(SignUtils.CHARSET_UTF8);
            String encryptAes = Base64.encode(CryptoUtil.AESEncrypt(bizBytes, aesKeyBytes, AES, CILAES, null));
            //填充请求数据字典
            // 交易码, RTPM表示实时代付
            finalBody.put("transCode", transCode);
            // 接入类型 0-商户接入，默认   1-平台接入
            finalBody.put("accessType", SandConfig.AP_PARAM_ACCESS_TYPE_VAL);
            finalBody.put("merId", config.getMchId());
            // 平台商户ID	平台接入必填，商户接入为空
            finalBody.put("plId", "");
            // 加密后的AES秘钥
            finalBody.put("encryptKey", encryptKey);
            // 加密后的请求/应答报文
            finalBody.put("encryptData", encryptAes);
            finalBody.put("sign", getSign(config, bizParametersJsonStr));
        } catch (UnsupportedEncodingException e) {
            _log.error(e, "String covert byte array exception");
            throw new ServiceException(RetEnum.RET_COMM_UNKNOWN_ERROR);
        } catch (Exception e) {
            _log.error(e, e.getMessage());
            throw new ServiceException((RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
        return finalBody;
    }
}
