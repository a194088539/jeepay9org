package org.jeepay.pay.channel.jeepaypay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.channel.jeepaypay.util.SignUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: aragom
 * @date: 19/02/13
 * @description: 红包通道回调
 */
@Service
public class JeepaypayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(JeepaypayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return JeepaypayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理"+getChannelName()+"支付回调】";
        _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = new JSONObject();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = JeepaypayConfig.RESPONSE_RESULT_FAIL;
        try {

            String resSign = req.getParameter("sign");  //接口返回sign参数值

            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("payOrderId", req.getParameter("payOrderId"));
            paramsMap.put("mchId", req.getParameter("mchId"));
            paramsMap.put("appId", req.getParameter("appId"));
            paramsMap.put("productId", req.getParameter("productId"));
            paramsMap.put("mchOrderNo", req.getParameter("mchOrderNo"));
            paramsMap.put("amount", req.getParameter("amount"));
            paramsMap.put("status", req.getParameter("status"));
            paramsMap.put("channelOrderNo", req.getParameter("channelOrderNo"));
            paramsMap.put("channelAttach", req.getParameter("channelAttach"));
            paramsMap.put("param1", req.getParameter("param1"));
            paramsMap.put("param2", req.getParameter("param2"));
            paramsMap.put("paySuccTime", req.getParameter("paySuccTime"));
            paramsMap.put("backType", req.getParameter("backType"));
            paramsMap.put("income", req.getParameter("income"));




            PayOrder order = rpcCommonService.rpcPayOrderService.findByPayOrderId(req.getParameter("mchOrderNo"));
            if (order == null) {
                _log.error("Can't found payOrder form db. payOrderId={}, ", req.getParameter("mchOrderNo"));
                retObj.put("retMsg", "Can't found payOrder");
                return retObj;
            }
            JeepaypayConfig jeepaypayConfig = new JeepaypayConfig(getPayParam(order));
            String sign = SignUtil.getSign(paramsMap, jeepaypayConfig.getMchKey());   //根据返回数据 和商户key 生成sign
            //验签
            if(!resSign.equals(sign)){
                return null;
            }
            // 核对金额
            long outPayAmt = new BigDecimal(req.getParameter("amount")).longValue();
            long dbPayAmt = Long.valueOf(order.getAmount());
            if (dbPayAmt != outPayAmt) {
                _log.error("金额不一致. outPayAmt={},payOrderId={}", outPayAmt,  req.getParameter("mchOrderNo"));
                retObj.put("retMsg", "金额不一致");
                return retObj;
            }
//            _log.info("{}异步通知内容：{}", logPrefix, params);
//            payContext.put("parameters", params);
//            if(!verifyPayParams(payContext)) {
//                retObj.put(PayConstant.RESPONSE_RESULT, JeepaypayConfig.RETURN_VALUE_FAIL);
//                return retObj;
//            }
//            payOrder = (PayOrder) payContext.get("payOrder");

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

//    public boolean verifyPayParams(Map<String, Object> payContext) {
//        JSONObject params = (JSONObject) payContext.get("parameters");
//        // 校验结果是否成功
//        String dt = params.getString("dt");                     // 随机时间
//        String userId = params.getString("userId");             // 支付宝用户ID
//        String money = params.getString("money");               // 支付金额
//        String remark = params.getString("remark");		     // 备注，即支付订单号
//        String no = params.getString("no");                     // 支付宝红包订单号
//        String type = params.getString("type");                 // 支付类型
//        String sign = params.getString("sign"); 		         // 签名
//
//        // 查询payOrder记录
//
//
//        JeepaypayConfig redpayConfig = new JeepaypayConfig(getPayParam(payOrder));
//        // 验证签名
//        // 签名格式: dt+userId+money+no+type+signkey+remark(将此字符串MD5加签 先后顺序按字典排序不可乱)
//        StringBuffer sb = new StringBuffer();
//        sb.append(dt).append(userId).append(money).append(no).append(type).append(redpayConfig.getKey()).append(remark);
//        String signValue = MD5Util.string2MD5(sb.toString());
//        if(!sign.equals(signValue)) {
//            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
//            payContext.put("retMsg", "验证签名失败");
//            return false;
//        }
//        String payOrderId = remark;
//        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
//        if (payOrder == null) {
//            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
//            payContext.put("retMsg", "Can't found payOrder");
//            return false;
//        }
//
//        // 核对金额
//        long outPayAmt = new BigDecimal(money).multiply(new BigDecimal(100)).longValue();
//        long dbPayAmt = payOrder.getAmount().longValue();
//        if (dbPayAmt != outPayAmt) {
//            _log.error("金额不一致. outPayAmt={},payOrderId={}", money, payOrderId);
//            payContext.put("retMsg", "金额不一致");
//            return false;
//        }
//        payContext.put("payOrder", payOrder);
//        return true;
//    }

}
