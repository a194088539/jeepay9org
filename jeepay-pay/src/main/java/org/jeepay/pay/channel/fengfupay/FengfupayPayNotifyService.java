package org.jeepay.pay.channel.fengfupay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.fengfupay.util.MD5;
import org.jeepay.pay.channel.fengfupay.util.SignUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


@Service
public class FengfupayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(FengfupayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return FengfupayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理"+getChannelName()+"支付回调】";
        _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;

        JSONObject retObj = new JSONObject();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = FengfupayConfig.RETURN_VALUE_FAIL;
        try {
            //接口返回sign参数值
            String resSign = req.getParameter("sign");
            SortedMap<String,String> paramsMap = new TreeMap();
            paramsMap.put("callbacks", req.getParameter("callbacks"));
            paramsMap.put("partnerid", req.getParameter("partnerid"));
            paramsMap.put("sysorderno", req.getParameter("sysorderno"));
            paramsMap.put("pay_type", req.getParameter("pay_type"));
            paramsMap.put("pay_time", req.getParameter("pay_time"));
            paramsMap.put("out_trade_no", req.getParameter("out_trade_no"));
            paramsMap.put("amount", req.getParameter("amount"));
            paramsMap.put("money", req.getParameter("money"));
            paramsMap.put("version", req.getParameter("version"));
            paramsMap.put("status", req.getParameter("status"));
            paramsMap.put("remark", req.getParameter("remark"));
            _log.info(logPrefix+"请求参数:{}", JSON.toJSONString(paramsMap));
            PayOrder order = rpcCommonService.rpcPayOrderService.findByPayOrderId(req.getParameter("out_trade_no"));
            if (order == null) {
                _log.error("Can't found payOrder form db. payOrderId={}, ", req.getParameter("out_trade_no"));
                retObj.put("retMsg", "Can't found payOrder");
                return retObj;
            }
            FengfupayConfig fengfupayConfig = new FengfupayConfig(getPayParam(order));
            Map<String,String> params = SignUtils.paraFilter(paramsMap);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = MD5.sign(preStr, "&key=" + fengfupayConfig.getKey(), "utf-8");
            _log.info("接口返回sign参数值:{},生成签名值:{}",resSign,sign);
            //验签
            if(!resSign.equals(sign)){
                _log.error("验签失败");
                return null;
            }
            // 核对金额
            long outPayAmt = new BigDecimal(req.getParameter("amount")).multiply(new BigDecimal("100")).longValue();
            long dbPayAmt = Long.valueOf(order.getAmount());
            if (dbPayAmt != outPayAmt) {
                _log.error("金额不一致. outPayAmt={},payOrderId={}", outPayAmt,  req.getParameter("out_trade_no"));
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
            respString = FengfupayConfig.RESPONSE_RESULT_SUCCESS;
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        }
    }
}
