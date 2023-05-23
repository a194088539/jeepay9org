package org.jeepay.core.service;

import org.jeepay.core.entity.MchAccount;
import org.jeepay.core.entity.PayOrder;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/4
 * @description:
 */
public interface IMchAccountService {

    List<MchAccount> listAll(int offset, int limit);

    int updateSettAmount(long mchId, long totalAmount);

    MchAccount findByMchId(Long mchId);

    /**
     * 当订单支付成功时,给商户账户加款,并记录资金流水
     * @param payOrder
     */
    void creditToAccount(PayOrder payOrder);

    /** 加款:有银行流水 **/
    void creditToAccount(Long mchId, Long mchIncome, Long agentId, Long parentAgentId, Long orderAmount, Long agentProfit, Long parentAgentProfit, Long platProfit, Long channelCost, String requestNo, String channelOrderNo, Byte bizType);

    /** 减款 :有银行流水**/
    void debitToAccount(Long mchId, Long amount, String requestNo, String channelOrderNo, Byte bizType);

    /** 冻结 **/
    void freezeAmount(Long mchId, Long freezeAmount);

    /** 结算成功：解冻+减款 **/
    void unFreezeAmount(Long mchId, Long amount, Long orderAmount, Long fee, String requestNo, Byte bizType);

    /** 结算失败：解冻 **/
    void unFreezeSettAmount(Long mchId, Long amount);

    /**
     * 代付冻结, 增加代付不可用余额
     * @param mchId
     * @param freezeAmount
     */
    void freeze4Agentpay(Long mchId, Long freezeAmount);

    /**
     * 代付成功：解冻+减款
     * @param mchId
     * @param amount
     * @param orderAmount
     * @param fee
     * @param requestNo
     * @param bizType
     * @param agentpayPassageId
     */
    void unFreeze4AgentpaySuccess(Long mchId, Long amount, Long orderAmount, Long fee, String requestNo, Byte bizType, Integer agentpayPassageId);

    /**
     * 代付失败：解冻
     * @param mchId
     * @param amount
     */
    void unFreeze4AgentpayFail(Long mchId, Long amount);

    /**
     * 给商户账户加钱
     * @param mchId
     * @param amount
     */
    void credit2Account(Long mchId, Byte bizType, Long amount, String bizItem);

    /**
     * 给商户账户加钱
     * @param mchId
     * @param amount
     */
    void credit2Account(Long mchId, Byte bizType, Long amount, String orderId, Long orderAmount, Long fee, String bizItem);

    /**
     * 代付充值
     * @param mchId
     * @param mchIncome
     * @param agentId
     * @param orderAmount
     * @param agentProfit
     * @param platProfit
     * @param channelCost
     * @param requestNo
     * @param channelOrderNo
     * @param bizType
     * @param bizItem
     */
    void recharge4Agentpay(Long mchId, Long agentId, Long mchIncome, Long orderAmount, Long agentProfit, Long parentAgentProfit, Long platProfit, Long channelCost, String requestNo, String channelOrderNo, Byte bizType, String bizItem);

    /**
     * 给商户账户减钱
     * @param mchId
     * @param amount
     */
    void debit2Account(Long mchId, Byte bizType, Long amount, String bizItem);
}
