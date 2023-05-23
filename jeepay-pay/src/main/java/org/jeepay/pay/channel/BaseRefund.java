package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.entity.*;
import org.jeepay.pay.service.RpcCommonService;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
@Component
public abstract class BaseRefund extends BaseService implements RefundInterface {

    @Autowired
    public RpcCommonService rpcCommonService;

    @Autowired
    public PayConfig payConfig;

    public abstract String getChannelName();

    public JSONObject refund(RefundOrder refundOrder) {
        return null;
    }

    public JSONObject query(RefundOrder refundOrder) {
        return null;
    }

    /**
     * 获取三方支付配置信息
     * 如果是平台账户,则使用平台对应的配置,否则使用商户自己配置的渠道
     * @param refundOrder
     * @return
     */
    public String getRefundParam(RefundOrder refundOrder) {
        String payOrderId = refundOrder.getPayOrderId();
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if(payOrder == null) return "";
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

}
