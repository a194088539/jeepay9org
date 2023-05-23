package org.jeepay.pay.channel.ylpay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.ylpay.bean.BatchDsDetailReqDTO;
import org.jeepay.pay.channel.ylpay.bean.BatchDsDetailResDTO;
import org.jeepay.pay.channel.ylpay.bean.MsgBean;
import org.jeepay.pay.channel.ylpay.bean.MsgBody;
import org.jeepay.pay.channel.ylpay.util.Base64;
import org.jeepay.pay.channel.ylpay.util.FlaterUtil;
import org.jeepay.pay.channel.ylpay.util.Util;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.mq.Mq4PayQuery;
import org.jeepay.pay.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class YlpayPaymentService extends BasePayment {

    @Autowired
    private Mq4PayQuery mq4PayQuery;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(YlpayPaymentService.class);

    @Override
    public String getChannelName() {
        return YlpayConfig.CHANNEL_NAME;
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case YlpayConfig.CHANNEL_NAME_GATHER :
                retObj = doPay(payOrder);
                break;
            case YlpayConfig.CHANNEL_NAME_BATCH_GATHER :
                retObj = doBatchPay(payOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }
    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public JSONObject query(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        JSONObject retObj;
        switch (channelId) {
            case YlpayConfig.CHANNEL_NAME_PAY :
                retObj = doQuery(payOrder);
                break;
            case YlpayConfig.CHANNEL_NAME_BATCH_GATHER :
                retObj = doBatchQuery(payOrder);
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    public JSONObject doQuery(PayOrder payOrder) {
        YlpayConfig ylpayConfig = new YlpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        MsgBean req_bean = new MsgBean();
        req_bean.setVERSION("2.1");
        req_bean.setMSG_TYPE("200002");
        req_bean.setBATCH_NO(payOrder.getPayOrderId());//同代付交易请求批次号
        req_bean.setUSER_NAME(ylpayConfig.getUserName());//系统后台登录名 302020000001

        MsgBody body = new MsgBody();
        body.setQUERY_NO_FLAG("1");
        body.setMER_ORDER_NO(payOrder.getPayOrderId());
        req_bean.getBODYS().add(body);

        String req = Util.signANDencrypt(req_bean,ylpayConfig);
        try {
            String result = HttpUtils.getInstance().post(ylpayConfig.getReqUrl(), req);
            _log.info("易联支付查询,响应结果:{}",result);
            if(StringUtils.isNotBlank(result)) {
                MsgBean res_bean = Util.decryptANDverify(result,ylpayConfig);
                _log.info(res_bean.toXml());
                // 1-成功
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    List<MsgBody> msgBodies = res_bean.getBODYS();
                    for(MsgBody msgBody : msgBodies){
                        if("0000".equals(msgBody.getPAY_STATE())){
                            // 交易成功
                            retObj.put("channelOrderNo",  msgBody.getSUCCESS_DATE());
                            retObj.put("status", "0");
                            retObj.put("transaction_id", msgBody.getMER_ORDER_NO());
                        }else if("00A4".equals(msgBody.getPAY_STATE())) {
                            // 交易处理中
                            _log.info(">>> 转账处理中");
                            retObj.put("status", 1);    // 处理中
                        }else {
                            // 交易失败
                            _log.info(" >>> 转账失败");
                            retObj.put("status", 3);    // 失败
                            retObj.put("channelOrderNo", msgBody.getSUCCESS_DATE());
                            retObj.put("channelErrCode", msgBody.getPAY_STATE());
                            retObj.put("channelErrMsg", msgBody.getREMARK());
                        }
                    }
                }else{
                    // 交易处理中
                    _log.info("{} >>> 转账处理中");
                    retObj.put("status", 1);    // 处理中
                }
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
                return retObj;
            }else{
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
    public JSONObject doBatchQuery(PayOrder payOrder) {
        YlpayConfig ylpayConfig = new YlpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        MsgBean req_bean = new MsgBean();
        req_bean.setVERSION("2.1");
        req_bean.setMSG_TYPE("200006");
        req_bean.setBATCH_NO(payOrder.getPayOrderId());
        req_bean.setUSER_NAME(ylpayConfig.getUserName());

        String req = Util.signANDencrypt(req_bean,ylpayConfig);
        try {
            String result = HttpUtils.getInstance().post(ylpayConfig.getReqUrl(), req);
            _log.info("易联批量支付查询,响应结果:{}",result);
            if(StringUtils.isNotBlank(result)) {
                MsgBean res_bean = Util.decryptANDverify(result,ylpayConfig);
                _log.info(res_bean.toXml());
                // 1-成功
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    _log.info("订单处理完成");
                    List<MsgBody> msgBodies = res_bean.getBODYS();
                    for(MsgBody msgBody : msgBodies){
                        boolean waiting = false;
                        boolean success = false;
                        String resFlaterStr = msgBody.getDETAIL_CONTENT();
                        if(StringUtils.isNotBlank(resFlaterStr)){
                            _log.info("批量代收查询，解压文件");
                            String resFileContent = FlaterUtil.inFlaterFromBase64(resFlaterStr, "GBK");
                            System.out.println("批量代收查询，解压响应明细："+resFileContent);
                            List<BatchDsDetailResDTO> resDetailList = JSONObject.parseArray(resFileContent, BatchDsDetailResDTO.class);

                            waiting = resDetailList.stream().anyMatch(batchDsDetailResDTO -> "00A4".equals(batchDsDetailResDTO.getPayState()));
                            success = resDetailList.stream().allMatch(batchDsDetailResDTO -> "0000".equals(batchDsDetailResDTO.getPayState()));
                            resDetailList.stream().forEach(resDTO->{
                                _log.info(String.format("批量代收明细，响应结果,订单号：%s，返回码：%s，返回码描述：%s",
                                        resDTO.getMerOrderNo(),resDTO.getPayState(),resDTO.getResMsg()));
                            });
                        }

                        if(waiting){
                            // 交易处理中
                            _log.info(">>> 转账处理中");
                            retObj.put("status", 1);    // 处理中
                        }else if(success) {
                            // 交易成功
                            retObj.put("channelOrderNo",  msgBody.getSUCCESS_DATE());
                            retObj.put("status", "0");
                            retObj.put("transaction_id", payOrder.getPayOrderId());
                        }else {
                            // 交易失败
                            _log.info(" >>> 转账失败");
                            retObj.put("status", 3);    // 失败
                            retObj.put("channelOrderNo", msgBody.getSUCCESS_DATE());
                            retObj.put("channelErrCode", msgBody.getPAY_STATE());
                            retObj.put("channelErrMsg", msgBody.getREMARK());
                        }
                    }
                }else{
                    // 交易处理中
                    _log.info("{} >>> 转账处理中");
                    retObj.put("status", 1);    // 处理中
                }
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
                return retObj;
            }else{
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
        String logPrefix = "【易联支付下单】";
        YlpayConfig ylpayConfig = new YlpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();

        String payOrderId = payOrder.getPayOrderId();
        MsgBean req_bean = new MsgBean();
        req_bean.setVERSION("2.1");
        req_bean.setMSG_TYPE("200001");
        req_bean.setBATCH_NO(payOrderId);
        req_bean.setUSER_NAME(ylpayConfig.getUserName());

        MsgBody body = new MsgBody();
        body.setSN(payOrderId);
        String extra = payOrder.getParams().getString("extra");
        if(StringUtils.isBlank(extra)) {
            retObj.put("errDes", "银行账户信息为空!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
        try {
            JSONObject extraObj = JSONObject.parseObject(extra);
            body.setACC_NO(extraObj.getString("accountNo"));
            body.setACC_NAME(extraObj.getString("accountName"));
        }catch (Exception e) {
            _log.error(e,"");
            retObj.put("errDes", "银行账户信息格式不对!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
        body.setAMOUNT(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
        body.setMER_ORDER_NO(payOrderId);
        body.setID_TYPE("0");
        body.setCNY("CNY");
        body.setRETURN_URL(payConfig.getNotifyUrl(getChannelName()));
        req_bean.getBODYS().add(body);
        String req = Util.signANDencrypt(req_bean,ylpayConfig);
        try {
            String result = HttpUtils.getInstance().post(ylpayConfig.getReqUrl(), req);
            _log.info("返回数据：{}",result);
            _log.info("易联支付下单,响应结果:{}",result);
            if(StringUtils.isBlank(result)) {
                _log.error("{} >>> 易联支付下单失败", logPrefix);
                retObj.put("errDes", "易联支付下单失败:返回对象为空");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }else {
                MsgBean res_bean = Util.decryptANDverify(result,ylpayConfig);
                _log.info(res_bean.toXml());
                // 处理中
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
                    _log.info("###### 商户统一下单处理完成 ######");
                    retObj.put("payOrderId", payOrderId);
                    JSONObject payParams = new JSONObject();
                    payParams.put("payUrl", res_bean.toXml());
                    retObj.put("payParams", payParams);
                    return retObj;
                }else{
                    retObj.put("errDes", "易联支付下单失败!");
                    retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                    return retObj;
                }
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }
    public JSONObject doBatchPay(PayOrder payOrder) {
        String logPrefix = "【易联批量支付下单】";
        YlpayConfig ylpayConfig = new YlpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();

        String payOrderId = payOrder.getPayOrderId();
        MsgBean req_bean = new MsgBean();
        req_bean.setVERSION("2.1");
        req_bean.setMSG_TYPE("200005");
        req_bean.setBATCH_NO(payOrderId);
        req_bean.setUSER_NAME(ylpayConfig.getUserName());

        MsgBody body = new MsgBody();
        body.setSN(payOrderId);
        String extra = payOrder.getParams().getString("extra");
        if(StringUtils.isBlank(extra)) {
            retObj.put("errDes", "银行账户信息为空!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
        try {
            JSONArray jsonArray = JSONObject.parseArray(extra);
            //交易明细
            List<BatchDsDetailReqDTO> detailList = new ArrayList<>();
            for(int i = 0;i < jsonArray.size();i++){
                BatchDsDetailReqDTO reqDTO1 = new BatchDsDetailReqDTO();
                reqDTO1.setMerOrderNo(new String(Base64.decode(Util.generateKey(99999,14))));
                reqDTO1.setAccNo(jsonArray.getJSONObject(i).getString("accountNo"));
                reqDTO1.setAccName(jsonArray.getJSONObject(i).getString("accountName"));
                //reqDTO1.setIdCardNo("510265790128303");
                //reqDTO1.setMobileNo("18100000000");
                reqDTO1.setAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
                reqDTO1.setTransDesc("批量代收");
                detailList.add(reqDTO1);
            }
            String jsonStr = JSONObject.toJSONString(detailList);
            _log.info("批量代收，交易明细json串:{}",jsonStr);

            String flaterStr = FlaterUtil.deflaterFromString(jsonStr, "GBK");
            _log.info("批量代收，压缩后交易明细:{}",flaterStr);
            body.setDETAIL_CONTENT(flaterStr);
        }catch (Exception e) {
            _log.error(e,"");
            retObj.put("errDes", "银行账户信息格式不对!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }

        body.setRETURN_URL(payConfig.getNotifyUrl(getChannelName()));
        req_bean.getBODYS().add(body);
        String req = Util.signANDencrypt(req_bean,ylpayConfig);
        try {
            String result = HttpUtils.getInstance().post(ylpayConfig.getReqUrl(), req);
            _log.info("返回数据：{}",result);
            _log.info("易联支付下单,响应结果:{}",result);
            if(StringUtils.isBlank(result)) {
                _log.error("{} >>> 易联支付下单失败", logPrefix);
                retObj.put("errDes", "易联支付下单失败:返回对象为空");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }else {
                MsgBean res_bean = Util.decryptANDverify(result,ylpayConfig);
                _log.info(res_bean.toXml());
                // 处理中
                if("0000".equals(res_bean.getTRANS_STATE())) {
                    MsgBody resBody = res_bean.getBODYS().get(0);
                    String resFlaterStr = resBody.getDETAIL_CONTENT();
                    String resFileContent = FlaterUtil.inFlaterFromBase64(resFlaterStr, "GBK");
                    System.out.println("批量代收，解压响应明细："+resFileContent);
                    List<BatchDsDetailResDTO> resDetailList = JSONObject.parseArray(resFileContent, BatchDsDetailResDTO.class);
                    boolean success = resDetailList.stream().allMatch(batchDsDetailResDTO -> "0000".equals(batchDsDetailResDTO.getPayState()));
                    resDetailList.stream().forEach(resDTO->{
                        _log.info(String.format("批量代收明细，响应结果,订单号：%s，返回码：%s，返回码描述：%s",
                                resDTO.getMerOrderNo(),resDTO.getPayState(),resDTO.getResMsg()));
                    });
                    if(success){
                        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
                        _log.info("###### 商户统一下单处理完成 ######");
                        retObj.put("payOrderId", payOrderId);
                        JSONObject payParams = new JSONObject();
                        payParams.put("payUrl", res_bean.toXml());
                        retObj.put("payParams", payParams);
                        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RESULT_VALUE_SUCCESS);
                        return retObj;
                    }else {
                        retObj.put("errDes", "易联支付下单失败!");
                        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                        return retObj;
                    }
                }else{
                    retObj.put("errDes", "易联支付下单失败!");
                    retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                    return retObj;
                }
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }



}
