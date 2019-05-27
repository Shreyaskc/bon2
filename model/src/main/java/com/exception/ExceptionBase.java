package com.exception;

import java.util.HashMap;

/**
 * Base exception class.
 * 
 * @author Shreyas
 */
@SuppressWarnings("serial")
public class ExceptionBase extends Exception {

    private String errorMessage;
    private String errorCode;
    private String status;
    private Object params;
    private Throwable throwable;

    /**
     * Default constructor
     */
    public ExceptionBase() {
	// TODO Auto-generated constructor stub
    }

    /**
     * Overloaded constructor having the errorCode as the argument
     * 
     * @param errorCode
     *            a constant for a specific error
     */
    public ExceptionBase(String errorCode, HashMap<String, String> errorConfig, String status, Object params) {

	super(ExceptionHandler.getMessage(errorCode, errorConfig));
	this.errorMessage = ExceptionHandler.getMessage(errorCode, errorConfig);
	this.errorCode = errorCode;
	this.setParams(params);
	this.setStatus(status);
	// System.out.println("Exception occurred!! TG base exception
	// called!!");
    }

    /**
     * Overloaded constructor having the throwable instance object as the
     * argument
     * 
     * @param throwable
     *            throwable instance
     */
    public ExceptionBase(Throwable throwable) {
	// TODO Auto-generated constructor stub
	super(throwable);
	this.throwable = throwable;
	this.errorMessage = throwable.getMessage();
    }

    /**
     * Overloaded constructor having the throwable instance object and the error
     * code as the arguments
     * 
     * @param throwable
     *            throwable instance
     * @param errorCode
     *            a constant for a specific error
     */
    public ExceptionBase(Throwable throwable, String errorCode, HashMap<String, String> errorConfig, String status,
	    Object params) {
	// TODO Auto-generated constructor stub
	super(errorCode, throwable);
	this.errorCode = errorCode;
	this.throwable = throwable;
	this.setParams(params);
	this.errorMessage = ExceptionHandler.getMessage(errorCode, errorConfig);
	this.setStatus(status);
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
    public ExceptionBase(String errorMessage, String errorCode, String status, Object params) {
	// TODO Auto-generated constructor stub
	this.errorMessage = errorMessage;
	this.errorCode = errorCode;
	this.setParams(params);
	this.setStatus(status);
    }

    /**
     * @return getter that returns the error message data member
     */
    public String getErrorMessage() {
	return errorMessage;
    }

    /**
     * setter that sets the errorMessage data member
     * 
     * @param errorMessage
     *            data member representing the error message
     */
    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    /**
     * @return getter that returns the errorCode data member
     */
    public String getErrorCode() {
	return errorCode;
    }

    /**
     * setter that sets the errorCode data member
     * 
     * @param errorCode
     *            data member representing the errorCode
     */
    public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * @return getter that returns the throwable instance data member
     */
    public Throwable getThrowable() {
	return throwable;
    }

    /**
     * setter that sets the throwable data member
     * 
     * @param throwable
     *            data member representing the throwable instance
     */
    public void setThrowable(Throwable throwable) {
	this.throwable = throwable;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Object getParams() {
	return params;
    }

    public void setParams(Object params) {
	this.params = params;
    }
}