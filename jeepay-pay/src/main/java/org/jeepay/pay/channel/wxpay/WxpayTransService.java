package org.jeepay.pay.channel.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.entpay.EntPayQueryResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.bean.result.WxPayCommonResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;

import java.io.File;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/25
 * @description:
 */
@Service
public class WxpayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(WxpayTransService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    @Override
    public JSONObject trans(TransOrder transOrder) {
        String logPrefix = "【微信企业付款】";
        JSONObject retObj = buildRetObj();
        try {
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(getTransParam(transOrder), "", payConfig.getCertRootPath() + File.separator + getChannelName(), payConfig.getNotifyUrl(getChannelName()));
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);


            EntPayRequest wxEntPayRequest = buildWxEntPayRequest(transOrder, wxPayConfig);
            String transOrderId = transOrder.getTransOrderId();

            EntPayResult result;
            try {
                result = wxPayService.getEntPayService().entPay(wxEntPayRequest);
                _log.info("{} >>> 转账成功", logPrefix);
                retObj.put("transOrderId", transOrderId);
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", result.getPaymentNo());
            } catch (WxPayException e) {
                _log.error(e, "转账失败");
                //出现业务错误
                _log.info("{}转账返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                retObj.put("transOrderId", transOrderId);
                retObj.put("isSuccess", false);
                retObj.put("channelErrCode", e.getErrCode());
                retObj.put("channelErrMsg", e.getErrCodeDes());
            }
            return retObj;
        } catch (Exception e) {
            _log.error(e, "微信转账异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    public JSONObject query(TransOrder transOrder) {
        String logPrefix = "【微信企业付款查询】";
        JSONObject retObj = buildRetObj();
        try {
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(getTransParam(transOrder), "", payConfig.getCertRootPath() + File.separator + getChannelName(), payConfig.getNotifyUrl(getChannelName()));
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            String transOrderId = transOrder.getTransOrderId();
            EntPayQueryResult result;
            try {
                result = wxPayService.getEntPayService().queryEntPay(transOrderId);
                _log.info("{} >>> 成功", logPrefix);
                retObj.putAll((Map) JSON.toJSON(result));
                retObj.put("isSuccess", true);
                retObj.put("transOrderId", transOrderId);
            } catch (WxPayException e) {
                _log.error(e, "失败");
                //出现业务错误
                _log.info("{}返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                retObj.put("channelErrCode", e.getErrCode());
                retObj.put("channelErrMsg", e.getErrCodeDes());
                retObj.put("isSuccess", false);
            }
            return retObj;
        } catch (Exception e) {
            _log.error(e, "微信企业付款查询异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    @Override
    public JSONObject balance(String payParam) {
        return null;
    }

    /**
     * 构建微信企业付款请求数据
     *
     * @param transOrder
     * @param wxPayConfig
     * @return
     */
    EntPayRequest buildWxEntPayRequest(TransOrder transOrder, WxPayConfig wxPayConfig) {
        // 微信企业付款请求对象

        EntPayRequest request = new EntPayRequest();
        request.setAmount(transOrder.getAmount().intValue()); // 金额,单位分
        String checkName = "NO_CHECK";
        if (transOrder.getExtra() != null) checkName = JSON.parseObject(transOrder.getExtra()).getString("checkName");
        request.setCheckName(checkName);
        request.setDescription(transOrder.getRemarkInfo());
        request.setReUserName(transOrder.getAccountName());
        request.setPartnerTradeNo(transOrder.getTransOrderId());
        request.setDeviceInfo(transOrder.getDevice());
        request.setSpbillCreateIp(transOrder.getClientIp());
        request.setOpenid(transOrder.getChannelUser());
        return request;
    }

}
