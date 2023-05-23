package org.jeepay.pay.channel.easybonuscard;

import com.alibaba.fastjson.JSONObject;

import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.common.unify.AbstractPaymentConfig;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.sand.SandConfig;


public class EasyBonusCardTransService extends BaseTrans {
    private final static String logPrefix = "【银盈通代付】";

    private static final String CILRSA = "RSA/ECB/PKCS1Padding";

    private  static final String CILAES = "AES/ECB/PKCS5Padding";
    @Override
    public String getChannelName() { return EasyBonusCardConfig.CHANNEL_NAME;}

    @Override
    public JSONObject trans(TransOrder transOrder) {
        EasyBonusCardConfig config = new EasyBonusCardConfig(getTransParam(transOrder));
        JSONObject bizParameters = getAgentpayParameters(config, transOrder);
        _log.info("{}请求参数：{}", logPrefix, bizParameters.toJSONString());
        HttpRequestTemplate requestTemplate = new HttpRequestTemplate(null);
        String origin = requestTemplate.postForObject(getReqUrl(config.getRequestUrl(), SandConfig.AGENT_APPLAY_URL),
                bizParameters, String.class);
        return  null;
    }

    @Override
    public JSONObject balance(String payParam) {
        return null;
    }

    /**
     * 代付请求报文
     * @param config
     * @param transOrder
     * @return
     */
    private JSONObject getAgentpayParameters(AbstractPaymentConfig config, TransOrder transOrder) {
        JSONObject transferMap = new JSONObject();
        transferMap.put("merchant_number",config.getAppId());//商户号
        transferMap.put("order_number",transOrder.getTransOrderId());//订单号
        transferMap.put("wallet_id",((EasyBonusCardConfig) config).getWallet_id());//付款钱包id
        transferMap.put("asset_id",((EasyBonusCardConfig) config).getAsset_id());//付款资产id
        transferMap.put("business_type",EasyBonusCardConfig.VAL_BUSINESS_TYPE);//业务类型
        transferMap.put("money_model",EasyBonusCardConfig.VAL_MONEY_MODEL);//资金模式
        transferMap.put("source",EasyBonusCardConfig.VAL_SOURCE);//代付渠道
        transferMap.put("password_type",EasyBonusCardConfig.VAL_PASSWORD_TYPE);//付款方密码类型
        transferMap.put("encrypt_type",EasyBonusCardConfig.VAL_ENCRYPT_TYPE);//付款方加密类型
        transferMap.put("pay_password",((EasyBonusCardConfig) config).getPay_password());//付款方支付密码
        transferMap.put("customer_type",EasyBonusCardConfig.VAL_CUSATOMER_TYPE);//收款客户类型
        transferMap.put("customer_name",transOrder.getAccountName());//收款客户姓名
        transferMap.put("currency",EasyBonusCardConfig.VAL_CURRENCY);//代付币种
        transferMap.put("amount",transOrder.getAmount());//代付金额
        transferMap.put("async_notification_addr",config.getRequestUrl());//异步通知地址
        transferMap.put("account_number",transOrder.getAccountNo());//收款人银行卡
        transferMap.put("issue_bank_name",transOrder.getBankName());//收款银行卡发卡行名称
        return transferMap;
    }

}
