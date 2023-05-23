package org.jeepay.pay.channel.ecpsspay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sand.sandutil.tool.Base64Encoder;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.ecpsspay.util.SignUtil;
import org.jeepay.pay.channel.ecpsspay.util.XmlUtil;
import org.jeepay.pay.channel.shengfutong.SftpayConfig;
import org.jeepay.pay.channel.swiftpay.util.XmlUtils;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.util.HttpUtils;
import org.jeepay.pay.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 汇潮支付
 */
@Service
public class EcpsspayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(EcpsspayPaymentService.class);

    private static AtomicLong pay_seq = new AtomicLong(0L);

    @Override
    public String getChannelName() {
        return EcpsspayConfig.CHANNEL_NAME;
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
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_B2CCREDIT :
                retObj = doPay(payOrder, "B2CCredit");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_B2CDEBIT :
                retObj = doPay(payOrder, "B2CDebit");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_NOCARD :
                retObj = doPay(payOrder, "noCard");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_UNIONSCANPAY_H5 :
                retObj = doPay(payOrder, "UnionScanPay_H5");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_UNIONSCANCREDITPAY_H5 :
                retObj = doPay(payOrder, "UnionScanCreditPay_H5");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_MANUALPAY :
                retObj = doPay(payOrder, "manualPay");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_B2B :
                retObj = doPay(payOrder, "B2B");
                break;
            case EcpsspayConfig.PAY_CHANNEL_ECPSSPAY_INSTALLMENT :
                retObj = doPay(payOrder, "Installment");
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
        EcpsspayConfig ecpsspayConfig = new EcpsspayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        try {
            SortedMap<String,String> map = new TreeMap();
            // 商户ID
            map.put("merCode", ecpsspayConfig.getMchId());
            // 商户订单号
            map.put("orderNumber", payOrder.getPayOrderId());
            // 商户交易时间
            map.put("beginTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS));
            // 商户交易时间
            map.put("endTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS));
            // 查詢
            map.put("tx", "1001");

            String reqUrl = ecpsspayConfig.getReqUrl()+"/merchantBatchQueryAPI";
            String signStr = ecpsspayConfig.getMchId();
            String sign = SignUtil.sign(ecpsspayConfig.getMchKey().getBytes(SignUtil.CHARACTER_ENCODING_UTF_8), signStr);

            map.put("sign", sign);
            String req = XmlUtils.parseXML(map);
            _log.info("汇潮支付查询请求数据:{}", req);
            Map<String,String> paramMap = new HashMap<>(1);
            paramMap.put("requestDomain", Base64Encoder.encode(req.getBytes()));
            String result = HttpUtils.getInstance().post(reqUrl, paramMap);
            _log.info("汇潮支付查询请求,响应结果:{}",result);
            if(StringUtils.isNotBlank(result)) {
                Map<String,Object> resultMap = XmlUtil.toMap(result.getBytes(), "utf-8");
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(resultMap));

                String resultCode = jsonObject.getString("resultCode");
                if(!"00".equals(resultCode)){
                    retObj.put("errDes", "操作失败!");
                    retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                    return retObj;
                }
                JSONObject listJsonObject = jsonObject.getJSONObject("list");
                System.out.println(listJsonObject.get("orderStatus"));
                String orderStatus = listJsonObject.getString("orderStatus");
                //1成功，0失败，3处理中
                if("1".equals(orderStatus)){
                    retObj.put("status", "0");
                    retObj.put("transaction_id", listJsonObject.get("orderNumber"));
                    retObj.put("channelAttach", Util.buildSwiftpayAttach(resultMap));
                }else if("0".equals(orderStatus)){
                    retObj.put("status", "3");
                }else {
                    retObj.put("status", "1");
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

    public JSONObject doPay(PayOrder payOrder,String payType) {
        String logPrefix = "【汇潮支付支付下单】";
        EcpsspayConfig ecpsspayConfig = new EcpsspayConfig(getPayParam(payOrder));
        String payOrderId = payOrder.getPayOrderId();
        JSONObject retObj = buildRetObj();
        try {
            SortedMap<String, String> params = new TreeMap<>();
            //商户号
            params.put("MerNo",ecpsspayConfig.getMchId());
            //商户订单号
            params.put("BillNo", payOrder.getPayOrderId());
            //支付金额
            params.put("Amount", AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
            //订单时间
            params.put("OrderTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS));
            //设置同步跳转地址
            params.put("ReturnURL", payConfig.getReturnUrl(getChannelName()));
            //设置异步通知地址
            params.put("AdviceURL", payConfig.getNotifyUrl(getChannelName()));
            //支付方式
            params.put("payType", payType);
            String signStr = "MerNo="+params.get("MerNo")+"&"
                +"BillNo="+params.get("BillNo")+"&"
                +"Amount="+params.get("Amount")+"&"
                +"OrderTime="+params.get("OrderTime")+"&"
                +"ReturnURL="+params.get("ReturnURL")+"&"
                +"AdviceURL="+params.get("AdviceURL");
            _log.info("{},签名参数:{},通道配置参数:{}",logPrefix,signStr,ecpsspayConfig);
            String sign = SignUtil.sign(ecpsspayConfig.getMchKey().getBytes(SignUtil.CHARACTER_ENCODING_UTF_8), signStr);
            //签名
            params.put("SignInfo", sign);
            String reqUrl = ecpsspayConfig.getReqUrl()+"/pay/sslpayment";
            _log.info("汇潮支付支付下单,请求URL:{},参数:{}",reqUrl,params);
            String result = HttpUtils.getInstance().post(reqUrl, params);
            _log.info("汇潮支付支付下单,响应结果:{}",result);
            if(StringUtils.isBlank(result)) {
                _log.error("{} >>> 汇潮支付支付下单失败", logPrefix);
                retObj.put("errDes", "汇潮支付支付下单失败:返回对象为空");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }else {
                rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("###### 商户统一下单处理完成 ######");
                retObj.put("payOrderId", payOrderId);
                // JSONObject payParams = new JSONObject();
                // payParams.put("payUrl", payUrl);
                // payParams.put("payMethod", PayConstant.PAY_METHOD_URL_JUMP);
                // retObj.put("payParams", payParams);
                JSONObject payParams = new JSONObject();
                payParams.put("payUrl", result);
                //payParams.put("payMethod", PayConstant.PAY_METHOD_URL_JUMP);
                retObj.put("payParams", payParams);
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "汇潮支付下单失败[调取通道异常]");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }

    public static void main(String[] args) throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<root>\n"
            + "    <merCode>16885</merCode>\n" + "    <beginDate></beginDate>\n" + "    <endDate></endDate>\n"
            + "    <resultCount>1</resultCount>\n" + "    <pageIndex>1</pageIndex>\n" + "    <pageSize>100</pageSize>\n"
            + "    <resultCode>00</resultCode>\n" + "    <list>\n"
            + "        <orderNumber>1355816167316</orderNumber>\n"
            + "        <orderDate>2016-04-18 15:36:08</orderDate>\n" + "        <orderAmount>10000.0</orderAmount>\n"
            + "        <orderStatus>1</orderStatus>\n" + "        <gouduiStatus>1</gouduiStatus>\n"
            + "\t\t\t\t<refundStatus>1</refundStatus >\n" + "    </list>\n" + "</root>";
        Map<String,Object> resultMap = XmlUtil.toMap(xml.getBytes(), "utf-8");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(resultMap));
        JSONObject listJsonObject = jsonObject.getJSONObject("list");
        System.out.println(listJsonObject.get("orderStatus"));
    }
}
