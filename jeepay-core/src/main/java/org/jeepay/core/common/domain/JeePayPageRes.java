package org.jeepay.core.common.domain;

import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/11/29
 * @description:
 */
public class JeePayPageRes extends JeePayResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private static final MyLog _log = MyLog.getLog(JeePayPageRes.class);

    public int count;     // 记录总数

    public JeePayPageRes(RetEnum retEnum, Object data, int count) {
        super(retEnum, data);
        this.count = count;
        _log.info(this.toString());
    }

    /**
     * 实例化函数 方法重载 添加ps参数
     * @param retEnum
     * @param data
     * @param ps
     * @param count
     */
    public JeePayPageRes(RetEnum retEnum, Object data,Map<String, Object> ps, int count) {
        super(retEnum, data, ps);
        this.count = count;
        _log.info(this.toString());
    }

    public static JeePayPageRes buildSuccess(Object data, int count) {
        JeePayPageRes jeePayResponse = new JeePayPageRes(RetEnum.RET_COMM_SUCCESS, data, count);
        return jeePayResponse;
    }

    /**
     * <p><b>Description: </b>buildSuccess方法重载   新增ps 扩展参数
     * <p>2018年9月17日 上午10:13:01
     * @author matf
     * @param data
     * @param ps
     * @param count
     * @return
     */
    public static JeePayPageRes buildSuccess(Object data, Map ps, int count) {
        JeePayPageRes jeePayResponse = new JeePayPageRes(RetEnum.RET_COMM_SUCCESS, data, ps, count);
        return jeePayResponse;
    }

    public static JeePayPageRes buildSuccess() {
        JeePayPageRes jeePayResponse = new JeePayPageRes(RetEnum.RET_COMM_SUCCESS, null, 0);
        return jeePayResponse;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "JeePayPageRes{" +
                "count=" + count +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
