package org.jeepay.pay.util;

import com.google.common.eventbus.AsyncEventBus;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.pay.mq.Mq4PayQuery;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.*;

/**
 * 事件处理器
 * <p>说明:</p>
 * <li></li>
 *
 * @date 2021/10/3 14:22
 */
public class EventBusUtil {
    private static final MyLog log = MyLog.getLog(EventBusUtil.class);
    /**
     * AsyncEventBus
     */
    private static AsyncEventBus eventBus = null;
    /**
     * DelayQueue
     */
    private static DelayQueue<EventItem> delayQueue = null;
    private static class EventBusUtilHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static EventBusUtil instance = new EventBusUtil();
    }
    private EventBusUtil() {
        TaskExecutor taskExecutor = new TaskExecutor() {
            ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(),
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),Executors.defaultThreadFactory());
            @Override
            public void execute(Runnable task) {
                executorService.execute(task);
            }
        };
        eventBus = new AsyncEventBus(taskExecutor);
        delayQueue = new DelayQueue<>();
    }
    public static EventBusUtil getInstance() {
        return EventBusUtilHolder.instance;
    }

    public void register(Object object){
        eventBus.register(object);
    }

    /**
     * 执行事件
     * <li></li>
     * @author duanyong@javacoo.com
     * @param object: 事件对象
     * @return: void
     */
    public void post(Object object){
        eventBus.post(object);
    }
    /**
     * 延迟执行事件
     * <li></li>
     * @author aragom
     * @param object: 事件对象
     * @param time: 延迟时间,单位:毫秒
     * @return: void
     */
    public void post(Object object, long time) {
        log.info("延迟执行事件->入延迟队列:{}",object);
        //入延迟队列
        delayQueue.put(new EventItem(object, time));
        //开启线程
        new Thread(()->execute()).start();
    }
    /**
     *
     * <li></li>
     * @author aragom
     * @return: void
     */
    private void execute(){
        try {
            // 使用DelayQueue的take方法获取当前队列里的元素(take方法是阻塞方法，如果队列里有值则取出，否则一直阻塞)
            eventBus.post(delayQueue.take().getEventObject());
            log.info("延迟执行事件");
        }catch (InterruptedException interruptedException){
            log.info("延迟执行事件异常:",interruptedException);
        }
    }
    /**
     * 卸载事件
     * @param object
     */
    public void unRegister(Object object){
        eventBus.unregister(object);
    }
    /**
     * 事件项
     * <li></li>
     * @author aragom
     */
    class EventItem<T> implements Delayed {
        /** 触发时间:单位 毫秒 */
        private long time;
        /** 事件对象 */
        private T eventObject;

        public EventItem(T eventObject, long time) {
            super();
            // 将传入的时间转换为超时的时刻
            this.time = TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS)
                    + System.nanoTime();
            this.eventObject = eventObject;
        }

        public long getTime() {
            return time;
        }

        public T getEventObject() {
            return eventObject;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            // 剩余时间= 到期时间-当前系统时间，系统一般是纳秒级的，所以这里做一次转换
            return unit.convert(time-System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            // 剩余时间-当前传入的时间= 实际剩余时间（单位纳秒）
            long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
            // 根据剩余时间判断等于0 返回1 不等于0
            // 有可能大于0 有可能小于0  大于0返回1  小于返回-1
            return (d == 0) ? 0 : ((d > 0) ? 1 : -1);
        }

        @Override
        public String toString() {
            return "EventItem{" +
                    "time=" + time +
                    ", eventObject='" + eventObject + '\'' +
                    '}';
        }
    }
}
