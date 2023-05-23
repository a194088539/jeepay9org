package org.jeepay.manage.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.core.entity.WxUser;
import org.jeepay.manage.common.service.RpcCommonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author: aragom
 * @date: 2019-01-20
 * @description:
 */
@Component
public class WxUserService {

    private static final MyLog _log = MyLog.getLog(WxUserService.class);
    @Autowired
    private RpcCommonService rpcCommonService;
    private Map<String, String> conMap;

    /**
     * 调用微信登录接口
     * @param wxUser
     * @return
     */
    public JSONObject geLoginQrcode(WxUser wxUser) {
        String loginUrl = getServerReqUrl(wxUser) + "/wxapi/login_qrcode";
        Map paramMap = new HashMap();
        paramMap.put("account", wxUser.getAccount());
        paramMap.put("softwareId", "888");
        paramMap.put("autoLogin", true);
        paramMap.put("randomId", wxUser.getRandomId());
        paramMap.put("extraData", "");
        loginUrl += "?" + JEEPayUtil.genUrlParams(paramMap);
        _log.info("发起微信登录请求,url={}", loginUrl);
        String result = JEEPayUtil.call4Post(loginUrl);
        if(StringUtils.isEmpty(result)) return null;
        return JSON.parseObject(result);
    }

    /**
     * 调用微信退出接口
     * @param wxUser
     * @return
     */
    public JSONObject logout(WxUser wxUser) {
        String loginUrl = getServerReqUrl(wxUser) + "/wxapi/logout";
        Map paramMap = new HashMap();
        paramMap.put("randomId", wxUser.getRandomId());
        loginUrl += "?" + JEEPayUtil.genUrlParams(paramMap);
        _log.info("发起微信退出请求,url={}", loginUrl);
        String result = JEEPayUtil.call4Post(loginUrl);
        if(StringUtils.isEmpty(result)) return null;
        return JSON.parseObject(result);
    }

    /**
     * 调用微信登录接口
     * @param wxUser
     * @return
     */
    public JSONObject againLogin(WxUser wxUser) {
        String loginUrl = getServerReqUrl(wxUser) + "/wxapi/login_again";
        Map paramMap = new HashMap();
        paramMap.put("account", wxUser.getAccount());
        paramMap.put("softwareId", "888");
        paramMap.put("randomId", wxUser.getRandomId());
        loginUrl += "?" + JEEPayUtil.genUrlParams(paramMap);
        _log.info("发起微信重新登录请求,url={}", loginUrl);
        String result = JEEPayUtil.call4Post(loginUrl);
        if(StringUtils.isEmpty(result)) return null;
        return JSON.parseObject(result);
    }

    /**
     * 调用微信登录接口
     * @param wxUser
     * @return
     */
    public JSONObject getLoginStatus(WxUser wxUser) {
        String loginUrl = getServerReqUrl(wxUser) + "/wxapi/login_status";
        Map paramMap = new HashMap();
        paramMap.put("randomId", wxUser.getRandomId());
        loginUrl += "?" + JEEPayUtil.genUrlParams(paramMap);
        _log.info("获取微信登录状态请求,url={}", loginUrl);
        String result = JEEPayUtil.call4Post(loginUrl);
        if(StringUtils.isEmpty(result)) return null;
        return JSON.parseObject(result);
    }

    /**
     * 调用微信收款二维码接口
     * @param wxUser
     * @return
     */
    public JSONObject getPaymentQrcode(WxUser wxUser) {
        String paymentUrl = getServerReqUrl(wxUser) + "/wxapi/payment_qrcode";
        Map paramMap = new HashMap();
        paramMap.put("account", wxUser.getAccount());
        paramMap.put("softwareId", "888");
        paramMap.put("autoLogin", true);
        paramMap.put("randomId", wxUser.getRandomId());
        paramMap.put("extraData", "");
        Random random = new Random();
        paramMap.put("amount", random.nextInt(90) + 10);
        paramMap.put("orderId", System.currentTimeMillis());
        paymentUrl += "?" + JEEPayUtil.genUrlParams(paramMap);
        _log.info("发起微信获取收款二维码请求,url={}", paymentUrl);
        String result = JEEPayUtil.call4Post(paymentUrl);
        if(StringUtils.isEmpty(result)) return null;
        return JSON.parseObject(result);
    }

    /**
     * 得到云微信支付下配置子账户Map
     * @return
     */
    public String getServerReqUrl(WxUser wxUser) {
        PayPassageAccount payPassageAccount = new PayPassageAccount();
        payPassageAccount.setIfTypeCode("ywxpay");
        payPassageAccount.setPassageMchId(wxUser.getServerId());
        List<PayPassageAccount> payPassageAccountList = rpcCommonService.rpcPayPassageAccountService.selectAll(payPassageAccount);
        if(CollectionUtils.isNotEmpty(payPassageAccountList)) {
            PayPassageAccount ppa = payPassageAccountList.get(0);
            String param = ppa.getParam();
            return JSONObject.parseObject(param).getString("reqUrl");
        }
        return "";
    }

}
