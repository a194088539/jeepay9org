package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.common.util.HtmlUtils;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.pay.service.RpcCommonService;


/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
@Component
public abstract class BasePayment extends BaseService implements PaymentInterface {

    protected static final MyLog _log = MyLog.getLog(BasePayment.class);

    @Autowired
    public RpcCommonService rpcCommonService;

    @Autowired
    public PayConfig payConfig;

    public abstract String getChannelName();

    public String getOrderId(PayOrder payOrder) {
        return null;
    }

    public Long getAmount(PayOrder payOrder) {
        return null;
    }

    public JSONObject pay(PayOrder payOrder) {
        return null;
    }

    public JSONObject query(PayOrder payOrder) {
        return null;
    }

    public JSONObject close(PayOrder payOrder) {
        return null;
    }

    /**
     * 获取三方支付配置信息
     * 如果是平台账户,则使用平台对应的配置,否则使用商户自己配置的渠道
     * @param payOrder
     * @return
     */
    public String getPayParam(PayOrder payOrder) {
        String payParam = "";
        PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(payOrder.getPassageAccountId());
        if(payPassageAccount != null && payPassageAccount.getStatus() == MchConstant.PUB_YES) {
            payParam = payPassageAccount.getParam();
        }
        if(StringUtils.isBlank(payParam)) {
            throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_ACCOUNT_NOT_EXIST);
        }
        return payParam;
    }


    protected String getReqUrl(String host, String queryString) {
        return new StringBuffer()
                .append(host)
                .append(queryString)
                .toString();
    }

    /**
     * 构建支付url对象
     * @param retObj 返回的jsonobject对象
     * @param payOrder 请求支付订单的模型对象
     * @param channelPayUrl 渠道返回的支付地址
     * @return
     */
    protected JSONObject buildPayResultOfCodeURL(JSONObject retObj, PayOrder payOrder, String channelPayUrl) {
        // 设置支付订单ID
        retObj.put("payOrderId", payOrder.getPayOrderId());
        JSONObject payInfo = new JSONObject();
        payInfo.put("codeUrl", channelPayUrl); // 二维码支付链接
        payInfo.put("codeImgUrl", payConfig.getPayUrl() + "/qrcode_img_get?url=" + channelPayUrl + "&widht=200&height=200");
        payInfo.put("payMethod", PayConstant.PAY_METHOD_CODE_IMG);
        retObj.put("payParams", payInfo);
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        return retObj;
    }

    /**
     * 构建支付url对象
     * @param retObj 返回的jsonobject对象
     * @param payOrder 请求支付订单的模型对象
     * @param channelPayUrl 渠道返回的支付地址
     * @return
     */
    protected JSONObject buildPayResultOfCodeURL(JSONObject retObj, PayOrder payOrder, String channelPayUrl, boolean isEncode) {
        // 设置支付订单ID
        retObj.put("payOrderId", payOrder.getPayOrderId());
        JSONObject payInfo = new JSONObject();
        payInfo.put("codeUrl", channelPayUrl); // 二维码支付链接
        payInfo.put("codeImgUrl", payConfig.getPayUrl() + "/qrcode_img_get?url=" + HtmlUtils.encodeURI(channelPayUrl) + "&widht=200&height=200");
        payInfo.put("payMethod", PayConstant.PAY_METHOD_CODE_IMG);
        retObj.put("payParams", payInfo);
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        return retObj;
    }

    protected JSONObject buildPayResultOfForm(JSONObject retObj, PayOrder payOrder, String payForm) {
        // 设置支付订单ID
        retObj.put("payOrderId", payOrder.getPayOrderId());
        // 具体付款内容
        JSONObject payInfo = new JSONObject();
        payInfo.put("payUrl",payForm);
        payInfo.put("payMethod",PayConstant.PAY_METHOD_FORM_JUMP);
        retObj.put("payParams", payInfo);
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        return retObj;
    }

}
