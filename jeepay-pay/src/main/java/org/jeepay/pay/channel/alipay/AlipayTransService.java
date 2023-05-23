package org.jeepay.pay.channel.alipay;

import javax.annotation.PostConstruct;

import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.util.EventBusUtil;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.BankcardExtInfo;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;

//import com.alipay.api.AlipayConfig;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.FileItem;
import java.util.Base64;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/25
 * @description:
 */
@Service
public class AlipayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(AlipayTransService.class);
    /**
     * 单笔转账到支付宝账户
     */
    public final static String PAY_CHANNEL_ALIPAY_TRANS_LOGONID = "alipay_trans_logonid";
    /**
     * 单笔转账到银行卡
     */
    public final static String PAY_CHANNEL_ALIPAY_TRANS_BANK = "alipay_trans_bank";
    
    
    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    @PostConstruct
    private void init(){
        EventBusUtil.getInstance().register(this);
    }
    
    @Override
    public JSONObject trans(TransOrder transOrder) {
        String channelId = transOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case PAY_CHANNEL_ALIPAY_TRANS_LOGONID :
                retObj = transLogonid(transOrder);
                break;
            case PAY_CHANNEL_ALIPAY_TRANS_BANK :
                retObj = transBank(transOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的支付宝渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }
    private JSONObject transLogonid(TransOrder transOrder){
        String logPrefix = "【单笔转账到支付宝】";
        String transOrderId = transOrder.getTransOrderId();
        JSONObject retObj = buildRetObj();
        AlipayConfig alipayConfig = new AlipayConfig(getTransParam(transOrder));
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(alipayConfig.getReqUrl());
        certAlipayRequest.setAppId(alipayConfig.getAppId());
        certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
        certAlipayRequest.setFormat(AlipayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(alipayConfig.getCertPath());
        certAlipayRequest.setAlipayPublicCertPath(alipayConfig.getAlipayPublicCertPath());
        certAlipayRequest.setRootCertPath(alipayConfig.getRootCertPath());
        AlipayClient client = null;
        try {
            client = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            _log.error(e, "");
            retObj = buildFailRetObj();
            return retObj;
        }
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(transOrderId);
        model.setTransAmount(AmountUtil.convertCent2Dollar(transOrder.getAmount().toString()));
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOrderTitle("支付宝-备付金账户");
        Participant participant = new Participant();
        participant.setIdentity(transOrder.getAccountNo());
        participant.setIdentityType("ALIPAY_LOGON_ID");
        participant.setName(transOrder.getAccountName());
        model.setPayeeInfo(participant);
        model.setRemark("备用金代付");
        request.setBizModel(model);
        // 设置异步通知地址
        request.setNotifyUrl(alipayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName())));
        // 设置同步跳转地址
        request.setReturnUrl(alipayConfig.transformUrl(payConfig.getReturnUrl(getChannelName())));

        retObj.put("transOrderId", transOrderId);
        retObj.put("isSuccess", false);
        try {
            AlipayFundTransUniTransferResponse response = client.certificateExecute(request);
            if(response.isSuccess()) {
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", response.getOrderId());
                //处理中
                retObj.put("status", 1);
                JSONObject msgObj = new JSONObject();
                msgObj.put("count", 1);
                msgObj.put("transOrderId", transOrderId);
                msgObj.put("channelName", getChannelName());
                retObj.put("transQuery", msgObj);
            }else {
                //出现业务错误
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
                //转账失败
                retObj.put("status", 3);
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }
    private JSONObject transBank(TransOrder transOrder){
        String logPrefix = "【支付宝转账到银行卡】";
        JSONObject retObj = buildRetObj();
        String transOrderId = transOrder.getTransOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getTransParam(transOrder));
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(alipayConfig.getReqUrl());
        certAlipayRequest.setAppId(alipayConfig.getAppId());
        certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
        certAlipayRequest.setFormat(AlipayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(alipayConfig.getCertPath());
        certAlipayRequest.setAlipayPublicCertPath(alipayConfig.getAlipayPublicCertPath());
        certAlipayRequest.setRootCertPath(alipayConfig.getRootCertPath());
        AlipayClient client = null;
        try {
            client = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            _log.error(e, "");
            retObj = buildFailRetObj();
            return retObj;
        }
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(transOrderId);
        model.setProductCode("TRANS_BANKCARD_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOrderTitle("支付宝-备付金账户");
        model.setTransAmount(AmountUtil.convertCent2Dollar(transOrder.getAmount().toString()));
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(transOrder.getAccountNo());
        payeeInfo.setIdentityType("BANKCARD_ACCOUNT");
        payeeInfo.setName(transOrder.getAccountName());
        BankcardExtInfo bankcardExtInfo = new BankcardExtInfo();
        bankcardExtInfo.setAccountType("2");
        bankcardExtInfo.setInstName(transOrder.getBankName());
        payeeInfo.setBankcardExtInfo(bankcardExtInfo);
        model.setPayeeInfo(payeeInfo);
        //model.setRemark(transOrder.getRemarkInfo());
        model.setRemark("备用金代付");
        JSONObject params = new JSONObject();
        params.put("withdraw_timeliness","T0");
        model.setBusinessParams(params.toJSONString());
        request.setBizModel(model);
        // 设置异步通知地址
        request.setNotifyUrl(alipayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName())));
        // 设置同步跳转地址
        request.setReturnUrl(alipayConfig.transformUrl(payConfig.getReturnUrl(getChannelName())));

        retObj.put("transOrderId", transOrderId);
        retObj.put("isSuccess", false);
        try {
            AlipayFundTransUniTransferResponse response = client.certificateExecute(request);
            if(response.isSuccess()) {
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", response.getOrderId());
                //处理中
                retObj.put("status", 1);
                JSONObject msgObj = new JSONObject();
                msgObj.put("count", 1);
                msgObj.put("transOrderId", transOrderId);
                msgObj.put("channelName", getChannelName());
                retObj.put("transQuery", msgObj);
            }else {
                //出现业务错误
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
                //转账失败
                retObj.put("status", 3);
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }
    
    @Override
    public JSONObject query(TransOrder transOrder) {
        String channelId = transOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case PAY_CHANNEL_ALIPAY_TRANS_LOGONID :
                retObj = queryLogonid(transOrder);
                break;
            case PAY_CHANNEL_ALIPAY_TRANS_BANK :
                retObj = queryBank(transOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的支付宝渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    private JSONObject queryBank(TransOrder transOrder) {
        String logPrefix = "【支付宝转账到银行卡查询】";
        JSONObject retObj = buildRetObj();
        String transOrderId = transOrder.getTransOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getTransParam(transOrder));
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(alipayConfig.getReqUrl());
        certAlipayRequest.setAppId(alipayConfig.getAppId());
        certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
        certAlipayRequest.setFormat(AlipayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(alipayConfig.getCertPath());
        certAlipayRequest.setAlipayPublicCertPath(alipayConfig.getAlipayPublicCertPath());
        certAlipayRequest.setRootCertPath(alipayConfig.getRootCertPath());
        AlipayClient client = null;
        try {
            client = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            _log.error(e, "");
            retObj = buildFailRetObj();
            return retObj;
        }
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest ();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        model.setOutBizNo(transOrderId);
        model.setProductCode("TRANS_BANKCARD_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOrderId(transOrder.getChannelOrderNo());
        request.setBizModel(model);
        retObj.put("transOrderId", transOrderId);
        try {
            AlipayFundTransCommonQueryResponse response = client.certificateExecute(request);
            retObj.put("channelOrderNo", response.getOrderId());
            if(response.isSuccess()){
                retObj.put("channelObj",JSON.toJSON(response));
                retObj.put("isSuccess", true);
                //转账成功
                retObj.put("status", 2);
            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
                //转账失败
                retObj.put("status", 3);
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }

    private JSONObject queryLogonid(TransOrder transOrder) {
        String logPrefix = "【支付宝转账到支付宝查询】";
        JSONObject retObj = buildRetObj();
        String transOrderId = transOrder.getTransOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getTransParam(transOrder));
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(alipayConfig.getReqUrl());
        certAlipayRequest.setAppId(alipayConfig.getAppId());
        certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
        certAlipayRequest.setFormat(AlipayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(alipayConfig.getCertPath());
        certAlipayRequest.setAlipayPublicCertPath(alipayConfig.getAlipayPublicCertPath());
        certAlipayRequest.setRootCertPath(alipayConfig.getRootCertPath());
        AlipayClient client = null;
        try {
            client = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            _log.error(e, "");
            retObj = buildFailRetObj();
            return retObj;
        }
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
        model.setOutBizNo(transOrderId);
        model.setOrderId(transOrder.getChannelOrderNo());
        request.setBizModel(model);
        retObj.put("transOrderId", transOrderId);
        try {
            AlipayFundTransOrderQueryResponse response = client.certificateExecute(request);
            retObj.put("channelOrderNo", response.getOrderId());
            if(response.isSuccess()){
                retObj.put("channelObj",JSON.toJSON(response));
                retObj.put("isSuccess", true);
                //转账成功
                retObj.put("status", 2);
            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
                //转账失败
                retObj.put("status", 3);
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }

    @Override
	public JSONObject balance(String payParam) {
        String logPrefix = "【余额查询】";
        JSONObject retObj = buildRetObj();
        AlipayConfig alipayConfig = new AlipayConfig(payParam);
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(alipayConfig.getReqUrl());
        certAlipayRequest.setAppId(alipayConfig.getAppId());
        certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
        certAlipayRequest.setFormat(AlipayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(alipayConfig.getCertPath());
        certAlipayRequest.setAlipayPublicCertPath(alipayConfig.getAlipayPublicCertPath());
        certAlipayRequest.setRootCertPath(alipayConfig.getRootCertPath());
        AlipayClient alipayClient = null;
        try {
        	alipayClient = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            _log.error(e, "");
            retObj = buildFailRetObj();
            return retObj;
        }
        AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAlipayUserId(alipayConfig.getPid());
        model.setAccountType("ACCTRANS_ACCOUNT");
        request.setBizModel(model);
        try {
        	AlipayFundAccountQueryResponse response = alipayClient.certificateExecute(request);
	        retObj.put("available_amount", response.getBody());
	        if (response.isSuccess()) {
	        	retObj.put("channelObj",JSON.toJSON(response));
                String balance = response.getAvailableAmount();
                retObj.put("cashBalance", balance);           // 余额
	        } else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
	        }
        } catch (AlipayApiException e) {
            _log.error(e, "查询余额异常");
            retObj = buildFailRetObj();
        }
        return retObj;
	}
    

}
