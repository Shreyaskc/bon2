package com.exception;

import java.util.HashMap;

/**
 * Exception class
 * 
 * @author Shreyas
 */
@SuppressWarnings("serial")
public class InvalidInputException extends SystemException {

    /**
     * Default Constructor
     */

    /**
     * Overloaded constructor having the errorCode as the argument
     * 
     * @param errorCode
     *            a constant for a specific error
     */
    public InvalidInputException(String errorCode, HashMap<String, String> errorConfig, String status, Object params) {
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
    public InvalidInputException(Throwable throwableObj) {
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
    public InvalidInputException(Throwable throwableObj, String errorCode, HashMap<String, String> errorConfig,
	    String status, Object params) {
	// TODO Auto-generated constructor stub
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
    public InvalidInputException(String errorMessage, String errorCode, String status, Object params) {
	// TODO Auto-generated constructor stub
	super(errorMessage, errorCode, status, params);
    }
}
