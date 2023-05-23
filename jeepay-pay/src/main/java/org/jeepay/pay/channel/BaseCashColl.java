package org.jeepay.pay.channel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.pay.service.RpcCommonService;

/**
 *
 */
@Component
public abstract class BaseCashColl extends BaseService implements CashCollInterface {

    @Autowired
    public RpcCommonService rpcCommonService;

    /**
     * 获取三方支付配置信息
     * 如果是平台账户,则使用平台对应的配置,否则使用商户自己配置的渠道
     * @param payOrder
     * @return
     */
    public String getPayParam(PayOrder payOrder) {
        String payParam = "";
        PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(payOrder.getPassageAccountId());
        if(payPassageAccount != null /*&& payPassageAccount.getStatus() == MchConstant.PUB_YES*/) {
            payParam = payPassageAccount.getParam();
        }
        if(StringUtils.isBlank(payParam)) {
            throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_ACCOUNT_NOT_EXIST);
        }
        return payParam;
    }

}
