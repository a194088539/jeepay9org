package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.AgentpayPassageAccount;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.service.RpcCommonService;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
@Component
public abstract class BaseTrans extends BaseService implements TransInterface {

    protected static final MyLog _log = MyLog.getLog(BaseTrans.class);

    @Autowired
    protected RpcCommonService rpcCommonService;

    @Autowired
    protected PayConfig payConfig;

    public abstract String getChannelName();

    public String getOrderId(TransOrder transOrder) {
        return null;
    }

    public JSONObject trans(TransOrder transOrder) {
        return null;
    }

    public JSONObject query(TransOrder transOrder) {
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

    protected String getReqUrl(String host, String queryString) {
        return new StringBuffer()
                .append(host)
                .append(queryString)
                .toString();
    }

}
