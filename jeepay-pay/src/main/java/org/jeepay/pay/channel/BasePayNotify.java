package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.RequestParamsContextHolder;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
@Component
public abstract class BasePayNotify extends BaseService implements PayNotifyInterface {

    protected static final MyLog _log = MyLog.getLog(BasePayNotify.class);

    @Autowired
    public RpcCommonService rpcCommonService;

    @Autowired
    public PayConfig payConfig;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    public abstract String getChannelName();

    @Override
    public JSONObject doNotify(Object notifyData) {
        return null;
    }
    @Override
    public JSONObject doReturn(Object notifyData) {
        return new JSONObject();
    }

    /**
     * 获取三方支付配置信息
     * 如果是平台账户,则使用平台对应的配置,否则使用商户自己配置的渠道
     * @param payOrder
     * @return
     */
    public String getPayParam(PayOrder payOrder) {
        String payParam = "";
        PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(payOrder.getPassageAccountId());
        if(payPassageAccount != null && payPassageAccount.getStatus() == MchConstant.PUB_YES) {
            payParam = payPassageAccount.getParam();
        }
        if(StringUtils.isBlank(payParam)) {
            throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_ACCOUNT_NOT_EXIST);
        }
        return payParam;
    }

    protected JSONObject getRequestParameters(Object notifyData) {
        HttpServletRequest request = (HttpServletRequest) notifyData;
        // 获取http请求体中的参数
        String contentType = request.getContentType();
        Map<String, Object> params = Maps.newHashMap();
        //json类型
        // application/json  application/json;charset=UTF-8  application/json; charset=UTF-8
        if(!StringUtils.equals(null,contentType)&&contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            params = RequestParamsContextHolder.getBody(request);
        } else if(!StringUtils.equals(null,contentType)&&contentType.contains(MediaType.TEXT_PLAIN_VALUE)) {
            params = RequestParamsContextHolder.getBody(request);
        } else if(!StringUtils.equals(null,contentType)&&contentType.contains(MediaType.APPLICATION_XML_VALUE)
                || !StringUtils.equals(null,contentType)&&contentType.contains(MediaType.TEXT_XML_VALUE)) {
            String xml = RequestParamsContextHolder.getXmlParameters(request);
            params.put("xml", xml);
        } else {
            params = RequestParamsContextHolder.getParameters(request);
        }
        _log.info("回调请求类型：{},http请求body：{}", contentType, JSON.toJSONString(params));

        return JSONObject.parseObject(JSON.toJSONString(params));
    }

}
