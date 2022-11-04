
package com.webank.weid.demo.exception;

/**
 * Business Exception.
 * @author v_wbgyang
 *
 */
public class BusinessException extends RuntimeException {

    /**
     * Version UID.
     */
    private static final long serialVersionUID = -3606254083553524719L;

    /**
     * constructor.
     * @param message exception message
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * constructor.
     * @param message exception message
     * @param e  exception object
     */
    public BusinessException(String message, Throwable e) {
        super(message, e);
    }
}
