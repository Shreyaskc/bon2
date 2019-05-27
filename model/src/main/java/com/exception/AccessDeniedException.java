package com.exception;

import java.util.HashMap;

/**
 * Exception class
 * 
 * @author Shreyas
 */
@SuppressWarnings("serial")
public class AccessDeniedException extends ValidationException {

    /**
     * Default constructor
     */
    public AccessDeniedException() {
	// TODO Auto-generated constructor stub
	super();
    }

    /**
     * Overloaded constructor having the errorCode as the argument
     * 
     * @param errorCode
     *            a constant for a specific error
     */
    public AccessDeniedException(String errorCode, HashMap<String, String> errorConfig, String status, Object params) {
	// TODO Auto-generated constructor stub
	super(errorCode, errorConfig, status, params);
    }

    /**
     * Overloaded constructor having the throwable instance object as the
     * argument
     * 
     * @param throwableObj
     *            throwable instance
     */
    public AccessDeniedException(Throwable throwableObj) {
	// TODO Auto-generated constructor stub
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
    public AccessDeniedException(Throwable throwableObj, String errorCode, HashMap<String, String> errorConfig,
	    String status, Object params) {
	// TODO Auto-generated constructor stub
	super(throwableObj, errorCode, errorConfig, status, params);
    }

    /**
     * Overloaded constructor having the error errorCode and the error code as
     * the arguments
     * 
     * @param errorMessage
     *            message to be displayed to the user
     * @param errorCode
     *            a constant for a specific error
     */
    public AccessDeniedException(String errorMessage, String errorCode, String status, Object params) {
	// TODO Auto-generated constructor stub
	super(errorMessage, errorCode, status, params);
    }
}
