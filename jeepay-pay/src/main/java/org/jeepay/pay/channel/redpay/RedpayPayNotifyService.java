package org.jeepay.pay.channel.redpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;

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
public class RedpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(RedpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return RedpayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理"+getChannelName()+"支付回调】";
        _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = new JSONObject();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = RedpayConfig.RESPONSE_RESULT_FAIL;
        try {
            String dt = req.getParameter("dt");                 // 时间戳
            String no = req.getParameter("no");                 // 订单号
            String money = req.getParameter("money");           // 订单金额
            String mUserid = req.getParameter("mUserid");	   // 用户ID
            String mTUserid = req.getParameter("mTUserid");     // 用户id
            String type = req.getParameter("type");             // 支付类型
            String remark = req.getParameter("remark");         // 订单备注
            String sign = req.getParameter("sign");             // 签名结果
            // 通知参数
            JSONObject params = new JSONObject();
            params.put("dt", dt);
            params.put("no", no);
            params.put("money", money);
            params.put("userId", mTUserid);
            params.put("type", type);
            params.put("remark", remark);
            params.put("sign", sign);
            _log.info("{}异步通知内容：{}", logPrefix, params);
            payContext.put("parameters", params);
            if(!verifyPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, RedpayConfig.RETURN_VALUE_FAIL);
                return retObj;
            }
            payOrder = (PayOrder) payContext.get("payOrder");

            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理"+getChannelName()+"支付回调通知 ======");
            // 返回上游信息
            respString = RedpayConfig.RESPONSE_RESULT_SUCCESS;
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        }
    }

    public boolean verifyPayParams(Map<String, Object> payContext) {
        JSONObject params = (JSONObject) payContext.get("parameters");
        // 校验结果是否成功
        String dt = params.getString("dt");                     // 随机时间
        String userId = params.getString("userId");             // 支付宝用户ID
        String money = params.getString("money");               // 支付金额
        String remark = params.getString("remark");		     // 备注，即支付订单号
        String no = params.getString("no");                     // 支付宝红包订单号
        String type = params.getString("type");                 // 支付类型
        String sign = params.getString("sign"); 		         // 签名

        // 查询payOrder记录
        String payOrderId = remark;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        RedpayConfig redpayConfig = new RedpayConfig(getPayParam(payOrder));
        // 验证签名
        // 签名格式: dt+userId+money+no+type+signkey+remark(将此字符串MD5加签 先后顺序按字典排序不可乱)
        StringBuffer sb = new StringBuffer();
        sb.append(dt).append(userId).append(money).append(no).append(type).append(redpayConfig.getKey()).append(remark);
        String signValue = MD5Util.string2MD5(sb.toString());
        if(!sign.equals(signValue)) {
            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = new BigDecimal(money).multiply(new BigDecimal(100)).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", money, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
