package org.jeepay.pay.channel.ylpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.ecpsspay.EcpsspayConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class YlpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(YlpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return YlpayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理"+getChannelName()+"支付回调】";
        _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;

        JSONObject retObj = new JSONObject();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = YlpayConfig.RESPONSE_RESULT_FAIL;
        try {
            //接口返回sign参数值
            String resSign = req.getParameter("MAC");
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("MERCHANT_NO", req.getParameter("MERCHANT_NO"));
            paramsMap.put("ORDER_NO", req.getParameter("ORDER_NO"));
            paramsMap.put("YL_BATCH_NO", req.getParameter("YL_BATCH_NO"));
            paramsMap.put("SN", req.getParameter("SN"));
            paramsMap.put("AMOUNT", req.getParameter("AMOUNT"));
            paramsMap.put("CURRENCY", req.getParameter("CURRENCY"));
            paramsMap.put("MOBILE_NO", req.getParameter("MOBILE_NO"));
            paramsMap.put("ACCOUNT_NO", req.getParameter("ACCOUNT_NO"));
            paramsMap.put("RESP_CODE", req.getParameter("RESP_CODE"));
            paramsMap.put("RESP_REMARK", req.getParameter("RESP_REMARK"));
            paramsMap.put("SETT_AMOUNT", req.getParameter("SETT_AMOUNT"));
            paramsMap.put("SETT_CURRENCY", req.getParameter("SETT_CURRENCY"));
            paramsMap.put("MER_ORDER_NO", req.getParameter("MER_ORDER_NO"));
            _log.info(logPrefix+"请求参数:{}", JSON.toJSONString(paramsMap));
            PayOrder order = rpcCommonService.rpcPayOrderService.findByPayOrderId(req.getParameter("MER_ORDER_NO"));
            if (order == null) {
                _log.error("Can't found payOrder form db. payOrderId={}, ", req.getParameter("MER_ORDER_NO"));
                retObj.put("retMsg", "Can't found payOrder");
                return retObj;
            }
            EcpsspayConfig ecpsspayConfig = new EcpsspayConfig(getPayParam(order));
//            String signStr = "MerNo="+paramsMap.get("MerNo")+"&"
//                +"BillNo="+paramsMap.get("BillNo")+"&"
//                +"OrderNo="+paramsMap.get("OrderNo")+"&"
//                +"Amount="+paramsMap.get("Amount")+"&"
//                +"Succeed="+paramsMap.get("Succeed");
//            String sign = SignUtil.sign(ecpsspayConfig.getMchKey().getBytes(SignUtil.CHARACTER_ENCODING_UTF_8), signStr);
//
//            _log.info("接口返回sign参数值:{},生成签名值:{}",resSign,sign);
//            //验签
//            if(!resSign.equals(sign)){
//                _log.error("验签失败");
//                return null;
//            }
            // 核对金额
            long outPayAmt = new BigDecimal(req.getParameter("AMOUNT")).multiply(new BigDecimal(100)).longValue();
            long dbPayAmt = order.getAmount();
            if (dbPayAmt != outPayAmt) {
                _log.error("金额不一致. outPayAmt={},dbPayAmt={},payOrderId={}", outPayAmt,dbPayAmt,  req.getParameter("MER_ORDER_NO"));
                retObj.put("retMsg", "金额不一致");
                return retObj;
            }
            // 处理订单
            // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            byte payStatus = order.getStatus();
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(order.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, order.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, order.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                order.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(order, true);
            _log.info("====== 完成处理"+getChannelName()+"支付回调通知 ======");
            // 返回上游信息
            respString = YlpayConfig.RESPONSE_RESULT_SUCCESS;
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        }
    }
}
