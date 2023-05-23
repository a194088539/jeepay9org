package org.jeepay.pay.channel.gomepay;

import java.text.SimpleDateFormat;

import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayment;
import org.jeepay.pay.channel.gomepay.util.DSDES;
import org.jeepay.pay.channel.gomepay.util.HexConvert;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: aragom
 * @date: 18/3/20
 * @description: 银盈通支付接口
 */
@Service
public class GomepayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(GomepayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_GOMEPAY;
    }

    @Override
    public JSONObject pay(PayOrder payOrder) {
        GomepayConfig gomepayConfig = new GomepayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();

        // 商户号
        String merchno = gomepayConfig.getMerchno();
        // 商户订单号
        String dsorderid = payOrder.getPayOrderId();
        // 商户名称
        String merchname = gomepayConfig.getMerchname();
        // 购买时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月dd日");
        String buytime = sdf.format(System.currentTimeMillis());
        // 电商钱包ID
        String mediumno = gomepayConfig.getMediumno();
        // 币种
        String currency = "CNY";
        // 交易总金额
        String amount = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");
        // 通知地址
        String dsyburl = gomepayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName()));
        // 页面跳转地址
        String dstburl = gomepayConfig.transformUrl(payConfig.getReturnUrl(getChannelName()));
        // 授权码返回地址(暂时没用)
        String authnourl = gomepayConfig.transformUrl(payConfig.getNotifyUrl(getChannelName()));
        // 商品名称
        String product = payOrder.getSubject();
        // 商品描述
        String productdesc = payOrder.getBody();
        // 订单付款姓名
        String dsusername = payOrder.getChannelUser();
        // 电商平台用户ID
        String dsuserno = "user";
        // 直接跳转网银页面标识，跳转B2C/B2B网银页面必填，值为1
        String net_flag ="1";
        // 发卡行ID，“跳转网银标识”为1时必填
        String issue_bank_id = "";
        // 账户类型编码，“跳转网银标识”为1且B2C时必填，B2B非必填
        String account_type_code = "01";
        // 客户类型，“跳转网银标识”为1时必填
        String customer_type = "01";
        String dstbdata = getPayTradeSign( merchno,
                dsorderid, merchname, buytime, mediumno,
                currency,  amount, dsyburl,
                dstburl, authnourl, product, productdesc,dsusername,
                dsuserno,net_flag,issue_bank_id,account_type_code,customer_type);

        //钱包密钥
        String dstbdatasign = null;
        try {
            dstbdatasign = DSDES.getBlackData(gomepayConfig.getMediumkey().getBytes(), dstbdata.getBytes("utf-8"));
        } catch (Exception e) {
            _log.error(e, "");
        }
        //_log.info("明文:"+dstbdata);
        //_log.info("密文:"+dstbdatasign);
        String url = gomepayConfig.getReqUrl() + "/?" + dstbdata + "&dstbdatasign=" + dstbdatasign;
        //_log.info("支付URL:{}", url);
        // 支付链接地址
        retObj.put("payUrl", url);
        // 支付跳转方法
        retObj.put("payAction", "POST");
        // 支付参数
        retObj.put("payParams", "");
        retObj.put("payOrderId", payOrder.getPayOrderId()); // 设置支付订单ID
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
        return retObj;
    }

    public static String getPayTradeSign(String merchno,
                                         String dsorderid,String merchname,String buytime,String mediumno,
                                         String currency, String amount,String dsyburl,
                                         String dstburl,String authnourl,String product,String productdesc,
                                         String dsusername, String dsuserno,String net_flag,String issue_bank_id, String account_type_code, String customer_type){

        String parameters = "";
        if (merchno != null && !merchno.trim().equals("")) {
            parameters += "merchno=" + merchno;// 商户号
        } else{
            parameters += "merchno=merchno is null";//商户号
        }
        if (dsorderid != null && !dsorderid.trim().equals("")) {
            parameters += "&dsorderid=" + dsorderid;// 商户订单号
        }else{
            parameters += "dsorderid=dsorderid is null";//商户号
        }
        if (merchname != null && !merchname.trim().equals("")) {
            parameters += "&merchname=" + HexConvert.toHexCode(merchname);// 商户名称
        }

        if (buytime != null && !buytime.trim().equals("")) {
            parameters += "&buytime=" + HexConvert.toHexCode(buytime);// 商户订单号
        }
        if (mediumno != null && !mediumno.trim().equals("")) {
            parameters += "&mediumno=" + mediumno;// 钱包id
        }
        if (currency != null && !currency.trim().equals("")) {
            parameters += "&currency=" + currency;// 币种
        }else{
            parameters += "currency=currency is null";//商户号
        }
        if (amount != null && !amount.trim().equals("")) {
            parameters += "&amount=" + amount;// 金额
        } else{
            parameters += "amount=amount is null";//商户号
        }
        if (dsyburl != null && !dsyburl.trim().equals("")) {
            parameters += "&dsyburl=" + dsyburl;// 异步通知地址
        }else{
            parameters += "dsyburl=dsyburl is null";//商户号
        }
        if (dstburl != null && !dstburl.trim().equals("")) {
            parameters += "&dstburl=" + dstburl;// 备用
        }else{
            parameters += "dstburl=dstburl is null";//商户号
        }
        if (authnourl != null && !authnourl.trim().equals("")) {
            parameters += "&authnourl=" + dstburl;// 授权码返回地址
        }else{
            parameters += "authnourl=authnourl is null";//商户号
        }

        if (product != null && !product.trim().equals("")) {
            parameters += "&product=" + HexConvert.toHexCode(product);// 商品名称
        } else{
            parameters += "product=product is null";//商户号
        }
        if (productdesc != null && !productdesc.trim().equals("")) {
            parameters += "&productdesc=" + HexConvert.toHexCode(productdesc);// 电商商品描述
        }
        if (dsuserno != null && !dsuserno.trim().equals("")) {
            parameters += "&dsuserno=" + dsuserno;// 电商平台用户Id
        }

        if (dsusername != null && !dsusername.trim().equals("")) {
            parameters += "&dsusername=" + dsusername;// 订单付款姓名
        }
        return parameters;
    }


}
