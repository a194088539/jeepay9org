package org.jeepay.core.common.constant;

/**
 * Created by admin on 2016/4/27.
 */
public class Constant {

    // 账户业务模块流水号前缀(account)
    public static final String AC_BIZ_SEQUENCE_NO_PREFIX = "ac";
    // 账户业务模块流水号前缀(config)
    public static final String CF_BIZ_SEQUENCE_NO_PREFIX = "cf";

    public final static byte PUB_YES = 1;   // 是
    public final static byte PUB_NO = 0;    // 否

    // 随机通讯码不重复的时间间隔(ms)
    public static final long RPC_SEQ_NO_NOT_REPEAT_INTERVAL = 5 * 1000;

    // 服务端返回map中业务数据结果对应的key名称
    public static final String BIZ_RESULT_KEY = "bizResult";

    public static final String MCH_CONTROLLER_ROOT_PATH = "/api";
    public static final String AGENT_CONTROLLER_ROOT_PATH = "/api";
    public static final String MGR_CONTROLLER_ROOT_PATH = "/api";

}
