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
import org.jeepay.core.entity.AgentpayPassageAccount;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.mq.BaseNotify4MchTrans;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/08/16
 * @description:
 */
@Component
public abstract class BaseTransNotify extends BaseService implements TransNotifyInterface {

    protected static final MyLog _log = MyLog.getLog(BaseTransNotify.class);

    @Autowired
    public RpcCommonService rpcCommonService;

    @Autowired
    public PayConfig payConfig;

    @Autowired
    public BaseNotify4MchTrans baseNotify4MchTrans;

    public abstract String getChannelName();

    public JSONObject doNotify(Object notifyData) {
        return null;
    }

    /**
     * 获取三方支付配置信息
     * 如果是平台账户,则使用平台对应的配置,否则使用商户自己配置的渠道
     * @param transOrder
     * @return
     */
    public String getTransParam(TransOrder transOrder) {
        String payParam = "";
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(transOrder.getPassageAccountId());
        if(agentpayPassageAccount != null && agentpayPassageAccount.getStatus() == MchConstant.PUB_YES) {
            payParam = agentpayPassageAccount.getParam();
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
        if(MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType)) {
            params = RequestParamsContextHolder.getBody(request);
        } else {
            params = RequestParamsContextHolder.getParameters(request);
        }
        _log.info("回调请求类型：{},http请求body：{}", contentType, JSON.toJSONString(params));

        return JSONObject.parseObject(JSON.toJSONString(params));
    }

}
