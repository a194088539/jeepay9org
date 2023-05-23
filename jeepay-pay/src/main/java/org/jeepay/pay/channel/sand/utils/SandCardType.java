package org.jeepay.pay.channel.sand.utils;

public enum SandCardType {
    DEBIT("2"),//贷记卡

    MEMORY("1"),//存储卡

    BLEND("3")//混合模式

    ;

    SandCardType(String cardType) {
        this.cardType = cardType;
    }

    private String cardType;

    public String getType() { return this.cardType; }
}
