package org.jeepay.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Package com.ukit.guava
 * @Class: AbstractGuavaCache.java
 * @Description:
 * @Author leo
 * @Date 2018/12/20 0:01
 * @Version
 **/
public abstract class AbstractGuavaCache<K, V> {

    protected final Logger LOGGER       = LoggerFactory.getLogger(AbstractGuavaCache.class);

    private static final int            MAX_SIZE     = 4096;
    public  static final int            EXPIRE_TIME  = 30;
    /** 用于初始化cache的参数及其缺省值 */
    private static final int            DEFAULT_SIZE = 100;

    private int                         maxSize      = MAX_SIZE;

    private int                         expireTime   = EXPIRE_TIME;
    /** 时间单位（秒） */
    private TimeUnit timeUnit     = TimeUnit.SECONDS;
    /** Cache初始化或被重置的时间  */
    private Date resetTime;

    /** 分别记录历史最多缓存个数及时间点*/
    private long                        highestSize  = 0;
    private Date                        highestTime;

    private volatile LoadingCache<K, V> cache;

    public LoadingCache<K, V> getCache() {
        //使用双重校验锁保证只有一个cache实例
        if(cache == null){
            synchronized (this) {
                if(cache == null){
                    //CacheBuilder的构造函数是私有的，只能通过其静态方法ne
                    //wBuilder()来获得CacheBuilder的实例
                    cache = CacheBuilder.newBuilder()
                            //设置缓存容器的初始容量为100
                            .initialCapacity(DEFAULT_SIZE)
                            //缓存数据的最大条目
                            .maximumSize(maxSize)
                            //定时回收:缓存项在给定时间内没有被写访问
                            //（创建或覆盖），则回收。
                            .expireAfterWrite(expireTime, timeUnit)
                            .concurrencyLevel(10)
                            //启用统计->统计缓存的命中率等
                            .recordStats()
                            //设置缓存的移除通知
                            .removalListener((notification)-> {
                                if (LOGGER.isDebugEnabled()){
                                    LOGGER.debug("guava被移除：{}->{}", notification.getKey(), notification.getValue());
                                }
                            })
                            .build(new CacheLoader<K, V>() {
                                @Override
                                public V load(K key) throws Exception {
                                    return fetchData(key);
                                }
                            });
                    this.resetTime = new Date();
                    this.highestTime = new Date();
                    if (LOGGER.isInfoEnabled()){
                        //...
                    }
                }
            }
        }

        return cache;
    }

    /**
     * 根据key从数据库或其他数据源中获取一个value，并被自动保存到缓存中。
     *
     * 改方法是模板方法，子类需要实现
     *
     * @param key
     * @return value,连同key一起被加载到缓存中的。
     */
    protected abstract V fetchData(K key);

    /**
     * 从缓存中获取数据（第一次自动调用fetchData从外部获取数据），并处理异常
     * @param key
     * @return Value
     * @throws ExecutionException
     */
    protected V getValue(K key) throws ExecutionException {
        V result = getCache().get(key);
        if (getCache().size() > highestSize) {
            highestSize = getCache().size();
            highestTime = new Date();
        }
        return result;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    public long getHighestSize() {
        return highestSize;
    }

    public void setHighestSize(long highestSize) {
        this.highestSize = highestSize;
    }

    public Date getHighestTime() {
        return highestTime;
    }

    public void setHighestTime(Date highestTime) {
        this.highestTime = highestTime;
    }
}

