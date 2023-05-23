package org.jeepay.task.common.service;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import org.jeepay.core.service.*;

/**
 * @author: aragom
 * @date: 17/12/04
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
    public IMchSettDailyCollectService rpcMchSettDailyCollectService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentInfoService rpcAgentInfoService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentAccountService rpcAgentAccountService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentAccountHistoryService rpcAgentAccountHistoryService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IAgentSettDailyCollectService rpcAgentSettDailyCollectService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayOrderService rpcPayOrderService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public ICheckService rpcCheckService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IMchBillService rpcMchBillService;

    @Reference(version = "1.0.0", timeout = 10000, retries = -1)
    public IPayProductService rpcPayProductService;

}
