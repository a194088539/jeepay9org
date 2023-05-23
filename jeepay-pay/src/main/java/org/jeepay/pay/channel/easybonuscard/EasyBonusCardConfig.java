package org.jeepay.pay.channel.easybonuscard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

public class EasyBonusCardConfig extends AbstractPaymentConfig {
    public static final String CHANNEL_NAME = "easybonuscard";
    public static final String VAL_BUSINESS_TYPE = "1";//支持：1、代付到个人储蓄卡 2、代付到个人信用 暂时支持1
    public static final String VAL_MONEY_MODEL = "0";//支持：0 = T0-垫资 ,1 = T1-预存,2 = 混合模式
    public static final String VAL_SOURCE = "0";//支持：0 = API接口,1 = 分账宝,2 = 企业版账户
    public static final String VAL_PASSWORD_TYPE = "02";//密码类型
    public static final String VAL_ENCRYPT_TYPE = "02";//加密类型
    public static final String VAL_CUSATOMER_TYPE = "01";//收款客户类型
    public static final String VAL_CURRENCY = "CNY";//币种类型

    private String wallet_id;
    private String asset_id;
    private String pay_password;

    public static final String AGENT_APPLAY_URL = "https://api.gomepay.com/CoreServlet?aid\\=AID&api_id\\=API_ID&signature\\=SIGNATURE&timestamp\\=TIMESTAMP&nonce\\=NONCE";
    public EasyBonusCardConfig(String payParam){
        Assert.notNull(payParam, "init sand config error");
        JSONObject object = JSON.parseObject(payParam);
        setAppId(object.getString("appId"));
        setPrivateKey(object.getString("privateKey"));
        setRequestUrl(object.getString("requestUrl"));

        setWallet_id(object.getString("walletId"));
        setAsset_id(object.getString("assetId"));
        setPay_password(object.getString("payPassword"));
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }
}
