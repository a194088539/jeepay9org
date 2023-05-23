package org.jeepay.pay.channel.shengfutong;

import org.jeepay.common.bean.Bank;

/**
 * @Package org.jeepay.pay.channel.shengfutong
 * @Class: SftpayBank.java
 * @Description:
 * @Author leo
 * @Date 2019/4/10 19:25
 * @Version
 **/
public enum SftpayBank implements Bank {
    ICBC("中国工商银行", "ICBC"),
    ABC("中国农业银行", "ABC"),
    BOC("中国银行", "BOC"),
    CCB("中国建设银行", "CCB"),
    CIB("兴业银行", "CIB"),
    CMB("招商银行", "CMB"),
    CMBC("中国民生银行", "CMBC"),
    CNCB("中信银行", "CITIC"),
    PSBC("邮政储蓄银行", "PSBC"),
    SPDB("上海浦东发展银行", "SPDB"),
    BCCB("北京银行", "BOB"),
    BOCOM("交通银行", "COMM"),
    BOS("上海银行", "BOSH"),
   // ("", ""),
    ;
    private String bankName;
    private String bankCode;

    SftpayBank(String bankName, String bankCode) {
        this.bankName = bankName;
        this.bankCode = bankCode;
    }

    @Override
    public String getCode() {
        return this.bankCode;
    }

    @Override
    public String getName() {
        return this.bankName;
    }
}
