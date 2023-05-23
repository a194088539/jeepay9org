package org.jeepay.pay.channel.duolabaopay;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.RpcSignUtils;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.jeepaypay.JeepaypayConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class DuolabaopayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(DuolabaopayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return DuolabaopayConfig.CHANNEL_NAME;
    }

@Override
public JSONObject doNotify(Object notifyData) {
    String logPrefix = "【处理"+getChannelName()+"支付回调】";
    _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
    HttpServletRequest req = (HttpServletRequest) notifyData;
    JSONObject retObj = new JSONObject();
    Map<String, Object> payContext = new HashMap();
    PayOrder payOrder;
    String respString = DuolabaopayConfig.RETURN_VALUE_FAIL;
    try {
        String timestamp = req.getHeader("timestamp");
        String token = req.getHeader("token");

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("requestNum", req.getParameter("requestNum"));
        paramsMap.put("orderNum", req.getParameter("orderNum"));
        paramsMap.put("orderAmount", req.getParameter("orderAmount"));
        paramsMap.put("status", req.getParameter("status"));
        paramsMap.put("completeTime", req.getParameter("completeTime"));


        PayOrder order = rpcCommonService.rpcPayOrderService.findByPayOrderId(req.getParameter("requestNum"));
        if (order == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", req.getParameter("requestNum"));
            retObj.put("retMsg", "Can't found payOrder");
            return retObj;
        }
        DuolabaopayConfig duolabaopayConfig = new DuolabaopayConfig(getPayParam(order));
        StringBuilder dist = new StringBuilder();
        dist.append("secretKey=").append(duolabaopayConfig.getPrivateKey())
                .append("&timestamp=").append(timestamp);
        String token2 = RpcSignUtils.sha1(dist.toString()).toUpperCase();
        _log.info("返回token：{},计算token：{}", token, token2);
        //验签
        if(!token.equals(token2)){
            retObj.put("retMsg", "验签失败");
            return retObj;
        }

        // 核对金额
            long outPayAmt = new BigDecimal(req.getParameter("orderAmount")).longValue()*100;
            long dbPayAmt = Long.valueOf(order.getAmount());
            if (dbPayAmt != outPayAmt) {
                _log.error("金额不一致. outPayAmt={},dbPayAmt={}", outPayAmt,  dbPayAmt);
                retObj.put("retMsg", "金额不一致");
                return retObj;
            }
            // 处理订单
            byte payStatus = order.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
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
            respString = JeepaypayConfig.RESPONSE_RESULT_SUCCESS;
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        }
    }

}
