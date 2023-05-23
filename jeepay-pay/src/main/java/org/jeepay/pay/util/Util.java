package org.jeepay.pay.util;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.PayConstant;

import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/25
 * @description:
 */
public class Util {

    public static boolean retIsSuccess(JSONObject retObj) {
        if(retObj == null) return false;
        return retObj.getBooleanValue(PayConstant.RETURN_PARAM_RETCODE);
    }

    // 构建威富通数据包
    public static String buildSwiftpayAttach(Map params) {
        JSONObject object = new JSONObject();
        object.put("bank_type", params.get("bank_type"));
        object.put("trade_type", params.get("trade_type"));
        return object.toString();
    }

    /**
     * 构建服务端跳转表单
     * @param jumpUrl
     * @param params
     * @return
     */
    public static String buildJumpForm(String jumpUrl, String params) {
        StringBuffer jumpForm = new StringBuffer();
        jumpForm.append("<form id=\"jeepay_jump\" name=\"jeepay_jump\" action=\""+jumpUrl+"\" method=\"post\">");
        jumpForm.append("<input type=\"hidden\" name=\"params\" id=\"params\" value=\""+params+"\">");
        jumpForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
        jumpForm.append("</form>");
        jumpForm.append("<script>document.forms[0].submit();</script>");
        return jumpForm.toString();
    }

}
