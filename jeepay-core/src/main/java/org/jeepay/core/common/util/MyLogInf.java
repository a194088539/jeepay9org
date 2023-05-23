package org.jeepay.core.common.util;

/**
 * @Description:
 * @author aragom qq194088539
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
public abstract interface MyLogInf {

    public abstract void debug(String paramString, Object[] paramArrayOfObject);

    public abstract void info(String paramString, Object[] paramArrayOfObject);

    public abstract void warn(String paramString, Object[] paramArrayOfObject);

    public abstract void error(Throwable paramThrowable, String paramString, Object[] paramArrayOfObject);
}
