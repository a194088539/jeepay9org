package org.jeepay.agent.common.service;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import org.jeepay.core.service.*;

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
    public IAgentAccountHistoryService rpcAgentAccountHistoryService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISysMessageService rpcSysMessageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISysService rpcSysService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentInfoService rpcAgentInfoService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentAccountService rpcAgentAccountService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentPassageService rpcAgentPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayProductService rpcPayProductService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchPayPassageService rpcMchPayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayOrderService rpcPayOrderService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISettRecordService rpcSettRecordService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentAgentpayPassageService rpcAgentAgentpayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentpayPassageService rpcAgentpayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ISysLogService rpcSysLogService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayPassageService rpcPayPassageService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayPassageAccountService rpcPayPassageAccountService;

}
