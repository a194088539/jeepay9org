package org.jeepay.pay.channel.sand;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.pay.channel.BaseTransNotify;
import org.jeepay.pay.channel.shengfutong.SftpayConfig;

/**
 * @Package org.jeepay.pay.channel.sand
 * @Class: SandTransNotifyService.java
 * @Description:
 * @Author leo
 * @Date 2019/4/12 16:39
 * @Version
 **/
@Component
public class SandTransNotifyService extends BaseTransNotify {

    private final static String logPrefix = "【杉德代付】";

    @Override
    public String getChannelName() {
        return SandConfig.CHANNEL_NAME;
    }

    /**
     * 由于sand实际没有进行异步通知，因此对代付的异步暂不处理
     * @param notifyData
     * @return
     */
    @Override
    public JSONObject doNotify(Object notifyData) {
        JSONObject bizContext = getRequestParameters(notifyData);
        JSONObject retObj = buildFailRetObj();
        _log.info("{}异步原始报文：{}", logPrefix, bizContext.toJSONString());

        retObj.put(PayConstant.RESPONSE_RESULT, SftpayConfig.RESPONSE_RESULT_FAIL);
        return retObj;
    }

}
