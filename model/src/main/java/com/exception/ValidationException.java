package com.exception;

import java.util.HashMap;

/**
 * Validation exception class
 * 
 * @author Shreyas
 */
@SuppressWarnings("serial")
public class ValidationException extends ExceptionBase {

    /**
     * Default constructor
     */
    public ValidationException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * Overloaded constructor having the errorCode as the argument
     * 
     * @param errorCode
     *            a constant for a specific error
     */
    public ValidationException(String errorCode, HashMap<String, String> errorConfig, String status, Object params) {
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
    public ValidationException(Throwable throwableObj) {
	// TODO Auto-generated constructor stub
	super(throwableObj);
    }

    /**
     * Overloaded constructor having the throwable instance object and the error
     * code as the arguments
     * 
     * @param throwableObj
     *            throwable instance
     * @param message
     *            a constant for a specific error
     */
    public ValidationException(Throwable throwableObj, String message, HashMap<String, String> errorConfig,
	    String status, Object params) {
	// TODO Auto-generated constructor stub
	super(throwableObj, message, errorConfig, status, params);
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
    public ValidationException(String errorMessage, String errorCode, String status, Object params) {
	// TODO Auto-generated constructor stub
	super(errorMessage, errorCode, status, params);
    }
}
