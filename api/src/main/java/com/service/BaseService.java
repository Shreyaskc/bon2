package com.service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.BusinessLayer;
import com.SendMailSSL;
import com.common.ConfigReader;
import com.constants.Constants;
import com.constants.ErrorCodes;
import com.dto.ArtistDTO;
import com.dto.ArtistSearchDTO;
import com.dto.BusinessProductDTO;
import com.dto.MailDTO;
import com.dto.MediaCommentDTO;
import com.dto.MediaDTO;
import com.dto.MediaLibraryPaginatedRequest;
import com.dto.MediaPaginatedRequest;
import com.dto.MediaPostDTO;
import com.dto.MessageDTO;
import com.dto.NearbyPaginatedRequest;
import com.dto.NotificationDTO;
import com.dto.PaginatedRequest;
import com.dto.PlaylistItemPaginatedRequest;
import com.dto.PlaylistMasterDTO;
import com.dto.ProductDTO;
import com.dto.ProductRequestDTO;
import com.dto.RegisterUserDTO;
import com.dto.ResponseDTO;
import com.dto.ScheduledDTO;
import com.dto.SearchArtistDTO;
import com.dto.SearchMediaRequest;
import com.dto.SearchPaginatedRequest;
import com.dto.ShopifyProductDTO;
import com.dto.StationItemPaginatedRequest;
import com.dto.StationMasterDTO;
import com.dto.UserContactRequestDTO;
import com.dto.UserMediaPaginatedRequest;
import com.dto.UserSearchPaginatedRequest;
import com.dto.WishlistDTO;
import com.exception.ExceptionHandler;
import com.exception.SystemException;

/**
 * Base Service
 * 
 * @author Shreyas
 *
 */
@Path("/")
@Produces({ MediaType.APPLICATION_JSON })
public class BaseService {
    private BusinessLayer BL;
    protected String resStr = null;
    protected Object obj = null;
    protected HashMap<String, String> errorMessages;
    protected ResponseDTO responseDTO;
    private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

    public BaseService() throws Exception {
	errorMessages = ConfigReader.getObject().getErrorConfig();
	BL = new BusinessLayer();
	responseDTO = new ResponseDTO();
    }

    protected void initRoutine() throws Exception {
	@SuppressWarnings("unused")
	ConfigReader configReader = ConfigReader.getObject();
    }

    @GET
    @Path("/testService")
    public String testService() throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    String dto = "Works fine...!";
	    obj = (dto);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @GET
    @Path("/getClassStructure")
    public String getClassStructure() throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    RegisterUserDTO dto = new RegisterUserDTO();
	    LinkedList<String> prefList = new LinkedList<String>();
	    prefList.add("1");
	    prefList.add("2");
	    prefList.add("3");
	    prefList.add("4");
	    dto.musicPrefList = prefList;
	    obj = dto;
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @GET
    @Path("/getTestList")
    public String getTestList() throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    obj = BL.getTestList();
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}
	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @GET
    @Path("/getMusicPrefList")
    public String getMusicPrefList() throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    obj = BL.getMusicPrefList();
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserMusicPrefList")
    public String getUserMusicPrefList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.getUserMusicPrefList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/registerUser")
    public String registerUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.registerUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateCategoryForUser")
    public String updateCategoryForUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateCategoryForUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateBusinessUserId")
    public String updateBusinessUserId(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateBusinessUserId(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateBusinessUser")
    public String updateBusinessUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateBusinessUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/sendMessageNotification")
    public String sendMessageNotification(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MessageDTO DTO;
	try {
	    initRoutine();
	    DTO = (MessageDTO) ServiceHelper.buildJsonString(reqStr, MessageDTO.class);
	    obj = BL.sendMessageNotification(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/editBusinessUserId")
    public String editBusinessUserId(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.editBusinessUserId(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateArtistUserId")
    public String updateArtistUserId(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateArtistUserId(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateArtistUser")
    public String updateArtistUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateArtistUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/editArtistUserId")
    public String editArtistUserId(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.editArtistUserId(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateUserToArtist")
    public String updateUserToArtist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateUserToArtist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateNotificationPref")
    public String updateNotificationPref(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateNotificationPref(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/bonMailService")
    public String bonMailService(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MailDTO DTO;
	try {
	    initRoutine();
	    DTO = (MailDTO) ServiceHelper.buildJsonString(reqStr, MailDTO.class);
	    SendMailSSL.sendMail(DTO.email, DTO.subject, DTO.mail);
	    responseDTO.setResponseObject("Mail Sent");
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateUserDetails")
    public String updateUserDetails(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateUserDetails(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}
	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	return resStr;
    }

    @POST
    @Path("/updateUserNotificationToken")
    public String updateUserNotificationToken(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateUserNotificationToken(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}
	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	return resStr;
    }

    @POST
    @Path("/getMediaMetadata")
    public String getMediaMetadata(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPostDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaPostDTO) ServiceHelper.buildJsonString(reqStr, MediaPostDTO.class);
	    obj = BL.getMediaMetadata(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getSpecificMedia")
    public String getSpecificMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPostDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaPostDTO) ServiceHelper.buildJsonString(reqStr, MediaPostDTO.class);
	    obj = BL.getSpecificMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getMediaData")
    public String getMediaData(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPostDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaPostDTO) ServiceHelper.buildJsonString(reqStr, MediaPostDTO.class);
	    obj = BL.getMediaData(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addMediaComment")
    public String addMediaComment(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaCommentDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaCommentDTO) ServiceHelper.buildJsonString(reqStr, MediaCommentDTO.class);
	    obj = BL.addMediaComment(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getMediaComments")
    public String getMediaComments(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (MediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, MediaPaginatedRequest.class);
	    obj = BL.getMediaComments(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteComment")
    public String deleteComment(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaCommentDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaCommentDTO) ServiceHelper.buildJsonString(reqStr, MediaCommentDTO.class);
	    obj = BL.deleteComment(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteBusinessProduct")
    public String deleteBusinessProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	BusinessProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (BusinessProductDTO) ServiceHelper.buildJsonString(reqStr, BusinessProductDTO.class);
	    obj = BL.deleteBusinessProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateBusinessProduct")
    public String updateBusinessProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	BusinessProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (BusinessProductDTO) ServiceHelper.buildJsonString(reqStr, BusinessProductDTO.class);
	    obj = BL.updateBusinessProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addBusinessProduct")
    public String addBusinessProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	BusinessProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (BusinessProductDTO) ServiceHelper.buildJsonString(reqStr, BusinessProductDTO.class);
	    obj = BL.addBusinessProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getBusinessProduct")
    public String getBusinessProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getBusinessProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getBusinessProductById")
    public String getBusinessProductById(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ProductRequestDTO DTO;
	try {
	    initRoutine();
	    DTO = (ProductRequestDTO) ServiceHelper.buildJsonString(reqStr, ProductRequestDTO.class);
	    obj = BL.getBusinessProductById(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/likeMedia")
    public String likeMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaCommentDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaCommentDTO) ServiceHelper.buildJsonString(reqStr, MediaCommentDTO.class);
	    obj = BL.likeMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/sendCustomNotification")
    public String sendCustomNotification(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	NotificationDTO DTO;
	try {
	    initRoutine();
	    DTO = (NotificationDTO) ServiceHelper.buildJsonString(reqStr, NotificationDTO.class);
	    obj = BL.sendCustomNotification(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/createArtist")
    public String createArtist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ArtistDTO DTO;
	try {
	    initRoutine();
	    DTO = (ArtistDTO) ServiceHelper.buildJsonString(reqStr, ArtistDTO.class);
	    obj = BL.createArtist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateArtist")
    public String updateArtist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ArtistDTO DTO;
	try {
	    initRoutine();
	    DTO = (ArtistDTO) ServiceHelper.buildJsonString(reqStr, ArtistDTO.class);
	    obj = BL.updateArtist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteArtist")
    public String deleteArtist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ArtistDTO DTO;
	try {
	    initRoutine();
	    DTO = (ArtistDTO) ServiceHelper.buildJsonString(reqStr, ArtistDTO.class);
	    obj = BL.deleteArtist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/unlikeMedia")
    public String unlikeMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaCommentDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaCommentDTO) ServiceHelper.buildJsonString(reqStr, MediaCommentDTO.class);
	    obj = BL.unlikeMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserContactList")
    public String getUserContactList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserContactRequestDTO DTO;
	try {
	    initRoutine();
	    DTO = (UserContactRequestDTO) ServiceHelper.buildJsonString(reqStr, UserContactRequestDTO.class);
	    obj = BL.getUserContactList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateMusicPref")
    public String updateMusicPref(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateMusicPref(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateUserFollowing")
    public String updateUserFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateUserFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateBusinessFollowing")
    public String updateBusinessFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateBusinessFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateArtistFollowing")
    public String updateArtistFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.updateArtistFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addUserFollowing")
    public String addUserFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.addUserFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addBusinessFollowing")
    public String addBusinessFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.addBusinessFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addUserArtistFollowing")
    public String addUserArtistFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.addUserArtistFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/validateUser")
    public String validateUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.validateUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/loginUser")
    public String loginUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.loginUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/resetPassword")
    public String resetPassword(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.resetPassword(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/forgotPassword")
    public String forgotPassword(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.forgotPassword(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/userIdExists")
    public String userIdExists(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.userIdExists(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/userEmailExists")
    public String userEmailExists(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.userEmailExists(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getExistingSocialMediaUser")
    public String getExistingSocialMediaUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.getExistingSocialMediaUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserFollowingFeedList")
    public String getUserFollowingFeedList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getUserFollowingFeedList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserFeedList")
    public String getUserFeedList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getUserFeedList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getPublicFeed")
    public String getPublicFeed(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserMediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserMediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserMediaPaginatedRequest.class);
	    obj = BL.getPublicFeed(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteUserFollowing")
    public String deleteUserFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.deleteUserFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteBusinessFollowing")
    public String deleteBusinessFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.deleteBusinessFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteArtistFollowing")
    public String deleteArtistFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.deleteArtistFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getTrendingFeed")
    public String getTrendingFeed(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getTrendingFeed(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getNearbyFeed")
    public String getNearbyFeed(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	NearbyPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (NearbyPaginatedRequest) ServiceHelper.buildJsonString(reqStr, NearbyPaginatedRequest.class);
	    obj = BL.getNearbyFeed(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getPlaylistForUser")
    public String getPlaylistForUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getPlaylistForUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getPlaylistForUserFollowing")
    public String getPlaylistForUserFollowing(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getPlaylistForUserFollowing(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/flagMedia")
    public String flagMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaDTO) ServiceHelper.buildJsonString(reqStr, MediaDTO.class);
	    obj = BL.flagMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}
	return resStr;
    }

    @POST
    @Path("/unFlagMedia")
    public String unFlagMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaDTO) ServiceHelper.buildJsonString(reqStr, MediaDTO.class);
	    BL.unFlagMedia(DTO);
	    obj = "admin approval avaiting";
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getPlaylistItems")
    public String getPlaylistItems(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PlaylistItemPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PlaylistItemPaginatedRequest) ServiceHelper.buildJsonString(reqStr,
		    PlaylistItemPaginatedRequest.class);
	    obj = BL.getPlaylistItems(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/loginRegisterSocialMedia")
    public String loginRegisterSocialMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.loginRegisterSocialMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addUserMedia")
    public String addUserMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPostDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaPostDTO) ServiceHelper.buildJsonString(reqStr, MediaPostDTO.class);
	    obj = BL.addUserMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addShopifyProduct")
    public String addShopifyProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ShopifyProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (ShopifyProductDTO) ServiceHelper.buildJsonString(reqStr, ShopifyProductDTO.class);
	    obj = BL.addShopifyProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateShopifyProduct")
    public String updateShopifyProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ShopifyProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (ShopifyProductDTO) ServiceHelper.buildJsonString(reqStr, ShopifyProductDTO.class);
	    obj = BL.updateShopifyProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteShopify")
    public String deleteShopify(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ShopifyProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (ShopifyProductDTO) ServiceHelper.buildJsonString(reqStr, ShopifyProductDTO.class);
	    obj = BL.deleteShopify(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getShopifyByVendor")
    public String getShopifyByVendor(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserSearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserSearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserSearchPaginatedRequest.class);
	    obj = BL.getShopifyByVendor(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchShopifyProduct")
    public String searchShopifyProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserSearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserSearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserSearchPaginatedRequest.class);
	    obj = BL.searchShopifyProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteMedia")
    public String deleteMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPostDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaPostDTO) ServiceHelper.buildJsonString(reqStr, MediaPostDTO.class);
	    obj = BL.deleteMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/editUserMedia")
    public String editUserMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPostDTO DTO;
	try {
	    initRoutine();
	    DTO = (MediaPostDTO) ServiceHelper.buildJsonString(reqStr, MediaPostDTO.class);
	    obj = BL.editUserMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addUserPlaylist")
    public String addUserPlaylist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PlaylistMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (PlaylistMasterDTO) ServiceHelper.buildJsonString(reqStr, PlaylistMasterDTO.class);
	    obj = BL.addUserPlaylist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/copyUserPlaylist")
    public String copyUserPlaylist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PlaylistMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (PlaylistMasterDTO) ServiceHelper.buildJsonString(reqStr, PlaylistMasterDTO.class);
	    obj = BL.copyUserPlaylist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addPlaylistItem")
    public String addPlaylistItem(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PlaylistMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (PlaylistMasterDTO) ServiceHelper.buildJsonString(reqStr, PlaylistMasterDTO.class);
	    obj = BL.addPlaylistItem(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deletePlaylistItem")
    public String deletePlaylistItem(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PlaylistMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (PlaylistMasterDTO) ServiceHelper.buildJsonString(reqStr, PlaylistMasterDTO.class);
	    obj = BL.deletePlaylistItem(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteUserPlaylist")
    public String deleteUserPlaylist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PlaylistMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (PlaylistMasterDTO) ServiceHelper.buildJsonString(reqStr, PlaylistMasterDTO.class);
	    obj = BL.deleteUserPlaylist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addUserStation")
    public String addUserStation(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	StationMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (StationMasterDTO) ServiceHelper.buildJsonString(reqStr, StationMasterDTO.class);
	    obj = BL.addUserStation(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addStationItem")
    public String addStationItem(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	StationMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (StationMasterDTO) ServiceHelper.buildJsonString(reqStr, StationMasterDTO.class);
	    obj = BL.addStationItem(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteUserStation")
    public String deleteUserStation(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	StationMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (StationMasterDTO) ServiceHelper.buildJsonString(reqStr, StationMasterDTO.class);
	    obj = BL.deleteUserStation(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserStation")
    public String getUserStation(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getUserStation(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteStationItem")
    public String deleteStationItem(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	StationMasterDTO DTO;
	try {
	    initRoutine();
	    DTO = (StationMasterDTO) ServiceHelper.buildJsonString(reqStr, StationMasterDTO.class);
	    obj = BL.deleteStationItem(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getStationItems")
    public String getStationItems(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	StationItemPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (StationItemPaginatedRequest) ServiceHelper.buildJsonString(reqStr,
		    StationItemPaginatedRequest.class);
	    obj = BL.getStationItems(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingUserCount")
    public String getFollowingUserCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowingUserCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingBusinessCount")
    public String getFollowingBusinessCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.getFollowingBusinessCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getBusinessFollowerCount")
    public String getBusinessFollowerCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getBusinessFollowerCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowerUserCount")
    public String getFollowerUserCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowerUserCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingArtistCount")
    public String getFollowingArtistCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	RegisterUserDTO DTO;
	try {
	    initRoutine();
	    DTO = (RegisterUserDTO) ServiceHelper.buildJsonString(reqStr, RegisterUserDTO.class);
	    obj = BL.getFollowingArtistCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getArtistFollowerCount")
    public String getArtistFollowerCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ArtistDTO DTO;
	try {
	    initRoutine();
	    DTO = (ArtistDTO) ServiceHelper.buildJsonString(reqStr, ArtistDTO.class);
	    obj = BL.getArtistFollowerCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getArtistFollowers")
    public String getArtistFollowers(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ArtistSearchDTO DTO;
	try {
	    initRoutine();
	    DTO = (ArtistSearchDTO) ServiceHelper.buildJsonString(reqStr, ArtistSearchDTO.class);
	    obj = BL.getArtistFollowers(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingFollowerCount")
    public String getFollowingFollowerCount(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowingFollowerCount(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingUserList")
    public String getFollowingUserList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowingUserList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingBusinessList")
    public String getFollowingBusinessList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowingBusinessList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/suggestFollowUserList")
    public String suggestFollowUserList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.suggestFollowUserList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/suggestFollowBusinessList")
    public String suggestFollowBusinessList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.suggestFollowBusinessList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingArtistList")
    public String getFollowingArtistList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowingArtistList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/suggestFollowArtistList")
    public String suggestFollowArtistList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.suggestFollowArtistList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getActivityList")
    public String getActivityList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getActivityList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowerActivityList")
    public String getFollowerActivityList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowerActivityList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowingActivityList")
    public String getFollowingActivityList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowingActivityList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getFollowerUserList")
    public String getFollowerUserList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getFollowerUserList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getRequestUserDetails")
    public String getRequestUserDetails(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserMediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserMediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserMediaPaginatedRequest.class);
	    obj = BL.getRequestUserDetails(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getRequestUserAdmin")
    public String getRequestUserAdmin(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserSearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserSearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserSearchPaginatedRequest.class);
	    obj = BL.getRequestUserAdmin(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getRequestedUserMedia")
    public String getRequestedUserMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserMediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserMediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserMediaPaginatedRequest.class);
	    obj = BL.getRequestedUserMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/genericDMLExecutor")
    public String genericDMLExecutor(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String query = params.get("data").get(0);
	LOG.info("Request String >>> " + query);

	try {
	    initRoutine();
	    obj = BL.genericDMLExecutor(query);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/genericSqlExecutor")
    @Produces("text/plain")
    public Response genericSqlExecutor(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	try {
	    initRoutine();
	    String query = params.get("data").get(0);
	    LOG.info("Request String >>> " + query);
	    String fileName = BL.genericSqlExecutor(query);
	    File file = new File(fileName);
	    ResponseBuilder response = Response.ok((Object) file);
	    response.header("Content-Disposition", "attachment; filename=\"" + fileName + ".csv\"");
	    return response.build();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}
	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));

	return null;
    }

    @POST
    @Path("/searchMedia")
    public String searchMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, SearchPaginatedRequest.class);
	    obj = BL.searchMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getPostsUserTaggedIn")
    public String getPostsUserTaggedIn(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, SearchPaginatedRequest.class);
	    obj = BL.getPostsUserTaggedIn(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getScheduledContent")
    public String getScheduledContent(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, SearchPaginatedRequest.class);
	    obj = BL.getScheduledContent(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addScheduledContent")
    public String addScheduledContent(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (MediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, MediaPaginatedRequest.class);
	    obj = BL.addScheduledContent(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserScheduledContent")
    public String getUserScheduledContent(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	PaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (PaginatedRequest) ServiceHelper.buildJsonString(reqStr, PaginatedRequest.class);
	    obj = BL.getUserScheduledContent(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteScheduledContent")
    public String deleteScheduledContent(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ScheduledDTO DTO;
	try {
	    initRoutine();
	    DTO = (ScheduledDTO) ServiceHelper.buildJsonString(reqStr, ScheduledDTO.class);
	    obj = BL.deleteScheduledContent(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchCategory")
    public String searchCategory(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserSearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserSearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserSearchPaginatedRequest.class);
	    obj = BL.searchCategory(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchCategoryByKeywords")
    public String searchCategoryByKeywords(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserSearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserSearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserSearchPaginatedRequest.class);
	    obj = BL.searchCategoryByKeywords(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchUserBusinessProduct")
    public String searchUserBusinessProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	NearbyPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (NearbyPaginatedRequest) ServiceHelper.buildJsonString(reqStr, NearbyPaginatedRequest.class);
	    obj = BL.searchUserBusinessProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchStation")
    public String searchStation(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, SearchPaginatedRequest.class);
	    obj = BL.searchStation(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserMedia")
    public String getUserMedia(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, SearchPaginatedRequest.class);
	    obj = BL.getUserMedia(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getUserMediaMetadata")
    public String getUserMediaMetadata(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchMediaRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchMediaRequest) ServiceHelper.buildJsonString(reqStr, SearchMediaRequest.class);
	    obj = BL.getUserMediaMetadata(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getBlockStatus")
    public String getBlockStatus(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserMediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserMediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserMediaPaginatedRequest.class);
	    obj = BL.getBlockStatus(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/blockUser")
    public String blockUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserMediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserMediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserMediaPaginatedRequest.class);
	    obj = BL.blockUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/unBlockUser")
    public String unBlockUser(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	UserMediaPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (UserMediaPaginatedRequest) ServiceHelper.buildJsonString(reqStr, UserMediaPaginatedRequest.class);
	    obj = BL.unBlockUser(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getMediaLibrary")
    public String getMediaLibrary(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	MediaLibraryPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (MediaLibraryPaginatedRequest) ServiceHelper.buildJsonString(reqStr,
		    MediaLibraryPaginatedRequest.class);
	    obj = BL.getMediaLibrary(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchArtist")
    public String searchArtist(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchArtistDTO DTO;
	try {
	    initRoutine();
	    DTO = (SearchArtistDTO) ServiceHelper.buildJsonString(reqStr, SearchArtistDTO.class);
	    obj = BL.searchArtist(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getArtistDetails")
    public String getArtistDetails(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ArtistDTO DTO;
	try {
	    initRoutine();
	    DTO = (ArtistDTO) ServiceHelper.buildJsonString(reqStr, ArtistDTO.class);
	    obj = BL.getArtistDetails(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/searchProduct")
    public String searchProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	SearchPaginatedRequest DTO;
	try {
	    initRoutine();
	    DTO = (SearchPaginatedRequest) ServiceHelper.buildJsonString(reqStr, SearchPaginatedRequest.class);
	    obj = BL.searchProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/createProduct")
    public String createProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (ProductDTO) ServiceHelper.buildJsonString(reqStr, ProductDTO.class);
	    obj = BL.createProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/updateProduct")
    public String updateProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (ProductDTO) ServiceHelper.buildJsonString(reqStr, ProductDTO.class);
	    obj = BL.updateProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteProduct")
    public String deleteProduct(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	ProductDTO DTO;
	try {
	    initRoutine();
	    DTO = (ProductDTO) ServiceHelper.buildJsonString(reqStr, ProductDTO.class);
	    obj = BL.deleteProduct(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @GET
    @Path("/removeArtistAssociation/{" + Constants.USER_ID + "}/{" + Constants.VERIFICATION_TOKEN + "}")
    public String removeArtistAssociation(@PathParam(Constants.USER_ID) String userId,
	    @PathParam(Constants.VERIFICATION_TOKEN) String verificationToken) throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    obj = BL.removeArtistAssociation(userId, verificationToken);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}
	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @GET
    @Path("/rejectFlag/{" + Constants.MEDIA_ID + "}/{" + Constants.VERIFICATION_TOKEN + "}")
    public String rejectFlag(@PathParam(Constants.MEDIA_ID) String mediaId,
	    @PathParam(Constants.VERIFICATION_TOKEN) String verificationToken) throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    obj = BL.rejectFlag(verificationToken, mediaId);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @GET
    @Path("/approveStationPartner/{" + Constants.USER_ID + "}/{" + Constants.VERIFICATION_TOKEN + "}")
    public String approveStationPartner(@PathParam(Constants.USER_ID) String userId,
	    @PathParam(Constants.VERIFICATION_TOKEN) String verificationToken) throws Exception {
	long start = System.currentTimeMillis();

	try {
	    initRoutine();
	    obj = BL.approveStationPartner(verificationToken, userId);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/addProductToWishList")
    public String addProductToWishList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	WishlistDTO DTO;
	try {
	    initRoutine();
	    DTO = (WishlistDTO) ServiceHelper.buildJsonString(reqStr, WishlistDTO.class);
	    obj = BL.addProductToWishList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/getWishList")
    public String getWishList(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	WishlistDTO DTO;
	try {
	    initRoutine();
	    DTO = (WishlistDTO) ServiceHelper.buildJsonString(reqStr, WishlistDTO.class);
	    obj = BL.getWishList(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

    @POST
    @Path("/deleteWishListItem")
    public String deleteWishListItem(MultivaluedMap<String, String> params) throws Exception {
	long start = System.currentTimeMillis();
	String reqStr = params.get("data").get(0);
	LOG.info("Request String >>> " + reqStr);

	WishlistDTO DTO;
	try {
	    initRoutine();
	    DTO = (WishlistDTO) ServiceHelper.buildJsonString(reqStr, WishlistDTO.class);
	    obj = BL.deleteWishListItem(DTO);
	    responseDTO.setResponseObject(obj);
	    responseDTO.setResponseStatus(true);
	    resStr = ServiceHelper.buildJsonString(responseDTO);
	} catch (SystemException e) {
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(e, errorMessages));
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    SystemException ex = new SystemException(e, ErrorCodes.GENERIC_EXCEPTION,
		    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
	    resStr = ServiceHelper.buildJsonString(ExceptionHandler.getServiceResponseErrorMessage(ex, errorMessages));
	    LOG.info(e.getMessage());
	}

	long end = System.currentTimeMillis();
	LOG.info("Total time taken>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  " + (end - start));
	// LOG.info("Response String >>> "+resStr);
	return resStr;
    }

}
