package org.jeepay.pay.channel.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.apache.commons.lang.StringUtils;
import org.jeepay.pay.channel.fengfupay.util.MD5;
import org.jeepay.pay.channel.fengfupay.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;
import org.jeepay.pay.mq.BaseNotify4CashColl;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
@Service
public class AlipayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(AlipayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    @Autowired
    private BaseNotify4CashColl baseNotify4CashColl;

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理支付宝支付回调】";
        _log.info("====== 开始处理支付宝支付回调通知 ======");
        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }
        _log.info("{}请求数据:{}", logPrefix, params);
        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail("请求数据为空"));
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        payContext.put("parameters", params);
        Object notifyTypeObj = params.get("notify_type");
        String notifyType = "";
        if(notifyTypeObj != null){
            notifyType = notifyTypeObj.toString();
        }
        if("trade_status_sync".equals(notifyType)){
            if(!verifyAliPayParamsTradeStatusSync(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return retObj;
            }
        }else{
            if(!verifyAliPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return retObj;
            }
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);
        String trade_status = params.get("trade_status").toString();		// 交易状态
        // 支付状态成功或者完成
        if (trade_status.equals(PayConstant.AlipayConstant.TRADE_STATUS_SUCCESS) ||
                trade_status.equals(PayConstant.AlipayConstant.TRADE_STATUS_FINISHED)) {
            int updatePayOrderRows;
            payOrder = (PayOrder)payContext.get("payOrder");
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
            	  String outTradeNo = StrUtil.toString(params.get("out_trade_no"),String.valueOf(params.get("trade_no")));
                //updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), outTradeNo);
            	  updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), null);
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    _log.info("{}响应给支付宝结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    retObj.put("resResult", PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);

                //订单支付成功后，mq调用支付宝结算接口，进行资金归集操作。
                baseNotify4CashColl.doNotify(payOrder.getPayOrderId());

            }
        }else{
            // 其他状态
            _log.info("{}支付状态trade_status={},不做业务处理", logPrefix, trade_status);
            _log.info("{}响应给支付宝结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
            retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
            return retObj;
        }
        try {
			baseNotify4MchPay.doNotify(payOrder, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        _log.info("====== 完成处理支付宝支付回调通知 ======");
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
        return retObj;
    }

    @Override
    public JSONObject doReturn(Object notifyData) {
        String logPrefix = "【处理支付宝同步跳转】";
        _log.info("====== 开始处理支付宝同步跳转 ======");

        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }
        _log.info("{}请求数据:{}", logPrefix, params);

        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail("请求数据为空"));
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();

        payContext.put("parameters", params);
        Object notifyTypeObj = params.get("notify_type");
        String notifyType = "";
        if(notifyTypeObj != null){
            notifyType = notifyTypeObj.toString();
        }
        if("trade_status_sync".equals(notifyType)){
            if(!verifyAliPayParamsTradeStatusSync(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return retObj;
            }
        }else{
            if(!verifyAliPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return retObj;
            }
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);

        PayOrder payOrder = (PayOrder)payContext.get("payOrder");

        _log.info("====== 完成处理支付宝同步跳转 ======");
        String url = baseNotify4MchPay.createNotifyUrl(payOrder, "1");
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
        retObj.put(PayConstant.JUMP_URL, url);
        return retObj;
    }

    /**
     * 解析支付宝回调请求的数据
     * @param request
     * @return
     */
    public Map buildNotifyData(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 验证支付宝支付通知参数
     * @return
     */
    public boolean verifyAliPayParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");
        String out_trade_no = params.get("out_trade_no");		// 商户订单号
        String total_amount = params.get("total_amount"); 		// 支付金额
        if (org.springframework.util.StringUtils.isEmpty(out_trade_no)) {
            _log.error("AliPay Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(total_amount)) {
            _log.error("AliPay Notify parameter total_amount is empty. total_fee={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        // 查询mchChannel记录
        /*Long mchId = payOrder.getMchId();
        String channelId = payOrder.getChannelId();
        String appId = payOrder.getAppId();
        MchChannel mchChannel = rpcCommonService.rpcMchChannelService.findByMACId(mchId, appId, channelId);
        if(mchChannel == null) {
            _log.error("Can't found mchChannel form db. mchId={} channelId={}, ", payOrderId, mchId, channelId);
            payContext.put("retMsg", "Can't found mchChannel");
            return false;
        }*/
        boolean verify_result = false;
        try {
            AlipayConfig alipayConfig = new AlipayConfig(getPayParam(payOrder));
            if(StringUtils.isNotBlank(alipayConfig.getAlipayPublicCertPath())){
            	verify_result = AlipaySignature.rsaCertCheckV1(params, alipayConfig.getAlipayPublicCertPath(), AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            } else {
            	verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            }
        } catch (AlipayApiException e) {
            _log.error(e, "AlipaySignature.rsaCertCheckV1 error");
        }

        // 验证签名
        if (!verify_result) {
            errorMessage = "rsaCertCheckV1 failed.";
            _log.error("AliPay Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long aliPayAmt = new BigDecimal(total_amount).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }
    public boolean verifyAliPayParamsTradeStatusSync(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");
        String out_trade_no = params.get("out_trade_no");		// 商户订单号
        String total_amount = params.get("total_fee"); 		// 支付金额
        String signType = params.get("sign_type"); 		// 类型
        total_amount = StringUtils.isBlank(total_amount) ? params.get("buyer_pay_amount") : total_amount;
        signType = StringUtils.isBlank(signType) ? AlipayConfig.SIGNTYPE : signType;
        if (org.springframework.util.StringUtils.isEmpty(out_trade_no)) {
            _log.error("AliPay Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(total_amount)) {
            _log.error("AliPay Notify parameter total_amount is empty. total_fee={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        // 查询mchChannel记录
        /*Long mchId = payOrder.getMchId();
        String channelId = payOrder.getChannelId();
        String appId = payOrder.getAppId();
        MchChannel mchChannel = rpcCommonService.rpcMchChannelService.findByMACId(mchId, appId, channelId);
        if(mchChannel == null) {
            _log.error("Can't found mchChannel form db. mchId={} channelId={}, ", payOrderId, mchId, channelId);
            payContext.put("retMsg", "Can't found mchChannel");
            return false;
        }*/
        boolean verify_result = false;
        try {
            AlipayConfig alipayConfig = new AlipayConfig(getPayParam(payOrder));
            if(StringUtils.isNotBlank(alipayConfig.getAlipayPublicCertPath())){
            	verify_result = AlipaySignature.rsaCertCheckV1(params, alipayConfig.getAlipayPublicCertPath(), AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            } else {
            	verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            }
        } catch (AlipayApiException e) {
            _log.error(e, "AlipaySignature.rsaCertCheckV1 error");
        }
        
        // 验证签名
        if (!verify_result) {
            errorMessage = "rsaCertCheckV1 failed.";
            _log.error("AliPay Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long aliPayAmt = new BigDecimal(total_amount).multiply(new BigDecimal("100")).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
