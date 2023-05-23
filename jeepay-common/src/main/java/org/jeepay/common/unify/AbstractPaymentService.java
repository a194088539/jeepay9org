package org.jeepay.common.unify;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.jeepay.common.http.HttpConfigStorage;
import org.jeepay.common.http.HttpRequestTemplate;
import org.jeepay.core.entity.PayOrder;


import java.util.Map;

/**
 * @Package org.jeepay.pay.channel
 * @Class: AbstractPaymentService.java
 * @Description:
 * @Author leo
 * @Date 2019/3/29 15:30
 * @Version
 **/
@Component
public abstract class AbstractPaymentService<PC extends AbstractPaymentConfig> {

    protected PC payConfigStorage;

    protected HttpRequestTemplate requestTemplate;

    /**
     * 如果上游通道对订单ID格式有特殊要求, 需要子类单独实现
     */
    protected boolean isNeedResetOrderId = false;
    /**
     * 如果上游通道对金额格式有特殊要求，子类单独实现
     */
    protected boolean isNeedFormatAmount = false;


    public AbstractPaymentService setPayConfigStorage(PC payConfigStorage) {
        this.payConfigStorage = payConfigStorage;
        return this;
    }

    public AbstractPaymentService setRequestTemplateConfigStorage(HttpConfigStorage configStorage) {
        this.requestTemplate = new HttpRequestTemplate(configStorage);
        return this;
    }

    public JSONObject doAction(PaymentConfigInterface config, PayOrder payOrder) {
        if(isNeedResetOrderId) {
            payOrder.setPayOrderId(resetRequestOrderId());
        }
        // 生成请求参数
        Map<String, Object> reqParameters = generateRequestData(payOrder);
        // 重新格式化请求的金额格式
        if(isNeedFormatAmount) {
            formatAmount(reqParameters);
        }
        // 生成签名字符串
        generateRequestSign(reqParameters);
        JSONObject origin = request(config, reqParameters);
        return handleResponse(origin);
    }

    /**
     * 获取请求支付的渠道名称
     * @return
     */
    abstract protected String getChannelName();

    /**
     * 生成请求参数
     * @param payOrder
     * @return
     */
    abstract protected Map<String, Object> generateRequestData(PayOrder payOrder);

    /**
     * 生成请求签名字符串
     * @return
     */
    abstract protected void generateRequestSign(Map<String ,Object> reqParameters);

    /**
     * 执行http请求
     * @param config
     * @param requestParameters
     * @return
     */
    abstract protected JSONObject request(PaymentConfigInterface config, Map<String, Object> requestParameters);

    /**
     * 如果上游通道对订单ID格式有特殊要求, 需要子类单独实现
     * @return
     */
    abstract protected String resetRequestOrderId();

    /**
     * 如果上游通道对金额格式有特殊要求，子类单独实现
     * @param reqParameters
     * @return
     */
    abstract protected void formatAmount(Map<String, Object> reqParameters);

    /**
     * 处理请求响应数据
     * @param resData
     * @return
     */
    abstract protected JSONObject handleResponse(JSONObject resData);

    protected void isNeedResetOrderId(boolean isNeedResetOrderId) {
        this.isNeedResetOrderId = isNeedResetOrderId;
    }

    protected void isNeedFormatAmount(boolean isNeedFormatAmount) {
        this.isNeedFormatAmount = isNeedFormatAmount;
    }

}
