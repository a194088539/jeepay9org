package org.jeepay.common.exception;


import org.jeepay.common.bean.result.PayError;

/**
 * @author  egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 *  </pre>
 */
public class PayErrorException extends RuntimeException  {

    private PayError error;

    public PayErrorException(PayError error) {
        super(error.getString());
        this.error = error;
    }


    public PayError getPayError() {
        return error;
    }
}
