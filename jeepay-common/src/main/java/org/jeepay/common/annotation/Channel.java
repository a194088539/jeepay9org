package org.jeepay.common.annotation;


import org.jeepay.common.api.BasePayConfigStorage;
import org.jeepay.common.api.PayConfigStorage;
import org.jeepay.common.bean.TransactionType;

import java.lang.annotation.*;

/**
 * @Package com.egzosn.pay.common.annotation
 * @Class: Channel.java
 * @Description: 支付渠道注解
 * @Author xxx
 * @Date 2018/12/11 15:31
 * @Version
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Channel {

    ChannelType name();

    Class<? extends PayConfigStorage> config() default BasePayConfigStorage.class ;

    Class<? extends TransactionType> transactionType() default TransactionType.class;
}
