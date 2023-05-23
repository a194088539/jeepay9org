package org.jeepay.common.unify;

public interface PaymentConfigInterface {
    /**
     * 商户号
     * @return
     */
    String getMchId();

    /**
     * 上游通道应用Id
     * @return
     */
    String getAppId();

    /**
     * 合作身份Id，服务商Id
     * @return
     */
    String getPartnerId();

    /**
     * 交易规则，上游通道结算到账规则标识，如(T+1, D+0)的标志
     * @return
     */
    String getTradeRule();

    /**
     * 请求私钥
     * @return
     */
    String getPrivateKey();

    /**
     * 请求公钥
     * @return
     */
    String getPublicKey();

    /**
     * http请求地址
     * @return
     */
    String getRequestUrl();

    /**
     * 私钥证书地址
     * @return
     */
    String getPrivateStorePath();

    /**
     * 私钥证书密码
     * @return
     */
    String getPrivateStorePathPwd();

    /**
     * 公钥证书地址
     * @return
     */
    String getPublicStorePath();

    /**
     * 公钥证书密码
     * @return
     */
    String getPublicStorePathPwd();

    /**
     * 上游通道交易产品标志，如支付宝，微信，银联的标志，具体根据上游通道文档配置
     * @return
     */
    String getPayProduct();

    /**
     * 上游通道交易类型标志，具体依据上游通道文档配置，如A渠道的支付宝标志位1001，配置为1001即可
     * @return
     */
    String getPayMode();

    /**
     * 扩展参数
     * @return
     */
    String getExtInfo();

    /**
     * 数据响应类型，如json，form表单等标识
     */
    String getResponseType();
}
