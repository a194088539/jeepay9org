package org.jeepay.pay.channel.yldevpay.bean;


public class MsgBody {

    private String SN = "";
    private String ACC_NO = "";
    private String ACC_NAME = "";
    private String ACC_PROVINCE = "";
    private String ACC_CITY = "";
    private String AMOUNT = "";
    private String MOBILE_NO = "";
    private String PAY_STATE = "";
    private String BANK_NAME = "";
    private String ACC_TYPE = "00";
    private String ACC_PROP = "0";
    private String ID_TYPE = "0";
    private String ID_NO = "";
    private String CNY = "CNY";
    private String EXCHANGE_RATE = "";
    private String SETT_AMOUNT = "";  //人民币结算金额
    private String SETT_DATE = "";
    private String REMARK = "";
    private String RESERVE = "";
    private String RETURN_URL = ""; //异步返回地址
    private String MER_ORDER_NO = ""; //商户订单号
    private String MER_SEQ_NO = ""; //商户订单号
    private String TRANS_DESC = ""; //交易描述
    private String SUCCESS_DATE = ""; //交易成功时间
    private String QUERY_NO_FLAG = "";
    private String SMS_CODE = ""; //短信码
    private String DETAIL_CONTENT="";//批量代收明细内容

    public String getACC_CITY() {
        return ACC_CITY;
    }
    public void setACC_CITY(String acc_city) {
        ACC_CITY = acc_city;
    }
    public String getACC_NAME() {
        return ACC_NAME;
    }
    public void setACC_NAME(String acc_name) {
        ACC_NAME = acc_name;
    }
    public String getACC_NO() {
        return ACC_NO;
    }
    public void setACC_NO(String acc_no) {
        ACC_NO = acc_no;
    }
    public String getACC_PROP() {
        return ACC_PROP;
    }
    public void setACC_PROP(String acc_prop) {
        ACC_PROP = acc_prop;
    }
    public String getACC_PROVINCE() {
        return ACC_PROVINCE;
    }
    public void setACC_PROVINCE(String acc_province) {
        ACC_PROVINCE = acc_province;
    }
    public String getACC_TYPE() {
        return ACC_TYPE;
    }
    public void setACC_TYPE(String acc_type) {
        ACC_TYPE = acc_type;
    }
    public String getAMOUNT() {
        return AMOUNT;
    }
    public void setAMOUNT(String amount) {
        AMOUNT = amount;
    }

    public String getBANK_NAME() {
        return BANK_NAME;
    }
    public void setBANK_NAME(String bank_name) {
        BANK_NAME = bank_name;
    }

    public String getCNY() {
        return CNY;
    }
    public void setCNY(String cny) {
        CNY = cny;
    }
    public String getID_NO() {
        return ID_NO;
    }
    public void setID_NO(String id_no) {
        ID_NO = id_no;
    }
    public String getID_TYPE() {
        return ID_TYPE;
    }
    public void setID_TYPE(String id_type) {
        ID_TYPE = id_type;
    }
    public String getPAY_STATE() {
        return PAY_STATE;
    }
    public void setPAY_STATE(String pay_state) {
        PAY_STATE = pay_state;
    }
    public String getREMARK() {
        return REMARK;
    }
    public void setREMARK(String remark) {
        REMARK = remark;
    }
    public String getRESERVE() {
        return RESERVE;
    }
    public void setRESERVE(String reserve) {
        RESERVE = reserve;
    }
    public String getSN() {
        return SN;
    }
    public void setSN(String sn) {
        SN = sn;
    }
    public String getSETT_DATE() {
        return SETT_DATE;
    }
    public void setSETT_DATE(String sett_date) {
        SETT_DATE = sett_date;
    }
    public String getMOBILE_NO() {
        return MOBILE_NO;
    }
    public void setMOBILE_NO(String mobile_no) {
        MOBILE_NO = mobile_no;
    }
    public String getEXCHANGE_RATE() {
        return EXCHANGE_RATE;
    }
    public void setEXCHANGE_RATE(String exchange_rate) {
        EXCHANGE_RATE = exchange_rate;
    }

    public String getSETT_AMOUNT() {
        return SETT_AMOUNT;
    }
    public void setSETT_AMOUNT(String sett_amount) {
        SETT_AMOUNT = sett_amount;
    }
    public String getRETURN_URL() {
        return RETURN_URL;
    }
    public void setRETURN_URL(String return_url) {
        RETURN_URL = return_url;
    }
    public String getMER_ORDER_NO() {
        return MER_ORDER_NO;
    }
    public void setMER_ORDER_NO(String mer_order_no) {
        MER_ORDER_NO = mer_order_no;
    }
    public String getTRANS_DESC() {
        return TRANS_DESC;
    }
    public void setTRANS_DESC(String tRANS_DESC) {
        TRANS_DESC = tRANS_DESC;
    }
    public String getQUERY_NO_FLAG() {
        return QUERY_NO_FLAG;
    }
    public void setQUERY_NO_FLAG(String qUERY_NO_FLAG) {
        QUERY_NO_FLAG = qUERY_NO_FLAG;
    }
    public String getMER_SEQ_NO() {
        return MER_SEQ_NO;
    }
    public void setMER_SEQ_NO(String mER_SEQ_NO) {
        MER_SEQ_NO = mER_SEQ_NO;
    }
    public String getSUCCESS_DATE() {
        return SUCCESS_DATE;
    }
    public void setSUCCESS_DATE(String sUCCESS_DATE) {
        SUCCESS_DATE = sUCCESS_DATE;
    }
    public String getSMS_CODE() {
        return SMS_CODE;
    }
    public void setSMS_CODE(String sMS_CODE) {
        SMS_CODE = sMS_CODE;
    }
    public String getDETAIL_CONTENT() {
        return DETAIL_CONTENT;
    }
    public void setDETAIL_CONTENT(String dETAIL_CONTENT) {
        DETAIL_CONTENT = dETAIL_CONTENT;
    }

}
