package com.dto;

/**
 * 
 * @author Shreyas
 *
 */
public class ResponseStatusDTO {
    private String responseStatus;
    private String message;
    private Object params;

    public String getResponseStatus() {
	return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
	this.responseStatus = responseStatus;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public Object getParams() {
	return params;
    }

    public void setParams(Object params) {
	this.params = params;
    }

}
