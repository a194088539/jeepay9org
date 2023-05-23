package org.jeepay.common.unify;

import lombok.Data;
import lombok.ToString;

/**
 * @Package org.jeepay.common.unify
 * @Class: AbstractPaymentConfig.java
 * @Description:
 * @Author leo
 * @Date 2019/3/30 12:43
 * @Version
 **/
@Data
@ToString
public abstract class AbstractPaymentConfig implements PaymentConfigInterface {

    /**
     * 商户号
     */
    private volatile String mchId;
    /**
     * 应用Id
     */
    private volatile String appId;
    /**
     * 合作身份Id，服务商id
     */
    private volatile String partnerId;

    private volatile String tradeRule;

    private volatile String privateKey;

    private volatile String publicKey;

    private volatile String requestUrl;

    private volatile String privateStorePath;

    private volatile String privateStorePathPwd;

    private volatile String publicStorePath;

    private volatile String publicStorePathPwd;

    private volatile String payProduct;

    private volatile String payMode;

    private volatile String extInfo;
    /**
     * 响应数据类型
     */
    private volatile String responseType;


}
