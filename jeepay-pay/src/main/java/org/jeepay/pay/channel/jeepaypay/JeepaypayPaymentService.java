package org.jeepay.pay.channel.jeepaypay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.jeepaypay.util.HttpUtil;
import org.jeepay.pay.channel.jeepaypay.util.SignUtil;
import org.jeepay.pay.mq.BaseNotify4MchPay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.jeepay.core.common.util.JEEPayUtil.genUrlParams;

/**
 * @author: aragom
 * @date: 19/1/20
 * @description: 转银行卡通道支付接口
 */
@Service
public class JeepaypayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(JeepaypayPaymentService.class);

    private static AtomicLong pay_seq = new AtomicLong(0L);

    @Override
    public String getChannelName() {
        return JeepaypayConfig.CHANNEL_NAME;
    }

    @Override
    public String getOrderId(PayOrder payOrder) {
        SimpleDateFormat fm = new SimpleDateFormat("yyMMddHHmmss");
        return String.format("P%sR%04d", fm.format(new Date()), pay_seq.getAndIncrement() % 10000);
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
            case JeepaypayConfig.CHANNEL_NAME_ALIPAY_PC :
                retObj = doTfPay4Alipay(payOrder, "pc");
                break;
            case JeepaypayConfig.CHANNEL_NAME_ALIPAY_H5 :
                retObj = doTfPay4Alipay(payOrder, "wap");
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
        // 上游没有查询接口，直接返回
        JSONObject retObj = buildRetObj();
        retObj.put("status", 1);    // 支付中
        return retObj;
    }

    /**
     * 支付宝支付(跳转页面,显示二维码)
     * @param payOrder
     * @param type
     * @return
     */
    public JSONObject doTfPay4Alipay(PayOrder payOrder, String type) {
        JeepaypayConfig jeepaypayConfig = new JeepaypayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        // 根据子账号配置得到转卡信息，拼接支付宝转账银行卡URL
        String mchId = jeepaypayConfig.getMchId();     // 支付宝用户ID
        String mchKey = jeepaypayConfig.getMchKey();   // 支付宝账号
//        System.out.println(payOrder.getAmount());
//      String amt = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");
//       String amountParam = new BigDecimal(payOrder.getAmount()).multiply(new BigDecimal(100)).setScale(0).toString();
//        System.out.println("-----amt"+amt);
//        System.out.println("-----amountParam"+amountParam);
        String amountParam =String.valueOf(payOrder.getAmount());
        System.out.println("-----amountParam"+amountParam);
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("mchId", mchId + "");  //商户ID
            params.put("appId", "7ca36fb15e8943b79d098ce8a36aec0a");  //商户应用ID
            params.put("productId", "8006");  //支付产品ID
            params.put("mchOrderNo", payOrder.getPayOrderId());   //商户订单号
            System.out.println("=========="+payOrder.getPayOrderId());
            params.put("currency", "cny");   //币种
            params.put("amount", amountParam);   //支付金额
            params.put("clientIp", "210.73.10.148");   //客户端IP
            params.put("device", "ios10.3.1");   //客户端设备
            params.put("returnUrl", payOrder.getReturnUrl());   //支付结果前端跳转URL
            params.put("notifyUrl", "http://42.51.45.8:53020/notify/jeepaypay/notify_res.htm");   //支付结果后台回调URL
            params.put("subject", "商业服务费");  //商品主题
            params.put("body", "商业服务费");   //商品描述信息
            params.put("param1", "");   //扩展参数1
            params.put("param2", "");   //扩展参数2
            params.put("extra", "");  //附加参数
            String sign = SignUtil.getSign(params, mchKey);  //签名
            params.put("sign", sign);
            String res = HttpUtil.post("http://42.51.45.8:53020/api/pay/create_order", genUrlParams(params));


            System.out.println("===="+res);
            JSONObject json  = JSONObject.parseObject(res);
            Object re =  json.get("payParams");
            System.out.println("--------"+re);
            JSONObject json1  = JSONObject.parseObject(re.toString());
            String codeUrl = (String) json1.get("codeUrl");

            _log.info("[{}]生成转账URL={}", getChannelName(), codeUrl);
            int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},result={}", getChannelName(), payOrder.getPayOrderId(), updateCount);

//            String codeImgUrl = payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(toUrl) + "&widht=200&height=200";
//            StringBuffer payForm = new StringBuffer();
//            String toPayUrl = payConfig.getPayUrl() + "/" + JeepaypayConfig.CHANNEL_NAME + "/pay_" + type + ".htm";
//            Boolean autoJump = jeepaypayConfig.getAutoJump();
//            payForm.append("<form style=\"display: none\" action=\""+toPayUrl+"\" method=\"post\">");
//            payForm.append("<input name=\"mchOrderNo\" value=\""+payOrder.getMchOrderNo()+"\" >");
//            payForm.append("<input name=\"payOrderId\" value=\""+payOrder.getPayOrderId()+"\" >");
//            payForm.append("<input name=\"amount\" value=\""+payOrder.getAmount()+"\" >");
//            payForm.append("<input name=\"codeImgUrl\" value=\""+codeImgUrl+"\" >");
//            payForm.append("<input name=\"codeUrl\" value=\""+toUrl+"\" >");
//            payForm.append("<input name=\"autoJump\" value=\""+autoJump+"\" >");
//            payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
//            payForm.append("</form>");
//            payForm.append("<script>document.forms[0].submit();</script>");
//
//            // 支付链接地址
//            retObj.put("payOrderId", payOrder.getPayOrderId()); // 设置支付订单ID
//            JSONObject payParams = new JSONObject();
//            payParams.put("payUrl", payForm);
//            String payJumpUrl = toPayUrl + "?mchOrderNo=" + payOrder.getMchOrderNo() + "&payOrderId=" + payOrder.getPayOrderId() +
//                    "&amount=" + payOrder.getAmount() + "&codeImgUrl=" + URLEncoder.encode(codeImgUrl) + "&codeUrl=" + URLEncoder.encode(toUrl) +
//                    "&autoJump=" + autoJump;
//            payParams.put("payJumpUrl", payJumpUrl);
//            payParams.put("payMethod", PayConstant.PAY_METHOD_FORM_JUMP);
            retObj.put("payurl", codeUrl);
            retObj.put("mchOrderNo", payOrder.getMchOrderNo());
            retObj.put("amount", amountParam);
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            return retObj;
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }


    private static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=").append(paraMap.get(key));
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }

}
