package org.jeepay.core.common.domain;

import com.alibaba.fastjson.JSONObject;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.ObjectValidUtil;
import org.jeepay.core.entity.BaseModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/11/29
 * @description:
 */
public class JeePayResponse extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;
    private static final MyLog _log = MyLog.getLog(JeePayResponse.class);

    public int code;     // 返回码
    public String msg;     // 返回消息
    public Object data;    // 返回数据

    public JeePayResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        _log.info(this.toString());
    }

    public JeePayResponse(RetEnum retEnum, Object data) {
        this.code = retEnum.getCode();
        this.msg = retEnum.getMessage();
        this.data = data;
        _log.info(this.toString());
    }

    public JeePayResponse(RetEnum retEnum, Object data, Map<String, Object> ps) {
        this.code = retEnum.getCode();
        this.msg = retEnum.getMessage();
        this.data = data;
        this.setPs(ps);
        _log.info(this.toString());
    }

    public static JeePayResponse build(RetEnum retEnum) {
        JeePayResponse jeePayResponse = new JeePayResponse(retEnum.getCode(), retEnum.getMessage(), null);
        return jeePayResponse;
    }

    public static JeePayResponse build(RetEnum retEnum, Object data) {
        JeePayResponse jeePayResponse = new JeePayResponse(retEnum.getCode(), retEnum.getMessage(), data);
        return jeePayResponse;
    }

    public static JeePayResponse buildSuccess() {
        return buildSuccess(null);
    }

    public static JeePayResponse buildSuccess(Object data) {
        JeePayResponse jeePayResponse = new JeePayResponse(RetEnum.RET_COMM_SUCCESS, data);
        return jeePayResponse;
    }

    public static JeePayResponse buildSuccess(Object data, JSONObject param) {
        if(param != null && param.getBooleanValue("returnArray")) {
            List<Object> objectList = new LinkedList<Object>();
            objectList.add(data);
            return new JeePayResponse(RetEnum.RET_COMM_SUCCESS, objectList);
        }else {
            return new JeePayResponse(RetEnum.RET_COMM_SUCCESS, data);
        }
    }

    public String getMsg() {
        return msg;
    }

    public JeePayResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JeePayResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "JeePayResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
