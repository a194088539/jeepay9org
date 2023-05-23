package org.jeepay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.PayConstant;

import java.io.File;

/**
 * @author: aragom
 * @date: 17/12/24
 * @description:
 */
public class BaseService {

    protected JSONObject buildRetObj() {
        JSONObject retObj = new JSONObject();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        return retObj;
    }

    protected JSONObject buildFailRetObj() {
        JSONObject retObj = new JSONObject();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        return retObj;
    }

    protected JSONObject buildRetObj(String retValue, String retMsg) {
        JSONObject retObj = new JSONObject();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, retValue);
        retObj.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        return retObj;
    }

    /**
     * 获取证书文件路径
     * @param channelName
     * @param fileName
     * @return
     */
    public String getCertFilePath(String channelName, String certRootPath, String fileName) {
        return certRootPath + File.separator + channelName + File.separator + fileName;
    }

}
