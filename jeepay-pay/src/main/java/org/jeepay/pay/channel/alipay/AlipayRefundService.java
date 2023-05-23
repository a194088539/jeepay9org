package org.jeepay.pay.channel.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.RefundOrder;
import org.jeepay.pay.channel.BaseRefund;

import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/25
 * @description:
 */
@Service
public class AlipayRefundService extends BaseRefund {

    private static final MyLog _log = MyLog.getLog(AlipayRefundService.class);

    @Autowired
    private AlipayConfig alipayConfig;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    public JSONObject refund(RefundOrder refundOrder) {
        String logPrefix = "【支付宝退款】";
        String refundOrderId = refundOrder.getRefundOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getRefundParam(refundOrder));
        AlipayClient client = new DefaultAlipayClient(alipayConfig.getReqUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(refundOrder.getPayOrderId());
        model.setTradeNo(refundOrder.getChannelPayOrderNo());
        model.setOutRequestNo(refundOrderId);
        model.setRefundAmount(AmountUtil.convertCent2Dollar(refundOrder.getRefundAmount().toString()));
        model.setRefundReason("正常退款");
        request.setBizModel(model);
        JSONObject retObj = buildRetObj();
        retObj.put("refundOrderId", refundOrderId);
        retObj.put("isSuccess", false);
        try {
            AlipayTradeRefundResponse response = client.execute(request);
            if(response.isSuccess()){
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", response.getTradeNo());
            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }

    public JSONObject query(RefundOrder refundOrder) {
        String logPrefix = "【支付宝退款查询】";
        String refundOrderId = refundOrder.getRefundOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getRefundParam(refundOrder));
        AlipayClient client = new DefaultAlipayClient(alipayConfig.getReqUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(refundOrder.getPayOrderId());
        model.setTradeNo(refundOrder.getChannelPayOrderNo());
        model.setOutRequestNo(refundOrderId);
        request.setBizModel(model);
        JSONObject retObj = buildRetObj();
        retObj.put("refundOrderId", refundOrderId);
        try {
            AlipayTradeFastpayRefundQueryResponse response = client.execute(request);
            if(response.isSuccess()){
                retObj.putAll((Map) JSON.toJSON(response));
                retObj.put("isSuccess", true);
            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }


}
