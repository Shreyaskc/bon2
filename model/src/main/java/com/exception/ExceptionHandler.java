package com.exception;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dto.ResponseDTO;

/**
 * The parent exception handler class.
 * 
 * @author Shreyas
 *
 */
public class ExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * exception handler that returns the error message
     * 
     * @param e
     *            Base exception type
     * @return returns the error message
     */
    public static String getMessage(ExceptionBase e, HashMap<String, String> errorConfig) {
	String message = null;
	if (e instanceof SystemException) {
	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
	    }
	    message = message + " - " + e.getErrorMessage();
	} else if (e instanceof InvalidInputException) {
	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
	    }
	    message = message + " - " + e.getErrorMessage();
	} else if (e instanceof AccessDeniedException) {
	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
	    }
	    message = message + " - " + e.getErrorMessage();
	} else if (e instanceof ValidationException) {
	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
	    }
	    message = message + " - " + e.getErrorMessage();
	}
	return message;
    }

    /**
     * method to get the message for a error code
     * 
     * @param errorCode
     *            code for which the message is required.
     * @return the error message.
     */
    public static String getMessage(String errorCode, HashMap<String, String> errorConfig) {
	String message = "";
	if (errorCode != null) {
	    if (errorConfig != null && errorConfig.containsKey(errorCode)) {
		message = errorConfig.get(errorCode);
	    }
	}
	return message;
    }

    /**
     * Gives the service response error message.
     * 
     * @param e
     *            exception object.
     * @return the service response data object.
     */
    public static ResponseDTO getServiceResponseErrorMessage(ExceptionBase e, HashMap<String, String> errorConfig) {
	ResponseDTO response = new ResponseDTO(false);
	String responseStatus = e.getStatus();
	String message = "";

	response.getResponseStatus().setResponseStatus(responseStatus);
	if (e instanceof SystemException) {

	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
	    }

	    response.getResponseStatus().setMessage(message);
	    response.getResponseStatus().setParams(e.getParams());
	} else if (e instanceof InvalidInputException) {

	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		message = "";
		if (errorConfig != null && errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
		response.getResponseStatus().setMessage(message);
		response.getResponseStatus().setParams(e.getParams());
	    }
	} else if (e instanceof AccessDeniedException) {

	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		message = "";
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
		response.getResponseStatus().setMessage(message);
		response.getResponseStatus().setParams(e.getParams());
	    }
	} else if (e instanceof ValidationException) {
	    String errCode = e.getErrorCode();
	    if (errCode != null) {
		message = "";
		if (errorConfig != null && errorConfig.containsKey(errCode)) {
		    message = errorConfig.get(errCode);
		}
		response.getResponseStatus().setMessage(message);
		response.getResponseStatus().setParams(e.getParams());
	    }
	}

	return response;
    }

}
