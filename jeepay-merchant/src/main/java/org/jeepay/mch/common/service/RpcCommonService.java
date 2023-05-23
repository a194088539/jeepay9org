package org.jeepay.mch.common.service;

import org.jeepay.core.service.IAgentAgentpayPassageService;
import org.jeepay.core.service.IAgentpayPassageService;
import org.jeepay.core.service.IChannelConfigService;
import org.jeepay.core.service.ICommonService;
import org.jeepay.core.service.IJeePayAgentpayService;
import org.jeepay.core.service.IMchAccountHistoryService;
import org.jeepay.core.service.IMchAccountService;
import org.jeepay.core.service.IMchAgentpayPassageService;
import org.jeepay.core.service.IMchAgentpayService;
import org.jeepay.core.service.IMchAppService;
import org.jeepay.core.service.IMchBankAccountService;
import org.jeepay.core.service.IMchBillService;
import org.jeepay.core.service.IMchInfoService;
import org.jeepay.core.service.IMchPayPassageService;
import org.jeepay.core.service.IMchQrCodeService;
import org.jeepay.core.service.IMchSettBatchRecordService;
import org.jeepay.core.service.IMchTradeOrderService;
import org.jeepay.core.service.IPayOrderService;
import org.jeepay.core.service.IPayPassageAccountService;
import org.jeepay.core.service.IPayPassageService;
import org.jeepay.core.service.IPayProductService;
import org.jeepay.core.service.IRefundOrderService;
import org.jeepay.core.service.ISettRecordService;
import org.jeepay.core.service.ISysLogService;
import org.jeepay.core.service.ISysMessageService;
import org.jeepay.core.service.ISysService;
import org.jeepay.core.service.ITransOrderService;
import org.jeepay.core.service.IUserAccountService;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * @author: aragom
 * @date: 17/12/05
 * @description:
 */
@Service
public class RpcCommonService {

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchInfoService rpcMchInfoService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchAccountService rpcMchAccountService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchAccountHistoryService rpcMchAccountHistoryService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchBankAccountService rpcMchBankAccountService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISettRecordService rpcSettRecordService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchSettBatchRecordService rpcMchSettBatchRecordService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayOrderService rpcPayOrderService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ITransOrderService rpcTransOrderService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IRefundOrderService rpcRefundOrderService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchAppService rpcMchAppService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IChannelConfigService rpcChannelConfigService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchQrCodeService rpcMchQrCodeService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISysMessageService rpcSysMessageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchBillService rpcMchBillService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IUserAccountService rpcUserAccountService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchAgentpayService rpcMchAgentpayService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISysService rpcSysService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchAgentpayPassageService rpcMchAgentpayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchPayPassageService rpcMchPayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentpayPassageService rpcAgentpayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayProductService rpcPayProductService;


    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayPassageService rpcPayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayPassageAccountService rpcPayPassageAccountService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ICommonService rpcCommonService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IJeePayAgentpayService rpcJeePayAgentpayService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISysLogService rpcSysLogService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentAgentpayPassageService rpcAgentAgentpayPassageService;

}
