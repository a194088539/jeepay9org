package org.jeepay.pay.channel.ylpay.bean;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class BatchDsDetailResDTO {
    private String merOrderNo;
    /**
     * 易联订单号
     */
    private String payecoOrderNo;
    private String accNo;
    private String accName;
    private String amount;
    private String mobileNo;
    private String payState;
    private String resMsg;


    public static List<BatchDsDetailResDTO> parseJson(String jsonStr){
        return JSONObject.parseArray(jsonStr, BatchDsDetailResDTO.class);
    }

    public String convertToJsonStr(){
        return JSONObject.toJSONString(this);
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getPayecoOrderNo() {
        return payecoOrderNo;
    }

    public void setPayecoOrderNo(String payecoOrderNo) {
        this.payecoOrderNo = payecoOrderNo;
    }
}
