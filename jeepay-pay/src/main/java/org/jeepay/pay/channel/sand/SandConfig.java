package org.jeepay.pay.channel.sand;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.jeepay.common.unify.AbstractPaymentConfig;

/**
 * @Package org.jeepay.pay.channel.sand
 * @Class: SandConfig.java
 * @Description:
 * @Author leo
 * @Date 2019/4/11 11:44
 * @Version
 **/
@Component
@Data
@ToString
@NoArgsConstructor
public class SandConfig extends AbstractPaymentConfig {

    public static final String CHANNEL_NAME = "sand";
    public static final String CHANNEL_NAME_SAND_QR = CHANNEL_NAME + "_unionqr";
    public static final String CHANNEL_NAME_SAND_JDQR = CHANNEL_NAME + "_jdqr";
    public static final String CHANNEL_NAME_SAND_QUICK = CHANNEL_NAME + "_quick";
    public static final String CHANNEL_NAME_SAND_B2C = CHANNEL_NAME + "_b2c";
    public static final String RETURN_VALUE_SUCCESS = "000000";
    public static final String RETURN_VALUE_FAIL = "0"; // 失败返回码
    public static final String RESPONSE_RESULT_SUCCESS = "respCode=000000"; // 返回上游成功
    public static final String RESPONSE_RESULT_FAIL = "respCode=-1";       // 返回上游失败
    public static final String AGENG_RETURN_VALUE_SUCCESS = "0000";

    // sand二维码下单地址
    public static final String ORDER_QR_CREATE = "/qr/api/order/create";
    // sand网关、快捷下单地址
    public static final String ORDER_QUICK_CREATE = "/gateway/api/order/pay";
    // 订单查询地址
    public static final String TRADE_ORDER_QUERY_URL = "/gateway/api/order/query";
    // 二维码订单查询地址
    public static final String TRADE_QR_QUERY_URL = "/qr/api/order/query";
    // sand代付下单地址
    public static final String AGENT_APPLAY_URL = "/agent-main/openapi/agentpay";
    // 代付订单查询
    public static final String AGENT_ORDER_QUERY_URL = "/agent-main/openapi/queryOrder";
    // 代付余额查询
    public static final String AGENT_BALANCE_QUERY_URL = "/agent-main/openapi/queryBalance";

    // sand异步通知订单成功标志
    public static final String ASYNC_NOTIFY_STATUS_OK = "1";

    // =============================sand公共请求参数默认值============================
    /**
     * 版本号
     */
    public static final String PARAM_VERSION_VAL = "1.0";
    /**
     * 接入类型 1-普通商户接入;2-平台商户接入;
     */
    public static final String PARAM_ACCESSTYPE_VAL = "1";
    /**
     * 字符编码
     */
    public static final String PARAM_CHARSET_VAL = "UTF-8";
    /**
     * 签名类型 固定填01
     */
    public static final String PARAM_SIGN_TYPE_VAL = "01";

    // ==================================sand代付参数默认值==========================================
    // 代付版本号
    public static final String AP_PARAMS_VERSION_VAL = "01";
    // 代付币种，固定填写156
    public static final String AP_PARAM_CURRENCY_VAL = "156";
    //账号类型      3-公司账户  4-银行卡
    public static final String AP_PARAM_ACC_TYPE_VAL = "4";
    // 代付接入类型 0-商户接入，默认   1-平台接入
    public static final String AP_PARAM_ACCESS_TYPE_VAL = "0";

    //交易码
    public final static String ORDER_QUERY = "ODQU";				//订单查询
    public final static String AGENT_PAY = "RTPM";					//实时代付
    public final static String MER_BALANCE_QUERY = "MBQU";			//商户余额查询
    public final static String AGENT_PAY_FEE_QUERY = "PTFQ";		//代付手续费查询
    public final static String COLLECTION = "RTCO";					//实时代收
    public final static String REALNAME_AUTH = "RNAU";				//实名认证
    public final static String REALNAME_POLICE_AUTH = "RNPA";		//实名公安认证
    public final static String CLEAR_FILE_CONTEXT = "CFCT";			//对账单申请


    public SandConfig(String payParam) {
        Assert.notNull(payParam, "init sand config error");
        JSONObject object = JSON.parseObject(payParam);
        setMchId(object.getString("mchId"));
        setPrivateStorePath(object.getString("privateStorePath"));
        setPrivateStorePathPwd(object.getString("privateStorePathPwd"));
        setPublicStorePath(object.getString("publicStorePath"));
        setRequestUrl(object.getString("requestUrl"));
        // SAN_QR:银联二维码
        setPayMode(object.getString("payMode"));
        setTradeRule(object.getString("clearCycle"));
    }



}
