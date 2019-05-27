package com.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.ConfigReader;
import com.constants.ErrorCodes;
import com.exception.SystemException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * 
 * @author Shreyas
 *
 */
public class ServiceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceHelper.class);
    private static final JsonSerializer<LocalDateTime> SER = (dateTime, typeOfSrc, context) -> dateTime == null ? null
	    : new JsonPrimitive(dateTime.toString());

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, SER).create();

    public static String buildJsonString(Object responseDTO) {
	String responseStr = null;
	try {
	    return GSON.toJson(responseDTO);
	} catch (Exception e) {
	    LOG.error("Error : " + e.getMessage());
	    try {
	    } catch (Exception e1) {
		LOG.error("Error : " + e1.getMessage());
		e1.printStackTrace();
	    }
	}
	return responseStr;
    }

    /**
     * Build Json object from the request
     * 
     * @param jsonString
     * @return
     * @throws SystemException
     * @throws Exception
     */
    public static Object buildJsonString(String jsonString, Class cls) throws SystemException, Exception {
	Object requestObject = null;
	try {
	    requestObject = new Gson().fromJson(jsonString, cls);
	} catch (Exception e) {
	    LOG.error("Error : " + e.getMessage());
	    throw new SystemException(ErrorCodes.INVALID_REQUEST_FORMAT, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.BAD_REQUEST, null);
	}
	return requestObject;
    }
}
