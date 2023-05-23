package org.jeepay.pay.channel.yldevpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.yldevpay.bean.MsgBean;
import org.jeepay.pay.channel.yldevpay.bean.MsgBody;
import org.jeepay.pay.channel.yldevpay.util.Base64;
import org.jeepay.pay.channel.yldevpay.util.Util;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.mq.Mq4TransQuery;
import org.jeepay.pay.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class YldevpayTransService extends BaseTrans {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Autowired
    private Mq4TransQuery mq4TransQuery;

    private static final MyLog _log = MyLog.getLog(YldevpayTransService.class);

    @Override
    public String getChannelName() {
        return YldevpayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject trans(TransOrder transOrder) {
        String channelId = transOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case YldevpayConfig.CHANNEL_NAME_PAY :
                retObj = doTransPay(transOrder);
                break;
            case YldevpayConfig.CHANNEL_NAME_BATCH_PAY :
                retObj = doTransPay(transOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    public JSONObject doTransPay(TransOrder transOrder) {
        String logPrefix = "【易联代付】";
        JSONObject retObj = buildRetObj();
        try {
            String transOrderId = transOrder.getTransOrderId();
            YldevpayConfig yldevpayConfig = new YldevpayConfig(getTransParam(transOrder));
            MsgBean req_bean = new MsgBean();
            req_bean.setVERSION("2.1");
            req_bean.setMSG_TYPE("100001");
            req_bean.setBATCH_NO(transOrderId);
            req_bean.setUSER_NAME(yldevpayConfig.getUserName());

            MsgBody body = new MsgBody();
            body.setSN(transOrderId);//流水号，同一批次不重复即可
            body.setACC_NO(transOrder.getAccountNo()); //邮储
            body.setACC_NAME(transOrder.getAccountName());
            body.setAMOUNT(AmountUtil.convertCent2Dollar(transOrder.getAmount().toString()));
            body.setACC_PROVINCE(transOrder.getProvince());
            body.setACC_CITY(transOrder.getCity());
            body.setMER_ORDER_NO(transOrderId);
            req_bean.getBODYS().add(body);

            String req = Util.signANDencrypt(req_bean, yldevpayConfig);
            String result = HttpUtils.getInstance().post(yldevpayConfig.getReqUrl(), req);
            _log.info("返回数据：{}",result);
            if(StringUtils.isBlank(result)) {
                _log.info("{} >>> 请求易联转账没有响应,将转账转为失败", logPrefix);
                retObj.put("status", 3);    // 失败
            }else {
                MsgBean res_bean = Util.decryptANDverify(result, yldevpayConfig);
                _log.info(res_bean.toXml());
                // 处理中
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    MsgBody msgBody = res_bean.getBODYS().get(0);
                    //处理成功
                    if("0000".equals(msgBody.getPAY_STATE())){
                        // 交易成功
                        _log.info("{} >>> 转账成功", logPrefix);
                        retObj.put("transOrderId", transOrderId);
                        retObj.put("status", 2);            // 成功
                        retObj.put("transOrderId", transOrderId);
                    }else if("00A4".equals(msgBody.getPAY_STATE())) {
                        // 交易处理中
                        _log.info("{} >>> 转账处理中", logPrefix);
                        retObj.put("status", 1);    // 处理中
                        JSONObject msgObj = new JSONObject();
                        msgObj.put("count", 1);
                        msgObj.put("transOrderId", transOrderId);
                        msgObj.put("channelName", getChannelName());
                        mq4TransQuery.send(msgObj.toJSONString(), 10 * 1000);  // 10秒后查询
                    }else{
                        // 交易失败
                        _log.info("{} >>> 转账失败", logPrefix);
                        retObj.put("status", 3);    // 失败
                        retObj.put("isSuccess", false);
                        retObj.put("transOrderId", transOrderId);
                        retObj.put("channelErrCode", msgBody.getPAY_STATE());
                        retObj.put("channelErrMsg", msgBody.getREMARK());
                    }
                }else{
                    // 交易失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    retObj.put("status", 3);    // 失败
                }
            }
            return retObj;
        }catch (Exception e){
            _log.error(e, "易联转账异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    @Override
    public JSONObject query(TransOrder transOrder) {
        String channelId = transOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case YldevpayConfig.CHANNEL_NAME_PAY :
                retObj = doQuery(transOrder,"100002");
                break;
            case YldevpayConfig.CHANNEL_NAME_BATCH_PAY :
                retObj = doQuery(transOrder,"100002");
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }
    public JSONObject doQuery(TransOrder transOrder,String msgType) {
        String logPrefix = "【易联转账查询】";
        JSONObject retObj = buildRetObj();
        try{
            YldevpayConfig yldevpayConfig = new YldevpayConfig(getTransParam(transOrder));
            String transOrderId = transOrder.getTransOrderId();
            MsgBean req_bean = new MsgBean();
            req_bean.setVERSION("2.1");
            req_bean.setMSG_TYPE(msgType);
            req_bean.setBATCH_NO(transOrderId);//同代付交易请求批次号
            req_bean.setUSER_NAME(yldevpayConfig.getUserName());//系统后台登录名 302020000001

		    MsgBody body = new MsgBody();
		    body.setQUERY_NO_FLAG("1");
		    body.setMER_ORDER_NO(transOrderId);
		    req_bean.getBODYS().add(body);

            String req = Util.signANDencrypt(req_bean, yldevpayConfig);
            String result = HttpUtils.getInstance().post(yldevpayConfig.getReqUrl(), req);

            if(StringUtils.isBlank(result)) {
                _log.info("{} >>> 请求易联没有响应,将转账转为失败", logPrefix);
                retObj.put("status", 3);    // 失败
            }else {
                MsgBean res_bean = Util.decryptANDverify(result, yldevpayConfig);
                _log.info(res_bean.toXml());
                // 1-成功
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    List<MsgBody> msgBodies = res_bean.getBODYS();
                    for(MsgBody msgBody : msgBodies){
                        if("0000".equals(msgBody.getPAY_STATE())){
                            // 交易成功
                            _log.info("{} >>> 转账成功", logPrefix);
                            retObj.put("transOrderId", msgBody.getMER_ORDER_NO());
                            retObj.put("status", 2);            // 成功
                            retObj.put("channelOrderNo",  msgBody.getSUCCESS_DATE());
                        }else if("00A4".equals(msgBody.getPAY_STATE())) {
                            // 交易处理中
                            _log.info("{} >>> 转账处理中", logPrefix);
                            retObj.put("status", 1);    // 处理中
                        }else {
                            // 交易失败
                            _log.info("{} >>> 转账失败", logPrefix);
                            retObj.put("status", 3);    // 失败
                            retObj.put("channelOrderNo", msgBody.getSUCCESS_DATE());
                            retObj.put("channelErrCode", msgBody.getPAY_STATE());
                            retObj.put("channelErrMsg", msgBody.getREMARK());
                        }
                    }
                }else{
                    // 交易处理中
                    _log.info("{} >>> 转账处理中", logPrefix);
                    retObj.put("status", 1);    // 处理中
                }
            }
            return retObj;
        }catch (Exception e) {
            _log.error(e, "易联代付查询异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    @Override
    public JSONObject balance(String payParam) {
        String logPrefix = "【易联余额查询】";
        _log.info("余额查询:{}",payParam);

        JSONObject retObj = buildRetObj();
        try{
            YldevpayConfig yldevpayConfig = new YldevpayConfig(payParam);
            MsgBean req_bean = new MsgBean();
            req_bean.setVERSION("2.1");
            req_bean.setMSG_TYPE("600001");
            req_bean.setBATCH_NO(new String(Base64.decode(Util.generateKey(99999,14))));//同代付交易请求批次号
            req_bean.setUSER_NAME(yldevpayConfig.getUserName());//系统后台登录名 302020000001

            String req = Util.signANDencrypt(req_bean, yldevpayConfig);
            String result = HttpUtils.getInstance().post(yldevpayConfig.getReqUrl(), req);

            if(StringUtils.isBlank(result)) {
                _log.info("{} >>> 请求易联没有响应", logPrefix);
            }else {
                MsgBean res_bean = Util.decryptANDverify(result, yldevpayConfig);
                _log.info(res_bean.toXml());
                // 1-成功
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    MsgBody msgBody = res_bean.getBODYS().get(0);
                    if("0000".equals(msgBody.getPAY_STATE())){
                        // 成功
                        _log.info("{} >>> 成功", logPrefix);
                        retObj.put("cashBalance", new BigDecimal(msgBody.getAMOUNT()));
                    }
                }
            }
            return retObj;
        }catch (Exception e) {
            _log.error(e, "易联代付余额查询异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }


}
