package com.constants;

/**
 * Target group error codes.
 * 
 * @author Shreyas
 *
 */
public interface ErrorCodes {

    String GENERIC_EXCEPTION = "10001";
    String MODEL_EXISTS = "100000";
    String QC_EXISTS = "100001";
    String FAILED_TO_DELETE = "100002";
    String MODEL_DOES_NOT_EXISTS = "100003";
    String USER_ALREADY_EXISTS = "100004";
    String INVALID_CREDENTIALS = "100005";
    String INVALID_USER = "100006";
    String INVALID_REQUEST = "100007";
    String EMAIL_ALREADY_EXISTS = "100008";
    String INVALID_PASSWORD = "100009";
    String INVALID_USER_ID = "100010";
    String USER_ID_ALREADY_EXISTS = "100011";
    String EMPTY_SOCIAL_MEDIA_ID = "100012";
    String EMPTY_LIST = "100013";
    String FALSE_EXCEPTION = "100014";
    String QUERY_SERVICE_EXCEPTION = "100015";
    String PLAYLIST_DOESNT_EXIST = "100016";
    String STATION_DOESNT_EXIST = "100017";
    String INVALID_COORDINATES = "100018";
    String INVALID_STATION_ITEM = "100019";
    String USER_BLOCKED = "100020";
    String EMPTY_SEARCH_STRING = "100021";
    String INSUFFICIENT_VALUES = "100022";
    String INVALID_REQUEST_FORMAT = "100023";
    String EMPTY_CATEGORY = "100024";
    String NOT_SCHEDULED = "100025";
    String ARTIST_LINKED = "100026";
    String INACTIVE = "100027";

    public interface StatusCodes {
	String SUCCESS = "200";
	String FAILURE = "500";
	String BAD_REQUEST = "400";
	String UN_AUTHORIZED = "401";
	String FORBIDDEN = "403";
	String DATA_NOT_FOUND = "410";
	String UNAUTHENTICATED = "504";

    }

}
