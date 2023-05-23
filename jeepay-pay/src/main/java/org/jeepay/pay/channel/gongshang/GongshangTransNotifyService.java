package org.jeepay.pay.channel.gongshang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.common.util.sign.encrypt.Base64;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTransNotify;
import org.jeepay.pay.channel.zhifu.ZhifuConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


@Service
public class GongshangTransNotifyService extends BaseTransNotify {
    private final static String logPrefix = "【工商代付】";
    @Override
    public String getChannelName() {
        return GsConfig.CHANNEL_NAME;
    }
    @Override
    public JSONObject doNotify(Object notifyData) {
        HttpServletRequest request = (HttpServletRequest) notifyData;
        StringBuffer recieveData = new StringBuffer();
        BufferedReader in = null;
        String inputLine = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                recieveData.append(inputLine);
            }
        } catch (IOException e) {
            _log.error(e, logPrefix + "处理异常");
        }
        _log.info("{}代付回调请求响应参数：{}", logPrefix, recieveData.toString());

        byte[] jdata = Base64.decode(recieveData.toString());

        String resData = null;
        try {
            String respData = new String(jdata, SignUtils.CHARSET_UTF8);
            byte[] decodeBase64KeyBytes = Base64.decode(respData);
            resData = new String(decodeBase64KeyBytes, SignUtils.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject bizContext = JSON.parseObject(resData);
        _log.info("{}代付回调解密后的参数：{}", logPrefix, bizContext.toString());
        JSONObject retObj = buildFailRetObj();
        try {
            if (!verifyPayParams(bizContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, ZhifuConfig.RESPONSE_RESULT_FAIL);
                return retObj;
            }
            TransOrder transOrder = (TransOrder) bizContext.get("transOrder");

            byte currStatus = transOrder.getStatus();
            // 如果当前订单已是成功状态，不在处理，直接返回成功
            if(currStatus == PayConstant.TRANS_STATUS_SUCCESS || currStatus == PayConstant.TRANS_STATUS_COMPLETE) {
                retObj.put(PayConstant.RESPONSE_RESULT, ZhifuConfig.RESPONSE_RESULT_SUCCESS);
                return retObj;
            }

            String transOrderId = transOrder.getTransOrderId();
            // status状态说明 ~~~ 1 => 提出申请 2 => 撤销申请 3 => 提交代付 4 => 处理理成功 5 => 申请驳回 6 => 处理理中
            JSONArray array = bizContext.getJSONArray("data");
            JSONObject  data = array.getJSONObject(0);
            String channelStatus = data.getString("result");
            String channelOrderNo = data.getString("rid");
            if(StringUtils.equals(channelStatus, "7")) {
                int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, updateTransOrderRows);
                if (updateTransOrderRows != 1) {
                    _log.error("{}更新代付状态失败,将transOrderId={},更新trnsStatus={}失败", logPrefix, transOrderId, PayConstant.TRANS_RESULT_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.error("{}更新代付状态成功,将transOrderId={},更新transStatus={}成功", logPrefix, transOrderId, PayConstant.TRANS_RESULT_SUCCESS);
                transOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            } else if(StringUtils.equals(channelStatus, "6")||StringUtils.equals(channelStatus, "8")) {
                int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId,"00001","失败");
                _log.info("更新代付订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, updateTransOrderRows);
                if (updateTransOrderRows != -1) {
                    _log.error("{}更新代付状态失败,将transOrderId={},更新trnsStatus={}失败", logPrefix, transOrderId, PayConstant.TRANS_RESULT_FAIL);
                    //"{\"status\":200,\"result\":7,\"retMsg\":\"回调成功\",\"retCode\":\"0\"}"
                    transOrder.setStatus(PayConstant.AGENTPAY_STATUS_FAIL);
                }
                transOrder.setStatus(PayConstant.TRANS_STATUS_FAIL);
            } else {
                transOrder.setStatus(PayConstant.TRANS_STATUS_TRANING);
            }
            // 业务系统后端通知
            baseNotify4MchTrans.doNotify(transOrder, true);
            retObj.put(PayConstant.RESPONSE_RESULT, "{\"status\":200,\"result\":7,\"retMsg\":\"回调成功\",\"retCode\":\"0\"}");
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        return retObj;
    }

    public boolean verifyPayParams(JSONObject payContext) {
        // 查询payOrder记录
        JSONArray array = payContext.getJSONArray("data");
        JSONObject  data = array.getJSONObject(0);
        String transOrderId = data.getString("rid");
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        if (ObjectUtils.isEmpty(transOrder)) {
            _log.error("Can't found transOrder form db. transOrderId={}, ", transOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        // 核对金额
        long outPayAmt = Util.conversionCentAmount(data.getBigDecimal("je"));
        long dbPayAmt = transOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outTransAmt={},transOrderId={}", outPayAmt, transOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("transOrder", transOrder);
        return true;
    }
}
