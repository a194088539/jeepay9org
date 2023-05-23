package org.jeepay.common.bean;

/**
 * @Package com.egzosn.pay.common.bean
 * @Class: CardType.java
 * @Description: 银行卡类型
 * @Author leo
 * @Date 2018/12/12 16:26
 * @Version
 **/
public enum  CardType {

    /**
     * 储蓄卡
     */
    DC(1, "储蓄卡"),
    /**
     * 信用卡
     */
    CC(2, "信用卡"),
    /**
     * 准贷记卡
     */
    SCC(3, "准贷记卡"),
    /**
     * 预付费卡
     */
    PC(4, "预付费卡");

    private Integer key;

    private String name;

    CardType(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getValue() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

}
