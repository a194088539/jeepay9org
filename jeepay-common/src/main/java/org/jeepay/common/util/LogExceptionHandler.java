package org.jeepay.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeepay.common.api.PayErrorExceptionHandler;
import org.jeepay.common.exception.PayErrorException;


/**
 * LogExceptionHandler 日志处理器
 * @author  egan
 * <pre>
 * email egzosn@gmail.com
 * date 2016-6-1 11:28:01
 *
 *
 * source chanjarster/weixin-java-tools
 * </pre>
 */
public class LogExceptionHandler implements PayErrorExceptionHandler {

    protected final Log log = LogFactory.getLog(PayErrorExceptionHandler.class);

    @Override
    public void handle(PayErrorException e) {

        log.error("Error happens", e);

    }

}
