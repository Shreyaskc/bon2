package com.dto;

import com.constants.ErrorCodes;

/**
 * Response object sent back to the client in the form of JSON string after
 * serializing (or de serializing)the data.
 * 
 * @author Shreyas
 *
 */
public class ResponseDTO {
    private Object responseObj;
    private ResponseStatusDTO status;
    private String responseStatus;

    /**
     * Default constructor.
     */
    public ResponseDTO() {
	super();
	status = new ResponseStatusDTO();

    }

    /**
     * Parameterized constructor receiving the response status as the argument.
     * 
     * @param responseStatus
     */
    public ResponseDTO(boolean responseStatus) {
	super();
	status = new ResponseStatusDTO();
	if (responseStatus) {
	    this.status.setResponseStatus(ErrorCodes.StatusCodes.SUCCESS);
	    this.responseStatus = this.status.getResponseStatus();
	} else {
	    this.status.setResponseStatus(ErrorCodes.StatusCodes.FAILURE);
	    this.responseStatus = this.status.getResponseStatus();
	}
    }

    public ResponseDTO(String responseStatus) {
	super();
	status = new ResponseStatusDTO();
	this.status.setResponseStatus(responseStatus);
	this.responseStatus = this.status.getResponseStatus();
    }

    /**
     * Method to get the response status.
     * 
     * @return the responseStatus
     */
    public ResponseStatusDTO getResponseStatus() {
	return this.status;
    }

    /**
     * Method to set the response status.
     * 
     * @param responseStatus
     *            the responseStatus to set
     */
    public void setResponseStatus(String responseStatus) {
	this.status.setResponseStatus(responseStatus);
	this.responseStatus = this.status.getResponseStatus();
    }

    /**
     * Overridden method for boolean true false for success and failure.
     * 
     * @param responseStatus
     */
    public void setResponseStatus(boolean responseStatus) {
	if (responseStatus) {
	    this.status.setResponseStatus(ErrorCodes.StatusCodes.SUCCESS);
	    this.responseStatus = this.status.getResponseStatus();
	} else {
	    this.status.setResponseStatus(ErrorCodes.StatusCodes.FAILURE);
	    this.responseStatus = this.status.getResponseStatus();
	}
    }

    public Object getResponseObject() {
	return responseObj;
    }

    public void setResponseObject(Object responseObj) {
	this.responseObj = responseObj;
    }
}
