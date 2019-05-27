package com.exception;

import java.util.HashMap;

/**
 * System exception class.
 * 
 * @author Shreyas
 */
@SuppressWarnings("serial")
public class SystemException extends ExceptionBase {

    /**
     * Overloaded constructor having the errorCode as the argument
     * 
     * @param errorCode
     *            a constant for a specific error
     */
    public SystemException(String errorCode, HashMap<String, String> errorConfig, String status, Object params) {
	super(errorCode, errorConfig, status, params);
    }

    /**
     * Overloaded constructor having the throwable instance object as the
     * argument
     * 
     * @param throwableObj
     *            throwable instance
     */
    public SystemException(Throwable throwableObj) {
	super(throwableObj);
    }

    /**
     * Overloaded constructor having the throwable instance object and the error
     * code as the arguments
     * 
     * @param throwableObj
     *            throwable instance
     * @param errorCode
     *            a constant for a specific error
     */
    public SystemException(Throwable throwableObj, String errorCode, HashMap<String, String> errorConfig, String status,
	    Object params) {
	super(throwableObj, errorCode, errorConfig, status, params);
    }

    /**
     * Overloaded constructor having the error message and the error code as the
     * arguments
     * 
     * @param errorMessage
     *            message to be displayed to the user
     * @param errorCode
     *            a constant for a specific error
     */
    public SystemException(String errorMessage, String errorCode, String status, Object params) {
	super(errorMessage, errorCode, status, params);
    }
}
