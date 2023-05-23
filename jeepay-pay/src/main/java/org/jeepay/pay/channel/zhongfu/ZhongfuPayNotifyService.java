package org.jeepay.pay.channel.zhongfu;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.common.util.Util;
import org.jeepay.common.util.sign.SignUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
@Service
public class ZhongfuPayNotifyService extends BasePayNotify {
    private final static String logPrefix = "【中付回调】";
    @Override
    public String getChannelName() {
        return ZhongfuConfig.CHANNEL_NAME;
    }
    @Override
    public JSONObject doNotify(Object notifyData) {
        _log.info("====== 开始处理中付支付回调通知 ======");
        JSONObject bizContext = getRequestParameters(notifyData);
        _log.info("{}回调请求响应参数：{}", logPrefix, bizContext.toJSONString());
        JSONObject retObj = buildFailRetObj();
        try {

            if (!verifyPayParams(bizContext,bizContext.getString("sign"),notifyData)) {
                retObj.put(PayConstant.RESPONSE_RESULT, "fail");
                return retObj;
            }
            PayOrder payOrder = (PayOrder) bizContext.get("payOrder");
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理中付支付回调通知 ======");
            retObj.put(PayConstant.RESPONSE_RESULT, "success");
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        return retObj;
    }

    public boolean verifyPayParams(JSONObject payContext,String backSign,Object notifyData) {
        // 查询payOrder记录
        String payOrderId = payContext.getString("businessnumber");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
   
        ZhongfuConfig config = new ZhongfuConfig(getPayParam(payOrder));

        if(!verifyIp(notifyData,config.getExtInfo())){
            _log.error("回调来源异常!");
            payContext.put("retMsg", "回调来源异常!");
            return false;
        }

        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderid={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        
        if(!payContext.getString("status").equals("成功")){
            _log.error("订单{}交易失败，回调内容", payOrderId,payContext.toJSONString());
            payContext.put("retMsg", "订单{}交易失败");
            return false;
        }
        

        String signValue = getSign(payContext,config.getPrivateKey());

        if(!backSign.equals(signValue)) {
            _log.error("验证签名失败. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "验证签名失败");
            return false;
        }

        // 核对金额
        long outPayAmt = payContext.getLongValue("amount");//Util.conversionCentAmount(payContext.getBigDecimal("amount"));
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("金额不一致. outPayAmt={},payOrderId={}", outPayAmt, payOrderId);
            payContext.put("retMsg", "金额不一致");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }
    /**
     * 签名
     * @param parameters
     * @param key
     * @return
     */
    private String getSign(JSONObject parameters,String key) {
        String signTxt = SignUtils.parameterText(parameters);
        _log.info("{}待签名字符串：{}", logPrefix, signTxt+"&"+key);
        String sign = SignUtils.MD5.createSign(signTxt, "&"+key, SignUtils.CHARSET_UTF8).toUpperCase();
        return sign;
    }
    
     //判断ip是否存在于ip池中
        public boolean verifyIp(Object notifyData,String ips){
        HttpServletRequest request = (HttpServletRequest) notifyData;
        String ip = getIpAddr(request);
        JSONObject msg = new JSONObject();
        msg.put("ip", ip);
        _log.info("{}回调服务器信息:{}",logPrefix,msg.toJSONString());
        if(ips.contains(ip)){
            return true;
        }
        return  false;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String localIp = "127.0.0.1";
            String localIpv6 = "0:0:0:0:0:0:0:1";
            if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        String ipSeparate = ",";
        int ipLength = 15;
        if (ipAddress != null && ipAddress.length() > ipLength) {
            if (ipAddress.indexOf(ipSeparate) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
            }
        }
        return ipAddress;
    }
}

