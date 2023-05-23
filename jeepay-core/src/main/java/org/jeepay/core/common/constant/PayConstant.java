package org.jeepay.core.common.constant;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 支付常量类
 * @author aragom qq194088539
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
public class PayConstant {

	public final static String PAY_CHANNEL_WX_MICROPAY = "wxpay_micropay"; 			// 微信刷卡支付
	public final static String PAY_CHANNEL_WX_JSAPI = "wxpay_jsapi"; 				// 微信公众号支付
	public final static String PAY_CHANNEL_WX_NATIVE = "wxpay_native";				// 微信原生扫码支付
	public final static String PAY_CHANNEL_WX_APP = "wxpay_app";					// 微信APP支付
	public final static String PAY_CHANNEL_WX_MWEB = "wxpay_mweb";					// 微信H5支付
	public final static String PAY_CHANNEL_IAP = "iap";							// 苹果应用内支付
	public final static String PAY_CHANNEL_ALIPAY_MOBILE = "alipay_mobile";		// 支付宝移动支付
	public final static String PAY_CHANNEL_ALIPAY_PC = "alipay_pc";	    		// 支付宝PC支付
	public final static String PAY_CHANNEL_ALIPAY_WAP = "alipay_wap";	    	// 支付宝WAP支付
	public final static String PAY_CHANNEL_ALIPAY_H5_G = "alipay_H5_G";
	public final static String PAY_CHANNEL_ALIPAY_QR = "alipay_qr";	    		// 支付宝当面付之扫码支付
	public final static String PAY_CHANNEL_ALIPAY_BAR = "alipay_bar";	    	// 支付宝当面付之条码支付

	public final static String CHANNEL_NAME_WXPAY = "wxpay"; 				// 渠道名称:微信
	public final static String CHANNEL_NAME_ALIPAY = "alipay"; 				// 渠道名称:支付宝
	public final static String CHANNEL_NAME_JDPAY = "jdpay"; 				// 渠道名称:京东
	public final static String CHANNEL_NAME_KQPAY = "kqpay"; 				// 渠道名称:快钱
	public final static String CHANNEL_NAME_SWIFTPAY = "swiftpay"; 			// 渠道名称:威富通
	public final static String CHANNEL_NAME_GOMEPAY = "gomepay"; 			// 渠道名称:银盈通
	public final static String CHANNEL_NAME_ACCOUNTPAY = "accountpay"; 	    // 渠道名称:账户支付
	public final static String CHANNEL_NAME_SANDPAY = "sandpay"; 	    	// 渠道名称:杉德支付
	//public final static String CHANNEL_NAME_SICPAY = "sicpay"; 	    		// 渠道名称:高汇通支付
	//public final static String CHANNEL_NAME_MAXPAY = "maxpay"; 	    		// 渠道名称:拉卡拉支付
	//public final static String CHANNEL_NAME_SILVERSPAY = "silverspay"; 		// 渠道名称:睿联支付
	//public final static String CHANNEL_NAME_TRANSFARPAY = "transfarpay"; 	// 渠道名称:传化支付
	public final static String CHANNEL_NAME_ECPSSPAY = "ecpsspay"; 			// 渠道名称:汇潮支付
	public final static String CHANNEL_NAME_HEEPAY = "heepay"; 				// 渠道名称:汇付宝支付
	//public final static String CHANNEL_NAME_YYKPAY = "yykpay"; 				// 渠道名称:易游酷支付

	public final static String CHANNEL_NAME_BUF = "buf"; 				    // 渠道名称:Buf

	public final static String PAY_CHANNEL_SWIFTPAY_WXPAY_NATIVE = CHANNEL_NAME_SWIFTPAY + "_wxpay_native";			// 威富通微信扫码
	public final static String PAY_CHANNEL_SWIFTPAY_ALIPAY_NATIVE = CHANNEL_NAME_SWIFTPAY + "_alipay_native";		// 威富通微支付宝扫码
	public final static String PAY_CHANNEL_SWIFTPAY_MICROPAY = CHANNEL_NAME_SWIFTPAY + "_micropay";					// 威富通统一刷卡

	public final static String PAY_CHANNEL_ACCOUNTPAY_BALANCE = CHANNEL_NAME_ACCOUNTPAY + "_balance";	    		// 账户支付余额支付

	public final static String PAY_CHANNEL_SANDPAY_AGENTPAY = CHANNEL_NAME_SANDPAY + "_agentpay";					// 杉德代付
	//public final static String PAY_CHANNEL_SICPAY_AGENTPAY = CHANNEL_NAME_SICPAY + "_agentpay";					// 高汇通代付
	//public final static String PAY_CHANNEL_MAXPAY_AGENTPAY = CHANNEL_NAME_MAXPAY + "_agentpay";					// 拉卡拉代付
	//public final static String PAY_CHANNEL_TRANSFARPAY_AGENTPAY = CHANNEL_NAME_TRANSFARPAY + "_agentpay";			// 传化代付

	//public final static String PAY_CHANNEL_SILVERSPAY_GATEWAY = CHANNEL_NAME_SILVERSPAY + "_gateway"; 			// 睿联支付(跳转网关快捷)
	public final static String PAY_CHANNEL_ECPSSPAY_GATEWAY = CHANNEL_NAME_ECPSSPAY + "_gateway"; 					// 汇潮支付(跳转网关快捷)
	//public final static String PAY_CHANNEL_YYKPAY_CARD = CHANNEL_NAME_YYKPAY + "_card"; 							// 易游酷充值卡支付

	//汇付宝支付
    public final static String PAY_CHANNEL_HEEPAY_UNION_PC = CHANNEL_NAME_HEEPAY + "_union_pc";						// 汇付宝支付银联PC端
    public final static String PAY_CHANNEL_HEEPAY_UNION_WAP = CHANNEL_NAME_HEEPAY + "_union_wap";					// 汇付宝支付银联WAP
    public final static String PAY_CHANNEL_HEEPAY_UNION_SCAN_CODE = CHANNEL_NAME_HEEPAY + "_union_scan_code";		// 汇付宝支付银联扫码
    public final static String PAY_CHANNEL_HEEPAY_QUICK_PC= CHANNEL_NAME_HEEPAY + "_quick_pc";						// 汇付宝支付B2C网银支付—PC储蓄卡
    public final static String PAY_CHANNEL_HEEPAY_TRANSFER= CHANNEL_NAME_HEEPAY + "_transfer";						// 汇付宝支付转账

	
	public final static byte PAY_STATUS_EXPIRED = -2; 	// 订单过期
	public final static byte PAY_STATUS_FAILED = -1; 	// 支付失败
	public final static byte PAY_STATUS_INIT = 0; 		// 初始态
	public final static byte PAY_STATUS_PAYING = 1; 	// 支付中
	public final static byte PAY_STATUS_SUCCESS = 2; 	// 支付成功
	public final static byte PAY_STATUS_COMPLETE = 3; 	// 业务完成
	public final static byte PAY_STATUS_REFUND = 4; 	// 已退款

	public final static byte TRANS_STATUS_INIT = 0; 		// 初始态
	public final static byte TRANS_STATUS_TRANING = 1; 		// 转账中
	public final static byte TRANS_STATUS_SUCCESS = 2; 		// 成功
	public final static byte TRANS_STATUS_FAIL = 3; 		// 失败
	public final static byte TRANS_STATUS_COMPLETE = 4; 	// 业务完成

	public final static byte TRANS_RESULT_INIT = 0; 		// 不确认结果
	public final static byte TRANS_RESULT_REFUNDING = 1; 	// 等待手动处理
	public final static byte TRANS_RESULT_SUCCESS = 2; 		// 确认成功
	public final static byte TRANS_RESULT_FAIL = 3; 		// 确认失败

	public final static byte REFUND_STATUS_INIT = 0; 		// 初始态
	public final static byte REFUND_STATUS_REFUNDING = 1; 	// 转账中
	public final static byte REFUND_STATUS_SUCCESS = 2; 	// 成功
	public final static byte REFUND_STATUS_FAIL = 3; 		// 失败
	public final static byte REFUND_STATUS_COMPLETE = 4; 	// 业务完成

	public final static byte REFUND_RESULT_INIT = 0; 		// 不确认结果
	public final static byte REFUND_RESULT_REFUNDING = 1; 	// 等待手动处理
	public final static byte REFUND_RESULT_SUCCESS = 2; 	// 确认成功
	public final static byte REFUND_RESULT_FAIL = 3; 		// 确认失败

	public final static byte AGENTPAY_STATUS_INIT = 0; 		// 待处理(初始态)
	public final static byte AGENTPAY_STATUS_ING = 1; 		// 代付中
	public final static byte AGENTPAY_STATUS_SUCCESS = 2; 	// 成功
	public final static byte AGENTPAY_STATUS_FAIL = 3; 		// 失败

	public final static String MCH_NOTIFY_TYPE_PAY = "1";		// 商户通知类型:支付订单
	public final static String MCH_NOTIFY_TYPE_TRANS = "2";		// 商户通知类型:转账订单
	public final static String MCH_NOTIFY_TYPE_REFUND = "3";	// 商户通知类型:退款订单
	public final static String MCH_NOTIFY_TYPE_AGENTPAY = "4";	// 商户通知类型:代付订单

	public final static byte MCH_NOTIFY_STATUS_NOTIFYING = 1;	// 通知中
	public final static byte MCH_NOTIFY_STATUS_SUCCESS = 2;		// 通知成功
	public final static byte MCH_NOTIFY_STATUS_FAIL = 3;		// 通知失败


	public final static String RESP_UTF8 = "UTF-8";			// 通知业务系统使用的编码

	public static final String RETURN_PARAM_RETCODE = "retCode";	// 通讯返回码
	public static final String RETURN_PARAM_RETMSG = "retMsg";
	public static final String RESULT_PARAM_RESCODE = "resCode";
	public static final String RESULT_PARAM_ERRCODE = "errCode";
	public static final String RESULT_PARAM_ERRDES = "errDes";
	public static final String RESULT_PARAM_SIGN = "sign";

	public static final String RETURN_VALUE_SUCCESS = "SUCCESS";
	public static final String RETURN_VALUE_FAIL = "FAIL";
	public static final Integer RESULT_VALUE_SUCCESS = 0;
	public static final Integer RESULT_VALUE_FAIL = -1;

	public static final String RESPONSE_RESULT = "resResult";
	public static final String JUMP_URL = "jumpUrl";

	public static final String RETURN_ALIPAY_VALUE_SUCCESS = "success";
	public static final String RETURN_ALIPAY_VALUE_FAIL = "fail";

	public static final String RETURN_SWIFTPAY_VALUE_SUCCESS = "success";
	public static final String RETURN_SWIFTPAY_VALUE_FAIL = "fail";
	public static final String RETURN_SILVERSPAY_VALUE_SUCCESS = "ok";
	public static final String RETURN_HCPAY_VALUE_SUCCESS = "ok";
	public static final String RETURN_YYKPAY_VALUE_SUCCESS = "SUCCESS";

	public static class JdConstant {
		public final static String CONFIG_PATH = "jd" + File.separator + "jd";	// 京东支付配置文件路径
	}

	public static class WxConstant {
		public final static String TRADE_TYPE_APP = "APP";									// APP支付
		public final static String TRADE_TYPE_JSPAI = "JSAPI";								// 公众号支付或小程序支付
		public final static String TRADE_TYPE_NATIVE = "NATIVE";							// 原生扫码支付
		public final static String TRADE_TYPE_MWEB = "MWEB";								// H5支付

	}

	public static class IapConstant {
		public final static String CONFIG_PATH = "iap" + File.separator + "iap";		// 苹果应用内支付
	}

	public static class AlipayConstant {
		public final static String CONFIG_PATH = "alipay" + File.separator + "alipay";	// 支付宝移动支付
		public final static String TRADE_STATUS_WAIT = "WAIT_BUYER_PAY";		// 交易创建,等待买家付款
		public final static String TRADE_STATUS_CLOSED = "TRADE_CLOSED";		// 交易关闭
		public final static String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";		// 交易成功
		public final static String TRADE_STATUS_FINISHED = "TRADE_FINISHED";	// 交易成功且结束
	}

	public static final String NOTIFY_BUSI_PAY = "NOTIFY_VV_PAY_RES";
	public static final String NOTIFY_BUSI_TRANS = "NOTIFY_VV_TRANS_RES";
	
	public static final String PAY_METHOD_FORM_JUMP = "formJump";	// 表单跳转
	public static final String PAY_METHOD_SDK_JUMP = "sdkJump";		// SDK跳转
	public static final String PAY_METHOD_URL_JUMP = "urlJump";		// URL跳转

	public static final String PAY_METHOD_CODE_IMG = "codeImg";		// 二维码图片
	public static final String RETURN_SUKEBAOPAY_VALUE_SUCCESS = null;

	public static boolean retIsSuccess(JSONObject retObj) {
		if(retObj == null) return false;
		String value = retObj.getString(PayConstant.RETURN_PARAM_RETCODE);
		if(StringUtils.isBlank(value)) return false;
		return "success".equalsIgnoreCase(value);
	}


	
}
