package org.jeepay.common.context;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jeepay.common.annotation.Channel;
import org.jeepay.common.annotation.ChannelType;
import org.jeepay.common.api.PayConfigStorage;
import org.jeepay.common.api.PayService;
import org.jeepay.common.bean.PayOrder;
import org.jeepay.common.bean.TransactionType;
import org.jeepay.common.bean.result.PayException;
import org.jeepay.common.exception.PayErrorException;
import org.jeepay.common.util.ConvertUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Package com.egzosn.pay.common.context
 * @Class: SimpleFactoryContext.java
 * @Description: 简单工厂模式（使用Annotation和简单工厂模式的Context类）
 * @Author xxx
 * @Date 2018/12/11 15:43
 * @Version
 **/
public class SimpleFactoryContext {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleFactoryContext.class);
    /**
     * 缓存所有的支付渠道具体实现
     */
    private static Map<ChannelType, HandleHolder> cacheStrategies;
    //private static Map<String, Class> cacheStrategiesConfig;

    /**
     * 支付渠道持有者
     */
    private PayService payService;
    /**
     * 支付渠道配置信息
     */
    private PayConfigStorage payConfigStorage;

    /**
     * 支付交易类型
     */
    private TransactionType transactionType;

    /**
     * 当前的支付业务处理持有者
     */
    private HandleHolder handleHolder;

    /**
     * 在com.egzosn.pay下查找所有带有@Channel注解的类，获取标识的注解元信息
     */
    static {
        cacheStrategies = new ConcurrentHashMap<>();
        Reflections reflections = new Reflections("com.egzosn.pay");
        Set<Class<?>> annotatedClasses =
                reflections.getTypesAnnotatedWith(Channel.class);
        HandleHolder handleHolder = null;
        for(Class<?> clazz : annotatedClasses) {
            Channel channel = clazz.getAnnotation(Channel.class);
            handleHolder = new HandleHolder(channel.name(), clazz, channel.config(), channel.transactionType());
            cacheStrategies.put(channel.name(), handleHolder);
        }
        cacheStrategies = Collections.unmodifiableMap(cacheStrategies);
    }

    private SimpleFactoryContext(ChannelType channelType) {

        if(!cacheStrategies.containsKey(channelType)) {
            LOG.error("Specified PayService name {} does not exist", channelType);
            throw new PayErrorException(new PayException("channelType error", "Specified channelType does not exist"));
        }

        LOG.debug("pay channel is {}", channelType);
        try {
            handleHolder = cacheStrategies.get(channelType);
            Constructor c = handleHolder.getAction().getConstructor(handleHolder.getConfig());
            payService = (PayService) c.newInstance(payConfigStorage);
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Instantiate PayService failed, channelName is {}", channelType, ex);
        } catch (InvocationTargetException | NoSuchMethodException ex) {
            LOG.error("Instantiate PayService failed, channelName is {}", channelType, ex);
        }
    }

    public SimpleFactoryContext(ChannelType channelType, Map payConfigParamMap) {
        this(channelType);
        try {
            this.payConfigStorage = (PayConfigStorage) ConvertUtils.convertMap2Bean(cacheStrategies.get(channelType).getConfig(), payConfigParamMap);
            this.payService.setPayConfigStorage(this.payConfigStorage);
        } catch (IntrospectionException | IllegalAccessException |
                InstantiationException | InvocationTargetException ex) {
            LOG.error("convert payConfigParamMap to payConfigStorage is error", ex);
            ex.printStackTrace();
        }
    }

    public SimpleFactoryContext(ChannelType channelType, String payConfigParam) {
        this(channelType);
        this.payConfigStorage = (PayConfigStorage) JSONObject.parseObject(payConfigParam, handleHolder.getConfig());
        this.payService.setPayConfigStorage(payConfigStorage);
    }

    public Map<String, Object> orderInfo(PayOrder payOrder, String transactionType) {
        parseTransactionType(transactionType);
        payOrder.setTransactionType(this.transactionType);
        isEmpty();
        return payService.orderInfo(payOrder);
    }


    private boolean isEmpty() {
        if(null == payConfigStorage) {
            throw new PayErrorException(new PayException("payConfigStorage", "pay meta info is empty"));
        }
        if(null == transactionType) {
            throw new PayErrorException(new PayException("transactionType", "pay transactionType info is empty"));
        }
        if(null == payService) {
            throw new PayErrorException(new PayException("payService", "pay action info is empty"));
        }
        return true;
    }

    private void parseTransactionType(String transactionType) {
        Class clazz = handleHolder.getTransactionType();
        List<?> list = Lists.newArrayList(clazz.getEnumConstants());
        TransactionType tmp = null;
        for(Object enu : list) {
            tmp = (TransactionType) enu;
            if(transactionType.equals(tmp.getType())) {
                this.transactionType = tmp;
                break;
            }
        }
    }

    /**
     * 支付请求具体的支付实现持有者
     */
    public static class HandleHolder {
        /**
         * 支付实现渠道类型
         */
        private ChannelType name;
        /**
         * 支付渠道真正的实现类
         */
        private Class< ? extends PayService> action;
        /**
         * 支付渠道实现需要的配置类class
         */
        private Class config;
        /**
         * 支付渠道实现类需要的具体的支付交易类型
         */
        private Class transactionType;

        public HandleHolder() {}

        public HandleHolder(ChannelType name, Class action, Class config, Class transactionType) {
            this.name = name;
            this.action = action;
            this.config = config;
            this.transactionType = transactionType;
        }

        public ChannelType getName() {
            return name;
        }

        public void setName(ChannelType name) {
            this.name = name;
        }

        public Class getAction() {
            return action;
        }

        public void setAction(Class action) {
            this.action = action;
        }

        public Class getConfig() {
            return config;
        }

        public void setConfig(Class config) {
            this.config = config;
        }

        public Class getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(Class transactionType) {
            this.transactionType = transactionType;
        }
    }
}
