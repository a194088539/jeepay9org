package org.jeepay.pay.channel.sand.utils;

public enum SandBank{
    CBC("01050000","中国建设银行"),
    BC("01040000","中国银行"),
    ABC("01030000","中国农业银行"),
    ICBC("01020000","中国工商银行"),
    CMSB("03050000","中国民生银行"),
    CPB("01000000","中国邮政银行"),
    CEB("03030000","中国光大银行"),
    GDB("03060000","广东发展银行"),
    BCS("04012900","中国上海银行"),
    BCB("04031000","中国北京银行"),
    HB("03040000","中国华夏银行")
    ;


    SandBank(String code, String name){
        this.code=code;
        this.name=name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
