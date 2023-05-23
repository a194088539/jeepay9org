package org.jeepay.core.common.constant;

/**
 * @author: aragom
 * @date: 17/12/3
 * @description:
 */
public class MchConstant {

    public final static byte STATUS_AUDIT_ING = -1; 	// 等待审核
    public final static byte STATUS_AUDIT_NOT = -2;     // 审核不通过
    public final static byte STATUS_STOP = 0; 	        // 停止使用
    public final static byte STATUS_OK = 1; 	        // 使用中

    public final static byte MCH_TYPE_PLATFORM = 1;     // 平台账户
    public final static byte MCH_TYPE_PRIVATE = 2;      // 私有账户

    public final static byte SETT_MODE_HAND = 1;         // 手工结算
    public final static byte SETT_MODE_AUTO = 2;         // 自动结算
    public final static byte SETT_MODE_BATCH = 2;        // 批量结算

    public final static byte SETT_TYPE_D0 = 1;          // 结算方式D0到账
    public final static byte SETT_TYPE_D1 = 2;          // 结算方式D1到账
    public final static byte SETT_TYPE_T0 = 3;          // 结算方式T0到账
    public final static byte SETT_TYPE_T1 = 4;          // 结算方式T1到账

    public final static byte SETT_STATUS_AUDIT_ING = 1;         // 等待审核
    public final static byte SETT_STATUS_AUDIT_OK = 2;          // 已审核
    public final static byte SETT_STATUS_AUDIT_NOT = 3;         // 审核不通过
    public final static byte SETT_STATUS_REMIT_ING = 4;         // 打款中
    public final static byte SETT_STATUS_REMIT_SUCCESS = 5;     // 打款成功
    public final static byte SETT_STATUS_REMIT_FAIL = 6;        // 打款失败

    public final static byte BIZ_TYPE_TRANSACT = 1;             // 交易
    public final static byte BIZ_TYPE_REMIT = 2;                // 打款(提现)
    public final static byte BIZ_TYPE_CHANGE_BALANCE = 3;       // 调账
    public final static byte BIZ_TYPE_RECHARGE = 4;             // 充值
    public final static byte BIZ_TYPE_ERROR_HANKLE = 5;         // 差错处理
    public final static byte BIZ_TYPE_AGENTPAY = 6;             // 代付

    public final static String BIZ_ITEM_BALANCE = "10";             // 余额
    public final static String BIZ_ITEM_AGENTPAY_BALANCE = "11";    // 代付余额
    public final static String BIZ_ITEM_FROZEN_MONEY = "12";        // 冻结金额
    public final static String BIZ_ITEM_SECURITY_MONEY = "13";      // 保证金
    public final static String BIZ_ITEM_PAY = "20";                 // 支付
    public final static String BIZ_ITEM_AGENTPAY = "21";            // 代付
    public final static String BIZ_ITEM_OFF = "22";                 // 线下充值
    public final static String BIZ_ITEM_ONLINE = "23";              // 线上充值

    public final static byte FUND_DIRECTION_ADD = 1;  // 加款
    public final static byte FUND_DIRECTION_SUB = 2;  // 减款

    public final static byte AGENT_BIZ_TYPE_PROFIT = 1;         // 代理商分润
    public final static byte AGENT_BIZ_TYPE_REMIT = 2;          // 代理商打款
    public final static byte AGENT_BIZ_TYPE_CHANGE_BALANCE = 3;          // 代理商调账

    public final static byte PUB_YES = 1;   // 是
    public final static byte PUB_NO = 0;    // 否

    public final static byte COLLECT_TYPE_NORMAL = 1;       // 存入/减少汇总
    public final static byte COLLECT_TYPE_TEMP = 2;         // 临时汇总
    public final static byte COLLECT_TYPE_LEAVE = 3;        // 遗留汇总

    public final static String MCH_ROLE_NO = "ROLE_MCH_NO"; 	            // 无权限
    public final static String MCH_ROLE_NORMAL = "ROLE_MCH_NORMAL"; 	    // 正常

    public final static String AGENT_ROLE_NO = "ROLE_AGENT_NO"; 	            // 无权限
    public final static String AGENT_ROLE_NORMAL = "ROLE_AGENT_NORMAL"; 	    // 正常

    // 生成(0),处理中(1),成功(2),失败(-1)
    public static final byte TRADE_ORDER_STATUS_INIT = 0;
    public static final byte TRADE_ORDER_STATUS_ING = 1;
    public static final byte TRADE_ORDER_STATUS_SUCCESS = 2;
    public static final byte TRADE_ORDER_STATUS_FAIL = -1;

    public final static String MGR_ROLE_NO = "ROLE_MGR_NO"; 	            // 无权限
    public final static String MGR_ROLE_NORMAL = "ROLE_MGR_NORMAL"; 	    // 正常

    public final static String MGR_SUPER_PASSWORD = "admin666888"; 				// 运营平台超级密码(运营平台涉及金额操作时会验证)
    public final static String MCH_DEFAULT_PASSWORD = "abc998998"; 	            // 商户登录默认密码
    public final static String MCH_DEFAULT_PAY_PASSWORD = "abc998998"; 	        // 商户支付默认密码

    public final static byte MGR_STATUS_STOP = 0; 	// 用户禁止使用
    public final static byte MGR_STATUS_OK = 1; 	// 用户正常

    public final static byte SYSTEM_MGR = 1; 	// 运营系统
    public final static byte SYSTEM_MCH = 2; 	// 商户系统
    public final static byte SYSTEM_AGENT = 3; 	// 代理商系统

    public final static byte MCH_BILL_STATUS_INT = 0;          // 初始,未生成
    public final static byte MCH_BILL_STATUS_COMPLETE = 1;     // 生成完成

    public final static byte SETT_INFO_TYPE_AGENT = 1;      // 结算商类型:代理商
    public final static byte SETT_INFO_TYPE_MCH = 2;        // 结算商类型:商户

    public final static byte PRODUCT_TYPE_PAY = 1;          // 产品类型:收款
    public final static byte PRODUCT_TYPE_RECHARGE = 2;     // 产品类型:充值

    public final static byte TRADE_TYPE_PAY = 1;          // 交易类型:收款
    public final static byte TRADE_TYPE_RECHARGE = 2;     // 交易类型:充值

    public final static byte AGENTPAY_OUT_BALANCE = 2;    // 代付出款余额,1:从收款账户出款,2:从代付余额账户出款

    public final static byte AGENTPAY_CHANNEL_PLAT = 1;     // 代付渠道:商户后台
    public final static byte AGENTPAY_CHANNEL_API = 2;      // 代付渠道:API接口

}
