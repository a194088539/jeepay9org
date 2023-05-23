package org.jeepay.pay.channel.sand.response;

public class SandResHead {
    /**
     * 版本号
     */
    private String version;
    /**
     * 响应时间
     */
    private  String respTime;
    /**
     * 响应码
     */
    private  String respCode;
    /**
     * 响应描述
     */
    private  String respMsg;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
