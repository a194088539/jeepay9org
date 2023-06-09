package org.jeepay.common.bean;

/**
 * 货币类型
 * @author Actinia
 *  <pre>
 * email hayesfu@qq.com
 * create 2017 2017/1/16
 * </pre>
 */
public enum CurType {

    CNY("人民币"),
    USD("美元"),
    HKD("港币"),
    MOP("澳门元"),
    EUR("欧元"),
    TWD("新台币"),
    KRW("韩元"),
    JPY("日元"),
    SGD("新加坡元"),
    AUD("澳大利亚元");
    /**
     * 币种名称
     */
    private String name;
    //索引
    private int index;

    /**
     * 构造函数
     * @param name
     */
    CurType(String name) {
        this.name = name;
    }


}
