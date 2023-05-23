package org.jeepay.pay.channel.zhifu;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTransNotify;


@Component
public class ZhifuTransNotifyService extends BaseTransNotify {
    private final static String logPrefix = "【智付】";
    @Override
    public String getChannelName() {
        return ZhifuConfig.CHANNEL_NAME;
    }
    @Override
    public JSONObject doNotify(Object notifyData) {
        JSONObject resultData = getRequestParameters(notifyData);
        JSONObject retObj = buildFailRetObj();
        try {
//            if(!org.jeepay.common.util.str.StringUtils.equals(resultData.getString("status"),"0000")){
//                retObj.put(PayConstant.RESPONSE_RESULT, ZhifuConfig.RESPONSE_RESULT_FAIL);
//                return retObj;
//            }
            String tradeData =resultData.getString("tradeData");
            JSONObject bizContext = JSONObject.parseObject(tradeData);
            if (!verifyPayParams(bizContext,tradeData,resultData.getString("tradeSign"))) {
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
            String channelOrderNo = bizContext.getString("orderId");
            String transOrderId = transOrder.getTransOrderId();
            // status状态说明 ~~~ 1 => 提出申请 2 => 撤销申请 3 => 提交代付 4 => 处理理成功 5 => 申请驳回 6 => 处理理中
            String channelStatus = bizContext.getString("result");
            if(StringUtils.equals(channelStatus, "S")) {
                int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, updateTransOrderRows);
                if (updateTransOrderRows != 1) {
                    _log.error("{}更新代付状态失败,将transOrderId={},更新trnsStatus={}失败", logPrefix, transOrderId, PayConstant.TRANS_RESULT_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.error("{}更新代付状态成功,将transOrderId={},更新transStatus={}成功", logPrefix, transOrderId, PayConstant.TRANS_RESULT_SUCCESS);
                transOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            } else if(StringUtils.equals(channelStatus, "F")) {
                int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId,
                        bizContext.getString("code"), bizContext.getString("msg"), channelOrderNo);
                _log.info("更新代付订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, updateTransOrderRows);
                if (updateTransOrderRows != -1) {
                    _log.error("{}更新代付状态失败,将transOrderId={},更新trnsStatus={}失败", logPrefix, transOrderId, PayConstant.TRANS_RESULT_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                transOrder.setStatus(PayConstant.TRANS_STATUS_FAIL);
            } else {
                transOrder.setStatus(PayConstant.TRANS_STATUS_TRANING);
            }
            // 业务系统后端通知
            baseNotify4MchTrans.doNotify(transOrder, true);
            retObj.put(PayConstant.RESPONSE_RESULT, ZhifuConfig.RESPONSE_RESULT_SUCCESS);
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        return retObj;
    }

    public boolean verifyPayParams(JSONObject payContext,String tradeData,String backSign) {
        // 查询payOrder记录
        String transOrderId = payContext.getString("orderId");
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        if (ObjectUtils.isEmpty(transOrder)) {
            _log.error("Can't found transOrder form db. transOrderId={}, ", transOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        ZhifuConfig Config = new ZhifuConfig(getTransParam(transOrder));

        String signValue = getSign(Config, tradeData);

        if(!backSign.equals(signValue)) {
            _log.error("验证签名失败. transOrderId={}, ", transOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = Util.conversionCentAmount(payContext.getBigDecimal("amount"));
        long dbPayAmt = transOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outTransAmt={},transOrderId={}", outPayAmt, transOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("transOrder", transOrder);
        return true;
    }

    private String getSign(ZhifuConfig Config, String tradeData) {

        String sign = SignUtils.MD5.createSign(tradeData, "&"+Config.getPrivateKey(), SignUtils.CHARSET_UTF8);
        return sign.toUpperCase();
    }
}
