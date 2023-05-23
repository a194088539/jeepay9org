package org.jeepay.pay.channel.fengfupay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.fengfupay.util.MD5;
import org.jeepay.pay.channel.fengfupay.util.SignUtils;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.mq.Mq4PayQuery;
import org.jeepay.pay.util.HttpUtils;
import org.jeepay.pay.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


@Service
public class FengfupayPaymentService extends BasePayment {

    @Autowired
    private Mq4PayQuery mq4PayQuery;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(FengfupayPaymentService.class);

    @Override
    public String getChannelName() {
        return FengfupayConfig.CHANNEL_NAME;
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
            case FengfupayConfig.CHANNEL_NAME_DMF :
                retObj = doPay(payOrder,"alipay_dmf");
                break;
            case FengfupayConfig.CHANNEL_NAME_WAP :
                retObj = doPay(payOrder,"alipay_wap");
                break;
            case FengfupayConfig.CHANNEL_NAME_TRADE :
                retObj = doPay(payOrder,"alipay_trade");
                break;
            case FengfupayConfig.CHANNEL_NAME_FF :
                retObj = doPay(payOrder,"alipay_ff");
                break;
            default:
                retObj = buildRetObj(PayConstant.RETURN_VALUE_FAIL, "不支持的支付宝渠道[channelId="+channelId+"]");
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
        FengfupayConfig fengfupayConfig = new FengfupayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        SortedMap<String,String> map = new TreeMap();
        // 商户ID
        map.put("partnerid", fengfupayConfig.getMchId());
        // 商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());
        // 版本
        map.put("version", fengfupayConfig.getVersion());
        String key = fengfupayConfig.getKey();
        String reqUrl = fengfupayConfig.getReqUrl()+"/index.php?c=merorderQuery";
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        try {
            _log.info("丰付支付查询,请求URL:{},参数:{}",reqUrl,map);
            String result = HttpUtils.getInstance().post(reqUrl, map);
            _log.info("丰付支付查询,响应结果:{}",result);
            if(StringUtils.isBlank(result)) {
                JSONObject resultObj = JSON.parseObject(result);
                String status = resultObj.getString("code");
                retObj.put("obj", resultObj);
                if(FengfupayConfig.RETURN_VALUE_SUCCESS.equals(status)){
                    String data = resultObj.getString("data");
                    JSONObject dataObj = JSON.parseObject(data);
                    retObj.put("status", "0");
                    retObj.put("transaction_id", dataObj.get("out_trade_no"));
                    retObj.put("channelAttach", Util.buildSwiftpayAttach(resultObj));
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

    public JSONObject doPay(PayOrder payOrder,String type) {
        String logPrefix = "【丰付支付下单】"+type;
        FengfupayConfig fengfupayConfig = new FengfupayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        SortedMap<String,String> map = new TreeMap();
        //支付金额
        map.put("amount", AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
        //商户号
        map.put("partnerid",fengfupayConfig.getMchId());
        //设置异步通知地址
        map.put("notifyUrl", payConfig.getNotifyUrl(getChannelName()));
        //商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());
        //请求支付类型。
        map.put("payType", type);
        //设置同步跳转地址
        map.put("returnUrl", payConfig.getReturnUrl(getChannelName()));
        map.put("version", fengfupayConfig.getVersion());
        map.put("format", "json");
        String key = fengfupayConfig.getKey();
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        String res;
        String reqUrl = fengfupayConfig.getReqUrl();
        String payOrderId = payOrder.getPayOrderId();
        try {
            _log.info("丰付支付下单,请求URL:{},参数:{}",reqUrl,map);
            String result = HttpUtils.getInstance().post(reqUrl, map);
            _log.info("丰付支付下单,响应结果:{}",result);
            if(StringUtils.isBlank(result)) {
                _log.error("{} >>> 丰付支付下单失败", logPrefix);
                retObj.put("errDes", "丰付支付下单失败:返回对象为空");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }else {
                JSONObject resultObj = JSON.parseObject(result);
                String status = resultObj.getString("code");
                String respDesc = resultObj.getString("msg");
                String data = resultObj.getString("data");
                retObj.put("channelErrCode", status);
                retObj.put("channelErrMsg", respDesc);
                if(!FengfupayConfig.RETURN_VALUE_SUCCESS.equals(status) || StringUtils.isBlank(data)){
                    retObj.put("errDes", "丰付支付下单失败!");
                    retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                    return retObj;
                }
                JSONObject dataObj = JSON.parseObject(data);
                String payUrl = dataObj.getString("url");
                rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("###### 商户统一下单处理完成 ######");
                retObj.put("payOrderId", payOrderId);
                JSONObject payParams = new JSONObject();
                payParams.put("payUrl", payUrl);
                payParams.put("payMethod", PayConstant.PAY_METHOD_URL_JUMP);
                retObj.put("payParams", payParams);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        }
    }



}
