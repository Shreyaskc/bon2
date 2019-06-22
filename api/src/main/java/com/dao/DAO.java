package com.dao;

import java.io.FileWriter;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.CommonUtils;
import com.common.ConfigReader;
import com.constants.Constants;
import com.constants.ErrorCodes;
import com.constants.SQLConstants;
import com.dao.helper.DAOHelper;
import com.dto.ActivityMasterDTO;
import com.dto.ArtistDTO;
import com.dto.ArtistMediaDTO;
import com.dto.ArtistSearchDTO;
import com.dto.BusinessProductDTO;
import com.dto.FeedResponseDTO;
import com.dto.MediaCommentDTO;
import com.dto.MediaDTO;
import com.dto.MediaLibraryDTO;
import com.dto.MediaLibraryPaginatedRequest;
import com.dto.MediaMetadataDTO;
import com.dto.MediaPaginatedRequest;
import com.dto.MediaPostDTO;
import com.dto.MediaTargetUserDTO;
import com.dto.MusicPrefDto;
import com.dto.NearbyPaginatedRequest;
import com.dto.PaginatedRequest;
import com.dto.PlaylistItemPaginatedRequest;
import com.dto.PlaylistMasterDTO;
import com.dto.ProductDTO;
import com.dto.ProductRequestDTO;
import com.dto.RegisterUserDTO;
import com.dto.ScheduledDTO;
import com.dto.SearchArtistDTO;
import com.dto.SearchMediaRequest;
import com.dto.SearchPaginatedRequest;
import com.dto.SeriesPaginatedRequest;
import com.dto.ShopifyProductDTO;
import com.dto.StationItemPaginatedRequest;
import com.dto.StationMasterDTO;
import com.dto.UserContactRequestDTO;
import com.dto.UserMediaPaginatedRequest;
import com.dto.UserSearchPaginatedRequest;
import com.dto.WishlistDTO;
import com.exception.SystemException;

/**
 * The data access object that interacts with the target group tables for UI
 * services.
 * 
 * @author Shreyas
 * 
 */
public class DAO {
    private static final Logger LOG = LoggerFactory.getLogger(DAO.class);

    private void closeDbConnection(Database db) {
	try {
	    db.closeConnection();
	} catch (Exception e) {
	    System.out.println("Error While Closing connection.");
	}
    }

    public LinkedList<String> getTestList() throws Exception {
	LinkedList<String> testList = new LinkedList<String>();
	Database db = new Database();
	try {
	    LOG.debug("query>>>>" + SQLConstants.GET_TEST_LIST);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add("12");
	    db.executeQuery(SQLConstants.GET_TEST_LIST, paramList);
	    while (db.cRowSet.next()) {
		String item = db.cRowSet.getString("abc");
		testList.add(item);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return testList;
    }

    public LinkedList<MusicPrefDto> getMusicPrefList() throws Exception {
	LinkedList<MusicPrefDto> prefList = new LinkedList<MusicPrefDto>();
	Database db = new Database();
	try {
	    LOG.debug("query>>>>" + SQLConstants.GET_MUSIC_PREF_LIST.toLowerCase());
	    db.executeQuery(SQLConstants.GET_MUSIC_PREF_LIST.toLowerCase(), null);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add("12");
	    while (db.cRowSet.next()) {
		MusicPrefDto dto = new MusicPrefDto();
		dto.prefId = db.cRowSet.getString("MUSIC_PREF_ID");
		dto.prefName = db.cRowSet.getString("MUSIC_PREF_NAME");
		dto.category = db.cRowSet.getString("CATEGORY");
		prefList.add(dto);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return prefList;
    }

    public LinkedList<String> getUserMusicPrefList(String userId, String accessToken) throws Exception {
	if (!validateUser(userId, accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<String> prefList = new LinkedList<String>();
	Database db = new Database();
	try {
	    LOG.debug("query>>>>" + SQLConstants.GET_USER_MUSIC_PREF.toLowerCase());
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    db.executeQuery(SQLConstants.GET_USER_MUSIC_PREF.toLowerCase(), paramList);
	    while (db.cRowSet.next()) {
		String prefId = db.cRowSet.getString("MUSIC_PREF_ID");
		prefList.add(prefId);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return prefList;
    }

    /**
     * Method to get the user details
     * 
     * @param dto
     * @return
     * @throws Exception
     */
    public RegisterUserDTO loginUser(RegisterUserDTO dto, boolean passwordFlag) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!passwordFlag) {
	    if (dto.password == null || dto.password.length() == 0) {
		throw new SystemException(ErrorCodes.INVALID_PASSWORD, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_DETAILS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		// dto = new RegisterUserDTO();
		String password = db.cRowSet.getString("PASSWORD");
		if (!passwordFlag) {
		    if (!password.equals(dto.password)) {
			throw new SystemException(ErrorCodes.INVALID_PASSWORD,
				ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
		    }
		}
		String isActive = db.cRowSet.getString("is_active");
		if (!Constants.Y.equalsIgnoreCase(isActive)) {
		    throw new SystemException(ErrorCodes.INACTIVE, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
		dto = userProduce(db, true);
		if (passwordFlag) {
		    dto.password = password;
		}
		return dto;
	    } else {
		return null;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public RegisterUserDTO getUserDetails(RegisterUserDTO dto) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_DETAILS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		dto = userProduce(db, true);
		return dto;
	    } else {
		return null;
	    }
	} catch (SystemException e) {
	    LOG.error("Exception : " + e.getMessage());
	    throw e;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    private RegisterUserDTO userProduce(Database db, boolean sendAccessToken) throws Exception {
	RegisterUserDTO dto = new RegisterUserDTO();
	dto.userId = db.cRowSet.getString("USER_ID");
	dto.googleId = db.cRowSet.getString("GOOGLE_ID");
	dto.fbId = db.cRowSet.getString("FB_ID");
	if (sendAccessToken) {
	    dto.accessToken = db.cRowSet.getString("ACCESS_TOKEN");
	}
	dto.firstName = db.cRowSet.getString("FIRST_NAME");
	dto.lastName = db.cRowSet.getString("LAST_NAME");
	dto.email = db.cRowSet.getString("EMAIL");
	dto.phone = db.cRowSet.getString("PHONE");
	dto.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
	dto.userType = db.cRowSet.getString("USER_TYPE");
	dto.location = db.cRowSet.getString("LOCATION");
	dto.bio = db.cRowSet.getString("BIO");
	dto.artistReference = db.cRowSet.getString("artist_reference");
	dto.category = db.cRowSet.getString("CATEGORY");
	dto.userType = db.cRowSet.getString("USER_TYPE");
	dto.businessEmail = db.cRowSet.getString("BUSINESS_EMAIL");
	dto.businessAddress = db.cRowSet.getString("BUSINESS_ADDRESS");
	dto.businessName = db.cRowSet.getString("BUSINESS_NAME");
	dto.businessPhone = db.cRowSet.getString("BUSINESS_PHONE");
	dto.businessBio = db.cRowSet.getString("BUSINESS_BIO");
	dto.businessWebsite = db.cRowSet.getString("BUSINESS_WEBSITE");
	dto.businessFacebook = db.cRowSet.getString("BUSINESS_FACEBOOK");
	dto.businessTwitter = db.cRowSet.getString("BUSINESS_TWITTER");
	dto.businessInstagram = db.cRowSet.getString("BUSINESS_INSTAGRAM");
	dto.businessYoutube = db.cRowSet.getString("BUSINESS_YOUTUBE");
	dto.businessProfilePicture = db.cRowSet.getString("BUSINESS_PROFILE_PICTURE");
	dto.keywords = db.cRowSet.getString("KEYWORDS");
	dto.businessUserId = db.cRowSet.getString("BUSINESS_USER_ID");
	dto.artistUserId = db.cRowSet.getString("ARTIST_USER_ID");
	dto.shopifyShopName = db.cRowSet.getString("shopify_shop_name");
	dto.shopifyAccessToken = db.cRowSet.getString("shopify_access_token");
	dto.isStationPartner = db.cRowSet.getString("is_station_partner");
	dto.partnerStationName = db.cRowSet.getString("partner_station_name");
	dto.privacySetting = db.cRowSet.getString("privacy_setting");
	dto.notificationToken = db.cRowSet.getString("NOTIFICATION_TOKEN");
	String pref = db.cRowSet.getString("NOTIFICATION_PREF");
	dto.sendCommentNotification = Constants.N;
	if (CommonUtils.sendCommentNotification(pref)) {
	    dto.sendCommentNotification = Constants.Y;
	}
	dto.sendFollowNotification = Constants.N;
	if (CommonUtils.sendFollowNotification(pref)) {
	    dto.sendFollowNotification = Constants.Y;
	}
	dto.sendLikeNotification = Constants.N;
	if (CommonUtils.sendLikeNotification(pref)) {
	    dto.sendLikeNotification = Constants.Y;
	}
	dto.sendLikeNotification = Constants.N;
	if (CommonUtils.sendLikeNotification(pref)) {
	    dto.sendLikeNotification = Constants.Y;
	}
	dto.sendMessageNotification = Constants.N;
	if (CommonUtils.sendMessageNotification(pref)) {
	    dto.sendMessageNotification = Constants.Y;
	}
	return dto;
    }

    public MediaMetadataDTO getMediaMetadata(MediaPostDTO dto) throws Exception {

	MediaMetadataDTO metadata = new MediaMetadataDTO();

	int likeCount = getUserMediaLikeCount(dto.mediaId, dto.targetUserId);
	if (likeCount > 0) {
	    metadata.userLikeFlag = true;
	} else {
	    metadata.userLikeFlag = false;
	}
	metadata.likeCount = getMediaLikeCount(dto.mediaId);
	metadata.commentCount = getMediaCommentCount(dto.mediaId);
	metadata.loopCount = getMediaLoopCount(dto.mediaId);
	metadata.cameoUsers = getCameoUserList(dto.mediaId);
	return metadata;
    }

    public int getMediaLikeCount(String mediaId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_LIKE_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getUserMediaLikeCount(String mediaId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_MEDIA_LIKE_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    paramList.add(userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public MediaDTO getMediaData(MediaPostDTO dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_DATA.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.mediaId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		MediaDTO media = new MediaDTO();
		media.metadataFilePath = db.cRowSet.getString("METADATA_FILE_PATH");
		return media;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public MediaDTO getMediaDetails(String mediaId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_DETAILS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		MediaDTO media = getMediaFromResultSet(db, null);
		return media;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public int getMediaCommentCount(String mediaId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_COMMENT_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getMediaLoopCount(String mediaId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_LOOP_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public LinkedList<RegisterUserDTO> getCameoUserList(String mediaId) throws Exception {
	Database db = new Database();
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();

	try {
	    String query = SQLConstants.GET_MEDIA_CAMEO_USERS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = new RegisterUserDTO();
		user.userId = db.cRowSet.getString("USER_ID");
		user.firstName = db.cRowSet.getString("FIRST_NAME");
		user.lastName = db.cRowSet.getString("LAST_NAME");
		user.email = db.cRowSet.getString("EMAIL");
		user.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
		user.location = db.cRowSet.getString("LOCATION");
		userList.add(user);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public RegisterUserDTO getRequestUserDetails(UserMediaPaginatedRequest user) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_DETAILS_FOLLOWING.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(user.requestUserId);
	    paramList.add(user.userId);
	    paramList.add(user.requestUserId);
	    paramList.add(user.requestUserId);
	    paramList.add(user.requestUserId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		RegisterUserDTO dto = userProduce(db, false);
		String followingUserId = db.cRowSet.getString("FOLLOWING_USER_ID");
		if (followingUserId != null && followingUserId.length() > 0) {
		    dto.following = true;
		} else {
		    dto.following = false;
		}
		dto.followingCount = db.cRowSet.getString("following_count");
		dto.followerCount = db.cRowSet.getString("follower_count");
		closeDbConnection(db);

		return dto;
	    } else {
		closeDbConnection(db);
		throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public LinkedList<RegisterUserDTO> getRequestUserAdmin(UserSearchPaginatedRequest searchUser) throws Exception {
	if (searchUser.userId == null || searchUser.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(searchUser);
	if (StringUtils.isEmpty(searchUser.searchString)) {
	    throw new SystemException(ErrorCodes.EMPTY_SEARCH_STRING, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	String searchString = searchUser.searchString;
	StringBuilder query = new StringBuilder();
	query.append("select * from user_master where ");
	String[] search = searchString.split("\\s+");
	for (int i = 0; i < search.length; i++) {
	    if (i != 0) {
		query.append(" and ");
	    }
	    String obj = search[i];
	    query.append("( CONCAT_WS(' ',lower(first_name),lower(last_name),lower(user_id),lower(email)) like '%"
		    + obj.toLowerCase() + "%' )");
	}
	query.append(" limit " + (searchUser.startRange - 1) + "," + (searchUser.endRange - searchUser.startRange + 1));
	Database db = new Database();
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	try {
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = new RegisterUserDTO();
		user.userId = db.cRowSet.getString("USER_ID");
		user.firstName = db.cRowSet.getString("FIRST_NAME");
		user.lastName = db.cRowSet.getString("LAST_NAME");
		user.email = db.cRowSet.getString("EMAIL");
		user.bio = db.cRowSet.getString("BIO");
		user.userType = db.cRowSet.getString("USER_TYPE");
		user.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
		user.location = db.cRowSet.getString("LOCATION");
		userList.add(user);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public LinkedList<RegisterUserDTO> searchCategory(UserSearchPaginatedRequest searchUser) throws Exception {
	if (searchUser.userId == null || searchUser.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(searchUser);

	String query = SQLConstants.SEARCH_USER_CATEGORY.toLowerCase();

	query += " limit " + (searchUser.startRange - 1) + "," + (searchUser.endRange - searchUser.startRange + 1);
	Database db = new Database();

	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	try {
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(searchUser.searchString);
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		userList.add(user);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public LinkedList<RegisterUserDTO> searchCategoryByKeywords(UserSearchPaginatedRequest searchUser)
	    throws Exception {
	if (searchUser.userId == null || searchUser.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(searchUser);

	String query = SQLConstants.SEARCH_USER_CATEGORY_KEYWORD_3.toLowerCase();
	boolean isThree = false;
	if (searchUser.searchString.length() > 3) {
	    query = SQLConstants.SEARCH_USER_CATEGORY_KEYWORD.toLowerCase();
	    isThree = true;
	}
	query += " limit " + (searchUser.startRange - 1) + "," + (searchUser.endRange - searchUser.startRange + 1);
	Database db = new Database();

	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	try {
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(searchUser.category);
	    paramList.add(searchUser.searchString);
	    if (isThree) {
		paramList.add(searchUser.category);
		paramList.add(searchUser.searchString);
	    }
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		userList.add(user);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public boolean blockUser(UserMediaPaginatedRequest dto) throws Exception {

	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    String query = SQLConstants.BLOCK_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    params.add(dto.requestUserId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    query = SQLConstants.DELETE_SPECIFIC_USER_FOLLOWING.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    params = new LinkedList<>();
	    params.add(dto.userId);
	    params.add(dto.requestUserId);
	    db.executeUpdate(query, params);

	    query = SQLConstants.DELETE_SPECIFIC_USER_FOLLOWING.toLowerCase();
	    params = new LinkedList<>();
	    LOG.debug("query>>>>" + query);
	    params = new LinkedList<>();
	    params.add(dto.requestUserId);
	    params.add(dto.userId);
	    db.executeUpdate(query, params);
	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean unBlockUser(UserMediaPaginatedRequest dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.UN_BLOCK_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    params.add(dto.requestUserId);
	    db.executeUpdate(query, params);
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean getBlockStatus(String userId, String requestUserId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_BLOCKED_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    paramList.add(requestUserId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		closeDbConnection(db);
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public MediaTargetUserDTO getMediaTargetUser(String userId, String mediaId) throws Exception {
	Database db = new Database();
	MediaTargetUserDTO media = new MediaTargetUserDTO();
	try {
	    String query = SQLConstants.GET_MEDIA_TARGET_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(mediaId);
	    paramList.add(userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		media.mUserId = db.cRowSet.getString("m_user_id");
		media.mFirstName = db.cRowSet.getString("m_fname");
		media.mLastName = db.cRowSet.getString("m_lname");
		media.mEmail = db.cRowSet.getString("email");
		media.cFirstName = db.cRowSet.getString("c_fname");
		media.cLastName = db.cRowSet.getString("c_lname");
		media.fileType = db.cRowSet.getString("file_type");
		media.title = db.cRowSet.getString("TITLE");
		media.notificationToken = db.cRowSet.getString("NOTIFICATION_TOKEN");
		String pref = db.cRowSet.getString("NOTIFICATION_PREF");
		media.sendCommentNotification = CommonUtils.sendCommentNotification(pref);
		media.sendFollowNotification = CommonUtils.sendFollowNotification(pref);
		media.sendLikeNotification = CommonUtils.sendLikeNotification(pref);
		media.sendMessageNotification = CommonUtils.sendMessageNotification(pref);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return media;
    }

    public MediaTargetUserDTO getTargetUser(String userId, String toUserId) throws Exception {
	Database db = new Database();
	MediaTargetUserDTO media = new MediaTargetUserDTO();
	try {
	    String query = SQLConstants.GET_TARGET_USER_NOTIFICATION.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    paramList.add(toUserId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		media.mFirstName = db.cRowSet.getString("first_name");
		media.mLastName = db.cRowSet.getString("last_name");
		media.mEmail = db.cRowSet.getString("email");
		media.notificationToken = db.cRowSet.getString("NOTIFICATION_TOKEN");
		String pref = db.cRowSet.getString("NOTIFICATION_PREF");
		media.sendCommentNotification = CommonUtils.sendCommentNotification(pref);
		media.sendFollowNotification = CommonUtils.sendFollowNotification(pref);
		media.sendLikeNotification = CommonUtils.sendLikeNotification(pref);
		media.sendMessageNotification = CommonUtils.sendMessageNotification(pref);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return media;
    }

    public MediaTargetUserDTO getUser(String userId) throws Exception {
	Database db = new Database();
	MediaTargetUserDTO media = new MediaTargetUserDTO();
	try {
	    String query = SQLConstants.GET_TARGET_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		media.mUserId = db.cRowSet.getString("m_user_id");
		media.mFirstName = db.cRowSet.getString("m_fname");
		media.mLastName = db.cRowSet.getString("m_lname");
		media.mEmail = db.cRowSet.getString("email");
		media.notificationToken = db.cRowSet.getString("NOTIFICATION_TOKEN");
		String pref = db.cRowSet.getString("NOTIFICATION_PREF");
		media.sendCommentNotification = CommonUtils.sendCommentNotification(pref);
		media.sendFollowNotification = CommonUtils.sendFollowNotification(pref);
		media.sendLikeNotification = CommonUtils.sendLikeNotification(pref);
		media.sendMessageNotification = CommonUtils.sendMessageNotification(pref);

	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return media;
    }

    public boolean getUserBlockStatus(String userId, String requestUserId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_BLOCKED_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    paramList.add(requestUserId);
	    paramList.add(requestUserId);
	    paramList.add(userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    /**
     * Method to get the user details
     * 
     * @param dto
     * @return
     * @throws Exception
     */
    public RegisterUserDTO loginSocialMediaUser(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_SOCIAL_MEDIA_USER_DETAILS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.googleId);
	    paramList.add(dto.fbId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		// dto = new RegisterUserDTO();
		// USER_ID,PASSWORD,ACCESS_TOKEN,LAST_LOGIN,ACCESS_TOKEN_REFRESH_TIME,FIRST_NAME,LAST_NAME,EMAIL,PHONE,PROFILE_PICTURE,USER_TYPE,LOCATION
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.googleId = db.cRowSet.getString("GOOGLE_ID");
		dto.fbId = db.cRowSet.getString("FB_ID");
		dto.accessToken = db.cRowSet.getString("ACCESS_TOKEN");
		dto.firstName = db.cRowSet.getString("FIRST_NAME");
		dto.lastName = db.cRowSet.getString("LAST_NAME");
		dto.email = db.cRowSet.getString("EMAIL");
		dto.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
		dto.userType = db.cRowSet.getString("USER_TYPE");
		dto.location = db.cRowSet.getString("LOCATION");
		return dto;
	    } else {
		return null;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public RegisterUserDTO getUserDetailsForEmail(RegisterUserDTO regDto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_SOCIAL_MEDIA_USER_FROM_EMAIL.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(regDto.email);
	    RegisterUserDTO dto = new RegisterUserDTO();
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.googleId = db.cRowSet.getString("GOOGLE_ID");
		dto.fbId = db.cRowSet.getString("FB_ID");
		dto.accessToken = db.cRowSet.getString("ACCESS_TOKEN");
		dto.firstName = db.cRowSet.getString("FIRST_NAME");
		dto.lastName = db.cRowSet.getString("LAST_NAME");
		dto.email = db.cRowSet.getString("EMAIL");
		dto.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
		dto.userType = db.cRowSet.getString("USER_TYPE");
		dto.location = db.cRowSet.getString("LOCATION");
		return dto;
	    } else {
		return null;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    /**
     * Method to get the user details
     * 
     * @param dto
     * @return
     * @throws Exception
     */
    public RegisterUserDTO loginRegisterSocialMedia(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	try {
	    String query = SQLConstants.GET_USER_DETAILS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		// dto = new RegisterUserDTO();
		String password = db.cRowSet.getString("PASSWORD");
		if (!password.equals(dto.password)) {
		    throw new SystemException(ErrorCodes.INVALID_PASSWORD, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
		// USER_ID,PASSWORD,ACCESS_TOKEN,LAST_LOGIN,ACCESS_TOKEN_REFRESH_TIME,FIRST_NAME,LAST_NAME,EMAIL,PHONE,PROFILE_PICTURE,USER_TYPE,LOCATION
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.accessToken = db.cRowSet.getString("ACCESS_TOKEN");
		dto.firstName = db.cRowSet.getString("FIRST_NAME");
		dto.lastName = db.cRowSet.getString("LAST_NAME");
		dto.email = db.cRowSet.getString("EMAIL");
		dto.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
		dto.userType = db.cRowSet.getString("USER_TYPE");
		dto.location = db.cRowSet.getString("LOCATION");

		return dto;
	    } else {
		throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean userEmailExists(RegisterUserDTO dto) throws Exception {
	RegisterUserDTO emailDto = getUserDetailsForEmail(dto);
	if (emailDto == null) {
	    return false;
	}
	return true;
    }

    /**
     * Method to check if the email already exists.
     * 
     * @param email
     * @return
     * @throws Exception
     * @throws Exception
     */
    public boolean userIdExists(String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.USER_ID_EXISTS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean validateUser(String userId, String accessToken) throws Exception {
	Database db = new Database();
	try {
	    LOG.debug("query>>>>" + SQLConstants.VALIDATE_USER.toLowerCase());
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    paramList.add(accessToken);
	    db.executeQuery(SQLConstants.VALIDATE_USER.toLowerCase(), paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public RegisterUserDTO getUserDetailsWithToken(String userId, String accessToken) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.VALIDATE_USER_WITH_TOKEN.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId);
	    paramList.add(accessToken);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return userProduce(db, true);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public boolean updateMusicPref(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_USER_MUSIC_PREF.toLowerCase();
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    db.executeUpdate(query, params);

	    LinkedList<String> musicPrefList = dto.musicPrefList;
	    if (musicPrefList == null || musicPrefList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String prefId : musicPrefList) {
		params = new LinkedList<>();
		query = SQLConstants.INSERT_USER_MUSIC_PREF.toLowerCase();
		params.add(dto.userId);
		params.add(prefId);
		db.executeUpdate(query, params);
	    }
	    db.commit();
	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    /**
     * Method to check if the User Following already exists.
     * 
     * @param email
     * @return
     * @throws Exception
     * @throws Exception
     */
    public boolean userFollowingExists(String userId, String followingUserId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.USER_FOLLOWING_EXISTS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId.toLowerCase());
	    paramList.add(followingUserId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error making a rollback");
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean artistFollowingExists(String userId, String followingArtistId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.ARTIST_FOLLOWING_EXISTS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId.toLowerCase());
	    paramList.add(followingArtistId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error making a rollback");
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean deleteUserFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (followingUserIdList == null || followingUserIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String followingId : followingUserIdList) {
		String query = SQLConstants.DELETE_SPECIFIC_USER_FOLLOWING.toLowerCase();
		LOG.debug("query>>>>" + query);
		LinkedList<Object> params = new LinkedList<>();

		params.add(dto.userId);
		params.add(followingId);
		db.executeUpdate(query, params);
	    }
	    db.commit();

	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		closeDbConnection(db);
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean deleteBusinessFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (followingUserIdList == null || followingUserIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String followingId : followingUserIdList) {
		String query = SQLConstants.DELETE_SPECIFIC_BUSINESS_FOLLOWING.toLowerCase();
		LOG.debug("query>>>>" + query);
		LinkedList<Object> params = new LinkedList<>();

		params.add(dto.userId);
		params.add(followingId);
		db.executeUpdate(query, params);
	    }
	    db.commit();

	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		closeDbConnection(db);
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean deleteArtistFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    LinkedList<String> followingArtistIdList = dto.followingArtistIdList;
	    if (followingArtistIdList == null || followingArtistIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String followingId : followingArtistIdList) {
		String query = SQLConstants.DELETE_SPECIFIC_ARTIST_FOLLOWING.toLowerCase();
		LOG.debug("query>>>>" + query);
		LinkedList<Object> params = new LinkedList<>();

		params.add(dto.userId);
		params.add(followingId);
		db.executeUpdate(query, params);
	    }
	    db.commit();
	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		closeDbConnection(db);
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean updateUserFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_USER_FOLLOWING.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    db.executeUpdate(query, params);

	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (followingUserIdList == null || followingUserIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    for (String followingId : followingUserIdList) {
		insertUserFollowing(followingId, dto.userId, db);
	    }
	    db.commit();
	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		closeDbConnection(db);
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}

	return true;
    }

    public boolean updateBusinessFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_BUSINESS_FOLLOWING.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    db.executeUpdate(query, params);

	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (followingUserIdList == null || followingUserIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    for (String followingId : followingUserIdList) {
		insertBusinessFollowing(followingId, dto.userId, db);
	    }
	    db.commit();
	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		closeDbConnection(db);
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}

	return true;
    }

    public boolean updateArtistFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_ARTIST_FOLLOWING.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    db.executeUpdate(query, params);

	    LinkedList<String> followingArtistIdList = dto.followingArtistIdList;
	    if (followingArtistIdList == null || followingArtistIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    for (String followingId : followingArtistIdList) {
		insertArtistFollowing(followingId, dto.userId, db);
	    }
	    db.commit();
	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		closeDbConnection(db);
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}

	return true;
    }

    public boolean addUserFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();

	db.setAutoCommitFalse();
	try {

	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (followingUserIdList == null || followingUserIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String followingId : followingUserIdList) {
		boolean userFollowingExists = userFollowingExists(dto.userId, followingId);
		if (!userFollowingExists) {
		    insertUserFollowing(followingId, dto.userId, db);
		}
	    }

	    db.commit();

	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}

	return true;
    }

    public boolean addBusinessFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();

	db.setAutoCommitFalse();
	try {

	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (followingUserIdList == null || followingUserIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String followingId : followingUserIdList) {
		boolean userFollowingExists = userFollowingExists(dto.userId, followingId);
		if (!userFollowingExists) {
		    insertBusinessFollowing(followingId, dto.userId, db);
		}
	    }

	    db.commit();

	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}

	return true;
    }

    public boolean addUserArtistFollowing(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    LinkedList<String> followingArtistIdList = dto.followingArtistIdList;
	    if (followingArtistIdList == null || followingArtistIdList.size() == 0) {
		throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    for (String followingId : followingArtistIdList) {
		boolean userFollowingExists = artistFollowingExists(dto.userId, followingId);
		if (!userFollowingExists) {
		    insertArtistFollowing(followingId, dto.userId, db);
		}
	    }
	    db.commit();
	    closeDbConnection(db);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}

	return true;
    }

    public void insertUserFollowing(String followingId, String userId, Database db) throws Exception {
	boolean isBlocked = getBlockStatus(userId, followingId);
	if (isBlocked) {
	    // throw new SystemException(ErrorCodes.USER_BLOCKED);
	    return;
	}
	String query = SQLConstants.INSERT_USER_FOLLOWING.toLowerCase();
	LinkedList<Object> params = new LinkedList<>();
	params.add(userId);
	params.add(followingId);
	params.add(Constants.REGULAR_PRIVILEGE);
	db.executeUpdate(query, params);
    }

    public void insertBusinessFollowing(String followingId, String userId, Database db) throws Exception {
	boolean isBlocked = getBlockStatus(userId, followingId);
	if (isBlocked) {
	    // throw new SystemException(ErrorCodes.USER_BLOCKED);
	    return;
	}
	String query = SQLConstants.INSERT_BUSINESS_FOLLOWING.toLowerCase();
	LinkedList<Object> params = new LinkedList<>();
	params.add(userId);
	params.add(followingId);
	params.add(Constants.REGULAR_PRIVILEGE);
	db.executeUpdate(query, params);
    }

    public void insertArtistFollowing(String followingId, String userId, Database db) throws Exception {
	String query = SQLConstants.INSERT_ARTIST_FOLLOWING.toLowerCase();
	LinkedList<Object> params = new LinkedList<>();
	params.add(userId);
	params.add(followingId);
	params.add(Constants.REGULAR_PRIVILEGE);
	db.executeUpdate(query, params);
    }

    public RegisterUserDTO registerUser(RegisterUserDTO dto, boolean isSocial) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!isSocial && (dto.password == null || dto.password.length() == 0)) {
	    throw new SystemException(ErrorCodes.INVALID_PASSWORD, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (userIdExists(dto.userId)) {
	    throw new SystemException(ErrorCodes.USER_ID_ALREADY_EXISTS, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (userEmailExists(dto)) {
	    throw new SystemException(ErrorCodes.EMAIL_ALREADY_EXISTS, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();

	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.INSERT_USER_MASTER.toLowerCase();
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    params.add(dto.password);
	    params.add(dto.accessToken);
	    params.add(dto.firstName);
	    params.add(dto.lastName);
	    params.add(dto.email);
	    params.add(dto.phone);
	    params.add(dto.profilePicture);
	    params.add(dto.userType);
	    params.add(dto.location);
	    params.add(dto.googleId);
	    params.add(dto.fbId);
	    params.add(dto.category);
	    params.add(dto.businessUserId);
	    params.add(dto.artistUserId);
	    params.add(dto.businessEmail);
	    params.add(dto.businessAddress);
	    params.add(dto.businessName);
	    params.add(dto.businessPhone);
	    params.add(dto.businessBio);
	    params.add(dto.businessWebsite);
	    params.add(dto.businessFacebook);
	    params.add(dto.businessYoutube);
	    params.add(dto.businessTwitter);
	    params.add(dto.businessInstagram);
	    params.add(dto.businessProfilePicture);
	    params.add(dto.keywords);
	    params.add(dto.artistReference);
	    params.add(dto.shopifyShopName);
	    params.add(dto.shopifyAccessToken);
	    params.add(dto.isStationPartner);
	    params.add(dto.partnerStationName);
	    params.add(dto.isActive);
	    params.add(dto.privacySetting);
	    db.executeUpdate(query, params);

	    LinkedList<String> musicPrefList = dto.musicPrefList;
	    if (dto.musicPrefList != null && dto.musicPrefList.size() > 0) {
		for (String prefId : musicPrefList) {
		    query = SQLConstants.INSERT_USER_MUSIC_PREF.toLowerCase();
		    params = new LinkedList<>();
		    params.add(dto.userId);
		    params.add(prefId);
		    db.executeUpdate(query, params);
		}
	    }
	    LinkedList<String> followingUserIdList = dto.followingUserIdList;
	    if (dto.followingUserIdList != null && dto.followingUserIdList.size() > 0) {
		for (String followingId : followingUserIdList) {
		    insertUserFollowing(followingId, dto.userId, db);
		}
	    }
	    db.commit();
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	String userId = dto.userId;
	String accessToken = dto.accessToken;
	dto = new RegisterUserDTO();
	dto.userId = userId;
	dto.accessToken = accessToken;
	return dto;
    }

    public String insertBusinessProduct(BusinessProductDTO dto, Database db, String userId) throws Exception {
	try {
	    String query = SQLConstants.CREATE_BUSINESS_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    String productId = DAOHelper.guidGenerator(Constants.BUSINESS_PRODUCT, "");
	    params.add(productId);
	    params.add(userId);
	    params.add(dto.productName);
	    params.add(dto.brandName);
	    params.add(dto.website);
	    params.add(dto.picture);
	    params.add(dto.thumbnail);
	    params.add(dto.keywords);
	    params.add(dto.tags);
	    params.add(dto.description);
	    params.add(dto.url);
	    params.add(dto.latitude);
	    params.add(dto.longitude);
	    params.add(dto.category);
	    params.add(dto.sku);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return productId;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error(e.getMessage());
	    return null;
	}
	return null;

    }

    public boolean updateUserDetails(RegisterUserDTO dto) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	RegisterUserDTO user = loginUser(dto, true);
	Database db = new Database();

	try {

	    String query = SQLConstants.UPDATE_USER_DETAILS;
	    if (!StringUtils.isEmpty(dto.password)) {
		user.password = dto.password;
	    }
	    if (!StringUtils.isEmpty(dto.firstName)) {
		user.firstName = dto.firstName;
	    }
	    if (!StringUtils.isEmpty(dto.lastName)) {
		user.lastName = dto.lastName;
	    }

	    if (!StringUtils.isEmpty(dto.profilePicture)) {
		user.profilePicture = dto.profilePicture;
	    }

	    if (!StringUtils.isEmpty(dto.phone)) {
		user.phone = dto.phone;
	    }

	    if (!StringUtils.isEmpty(dto.location)) {
		user.location = dto.location;
	    }

	    if (!StringUtils.isEmpty(dto.bio)) {
		user.bio = dto.bio;
	    }

	    if (StringUtils.isEmpty(dto.email)) {
		RegisterUserDTO emailDto = getUserDetailsForEmail(dto);
		if (emailDto == null) {
		    user.email = dto.email;
		} else if (!dto.userId.equalsIgnoreCase(emailDto.userId)) {
		    throw new SystemException(ErrorCodes.EMAIL_ALREADY_EXISTS,
			    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
		}
	    }

	    LinkedList<Object> params = new LinkedList<>();

	    params.add(user.password);
	    params.add(user.firstName);
	    params.add(user.lastName);
	    params.add(user.email);
	    params.add(dto.privacySetting);
	    params.add(user.profilePicture);
	    params.add(user.phone);
	    params.add(user.location);
	    params.add(user.bio);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		LOG.error("The transaction was successfully executed");
		return true;
	    } else {
		return false;
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public boolean updateUserNotificationToken(RegisterUserDTO dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.UPDATE_USER_NOTIFICATION_TOKEN;
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.notificationToken);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		LOG.error("The transaction was successfully executed");
		return true;
	    } else {
		return false;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public RegisterUserDTO updateSocialIdForUser(RegisterUserDTO dto) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {

	    String query = "";
	    String param = "";
	    if (dto.googleId != null && dto.googleId.length() > 0) {
		query = SQLConstants.UPDATE_GOOGLE_ID_FOR_USER.toLowerCase();
		param = dto.googleId;
	    } else {
		query = SQLConstants.UPDATE_FB_ID_FOR_USER.toLowerCase();
		param = dto.fbId;
	    }
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(param);
	    params.add(dto.userId);
	    db.executeUpdate(query, params);
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	String userId = dto.userId;
	String accessToken = dto.accessToken;
	dto = new RegisterUserDTO();
	dto.userId = userId;
	dto.accessToken = accessToken;
	return dto;
    }

    public boolean updateBusinessProduct(BusinessProductDTO dto) throws Exception {
	Database db = new Database();

	try {
	    String query = SQLConstants.UPDATE_BUSINESS_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.productName);
	    params.add(dto.brandName);
	    params.add(dto.website);
	    params.add(dto.picture);
	    params.add(dto.thumbnail);
	    params.add(dto.keywords);
	    params.add(dto.tags);
	    params.add(dto.description);
	    params.add(dto.url);
	    params.add(dto.latitude);
	    params.add(dto.longitude);
	    params.add(dto.category);
	    params.add(dto.sku);
	    params.add(dto.productId);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;

    }

    public boolean updateBusinessUser(RegisterUserDTO dto) throws Exception {
	Database db = new Database();

	try {
	    String query = SQLConstants.UPDATE_BUSINESS_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.category);
	    params.add(dto.businessEmail);
	    params.add(dto.businessAddress);
	    params.add(dto.businessName);
	    params.add(dto.businessPhone);
	    params.add(dto.businessBio);
	    params.add(dto.businessWebsite);
	    params.add(dto.businessFacebook);
	    params.add(dto.businessYoutube);
	    params.add(dto.businessTwitter);
	    params.add(dto.businessInstagram);
	    params.add(dto.businessProfilePicture);
	    params.add(dto.shopifyShopName);
	    params.add(dto.shopifyAccessToken);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;

    }

    public boolean deleteBusinessProduct(String userId, String productId) throws Exception {
	Database db = new Database();
	try {

	    String query = SQLConstants.DELETE_SPECIFIC_BUSINESS_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(userId);
	    params.add(productId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public void validateStartEndRange(PaginatedRequest requestDTO) throws Exception {
	if (requestDTO.startRange <= 0 || requestDTO.startRange >= requestDTO.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
    }

    public LinkedList<BusinessProductDTO> getBusinessProduct(PaginatedRequest requestDTO) throws Exception {
	validateStartEndRange(requestDTO);
	LinkedList<BusinessProductDTO> responseList = new LinkedList<BusinessProductDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_BUSINESS_PRODUCT.toLowerCase();
	    query += " order by created_date limit " + (requestDTO.startRange - 1) + ","
		    + (requestDTO.endRange - requestDTO.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		BusinessProductDTO productDTO = businessProductProduce(db);
		responseList.add(productDTO);
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return responseList;
    }

    public BusinessProductDTO getBusinessProductById(ProductRequestDTO requestDTO) throws Exception {
	LinkedList<BusinessProductDTO> responseList = new LinkedList<BusinessProductDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_BUSINESS_PRODUCT_BY_ID.toLowerCase();

	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.productId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		BusinessProductDTO productDTO = businessProductProduce(db);
		return (productDTO);
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public LinkedList<BusinessProductDTO> searchUserBusinessProduct3(NearbyPaginatedRequest searchUser)
	    throws Exception {
	if (searchUser.userId == null || searchUser.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(searchUser);

	String query = SQLConstants.SEARCH_USER_BUSINESS_PRODUCT_DIST_3.toLowerCase();

	boolean distFlag = true;
	if (StringUtils.isEmpty(searchUser.latitude) || StringUtils.isEmpty(searchUser.longitude)) {
	    query = SQLConstants.SEARCH_USER_BUSINESS_PRODUCT_3.toLowerCase();
	    distFlag = false;
	}
	query += " limit " + (searchUser.startRange - 1) + "," + (searchUser.endRange - searchUser.startRange + 1);
	Database db = new Database();
	LinkedList<BusinessProductDTO> productList = new LinkedList<BusinessProductDTO>();
	try {

	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    if (distFlag) {
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.longitude);
	    }
	    paramList.add(searchUser.searchString);
	    if (distFlag) {
		String distance = ConfigReader.getObject().getAppConfig(Constants.BUSINESS_PRODUCT_DISTANCE);
		paramList.add(distance);
	    }

	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		BusinessProductDTO product = businessProductProduce(db);
		if (distFlag) {
		    product.distance = db.cRowSet.getString("DIST");
		}
		productList.add(product);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return productList;
    }

    public LinkedList<BusinessProductDTO> searchUserBusinessProduct(NearbyPaginatedRequest searchUser)
	    throws Exception {
	if (searchUser.userId == null || searchUser.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(searchUser);

	String query = SQLConstants.SEARCH_USER_BUSINESS_PRODUCT_DIST.toLowerCase();
	boolean distFlag = true;
	if (StringUtils.isEmpty(searchUser.latitude) || StringUtils.isEmpty(searchUser.longitude)) {
	    query = SQLConstants.SEARCH_USER_BUSINESS_PRODUCT.toLowerCase();
	    distFlag = false;
	}
	query += " limit " + (searchUser.startRange - 1) + "," + (searchUser.endRange - searchUser.startRange + 1);
	Database db = new Database();
	LinkedList<BusinessProductDTO> productList = new LinkedList<BusinessProductDTO>();
	try {
	    String distance = ConfigReader.getObject().getAppConfig(Constants.BUSINESS_PRODUCT_DISTANCE);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    if (distFlag) {
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.longitude);
	    }
	    paramList.add(searchUser.searchString);
	    paramList.add(searchUser.category);
	    if (distFlag) {
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.longitude);
	    }
	    paramList.add(searchUser.searchString);
	    paramList.add(searchUser.category);
	    if (distFlag) {
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.longitude);
	    }
	    paramList.add(searchUser.searchString);
	    paramList.add(searchUser.category);
	    if (distFlag) {
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.longitude);
	    }
	    paramList.add(searchUser.searchString);
	    paramList.add(searchUser.category);
	    if (distFlag) {
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.latitude);
		paramList.add(searchUser.longitude);
	    }
	    paramList.add(searchUser.searchString);
	    paramList.add(searchUser.category);

	    if (distFlag) {
		paramList.add(distance);
	    }

	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		BusinessProductDTO product = businessProductProduce(db);
		if (distFlag) {
		    product.distance = db.cRowSet.getString("DIST");
		}
		productList.add(product);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return productList;
    }

    private BusinessProductDTO businessProductProduce(Database db) throws Exception {
	BusinessProductDTO responseDTO = new BusinessProductDTO();
	responseDTO.userId = db.cRowSet.getString("user_id");
	responseDTO.productId = db.cRowSet.getString("product_id");
	responseDTO.productName = db.cRowSet.getString("product_name");
	responseDTO.brandName = db.cRowSet.getString("brand_name");
	responseDTO.website = db.cRowSet.getString("website");
	responseDTO.picture = db.cRowSet.getString("picture");
	responseDTO.thumbnail = db.cRowSet.getString("thumbnail");
	responseDTO.keywords = db.cRowSet.getString("keywords");
	responseDTO.tags = db.cRowSet.getString("tags");
	responseDTO.description = db.cRowSet.getString("description");
	responseDTO.url = db.cRowSet.getString("url");
	responseDTO.latitude = db.cRowSet.getString("latitude");
	responseDTO.longitude = db.cRowSet.getString("longitude");
	responseDTO.category = db.cRowSet.getString("category");
	responseDTO.sku = db.cRowSet.getString("sku");
	return responseDTO;

    }

    public String addBusinessProduct(BusinessProductDTO dto) throws Exception {
	Database db = new Database();
	try {
	    return insertBusinessProduct(dto, db, dto.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public String addProductToWishList(WishlistDTO dto) throws Exception {
	Database db = new Database();

	try {
	    String query = SQLConstants.CREATE_PRODUCT_WISHLIST.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    String wishlistId = DAOHelper.guidGenerator(Constants.PRODUCT_WISHLIST, "");
	    params.add(wishlistId);
	    params.add(dto.userId);
	    params.add(dto.productId);
	    params.add(dto.productSource);
	    params.add(dto.productStore);
	    params.add(dto.productHandle);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return wishlistId;
	    }
	    // LOG.info("The transaction was successfully executed");

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		ErrorCodes.StatusCodes.FAILURE, null);
    }

    public LinkedList<WishlistDTO> getWishList(WishlistDTO dto) throws Exception {
	String query = SQLConstants.GET_WISH_LIST.toLowerCase();
	query += (" limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1));
	LinkedList<WishlistDTO> wishList = new LinkedList<WishlistDTO>();
	Database db = new Database();
	try {
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);

	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		WishlistDTO wishlistObject = new WishlistDTO();
		wishlistObject.wishlistId = db.cRowSet.getString("wishlist_id");
		wishlistObject.userId = db.cRowSet.getString("user_id");
		wishlistObject.productId = db.cRowSet.getString("product_id");
		wishlistObject.productSource = db.cRowSet.getString("product_source");
		String productStore = db.cRowSet.getString("product_store");
		String productHandle = db.cRowSet.getString("product_handle");
		if (StringUtils.isEmpty(productStore)) {
		    productStore = db.cRowSet.getString("business_username");
		}
		if (StringUtils.isEmpty(productHandle)) {
		    productHandle = db.cRowSet.getString("buy_now_url");
		}

		wishlistObject.productStore = productStore;
		wishlistObject.productHandle = productHandle;

		wishlistObject.productName = db.cRowSet.getString("p_product_name");
		wishlistObject.businessProductName = db.cRowSet.getString("b_product_name");
		wishlistObject.productImage = db.cRowSet.getString("p_picture");
		wishlistObject.businessProductImage = db.cRowSet.getString("b_picture");
		wishlistObject.businessProductThumbnail = db.cRowSet.getString("b_thumbnail");
		wishlistObject.title = db.cRowSet.getString("title");
		wishlistObject.imageSrc = db.cRowSet.getString("image_src");

		wishlistObject.createdDate = db.cRowSet.getString("created_date");
		wishList.add(wishlistObject);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return wishList;

    }

    public boolean deleteWishListItem(WishlistDTO dto) throws Exception {
	Database db = new Database();
	try {

	    String query = SQLConstants.DELETE_WISH_LIST.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.wishlistId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		return false;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    @SuppressWarnings("unused")
    private int deleteAllBusinessProduct(Database db, String userId) throws Exception {
	String query = SQLConstants.DELETE_BUSINESS_PRODUCT.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(userId);
	return db.executeUpdate(query, params);
    }

    public boolean updateCategoryForUser(RegisterUserDTO dto) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {

	    if (dto.category == null || dto.category.length() <= 0 || dto.userType == null
		    || dto.userType.length() <= 0) {
		throw new SystemException(ErrorCodes.INSUFFICIENT_VALUES, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    String query = SQLConstants.UPDATE_CATEGORY_FOR_USER.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.category);
	    params.add(dto.userType);
	    params.add(dto.businessEmail);
	    params.add(dto.businessAddress);
	    params.add(dto.businessName);
	    params.add(dto.businessPhone);
	    params.add(dto.businessBio);
	    params.add(dto.businessWebsite);
	    params.add(dto.businessFacebook);
	    params.add(dto.businessYoutube);
	    params.add(dto.businessTwitter);
	    params.add(dto.businessInstagram);
	    params.add(dto.businessProfilePicture);
	    params.add(dto.keywords);
	    params.add(dto.shopifyShopName);
	    params.add(dto.shopifyAccessToken);
	    params.add(dto.userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean updateBusinessUserId(String businessUserId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.UPDATE_BUSINESS_USER_ID.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(businessUserId);
	    params.add(userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean updateArtistUserId(String artistUserId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.UPDATE_ARTIST_USER_ID.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(artistUserId);
	    params.add(userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean flagMedia(MediaDTO dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.FLAG_MEDIA.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(Constants.Y_LOWER_CASE);
	    params.add(dto.userId);
	    params.add(dto.flaggedReason);
	    params.add(dto.mediaId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean rejectFlag(String mediaId) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.FLAG_MEDIA.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(Constants.N_LOWER_CASE);
	    params.add("");
	    params.add("");
	    params.add(mediaId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean activateUser(String userId) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.ACTIVATE_USER.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(Constants.Y);
	    params.add(userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean updateArtistReference(String artistUserId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.UPDATE_ARTIST_REFERENCE.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(artistUserId);
	    params.add(userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean updateNotificationPref(String userId, String pref) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.UPDATE_USER_NOTIFICATION_PREF.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(pref);
	    params.add(userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean updateUserToArtist(RegisterUserDTO dto) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {

	    if (StringUtils.isEmpty(dto.artistReference) || StringUtils.isEmpty(dto.userType)) {
		throw new SystemException(ErrorCodes.INSUFFICIENT_VALUES, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    String query = SQLConstants.UPDATE_USER_TO_ARTIST.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.artistReference);
	    params.add(dto.userType);
	    params.add(dto.userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean removeArtistAssociation(String userId) throws Exception {
	if (StringUtils.isEmpty(userId)) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {

	    String query = SQLConstants.REMOVE_ARTIST_ASSOCIATION.toLowerCase();

	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add("");
	    params.add("");
	    params.add("");
	    params.add(userId);
	    db.executeUpdate(query, params);

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean updateSocialIdForEmail(RegisterUserDTO dto) throws Exception {
	boolean googleCheck = false;
	boolean fbCheck = false;
	if (dto.googleId == null || dto.googleId.length() == 0) {
	    fbCheck = true;
	}
	if (dto.fbId == null || dto.fbId.length() == 0) {
	    googleCheck = true;
	}
	if (fbCheck && googleCheck) {
	    throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	int count = 0;
	try {

	    String query = "";
	    String param = "";
	    if (googleCheck) {
		query = SQLConstants.UPDATE_GOOGLE_ID_FOR_EMAIL.toLowerCase();
		param = dto.googleId;
	    } else {
		query = SQLConstants.UPDATE_FB_ID_FOR_EMAIL.toLowerCase();
		param = dto.fbId;
	    }
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(param);
	    params.add(dto.email);
	    count = db.executeUpdate(query, params);

	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	if (count > 0) {
	    return true;
	}
	return false;
    }

    public LinkedList<RegisterUserDTO> getUserContactList(UserContactRequestDTO contact) throws Exception {
	if (!validateUser(contact.userId, contact.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FRIEND_LIST.toLowerCase();

	    String email = "";
	    LinkedList<String> emailList = contact.emailList;
	    if (emailList != null && emailList.size() > 0) {
		int ctr = 1;
		for (String emailObj : emailList) {
		    if (ctr != 1) {
			email += ",";
		    }
		    ctr++;
		    email += "'" + emailObj + "'";
		}
		query = query.replace(SQLConstants.EMAIL_PLACEHOLDER.toLowerCase(), email);
	    }
	    String phone = "";
	    LinkedList<String> phoneList = contact.phoneNumberList;
	    if (phoneList != null && phoneList.size() > 0) {
		int ctr = 1;
		for (String phoneObj : phoneList) {
		    if (ctr != 1) {
			phone += ",";
		    }
		    ctr++;
		    phone += "'" + phoneObj + "'";
		}
		query = query.replace(SQLConstants.PHONE_PLACEHOLDER.toLowerCase(), phone);
	    }
	    LOG.debug("query>>>>" + query.toLowerCase());
	    db.executeQuery(query.toLowerCase(), null);
	    while (db.cRowSet.next()) {
		RegisterUserDTO dto = new RegisterUserDTO();
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.firstName = db.cRowSet.getString("FIRST_NAME");
		dto.lastName = db.cRowSet.getString("LAST_NAME");
		dto.email = db.cRowSet.getString("EMAIL");
		dto.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
		dto.location = db.cRowSet.getString("LOCATION");
		userList.add(dto);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public LinkedList<FeedResponseDTO> getUserFollowingFeedList(PaginatedRequest requestDTO) throws Exception {
	validateStartEndRange(requestDTO);
	LinkedList<FeedResponseDTO> responseList = new LinkedList<FeedResponseDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_FEED.toLowerCase();
	    if (!requestDTO.isAdmin) {
		query += SQLConstants.MEDIA_FLAG;
	    }
	    query += " order by m.created_date desc limit " + (requestDTO.startRange - 1) + ","
		    + (requestDTO.endRange - requestDTO.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.userId);
	    paramList.add(requestDTO.targetUserId);
	    db.executeQuery(query, paramList);
	    responseList = feedResponseProduce(db, requestDTO.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return responseList;
    }

    public LinkedList<FeedResponseDTO> getUserFeedList(PaginatedRequest requestDTO, boolean follows) throws Exception {
	validateStartEndRange(requestDTO);
	LinkedList<FeedResponseDTO> responseList = new LinkedList<FeedResponseDTO>();
	Database db = new Database();
	try {
	    StringBuilder query = new StringBuilder();
	    query.append(SQLConstants.USER_FEED_PREFIX);
	    if (follows) {
		query.append(SQLConstants.USER_FEED_FOLLOW);
	    }
	    query.append(SQLConstants.USER_FEED_POSTFIX);
	    SQLConstants.GET_FOLLOWING_FEED.toLowerCase();
	    query.append(" order by m.created_date desc limit " + (requestDTO.startRange - 1) + ","
		    + (requestDTO.endRange - requestDTO.startRange + 1));
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.userId);
	    paramList.add(requestDTO.targetUserId);
	    db.executeQuery(query.toString(), paramList);
	    responseList = feedResponseProduce(db, requestDTO.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return responseList;
    }

    public boolean userFollows(String userId, String targetUserId) throws Exception {
	if (userId.equalsIgnoreCase(targetUserId)) {
	    return true;
	}
	Database db = new Database();
	try {
	    String query = SQLConstants.USER_FOLLOWS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(targetUserId);
	    paramList.add(userId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public LinkedList<FeedResponseDTO> getPublicFeed(UserMediaPaginatedRequest requestDTO) throws Exception {

	if (requestDTO.startRange < 0 || requestDTO.startRange >= requestDTO.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<FeedResponseDTO> responseList = new LinkedList<FeedResponseDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_PUBLIC_FEED_PREFIX.toLowerCase();
	    boolean userTypeFlag = false;
	    if (!StringUtils.isEmpty(requestDTO.userType)) {
		query += SQLConstants.GET_PUBLIC_FEED_FOR_BUSINESS.toLowerCase();
		userTypeFlag = true;
	    }
	    query += SQLConstants.GET_PUBLIC_FEED_POSTFIX.toLowerCase();
	    if (!requestDTO.isAdmin) {
		query += SQLConstants.MEDIA_FLAG;
	    }
	    query += " limit " + (requestDTO.startRange - 1) + "," + (requestDTO.endRange - requestDTO.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.userId);
	    if (userTypeFlag) {
		paramList.add(requestDTO.userType);
	    }
	    db.executeQuery(query, paramList);
	    responseList = feedResponseProduce(db, requestDTO.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return responseList;
    }

    public LinkedList<FeedResponseDTO> getTrendingFeed(PaginatedRequest requestDTO) throws Exception {
	if (!validateUser(requestDTO.userId, requestDTO.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (requestDTO.startRange < 0 || requestDTO.startRange >= requestDTO.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<FeedResponseDTO> responseList = new LinkedList<FeedResponseDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_TRENDING_MEDIA_FEED.toLowerCase();
	    query += " order by m.comment_date desc limit " + (requestDTO.startRange - 1) + ","
		    + (requestDTO.endRange - requestDTO.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.userId);
	    paramList.add(requestDTO.userId);
	    paramList.add(requestDTO.userId);
	    paramList.add(requestDTO.userId);
	    db.executeQuery(query, paramList);
	    responseList = feedResponseProduce(db, requestDTO.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return responseList;
    }

    public LinkedList<FeedResponseDTO> getNearbyFeed(NearbyPaginatedRequest requestDTO) throws Exception {
	if (!validateUser(requestDTO.userId, requestDTO.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (requestDTO.startRange < 0 || requestDTO.startRange >= requestDTO.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<FeedResponseDTO> responseList = new LinkedList<FeedResponseDTO>();
	Database db = new Database();
	try {
	    String query = "SELECT distinct " + SQLConstants.MEDIA_COLUMNS
		    + ",u.*,lm.USER_ID as like_user_id, (DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS("
		    + requestDTO.latitude + ")) +         cos(RADIANS(LATITUDE)) * cos(RADIANS(" + requestDTO.latitude
		    + ")) *         cos(RADIANS(LONGITUDE - " + requestDTO.longitude
		    + "))))*60*1.1515) AS DIST         FROM  media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?, user_master u  where  (m.uploader = u.user_id) AND PRIVILEGE = '"
		    + Constants.PUBLIC_VISIBILITY + "' order by DIST ".toLowerCase();
	    query += " order by m.comment_date desc limit " + (requestDTO.startRange - 1) + ","
		    + (requestDTO.endRange - requestDTO.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(requestDTO.userId);
	    db.executeQuery(query, paramList);
	    responseList = feedResponseProduce(db, requestDTO.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return responseList;
    }

    public LinkedList<PlaylistMasterDTO> getPlaylistForUser(PaginatedRequest user) throws Exception {
	if (!validateUser(user.userId, user.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (user.startRange < 0 || user.startRange >= user.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<PlaylistMasterDTO> playlist = new LinkedList<PlaylistMasterDTO>();
	Database db = new Database();
	try {
	    // select * from playlist_master where user_id =?
	    String query = SQLConstants.GET_PLAYLIST_FOR_USER.toLowerCase();
	    query += " limit " + (user.startRange - 1) + "," + (user.endRange - user.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(user.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		PlaylistMasterDTO dto = new PlaylistMasterDTO();
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.playlistId = db.cRowSet.getString("PLAYLIST_ID");
		dto.playlistName = db.cRowSet.getString("PLAYLIST_NAME");
		dto.privilege = db.cRowSet.getString("privilege");
		dto.thumbnail = db.cRowSet.getString("THUMBNAIL");
		playlist.add(dto);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return playlist;
    }

    public LinkedList<PlaylistMasterDTO> getPlaylistForUserFollowing(PaginatedRequest user) throws Exception {
	validateStartEndRange(user);
	LinkedList<PlaylistMasterDTO> playlist = new LinkedList<PlaylistMasterDTO>();
	Database db = new Database();
	try {

	    String query = SQLConstants.GET_PLAYLIST_FOR_USER_FOLLOWING.toLowerCase();
	    query += " limit " + (user.startRange - 1) + "," + (user.endRange - user.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(user.targetUserId);
	    paramList.add(Constants.PRIVATE_VISIBILITY);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		PlaylistMasterDTO dto = new PlaylistMasterDTO();
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.playlistId = db.cRowSet.getString("PLAYLIST_ID");
		dto.playlistName = db.cRowSet.getString("PLAYLIST_NAME");
		dto.privilege = db.cRowSet.getString("privilege");
		dto.thumbnail = db.cRowSet.getString("THUMBNAIL");
		playlist.add(dto);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return playlist;
    }

    public LinkedList<MediaDTO> getPlaylistItems(PlaylistItemPaginatedRequest user) throws Exception {
	validateStartEndRange(user);
	if (!playListExists(user.playlistId, user.userId)) {
	    throw new SystemException(ErrorCodes.PLAYLIST_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<MediaDTO> playlist = new LinkedList<MediaDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_PLAYLIST_ITEMS.toLowerCase();
	    query += " limit " + (user.startRange - 1) + "," + (user.endRange - user.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(user.userId);
	    paramList.add(user.playlistId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		MediaDTO media = getMediaFromResultSet(db, user.userId);
		playlist.add(media);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return playlist;
    }

    public HashMap<String, Object> getRequestedUserMedia(UserMediaPaginatedRequest user) throws Exception {

	LinkedList<MediaDTO> mediaList = new LinkedList<MediaDTO>();
	Database db = new Database();
	HashMap<String, Object> object = new HashMap<String, Object>();
	try {
	    String query = "";
	    RegisterUserDTO userFollowing = getRequestUserDetails(user);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    if (user.userId.equalsIgnoreCase(user.requestUserId)) {
		// select * from media_master where media_id in (select media_id
		// from user_media where user_id = ? )
		query = SQLConstants.GET_SELF_MEDIA.toLowerCase();
		paramList.add(user.userId);
		paramList.add(user.userId);
	    } else if (userFollowing.following) {
		// select * from media_master where media_id in (select media_id
		// from user_media where user_id = ? and privilege != ? )
		query = SQLConstants.GET_FOLLOWING_USER_MEDIA.toLowerCase();
		paramList.add(user.requestUserId);
		paramList.add(Constants.PRIVATE_VISIBILITY);
		paramList.add(user.requestUserId);
	    } else {
		// select * from media_master where media_id in (select media_id
		// from user_media where user_id = ? and privilege = ? )
		query = SQLConstants.GET_PUBLIC_USER_MEDIA.toLowerCase();
		paramList.add(user.requestUserId);
		paramList.add(user.requestUserId);
		paramList.add(Constants.PUBLIC_VISIBILITY);
	    }
	    if (user.mediaType != null && user.mediaType.length() > 0) {
		query += " AND FILE_TYPE='" + user.mediaType + "' ";
	    }
	    query += " limit " + (user.startRange - 1) + "," + (user.endRange - user.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		MediaDTO media = getMediaFromResultSet(db, user.userId);
		mediaList.add(media);
	    }
	    object.put(Constants.USER, userFollowing);
	    object.put(Constants.MEDIA, mediaList);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return object;
    }

    public boolean addUserMedia(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    // "INSERT INTO
	    // MEDIA_MASTER(MEDIA_ID,FILE_NAME,FILE_TYPE,MUSIC_PREF_ID,UPLOADER,TAGS,PATH,IS_AD,SKIP_FLAG,LOCATION,ARTIST,THUMBNAIL,DESCRIPTION,ARTIST_ID,ALBUM_ID,MEDIA_LENGTH)
	    // VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	    String activityId = DAOHelper.guidGenerator(Constants.ACTIVITY_PREFIX, "");

	    String query = SQLConstants.INSERT_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.mediaId);
	    params.add(dto.fileName);
	    params.add(dto.fileType);
	    params.add(dto.musicPrefId);
	    params.add(dto.userId);
	    params.add(dto.tags);
	    params.add(dto.path);
	    params.add(dto.isAd);
	    params.add(dto.skipFlag);
	    params.add(dto.location);
	    params.add(dto.privilege);
	    params.add(dto.latitude);
	    params.add(dto.longitude);
	    params.add(dto.artist);
	    params.add(dto.thumbnail);
	    params.add(dto.description);
	    params.add(dto.artistId);
	    params.add(dto.albumId);
	    params.add(dto.mediaLength);
	    params.add(dto.rank);
	    params.add(dto.title);
	    params.add(dto.songTitle);
	    params.add(dto.edlFilePath);
	    params.add(dto.metadataFilePath);
	    params.add(dto.isSkit);
	    params.add(dto.stationList);
	    params.add(dto.isFeatured);
	    params.add(dto.projectName);
	    params.add(dto.postType);
	    params.add(dto.postedAs);
	    params.add(dto.postedId);
	    params.add(dto.scheduledStartTime);
	    params.add(dto.scheduledEndTime);
	    params.add(dto.isScheduled);
	    params.add(dto.seriesId);
	    params.add(dto.episodeNumber);

	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    if (!dto.isLibrary) {
		// "INSERT INTO
		// USER_MEDIA(USER_ID,MEDIA_ID,MEDIA_DATE,PRIVILEGE,ACTIVITY_ID)
		// VALUES (?,?,NOW(),?,?)"
		query = SQLConstants.INSERT_USER_MEDIA.toLowerCase();
		LOG.debug("query>>>>" + query);
		params = new LinkedList<>();
		params.add(dto.userId);
		params.add(dto.mediaId);
		params.add(dto.privilege);
		params.add(activityId);
		count = db.executeUpdate(query, params);
		if (count <= 0) {
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}

		// INSERT INTO
		// ACTIVITY_MASTER(ACTIVITY_ID,ACTIVITY_NAME,ACTIVITY_DESCRIPTION,CREATED_DATE,USER_ID,ACTIVITY_TYPE,MEDIA_ID)
		// VALUES (?,?,?,NOW(),?,?,?)
		query = SQLConstants.INSERT_USER_MEDIA_ACTIVITY.toLowerCase();
		LOG.debug("query>>>>" + query);

		params = new LinkedList<>();

		params.add(activityId);
		params.add(dto.userId + Constants.NEW_ACTIVITY_MEDIA_TEXT + dto.title);
		params.add(dto.userId + Constants.NEW_ACTIVITY_MEDIA_TEXT + dto.title);
		params.add(dto.userId);
		params.add(Constants.NEW_MEDIA);
		params.add(dto.mediaId);

		count = db.executeUpdate(query, params);
		if (count <= 0) {
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
	    }
	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public String mediaExists(String uploader, String projectName) throws Exception {
	if (projectName == null || projectName.length() == 0) {
	    return null;
	}
	String mediaId = null;
	Database db = new Database();
	String query = SQLConstants.GET_MEDIA_UPLOADER_PROJECT.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> paramList = new LinkedList<Object>();
	paramList.add(uploader.toLowerCase());
	paramList.add(projectName.toLowerCase());

	db.executeQuery(query, paramList);
	if (db.cRowSet.next()) {

	    mediaId = db.cRowSet.getString(1);
	}

	return mediaId;
    }

    public boolean addMediaComment(MediaCommentDTO dto, MediaTargetUserDTO target) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String activityId = DAOHelper.guidGenerator(Constants.ACTIVITY_PREFIX, "");
	    String commentId = DAOHelper.guidGenerator(Constants.COMMENT, "");

	    String query = SQLConstants.INSERT_MEDIA_COMMENT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();

	    params.add(commentId);
	    params.add(dto.mediaId);
	    params.add(dto.userId);
	    params.add(dto.comment);
	    params.add(dto.tags);
	    params.add(activityId);

	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    query = SQLConstants.INSERT_USER_MEDIA_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    MediaDTO media = getMediaDetails(dto.mediaId);
	    params = new LinkedList<>();

	    params.add(activityId);
	    String cName = CommonUtils.getName(target.cFirstName, target.cLastName, dto.userId);
	    String mName = CommonUtils.getName(target.mFirstName, target.mLastName, target.mUserId);
	    String activity = cName + " " + Constants.NEW_MEDIA_COMMENT + mName + "'s " + media.title;
	    params.add(activity);
	    params.add(activity);
	    params.add(dto.userId);
	    params.add(Constants.NEW_COMMENT);
	    params.add(dto.mediaId);
	    count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean likeMedia(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {
	    // INSERT INTO LIKE_MASTER(USER_ID,MEDIA_ID) VALUES (?,?)
	    String query = SQLConstants.LIKE_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();

	    params.add(dto.userId);
	    params.add(dto.mediaId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public boolean unlikeMedia(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();

	try {

	    String query = SQLConstants.UNLIKE_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.userId);
	    params.add(dto.mediaId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		return false;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error(e.getMessage());

	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	return true;
    }

    public boolean deleteComment(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {

	    String query = SQLConstants.DELETE_COMMENT.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.commentId);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		return false;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    public boolean addUserPlaylist(PlaylistMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();

	db.setAutoCommitFalse();
	try {
	    // INSERT INTO
	    // PLAYLIST_MASTER(PLAYLIST_ID,USER_ID,PLAYLIST_NAME,CREATED_DATE,privilege,THUMBNAIL,ACTIVITY_ID)
	    // VALUES (?,?,?,NOW(),?,?,?)
	    String activityId = DAOHelper.guidGenerator(Constants.ACTIVITY_PREFIX, "");

	    String query = SQLConstants.INSERT_PLAYLIST.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.playlistId);
	    params.add(dto.userId);
	    params.add(dto.playlistName);
	    params.add(dto.privilege);
	    params.add(dto.thumbnail);
	    params.add(activityId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    LinkedList<String> playlistItems = dto.playlistItems;
	    if (playlistItems != null && playlistItems.size() > 0) {
		LinkedList<String> mediaIdList = new LinkedList<String>();
		for (String item : playlistItems) {
		    if (!mediaIdList.contains(item)) {
			mediaIdList.add(item);
		    }
		}
		for (String mediaId : mediaIdList) {
		    count = addPlaylistItem(db, dto.playlistId, mediaId);
		    if (count <= 0) {
			throw new SystemException(ErrorCodes.GENERIC_EXCEPTION,
				ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
		    }
		}
	    }

	    // INSERT INTO
	    // ACTIVITY_MASTER(ACTIVITY_ID,ACTIVITY_NAME,ACTIVITY_DESCRIPTION,CREATED_DATE,USER_ID,ACTIVITY_TYPE)
	    // VALUES (?,?,?,NOW(),?,?)
	    query = SQLConstants.INSERT_USER_MEDIA_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(activityId);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_PLAYLIST_TEXT + dto.playlistName);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_PLAYLIST_TEXT + dto.playlistName);
	    params.add(dto.userId);
	    params.add(Constants.NEW_PLAYLIST);
	    params.add(dto.playlistId);

	    count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error in DB query " + e.getMessage());
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}
    }

    public boolean copyUserPlaylist(PlaylistMasterDTO dto, String oldPlaylistId) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();

	db.setAutoCommitFalse();
	try {
	    // INSERT INTO
	    // PLAYLIST_MASTER(PLAYLIST_ID,USER_ID,PLAYLIST_NAME,CREATED_DATE,privilege,THUMBNAIL,ACTIVITY_ID)
	    // VALUES (?,?,?,NOW(),?,?,?)
	    if (!getPlayListFollowerExists(oldPlaylistId, dto.userId)) {
		throw new SystemException(ErrorCodes.PLAYLIST_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    String activityId = DAOHelper.guidGenerator(Constants.ACTIVITY_PREFIX, "");

	    String query = SQLConstants.INSERT_PLAYLIST.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.playlistId);
	    params.add(dto.userId);
	    params.add(dto.playlistName);
	    params.add(dto.privilege);
	    params.add(dto.thumbnail);
	    params.add(activityId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    count = copyPlaylistItem(db, dto.playlistId, oldPlaylistId);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    // INSERT INTO
	    // ACTIVITY_MASTER(ACTIVITY_ID,ACTIVITY_NAME,ACTIVITY_DESCRIPTION,CREATED_DATE,USER_ID,ACTIVITY_TYPE)
	    // VALUES (?,?,?,NOW(),?,?)
	    query = SQLConstants.INSERT_USER_MEDIA_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(activityId);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_PLAYLIST_TEXT + dto.playlistName);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_PLAYLIST_TEXT + dto.playlistName);
	    params.add(dto.userId);
	    params.add(Constants.NEW_PLAYLIST);
	    params.add(dto.playlistId);
	    count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    private int copyPlaylistItem(Database db, String playlistId, String oldPlaylistId) throws Exception {

	String query = SQLConstants.COPY_PLAYLIST_ITEM.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(playlistId);
	params.add(oldPlaylistId);
	return db.executeUpdate(query, params);
    }

    public boolean playListExists(String playlistId, String userId) throws Exception {
	String playlistUserId = getPlayListUserOwner(playlistId, userId);
	if (playlistUserId != null && playlistUserId.length() > 0) {
	    if (playlistUserId.equalsIgnoreCase(userId)) {
		return true;
	    }
	}
	return false;
    }

    public String getPlayListUserOwner(String playlistId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.PLAYLIST_EXISTS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(playlistId);
	    // paramList.add(userId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		String playlistUserId = db.cRowSet.getString("USER_ID");

		return playlistUserId;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public boolean getPlayListFollowerExists(String playlistId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.PLAYLIST_FOLLOWER_EXISTS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(playlistId);
	    paramList.add(userId.toLowerCase());
	    paramList.add(Constants.PUBLIC_VISIBILITY);
	    paramList.add(Constants.PRIVATE_VISIBILITY);
	    paramList.add(userId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

	return false;
    }

    private int addPlaylistItem(Database db, String playlistId, String mediaId) throws Exception {
	String query = SQLConstants.INSERT_PLAYLIST_ITEM.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(playlistId);
	params.add(mediaId);
	params.add("0");
	return db.executeUpdate(query, params);
    }

    private int deletePlaylistItem(Database db, String playlistId, String mediaId) throws Exception {
	String query = SQLConstants.DELETE_PLAYLIST_ITEM.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(playlistId);
	params.add(mediaId);
	return db.executeUpdate(query, params);
    }

    public boolean addPlaylistItem(PlaylistMasterDTO dto) throws Exception {

	if (playListExists(dto.playlistId, dto.userId)) {
	    Database db = new Database();
	    db.setAutoCommitFalse();
	    try {

		LinkedList<String> playlistItems = dto.playlistItems;
		if (playlistItems != null && playlistItems.size() > 0) {
		    LinkedList<String> mediaIdList = new LinkedList<String>();
		    for (String item : playlistItems) {
			if (!mediaIdList.contains(item)) {
			    mediaIdList.add(item);
			}
		    }
		    for (String mediaId : mediaIdList) {
			int count = addPlaylistItem(db, dto.playlistId, mediaId);
			if (count <= 0) {
			    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION,
				    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
			}
		    }
		} else {
		    throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
		db.commit();
		LOG.error("The transaction was successfully executed");
		return true;
	    } catch (Exception e) {
		try {
		    // We rollback the transaction, atomicity!
		    db.rollBack();

		    LOG.error(e.getMessage());
		    LOG.error("The transaction was rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		} catch (Exception e1) {
		    LOG.error("There was an error making a rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
	    } finally {
		closeDbConnection(db);
	    }
	} else {
	    throw new SystemException(ErrorCodes.PLAYLIST_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
    }

    public boolean deletePlaylistItem(PlaylistMasterDTO dto) throws Exception {

	if (playListExists(dto.playlistId, dto.userId)) {
	    Database db = new Database();
	    db.setAutoCommitFalse();
	    try {

		LinkedList<String> playlistItems = dto.playlistItems;
		if (playlistItems != null && playlistItems.size() > 0) {
		    LinkedList<String> mediaIdList = new LinkedList<String>();
		    for (String item : playlistItems) {
			if (!mediaIdList.contains(item)) {
			    mediaIdList.add(item);
			}
		    }
		    for (String mediaId : mediaIdList) {
			int count = deletePlaylistItem(db, dto.playlistId, mediaId);
			if (count <= 0) {
			    db.rollBack();

			    return false;
			}
		    }
		} else {
		    throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
		db.commit();
		LOG.error("The transaction was successfully executed");
		return true;
	    } catch (Exception e) {
		try {
		    db.rollBack();
		    LOG.error(e.getMessage());
		    LOG.error("The transaction was rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		} catch (Exception e1) {
		    LOG.error("There was an error making a rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
	    } finally {
		closeDbConnection(db);
	    }
	} else {
	    throw new SystemException(ErrorCodes.PLAYLIST_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
    }

    public boolean addUserStation(StationMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();

	db.setAutoCommitFalse();
	try {
	    String activityId = DAOHelper.guidGenerator(Constants.ACTIVITY_PREFIX, "");

	    String query = SQLConstants.INSERT_STATION.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.stationId);
	    params.add(dto.userId);
	    params.add(dto.musicPrefId);
	    params.add(activityId);
	    params.add(dto.stationName);
	    params.add(dto.privilege);
	    params.add(dto.thumbnail);
	    params.add(dto.tags);

	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    LinkedList<String> stationItems = dto.stationItems;
	    if (stationItems != null && stationItems.size() > 0) {
		LinkedList<String> mediaIdList = new LinkedList<String>();
		for (String item : stationItems) {
		    if (!mediaIdList.contains(item)) {
			mediaIdList.add(item);
		    }
		}
		for (String mediaId : mediaIdList) {
		    count = addStationItem(db, dto.stationId, mediaId);
		    if (count <= 0) {
			throw new SystemException(ErrorCodes.GENERIC_EXCEPTION,
				ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
		    }
		}
	    }

	    query = SQLConstants.INSERT_USER_MEDIA_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(activityId);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_STATION_TEXT + dto.stationName);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_STATION_TEXT + dto.stationName);
	    params.add(dto.userId);
	    params.add(Constants.NEW_PLAYLIST);
	    params.add(dto.stationId);
	    count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean stationExistsForUser(String stationId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.STATION_EXISTS_FOR_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(stationId);
	    paramList.add(userId.toLowerCase());
	    paramList.add(stationId);
	    paramList.add(userId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {

		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean stationExists(String stationId, String userId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.STATION_EXISTS.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(stationId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		return true;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public boolean addStationItem(StationMasterDTO dto) throws Exception {

	if (stationExistsForUser(dto.stationId, dto.userId)) {
	    Database db = new Database();

	    db.setAutoCommitFalse();
	    try {

		LinkedList<String> stationItems = dto.stationItems;
		if (stationItems != null && stationItems.size() > 0) {
		    LinkedList<String> mediaIdList = new LinkedList<String>();
		    for (String item : stationItems) {
			if (!mediaIdList.contains(item)) {
			    mediaIdList.add(item);
			}
		    }
		    for (String mediaId : mediaIdList) {
			int count = addStationItem(db, dto.stationId, mediaId);
			if (count <= 0) {
			    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION,
				    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
			}
		    }
		} else {
		    throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
		db.commit();
		LOG.error("The transaction was successfully executed");
		return true;
	    } catch (Exception e) {
		try {
		    // We rollback the transaction, atomicity!
		    db.rollBack();

		    LOG.error(e.getMessage());
		    LOG.error("The transaction was rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		} catch (Exception e1) {
		    LOG.error("There was an error making a rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
	    } finally {
		closeDbConnection(db);
	    }
	} else {
	    throw new SystemException(ErrorCodes.STATION_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
    }

    private int addStationItem(Database db, String stationId, String mediaId) throws Exception {
	String query = SQLConstants.INSERT_STATION_ITEM.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(stationId);
	params.add(mediaId);
	params.add("0");
	return db.executeUpdate(query, params);
    }

    public String getActivityIdForUserMedia(String userId, String mediaId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_ACTIVITY_ID_FOR_USER_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(userId.toLowerCase());
	    paramList.add(mediaId.toLowerCase());
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {

		String activityId = db.cRowSet.getString("ACTIVITY_ID");

		return activityId;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return "";
    }

    public boolean editUserMedia(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();

	db.setAutoCommitFalse();
	try {
	    String activityId = getActivityIdForUserMedia(dto.userId, dto.mediaId);

	    String query = SQLConstants.EDIT_MEDIA.toLowerCase();
	    // UPDATE MEDIA_MASTER SET
	    // FILE_NAME=?,FILE_TYPE=?,MUSIC_PREF_ID=?,UPLOADER=?,TAGS=?,PATH=?,IS_AD=?,SKIP_FLAG=?,
	    // LOCATION=?,PRIVILEGE=?,LATITUDE=?,LONGITUDE=?,ARTIST=?,THUMBNAIL=?,DESCRIPTION=?,ARTIST_ID=?,ALBUM_ID=?,MEDIA_LENGTH=?,
	    // RANK=?,TITLE=?,SONG_TITLE=?,EDL_FILE_PATH=?,METADATA_FILE_PATH=?,IS_SKIT=?,STATION_LIST=?,IS_FEATURED=?,
	    // PROJECT_NAME=?,POST_TYPE=?,posted_as=?,posted_id=?,scheduled_Start_Time=?,scheduled_End_Time=?,is_Scheduled=?
	    // WHERE MEDIA_ID=?"
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.fileName);
	    params.add(dto.fileType);
	    params.add(dto.musicPrefId);
	    params.add(dto.userId);
	    params.add(dto.tags);
	    params.add(dto.path);
	    params.add(dto.isAd);
	    params.add(dto.skipFlag);
	    params.add(dto.location);
	    params.add(dto.privilege);
	    params.add(dto.latitude);
	    params.add(dto.longitude);
	    params.add(dto.artist);
	    params.add(dto.thumbnail);
	    params.add(dto.description);
	    params.add(dto.artistId);
	    params.add(dto.albumId);
	    params.add(dto.mediaLength);
	    params.add(dto.rank);
	    params.add(dto.title);
	    params.add(dto.songTitle);
	    params.add(dto.edlFilePath);
	    params.add(dto.isSkit);
	    params.add(dto.stationList);
	    params.add(dto.isFeatured);
	    params.add(dto.projectName);
	    params.add(dto.postType);
	    params.add(dto.postedAs);
	    params.add(dto.postedId);
	    params.add(dto.scheduledStartTime);
	    params.add(dto.scheduledEndTime);
	    params.add(dto.isScheduled);
	    if (!StringUtils.isEmpty(dto.metadataFilePath)) {
		query = SQLConstants.EDIT_MEDIA_WITH_METADATA;
		params.add(dto.metadataFilePath);
	    }
	    params.add(dto.mediaId);
	    LOG.debug("query>>>>" + query);

	    int count = db.executeUpdate(query.toLowerCase(), params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    query = SQLConstants.EDIT_USER_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();

	    params.add(dto.privilege);
	    params.add(dto.userId);
	    params.add(dto.mediaId);
	    count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    query = SQLConstants.EDIT_USER_MEDIA_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.userId + Constants.EDIT_ACTIVITY_MEDIA_TEXT + dto.fileName);
	    params.add(dto.userId + Constants.EDIT_ACTIVITY_MEDIA_TEXT + dto.fileName);
	    params.add(Constants.UPDATED_MEDIA);
	    params.add(activityId);
	    db.executeUpdate(query, params);

	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public int getFollowingUserCount(PaginatedRequest dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_USER_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getFollowingBusinessCount(RegisterUserDTO dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_BUSINESS_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getFollowingArtistCount(RegisterUserDTO dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_ARTIST_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getBusinessFollowerCount(PaginatedRequest dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_BUSINESS_FOLLOWER_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getArtistFollowerCount(ArtistDTO dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_ARTIST_FOLLOWER_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.artistId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public int getFollowerUserCount(PaginatedRequest dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWERS_USER_COUNT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		int count = db.cRowSet.getInt(1);
		return count;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return 0;
    }

    public HashMap<String, Object> getFollowingUserList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	HashMap<String, Object> objectMap = new HashMap<String, Object>();
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_USERS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.targetUserId);
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    String count = "";
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		count = db.cRowSet.getString("count_val");
		String follows = db.cRowSet.getString("follows");
		if (!StringUtils.isEmpty(follows) && dto.userId.equalsIgnoreCase(follows)) {
		    user.following = true;
		} else {
		    user.following = false;
		}
		userList.add(user);
	    }
	    objectMap.put(Constants.FOLLOWER_LIST, userList);
	    objectMap.put(Constants.COUNT, count);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return objectMap;
    }

    public HashMap<String, Object> getFollowingBusinessList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	HashMap<String, Object> objectMap = new HashMap<String, Object>();
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_BUSINESS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    String count = "";
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		count = db.cRowSet.getString("count_val");
		userList.add(user);
	    }
	    objectMap.put(Constants.FOLLOWER_LIST, userList);
	    objectMap.put(Constants.COUNT, count);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return objectMap;
    }

    public LinkedList<RegisterUserDTO> suggestFollowUserList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.SUGGEST_FOLLOWING_USERS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		userList.add(user);
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public LinkedList<RegisterUserDTO> suggestFollowBusinessList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.SUGGEST_FOLLOWING_BUSINESS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();

	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		userList.add(user);
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;
    }

    public HashMap<String, Object> getFollowingArtistList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	HashMap<String, Object> objectMap = new HashMap<String, Object>();
	LinkedList<ArtistDTO> artistList = new LinkedList<ArtistDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_ARTISTS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.targetUserId);
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    String count = "";
	    while (db.cRowSet.next()) {
		ArtistDTO artistDTO = getArtistFromRowset(db);
		count = db.cRowSet.getString("count_val");
		String follows = db.cRowSet.getString("follows");
		if (!StringUtils.isEmpty(follows) && dto.userId.equalsIgnoreCase(follows)) {
		    artistDTO.following = true;
		} else {
		    artistDTO.following = false;
		}
		artistList.add(artistDTO);
	    }
	    objectMap.put(Constants.FOLLOWER_LIST, artistList);
	    objectMap.put(Constants.COUNT, count);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return objectMap;
    }

    public LinkedList<ArtistDTO> suggestFollowArtistList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	LinkedList<ArtistDTO> artistList = new LinkedList<ArtistDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.SUGGEST_FOLLOWING_ARTISTS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		ArtistDTO artistDTO = getArtistFromRowset(db);
		artistList.add(artistDTO);
	    }

	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return artistList;
    }

    public HashMap<String, Object> getFollowerUserList(PaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	HashMap<String, Object> objectMap = new HashMap<String, Object>();
	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWERS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.targetUserId);
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    String count = "";
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		count = db.cRowSet.getString("count_val");
		String follows = db.cRowSet.getString("follows");
		if (!StringUtils.isEmpty(follows) && dto.userId.equalsIgnoreCase(follows)) {
		    user.following = true;
		} else {
		    user.following = false;
		}
		userList.add(user);
	    }
	    objectMap.put(Constants.FOLLOWER_LIST, userList);
	    objectMap.put(Constants.COUNT, count);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return objectMap;
    }

    public String genericSqlExecutor(String query) throws Exception {
	java.util.Date dateObj = new java.util.Date();
	String dateString = new Timestamp(dateObj.getTime()).toString();
	String fileName = "select" + dateString;
	fileName = fileName.replaceAll(":", "_");
	fileName = fileName.replaceAll("\\.", "_");
	fileName = fileName.replaceAll("-", "_");
	fileName = fileName.replaceAll("&", "_");
	fileName = fileName.replaceAll("\\\\", "_");
	fileName = fileName.replaceAll("/", "_");
	fileName = fileName.replaceAll(" ", "_");
	fileName += ".csv";
	Database db = new Database();
	try {
	    LOG.debug("query>>>>" + query);
	    db.executeQuery(query, null);
	    FileWriter writer = new FileWriter(fileName);
	    writer.append(query);
	    writer.append('\n');
	    ResultSetMetaData rsmd = db.cRowSet.getMetaData();
	    int columnCount = rsmd.getColumnCount();
	    boolean firstIteration = true;
	    // The column count starts from 1
	    for (int i = 1; i <= columnCount; i++) {
		String name = rsmd.getColumnName(i);
		if (!firstIteration) {
		    writer.append(',');
		}
		firstIteration = false;
		writer.append(name);
	    }
	    writer.append('\n');
	    while (db.cRowSet.next()) {
		int counter = 1;
		boolean endOfIteration = false;
		try {
		    if (endOfIteration) {
			break;
		    }
		    firstIteration = true;
		    while (true) {
			if (!firstIteration) {
			    writer.append(',');
			}
			writer.append(db.cRowSet.getString(counter++));
			firstIteration = false;
		    }
		} catch (Exception e) {
		    endOfIteration = true;
		} finally {
		    endOfIteration = true;
		    writer.append('\n');
		}
	    }
	    writer.flush();
	    writer.close();
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return fileName;
    }

    public String genericDMLExecutor(String query) throws Exception {
	String result = "false";
	Database db = new Database();
	try {

	    LOG.debug("query>>>>" + query);
	    int count = db.executeUpdate(query, null);
	    if (count > 0) {
		result = "true";
	    } else {
		result = "false";
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return result;
    }

    public LinkedList<ActivityMasterDTO> getActivityList(PaginatedRequest dto) throws Exception {

	LinkedList<ActivityMasterDTO> activityList = new LinkedList<ActivityMasterDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_ALL_ACTIVITY.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		ActivityMasterDTO activity = new ActivityMasterDTO();
		activity.activityId = db.cRowSet.getString("ACTIVITY_ID");
		activity.activityName = db.cRowSet.getString("ACTIVITY_NAME");
		activity.activityDescription = db.cRowSet.getString("ACTIVITY_DESCRIPTION");
		activity.user.userId = db.cRowSet.getString("USER_ID");
		activity.activityType = db.cRowSet.getString("ACTIVITY_TYPE");
		activity.referenceId = db.cRowSet.getString("MEDIA_ID");
		MediaDTO mediaDTO = getMediaFromResultSet(db, dto.userId);
		activity.data = mediaDTO;
		activityList.add(activity);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

	return activityList;
    }

    public LinkedList<ActivityMasterDTO> getFollowerActivityList(PaginatedRequest dto) throws Exception {

	LinkedList<ActivityMasterDTO> activityList = new LinkedList<ActivityMasterDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWER_ACTIVITY.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    activityList = activityProduce(db, dto.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return activityList;
    }

    public LinkedList<ActivityMasterDTO> getFollowingActivityList(PaginatedRequest dto) throws Exception {

	LinkedList<ActivityMasterDTO> activityList = new LinkedList<ActivityMasterDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_FOLLOWING_ACTIVITY.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    activityList = activityProduce(db, dto.userId);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return activityList;
    }

    private LinkedList<ActivityMasterDTO> activityProduce(Database db, String userId) throws Exception {
	LinkedList<ActivityMasterDTO> activityList = new LinkedList<ActivityMasterDTO>();
	while (db.cRowSet.next()) {
	    ActivityMasterDTO activity = new ActivityMasterDTO();
	    activity.activityId = db.cRowSet.getString("ACTIVITY_ID");
	    activity.activityName = db.cRowSet.getString("ACTIVITY_NAME");
	    activity.activityDescription = db.cRowSet.getString("ACTIVITY_DESCRIPTION");
	    activity.user.userId = db.cRowSet.getString("USER_ID");
	    activity.activityType = db.cRowSet.getString("ACTIVITY_TYPE");
	    activity.referenceId = db.cRowSet.getString("MEDIA_ID");
	    activity.user.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
	    activity.user.firstName = db.cRowSet.getString("FIRST_NAME");
	    activity.user.lastName = db.cRowSet.getString("LAST_NAME");
	    try {
		activity.createdDate = db.cRowSet.getTimestamp("cr_date");
	    } catch (Exception e) {
		LOG.error("Error getting created dates : " + e.getMessage());
	    }
	    MediaDTO mediaDTO = getMediaFromResultSet(db, userId);
	    activity.data = mediaDTO;
	    activityList.add(activity);
	}
	return activityList;
    }

    private MediaDTO getMediaFromResultSet(Database db, String userId) throws Exception {
	MediaDTO media = new MediaDTO();
	media.mediaId = db.cRowSet.getString("MEDIA_ID");
	media.fileName = db.cRowSet.getString("FILE_NAME");
	media.fileType = db.cRowSet.getString("FILE_TYPE");
	media.musicPrefId = db.cRowSet.getString("MUSIC_PREF_ID");
	media.uploader = db.cRowSet.getString("UPLOADER");
	media.tags = db.cRowSet.getString("TAGS");
	media.path = db.cRowSet.getString("PATH");
	media.isAd = db.cRowSet.getString("IS_AD");
	media.skipFlag = db.cRowSet.getString("SKIP_FLAG");
	media.location = db.cRowSet.getString("LOCATION");
	media.privilege = db.cRowSet.getString("PRIVILEGE");
	media.latitude = db.cRowSet.getString("LATITUDE");
	media.longitude = db.cRowSet.getString("LONGITUDE");
	media.artist = db.cRowSet.getString("ARTIST");
	media.thumbnail = db.cRowSet.getString("THUMBNAIL");
	media.description = db.cRowSet.getString("DESCRIPTION");
	media.artistId = db.cRowSet.getString("ARTIST_ID");
	media.albumId = db.cRowSet.getString("ALBUM_ID");
	media.mediaLength = db.cRowSet.getString("MEDIA_LENGTH");
	media.rank = db.cRowSet.getString("RANK");
	media.title = db.cRowSet.getString("TITLE");
	media.songTitle = db.cRowSet.getString("SONG_TITLE");
	media.edlFilePath = db.cRowSet.getString("EDL_FILE_PATH");
	media.postType = db.cRowSet.getString("POST_TYPE");
	media.postedAs = db.cRowSet.getString("posted_as");
	media.postedId = db.cRowSet.getString("posted_id");
	try {
	    media.seriesId = db.cRowSet.getString("series_id");
	    media.episodeNumber = db.cRowSet.getString("episode_number");
	} catch (Exception e) {
	    LOG.error("Error getting Series details : " + e.getMessage());
	}
	try {
	    media.scheduledStartTime = db.cRowSet.getTimestamp("scheduled_Start_Time");
	    media.scheduledEndTime = db.cRowSet.getTimestamp("scheduled_End_Time");
	} catch (Exception e) {
	    LOG.error("Error getting scheduled dates : " + e.getMessage());
	}
	try {
	    media.flag = db.cRowSet.getString("FLAG");
	    media.flaggedBy = db.cRowSet.getString("FLAGGED_BY");
	    media.flaggedReason = db.cRowSet.getString("FLAGGED_REASON");
	} catch (Exception e) {
	    LOG.error("Error getting flag details : " + e.getMessage());
	}
	try {
	    media.createdDate = db.cRowSet.getTimestamp("cr_date");
	} catch (Exception e) {
	    // LOG.error("Error getting creared dates : " + e.getMessage());
	}
	media.isScheduled = db.cRowSet.getString("is_Scheduled");
	// media.metadataFilePath = db.cRowSet.getString("METADATA_FILE_PATH");
	media.isSkit = db.cRowSet.getString("IS_SKIT");
	try {
	    if (!StringUtils.isEmpty(userId)) {
		boolean likeFlag = false;
		String likeUserId = db.cRowSet.getString("like_user_id");
		if (userId.equalsIgnoreCase(likeUserId)) {
		    likeFlag = true;
		}
		media.likeFlag = likeFlag;
	    }

	} catch (Exception e) {
	    LOG.error("Like Information is not present.");
	}
	try {
	    media.mediaDate = db.cRowSet.getDate("MEDIA_DATE");
	} catch (Exception e) {
	    LOG.error("Error converting the date.");
	}
	media.stationList = db.cRowSet.getString("STATION_LIST");
	media.isFeatured = db.cRowSet.getString("IS_FEATURED");
	media.projectName = db.cRowSet.getString("PROJECT_NAME");
	return media;
    }

    public PlaylistMasterDTO getPlaylistActiviy(String playlistId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_PLAYLIST_OF_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(playlistId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		PlaylistMasterDTO dto = new PlaylistMasterDTO();
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.playlistId = db.cRowSet.getString("PLAYLIST_ID");
		dto.playlistName = db.cRowSet.getString("PLAYLIST_NAME");
		dto.privilege = db.cRowSet.getString("privilege");
		dto.thumbnail = db.cRowSet.getString("THUMBNAIL");

		return dto;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public StationMasterDTO getStationActiviy(String activityId) throws Exception {
	Database db = new Database();
	try {
	    // select * from station_master where activity_id = ?
	    String query = SQLConstants.GET_STATION_OF_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(activityId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		StationMasterDTO dto = new StationMasterDTO();
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.stationId = db.cRowSet.getString("STATION_ID");
		dto.musicPrefId = db.cRowSet.getString("MUSIC_PREF_ID");
		dto.stationName = db.cRowSet.getString("STATION_NAME");

		return dto;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public MediaCommentDTO getCommentActiviy(String commentId) throws Exception {
	Database db = new Database();
	try {
	    // select * from media_comments where activity_id = ?
	    String query = SQLConstants.GET_COMMENT_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(commentId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		MediaCommentDTO dto = new MediaCommentDTO();
		dto.commentId = db.cRowSet.getString("COMMENT_ID");
		dto.mediaId = db.cRowSet.getString("MEDIA_ID");
		dto.userId = db.cRowSet.getString("USER_ID");
		dto.comment = db.cRowSet.getString("COMMENT");
		dto.tags = db.cRowSet.getString("TAGS");
		dto.commentDate = db.cRowSet.getDate("COMMENT_DATE");

		return dto;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public LinkedList<MediaCommentDTO> getMediaComments(MediaPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(dto);
	LinkedList<MediaCommentDTO> commentList = new LinkedList<MediaCommentDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_COMMENTS.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.mediaId);
	    db.executeQuery(query, paramList);
	    while (db.cRowSet.next()) {
		MediaCommentDTO comment = new MediaCommentDTO();
		comment.commentId = db.cRowSet.getString("COMMENT_ID");
		comment.mediaId = db.cRowSet.getString("MEDIA_ID");
		comment.userId = db.cRowSet.getString("USER_ID");
		comment.comment = db.cRowSet.getString("COMMENT");
		comment.tags = db.cRowSet.getString("TAGS");
		comment.commentDate = db.cRowSet.getDate("COMMENT_DATE");
		comment.user = userProduce(db, false);
		commentList.add(comment);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return commentList;
    }

    public boolean deleteUserPlaylist(PlaylistMasterDTO dto) throws Exception {

	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_PLAYLIST_ITEMS.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.playlistId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    query = SQLConstants.DELETE_PLAYLIST.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.playlistId);
	    params.add(dto.userId);
	    count = db.executeUpdate(query, params);
	    if (count == 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    db.commit();

	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    public FeedResponseDTO getSpecificMedia(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_SPECIFIC_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.mediaId);
	    db.executeQuery(query, paramList);
	    LinkedList<FeedResponseDTO> responseList = feedResponseProduce(db, dto.userId);
	    if (responseList != null && responseList.size() > 0) {
		return responseList.get(0);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public boolean deleteMedia(MediaPostDTO dto) throws Exception {

	Database db = new Database();

	db.setAutoCommitFalse();
	try {
	    String activityId = DAOHelper.guidGenerator(Constants.ACTIVITY_PREFIX, "");
	    // INSERT INTO
	    // ACTIVITY_MASTER(ACTIVITY_ID,ACTIVITY_NAME,ACTIVITY_DESCRIPTION,CREATED_DATE,USER_ID,ACTIVITY_TYPE)
	    // VALUES (?,?,?,NOW(),?,?)
	    String query = SQLConstants.INSERT_USER_MEDIA_ACTIVITY.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    MediaDTO media = getMediaDetails(dto.mediaId);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(activityId);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_MEDIA_TEXT + media.title);
	    params.add(dto.userId + Constants.NEW_ACTIVITY_MEDIA_TEXT + media.title);
	    params.add(dto.userId);
	    params.add(Constants.DELETE_MEDIA);
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    query = SQLConstants.DELETE_STATION_ITEMS_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete Cameos for a media.
	    query = SQLConstants.DELETE_CAMEO_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete Media Likes
	    query = SQLConstants.DELETE_LIKE_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete comments for a media.
	    query = SQLConstants.DELETE_COMMENTS_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete Loops for a media.
	    query = SQLConstants.DELETE_LOOP_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete metadata for a media.
	    query = SQLConstants.DELETE_MEDIA_METADATA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete Playlist items for a media.
	    query = SQLConstants.DELETE_PLAYLIST_ITEMS_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete Station items for a media.
	    query = SQLConstants.DELETE_STATION_ITEMS_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete User media for a media.
	    query = SQLConstants.DELETE_USER_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    // Delete comments for a media.
	    query = SQLConstants.DELETE_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.mediaId);
	    db.executeUpdate(query, params);

	    db.commit();

	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    public boolean deleteUserStation(StationMasterDTO dto) throws Exception {

	Database db = new Database();

	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_STATION_ITEMS.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.stationId);
	    db.executeUpdate(query, params);

	    query = SQLConstants.DELETE_STATION.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    params = new LinkedList<>();
	    params.add(dto.stationId);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		db.rollBack();
		return false;
	    }
	    db.commit();

	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();

		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public LinkedList<StationMasterDTO> searchStation(SearchPaginatedRequest dto) throws Exception {
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<StationMasterDTO> stationList = new LinkedList<StationMasterDTO>();
	Database db = new Database();
	try {
	    // SELECT * FROM station_master WHERE user_id= ? or STATION_ID in
	    // (select station_id from station_share where user_id = ?)
	    String query = SQLConstants.SEARCH_STATION.toLowerCase();
	    String search = dto.searchString;
	    if (search != null && search.length() > 0) {
		query += SQLConstants.SEARCH_STATION_CONDITION.toLowerCase();
	    }
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    if (search != null && search.length() > 0) {
		paramList.add(dto.searchString);
	    }
	    db.executeQuery(query, paramList);
	    stationList = getStationList(db);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return stationList;
    }

    public LinkedList<StationMasterDTO> getUserStation(PaginatedRequest dto) throws Exception {

	LinkedList<StationMasterDTO> stationList = new LinkedList<StationMasterDTO>();
	Database db = new Database();
	try {
	    // SELECT * FROM station_master WHERE user_id= ? or STATION_ID in
	    // (select station_id from station_share where user_id = ?)
	    String query = SQLConstants.GET_STATION_FOR_USER.toLowerCase();
	    query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    stationList = getStationList(db);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return stationList;
    }

    private LinkedList<StationMasterDTO> getStationList(Database db) throws Exception {
	LinkedList<StationMasterDTO> stationList = new LinkedList<StationMasterDTO>();
	while (db.cRowSet.next()) {
	    StationMasterDTO station = new StationMasterDTO();
	    station.stationId = db.cRowSet.getString("STATION_ID");
	    station.userId = db.cRowSet.getString("USER_ID");
	    station.musicPrefId = db.cRowSet.getString("MUSIC_PREF_ID");
	    station.activityId = db.cRowSet.getString("ACTIVITY_ID");
	    station.stationName = db.cRowSet.getString("STATION_NAME");
	    station.privilege = db.cRowSet.getString("PRIVILEGE");
	    station.thumbnail = db.cRowSet.getString("THUMBNAIL");
	    stationList.add(station);
	}
	return stationList;
    }

    public boolean deleteStationItem(StationMasterDTO dto) throws Exception {

	if (stationExistsForUser(dto.stationId, dto.userId)) {
	    Database db = new Database();
	    db.setAutoCommitFalse();
	    try {

		LinkedList<String> stationItems = dto.stationItems;
		if (stationItems != null && stationItems.size() > 0) {
		    LinkedList<String> mediaIdList = new LinkedList<String>();
		    for (String item : stationItems) {
			if (!mediaIdList.contains(item)) {
			    mediaIdList.add(item);
			}
		    }
		    for (String mediaId : mediaIdList) {
			int count = deleteStationItem(db, dto.stationId, mediaId);
			if (count <= 0) {
			    db.rollBack();

			    throw new SystemException(ErrorCodes.INVALID_STATION_ITEM,
				    ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
			}
		    }
		} else {
		    throw new SystemException(ErrorCodes.EMPTY_LIST, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
		db.commit();
		LOG.error("The transaction was successfully executed");
		return true;
	    } catch (Exception e) {
		try {
		    // We rollback the transaction, atomicity!
		    db.rollBack();

		    LOG.error(e.getMessage());
		    LOG.error("The transaction was rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		} catch (Exception e1) {
		    LOG.error("There was an error making a rollback");
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
	    } finally {
		closeDbConnection(db);
	    }
	} else {
	    throw new SystemException(ErrorCodes.STATION_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
    }

    private int deleteStationItem(Database db, String stationId, String mediaId) throws Exception {
	// DELETE FROM STATION_ITEMS WHERE STATION_ID=? AND MEDIA_ID=?

	String query = SQLConstants.DELETE_STATION_ITEM.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(stationId);
	params.add(mediaId);
	return db.executeUpdate(query, params);
    }

    public HashMap<String, Object> getStationItems(StationItemPaginatedRequest user) throws Exception {

	HashMap<String, Object> objectMap = new HashMap<String, Object>();
	LinkedList<MediaDTO> stationItems = new LinkedList<MediaDTO>();
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_NEXT_STATION_ITEMS.toLowerCase();
	    query += " limit " + (user.startRange - 1) + "," + (user.endRange - user.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(user.userId);
	    paramList.add(user.stationId);
	    paramList.add(user.userId);
	    paramList.add(Constants.PRIVATE_VISIBILITY);
	    db.executeQuery(query, paramList);
	    LinkedList<String> stationMediaIdList = new LinkedList<String>();
	    while (db.cRowSet.next()) {
		MediaDTO media = getMediaFromResultSet(db, user.userId);
		String stationMediaId = db.cRowSet.getString("id_from_station");
		if (!stationMediaIdList.contains(stationMediaId)) {
		    stationMediaIdList.add(stationMediaId);
		}
		stationItems.add(media);
	    }
	    objectMap.put(Constants.ALL_STATION_ITEMS, stationItems);
	    objectMap.put(Constants.STATION_ITEMS, stationMediaIdList);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return objectMap;
    }

    public LinkedList<FeedResponseDTO> searchMedia(SearchPaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	Database db = new Database();
	try {
	    String searchString = dto.searchString;
	    if (StringUtils.isEmpty(searchString)) {
		searchString = " ";
	    }
	    searchString = searchString.toLowerCase();
	    String query = SQLConstants.SEARCH_USER_MEDIA.toLowerCase();
	    if (dto.isFeatured != null && dto.isFeatured.length() > 0) {
		query += SQLConstants.SEARCH_MEDIA_FEATURED + " '" + dto.isFeatured + "' ";
	    }
	    if (!dto.isAdmin) {
		query += SQLConstants.MEDIA_FLAG;
	    }
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(searchString);
	    paramList.add(searchString);
	    paramList.add(searchString);
	    paramList.add(searchString);
	    paramList.add(dto.targetUserId);
	    paramList.add(dto.targetUserId);
	    if (!StringUtils.isEmpty(dto.postType)) {
		query += SQLConstants.POST_TYPE_FILTER;
		paramList.add(dto.postType);

	    }
	    query += " order by m.created_date desc  limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);

	    LOG.debug("query>>>>" + query);
	    db.executeQuery(query, paramList);
	    LinkedList<FeedResponseDTO> responseList = feedResponseProduce(db, dto.userId);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public LinkedList<FeedResponseDTO> getPostsUserTaggedIn(SearchPaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	Database db = new Database();
	try {
	    String targetUserId = dto.targetUserId;

	    String query = SQLConstants.GET_POSTS_USER_TAGGED_IN.toLowerCase();
	    query = query.replaceAll(Constants.USER_ID_OBJ, "@" + targetUserId);
	    query = query.replaceAll(Constants.USER_ID_TAG, "#" + targetUserId);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    query += " order by m.created_date desc  limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    db.executeQuery(query, paramList);
	    LinkedList<FeedResponseDTO> responseList = feedResponseProduce(db, dto.userId);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public LinkedList<FeedResponseDTO> getSeriesMedia(SeriesPaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_MEDIA_IN_SERIES.toLowerCase();
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.seriesId);
	    query += " order by m.created_date desc  limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    db.executeQuery(query, paramList);
	    LinkedList<FeedResponseDTO> responseList = feedResponseProduce(db, dto.userId);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public LinkedList<FeedResponseDTO> getScheduledContent(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(dto);
	Database db = new Database();
	try {
	    String searchString = dto.searchString;
	    if (!StringUtils.isEmpty(searchString)) {
		searchString = searchString.toLowerCase();
	    } else {
		searchString = "";
	    }
	    String query = SQLConstants.GET_SCHEDULED_DETAILS.toLowerCase();
	    searchString = StringEscapeUtils.escapeSql(searchString);
	    query = query.replaceAll(Constants.STATION_NAME.toLowerCase(), searchString);
	    query += " order by m.created_date desc  limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    db.executeQuery(query, paramList);
	    LinkedList<FeedResponseDTO> responseList = feedResponseProduce(db, dto.userId);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public MediaDTO getScheduledMedia(MediaPaginatedRequest dto) throws Exception {
	Database db = new Database();
	try {

	    String query = SQLConstants.GET_SCHEDULED_MEDIA.toLowerCase();

	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.mediaId);
	    db.executeQuery(query, paramList);

	    if (db.cRowSet.next()) {
		String mediaId = db.cRowSet.getString("MEDIA_ID");
		MediaDTO mediaDTO = new MediaDTO();
		mediaDTO.mediaId = mediaId;
		return mediaDTO;
	    }
	    return null;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public String scheduledMediaExists(MediaPaginatedRequest dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.SCHEDULED_MEDIA_EXISTS.toLowerCase();

	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.mediaId);
	    db.executeQuery(query, paramList);

	    if (db.cRowSet.next()) {
		return db.cRowSet.getString("schedule_id");
	    }
	    return null;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public String addScheduledContent(MediaPaginatedRequest dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.INSERT_SCHEDULED_MEDIA.toLowerCase();
	    LinkedList<Object> params = new LinkedList<>();
	    String scheduledId = dto.userId + "_" + dto.mediaId;
	    params.add(scheduledId);
	    params.add(dto.userId);
	    params.add(dto.mediaId);
	    params.add(dto.mediaId);
	    params.add(dto.userId);
	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return scheduledId;
	    }

	} catch (Exception e) {
	    try {
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public String createSeries(SeriesPaginatedRequest dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.INSERT_SERIES.toLowerCase();
	    String seriesId = DAOHelper.guidGenerator(Constants.SERIES, "");

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(seriesId);
	    params.add(dto.title);
	    params.add(dto.description);
	    params.add(dto.thumbnail);
	    params.add(dto.director);
	    params.add(dto.producer);
	    params.add(dto.cast);

	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return seriesId;
	    }

	} catch (Exception e) {
	    try {
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public LinkedList<SeriesPaginatedRequest> getSeries(SeriesPaginatedRequest dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_SERIES.toLowerCase();
	    query += " order by created_date desc limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
//    	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    LinkedList<SeriesPaginatedRequest> responseList = seriesResponseProduce(db);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    private LinkedList<SeriesPaginatedRequest> seriesResponseProduce(Database db) throws Exception {
	LinkedList<SeriesPaginatedRequest> responseList = new LinkedList<SeriesPaginatedRequest>();
	while (db.cRowSet.next()) {
	    SeriesPaginatedRequest series = new SeriesPaginatedRequest();
	    series.seriesId = db.cRowSet.getString("series_id");
	    series.title = db.cRowSet.getString("title");
	    series.description = db.cRowSet.getString("description");
	    series.thumbnail = db.cRowSet.getString("thumbnail");
	    series.director = db.cRowSet.getString("director");
	    series.producer = db.cRowSet.getString("producer");
	    series.cast = db.cRowSet.getString("cast");
	    responseList.add(series);
	}
	return responseList;
    }

    public LinkedList<ScheduledDTO> getUserScheduledContent(PaginatedRequest dto) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_USER_SCHEDULED_MEDIA.toLowerCase();
	    query += " order by created_date desc limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    db.executeQuery(query, paramList);
	    LinkedList<ScheduledDTO> responseList = scheduleResponseProduce(db);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public boolean deleteScheduledContent(ScheduledDTO dto) throws Exception {
	Database db = new Database();
	try {

	    String query = SQLConstants.DELETE_SCHEDULED_MEDIA.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.scheduleId);

	    int count = db.executeUpdate(query, params);
	    if (count > 0) {
		return true;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return false;
    }

    public LinkedList<FeedResponseDTO> getUserMedia(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	validateStartEndRange(dto);
	Database db = new Database();
	try {
	    String query = SQLConstants.SEARCH_MEDIA.toLowerCase();
	    if (!dto.isAdmin) {
		query += SQLConstants.MEDIA_FLAG;
	    }
	    query += " order by m.created_date desc limit " + (dto.startRange - 1) + ","
		    + (dto.endRange - dto.startRange + 1);
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.userId);
	    paramList.add(dto.targetUserId);
	    db.executeQuery(query, paramList);
	    LinkedList<FeedResponseDTO> responseList = feedResponseProduce(db, dto.userId);
	    return responseList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    private LinkedList<FeedResponseDTO> feedResponseProduce(Database db, String userId) throws Exception {
	LinkedList<FeedResponseDTO> responseList = new LinkedList<FeedResponseDTO>();
	while (db.cRowSet.next()) {
	    FeedResponseDTO responseDTO = new FeedResponseDTO();
	    RegisterUserDTO user = new RegisterUserDTO();
	    user.userId = db.cRowSet.getString("USER_ID");
	    user.firstName = db.cRowSet.getString("FIRST_NAME");
	    user.lastName = db.cRowSet.getString("LAST_NAME");
	    user.email = db.cRowSet.getString("EMAIL");
	    user.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
	    user.location = db.cRowSet.getString("LOCATION");
	    user.category = db.cRowSet.getString("CATEGORY");
	    user.userType = db.cRowSet.getString("USER_TYPE");
	    user.bio = db.cRowSet.getString("BIO");
	    user.businessBio = db.cRowSet.getString("BUSINESS_BIO");
	    MediaDTO media = getMediaFromResultSet(db, userId);
	    try {
		// If distance is not queried.
		responseDTO.distance = db.cRowSet.getString("DIST");
	    } catch (Exception e) {
		// eating up the exception
	    }
	    responseDTO.user = user;
	    responseDTO.media = media;
	    responseList.add(responseDTO);
	}
	return responseList;
    }

    private LinkedList<ScheduledDTO> scheduleResponseProduce(Database db) throws Exception {
	LinkedList<ScheduledDTO> responseList = new LinkedList<ScheduledDTO>();
	while (db.cRowSet.next()) {
	    ScheduledDTO schedule = new ScheduledDTO();
	    schedule.userId = db.cRowSet.getString("USER_ID");
	    schedule.scheduleId = db.cRowSet.getString("schedule_id");
	    schedule.mediaId = db.cRowSet.getString("MEDIA_ID");
	    responseList.add(schedule);
	}
	return responseList;
    }

    public LinkedList<MediaLibraryDTO> getMediaLibrary(MediaLibraryPaginatedRequest libReq) throws Exception {

	// if (libReq.searchString == null || libReq.searchString.length()==0) {
	// throw new SystemException(ErrorCodes.EMPTY_SEARCH_STRING);
	// }
	//
	String searchString = libReq.searchString;
	StringBuilder query = new StringBuilder();
	query.append("select * from media_library  ");
	if (libReq.searchString != null && libReq.searchString.length() != 0) {
	    query.append(" where ");
	    String[] search = searchString.split("\\s+");
	    for (int i = 0; i < search.length; i++) {
		if (i != 0) {
		    query.append(" and ");
		}
		String obj = search[i];
		query.append(
			"( CONCAT_WS(' ',lower(title),lower(media_key),lower(artist),lower(album),lower(genre)) like '%"
				+ obj.toLowerCase() + "%' )");
	    }
	}

	query.append(" limit " + (libReq.startRange - 1) + "," + (libReq.endRange - libReq.startRange + 1));
	Database db = new Database();
	try {
	    LinkedList<MediaLibraryDTO> mediaLibList = new LinkedList<MediaLibraryDTO>();

	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		MediaLibraryDTO media = new MediaLibraryDTO();
		media.mediaKey = db.cRowSet.getString("media_key");
		media.title = db.cRowSet.getString("title");
		media.album = db.cRowSet.getString("album");
		media.artist = db.cRowSet.getString("artist");
		media.length = db.cRowSet.getInt("length");
		media.genre = db.cRowSet.getString("genre");
		mediaLibList.add(media);
	    }
	    return mediaLibList;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public String createArtist(ArtistDTO dto) throws Exception {

	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    String artistId = DAOHelper.guidGenerator(Constants.ARTIST_PREFIX, "");
	    String query = SQLConstants.CREATE_ARTIST.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(artistId);
	    params.add(dto.firstName + " " + dto.lastName);
	    params.add(dto.artistDescription);
	    params.add(dto.birthday);
	    params.add(dto.tags);
	    params.add(dto.nationality);
	    params.add(dto.age);
	    params.add(dto.genre);
	    params.add(dto.firstName);
	    params.add(dto.lastName);
	    params.add(dto.nickName);
	    params.add(dto.profilePicture);
	    params.add(dto.fbUrl);
	    params.add(dto.instagramUrl);
	    params.add(dto.pintrestUrl);
	    params.add(dto.youtubeUrl);
	    params.add(dto.bon2Url);
	    params.add(dto.userId);
	    params.add(dto.generalCategory);

	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    boolean insertedArtistMedia = insertArtistMediaUtil(dto.mediaList, db, artistId, dto.userId);
	    if (!insertedArtistMedia) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    db.commit();
	    System.out.println("The transaction was successfully executed");
	    return artistId;
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    try {
		db.rollBack();
	    } catch (Exception e1) {
		e1.printStackTrace();
		LOG.error(e1.getMessage());
	    }
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    private boolean insertArtistMediaUtil(List<ArtistMediaDTO> artistMediaList, Database db, String artistId,
	    String userId) throws Exception {
	if (artistMediaList != null && artistMediaList.size() > 0) {
	    for (ArtistMediaDTO artistMediaDTO : artistMediaList) {
		String artistMediaId = DAOHelper.guidGenerator(Constants.ARTIST_PREFIX, "");
		String query = SQLConstants.INSERT_ARTIST_MEDIA.toLowerCase();
		LOG.debug("query>>>>" + query);
		LinkedList<Object> params = new LinkedList<>();

		params.add(artistMediaId);
		params.add(artistId);
		params.add(artistMediaDTO.mediaName);
		params.add(userId);
		params.add(artistMediaDTO.description);
		params.add(artistMediaDTO.tags);
		int count = db.executeUpdate(query, params);
		if (count <= 0) {
		    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		}
	    }
	}
	return true;
    }

    public boolean deleteArtist(ArtistDTO dto) throws Exception {
	Database db = new Database();
	db.setAutoCommitFalse();
	try {

	    String query = SQLConstants.DELETE_ARTIST.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.artistId);

	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		return false;
	    }
	    deleteArtistMediaUtil(dto.artistId, db);
	    db.commit();
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		db.rollBack();
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
	return true;
    }

    public boolean deleteArtistMediaUtil(String artistId, Database db) throws Exception {
	String query = SQLConstants.DELETE_ARTIST_MEDIA.toLowerCase();
	LOG.debug("query>>>>" + query);
	LinkedList<Object> params = new LinkedList<>();
	params.add(artistId);
	db.executeUpdate(query, params);
	return true;
    }

    public boolean updateArtistUser(ArtistDTO dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.UPDATE_ARTIST_USER.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();

	    params.add(dto.artistUserId);
	    params.add(dto.artistId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		return false;
	    }
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean updateArtist(ArtistDTO dto) throws Exception {

	Database db = new Database();
	db.setAutoCommitFalse();
	try {
	    String query = SQLConstants.UPDATE_ARTIST.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.firstName + " " + dto.lastName);
	    params.add(dto.artistDescription);
	    params.add(dto.birthday);
	    params.add(dto.tags);
	    params.add(dto.nationality);
	    params.add(dto.age);
	    params.add(dto.genre);
	    params.add(dto.firstName);
	    params.add(dto.lastName);
	    params.add(dto.nickName);
	    params.add(dto.profilePicture);
	    params.add(dto.fbUrl);
	    params.add(dto.instagramUrl);
	    params.add(dto.pintrestUrl);
	    params.add(dto.youtubeUrl);
	    params.add(dto.bon2Url);
	    params.add(dto.generalCategory);
	    params.add(dto.artistId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    deleteArtistMediaUtil(dto.artistId, db);
	    boolean insertedArtistMedia = insertArtistMediaUtil(dto.mediaList, db, dto.artistId, dto.userId);
	    if (!insertedArtistMedia) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    db.commit();
	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public ArtistDTO getArtistDetails(ArtistDTO dto) throws Exception {
	StringBuilder query = new StringBuilder();
	query.append(SQLConstants.GET_ARTIST_DETAILS);
	Database db = new Database();
	ArtistDTO artistDTO = null;
	try {
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.artistId);
	    LOG.debug("query>>>>" + query.toString());
	    db.executeQuery(query.toString(), paramList);
	    LinkedList<ArtistDTO> artistList = artistProduce(db);
	    artistDTO = artistList.get(0);
	    artistDTO.mediaList = getArtistMediaList(dto.artistId);
	} catch (Exception e) {
	    LOG.error("Artist Does not exist");
	} finally {
	    closeDbConnection(db);
	}
	return artistDTO;
    }

    private List<ArtistMediaDTO> getArtistMediaList(String artistId) throws Exception {
	StringBuilder query = new StringBuilder();
	query.append(SQLConstants.GET_ARTIST_MEDIA);
	Database db = new Database();
	LinkedList<ArtistMediaDTO> artistList = new LinkedList<>();
	try {
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(artistId);
	    LOG.debug("query>>>>" + query.toString());
	    db.executeQuery(query.toString(), paramList);
	    artistList = artistMediaProduce(db);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return artistList;

    }

    public LinkedList<ArtistDTO> searchArtist(SearchArtistDTO dto) throws Exception {
	StringBuilder query = new StringBuilder();
	if (dto.searchMedia) {
	    query.append(SQLConstants.SEARCH_ARTIST_MEDIA);

	} else {
	    query.append(SQLConstants.SEARCH_ARTIST);
	}
	String searchString = dto.searchString;
	if (searchString == null || searchString.length() <= 0) {
	    searchString = " ";
	}
	// query.append(" '"+searchString+"'");
	query.append(" limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1));
	LinkedList<ArtistDTO> artistList = new LinkedList<ArtistDTO>();
	Database db = new Database();
	try {
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(searchString);
	    LOG.debug("query>>>>" + query.toString());
	    db.executeQuery(query.toString(), paramList);
	    artistList = artistProduce(db);
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return artistList;

    }

    public LinkedList<RegisterUserDTO> getArtistFollowers(ArtistSearchDTO dto) throws Exception {
	String query = SQLConstants.GET_ARTIST_FOLLOWERS
		+ (" limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1));

	LinkedList<RegisterUserDTO> userList = new LinkedList<RegisterUserDTO>();
	Database db = new Database();
	try {
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.artistId);
	    LOG.debug("query>>>>" + query.toString());
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		RegisterUserDTO user = userProduce(db, false);
		userList.add(user);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return userList;

    }

    private LinkedList<ArtistMediaDTO> artistMediaProduce(Database db) throws Exception {
	LinkedList<ArtistMediaDTO> artistList = new LinkedList<ArtistMediaDTO>();
	while (db.cRowSet.next()) {
	    ArtistMediaDTO artistMediaDTO = new ArtistMediaDTO();
	    artistMediaDTO.description = db.cRowSet.getString("media_name");
	    artistMediaDTO.mediaName = db.cRowSet.getString("DESCRIPTION");
	    artistMediaDTO.tags = db.cRowSet.getString("TAGS");

	    artistList.add(artistMediaDTO);
	}

	return artistList;
    }

    private LinkedList<ArtistDTO> artistProduce(Database db) throws Exception {
	LinkedList<ArtistDTO> artistList = new LinkedList<ArtistDTO>();
	while (db.cRowSet.next()) {
	    ArtistDTO artistDTO = getArtistFromRowset(db);
	    artistList.add(artistDTO);
	}

	return artistList;
    }

    private ArtistDTO getArtistFromRowset(Database db) throws Exception {
	ArtistDTO artistDTO = new ArtistDTO();
	artistDTO.artistId = db.cRowSet.getString("ARTIST_ID");
	artistDTO.artistDescription = db.cRowSet.getString("ARTIST_DESCRIPTION");
	artistDTO.birthday = db.cRowSet.getTimestamp("BIRTHDAY");
	artistDTO.tags = db.cRowSet.getString("TAGS");
	artistDTO.nationality = db.cRowSet.getString("NATIONALITY");
	artistDTO.age = db.cRowSet.getString("AGE");
	artistDTO.genre = db.cRowSet.getString("GENERE");
	artistDTO.firstName = db.cRowSet.getString("FIRST_NAME");
	artistDTO.lastName = db.cRowSet.getString("LAST_NAME");
	artistDTO.nickName = db.cRowSet.getString("NICK_NAME");
	artistDTO.profilePicture = db.cRowSet.getString("PROFILE_PICTURE");
	artistDTO.fbUrl = db.cRowSet.getString("FB_URL");
	artistDTO.instagramUrl = db.cRowSet.getString("INSTAGRAM_URL");
	artistDTO.pintrestUrl = db.cRowSet.getString("PINTREST_URL");
	artistDTO.youtubeUrl = db.cRowSet.getString("YOUTUBE_URL");
	artistDTO.bon2Url = db.cRowSet.getString("BON2_URL");
	artistDTO.uploader = db.cRowSet.getString("uploader");
	artistDTO.artistUserId = db.cRowSet.getString("artist_user_id");
	artistDTO.generalCategory = db.cRowSet.getString("general_category");

	return artistDTO;
    }

    public RegisterUserDTO forgotPassword(String email) throws Exception {
	RegisterUserDTO dto = null;
	Database db = new Database();
	try {
	    String query = SQLConstants.FORGOT_PASSWORD.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(email);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		// dto = new RegisterUserDTO();

		dto = new RegisterUserDTO();
		dto.password = db.cRowSet.getString("PASSWORD");
		dto.email = db.cRowSet.getString("EMAIL");
		try {

		} catch (Exception e) {
		    LOG.error("Error While Closing connection.");
		}

		return dto;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

    public boolean resetPassword(RegisterUserDTO dTO) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.RESET_PASSWORD.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();

	    params.add(dTO.newPassword);
	    params.add(dTO.email);
	    params.add(dTO.password);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		return false;
	    }
	    LOG.debug("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public LinkedList<ProductDTO> searchProduct(SearchPaginatedRequest dto) throws Exception {
	String query = SQLConstants.SEARCH_PRODUCT_3.toLowerCase();
	String searchString = dto.searchString;
	if (StringUtils.isEmpty(dto.searchString)) {
	    throw new SystemException(ErrorCodes.EMPTY_SEARCH_STRING, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean nonThree = false;
	if (searchString.length() > 3) {
	    query = SQLConstants.SEARCH_PRODUCT.toLowerCase();
	    nonThree = true;
	}
	query += (" limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1));
	LinkedList<ProductDTO> productList = new LinkedList<ProductDTO>();
	Database db = new Database();
	try {
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(searchString);
	    if (nonThree) {
		paramList.add(searchString);
		paramList.add(searchString);
	    }
	    LOG.debug("query>>>>" + query.toString());

	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		ProductDTO product = new ProductDTO();
		product.productId = db.cRowSet.getString("product_id");
		product.productName = db.cRowSet.getString("product_name");
		product.brandName = db.cRowSet.getString("brand_name");
		product.productDescription = db.cRowSet.getString("description");
		product.website = db.cRowSet.getString("website");
		product.picture = db.cRowSet.getString("picture");
		product.shopLink1 = db.cRowSet.getString("shop_link_1");
		product.shopLink2 = db.cRowSet.getString("shop_link_2");
		product.shopLink3 = db.cRowSet.getString("shop_link_3");
		product.shopLink4 = db.cRowSet.getString("shop_link_4");
		product.shopLink5 = db.cRowSet.getString("shop_link_5");
		product.generalCategory = db.cRowSet.getString("general_category");
		product.sku = db.cRowSet.getString("sku");
		productList.add(product);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return productList;

    }

    public boolean createProduct(ProductDTO dto) throws Exception {

	Database db = new Database();
	try {
	    // INSERT INTO product_master( `product_name`, `brand_name`,
	    // `website`, `picture`, `shop_link_1`, `shop_link_2`,
	    // `shop_link_3`,
	    // `shop_link_4`, `shop_link_5`, `description`) VALUES
	    // (?,?,?,?,?,?,?,?,?)"
	    String query = SQLConstants.CREATE_PRODUCT.toLowerCase();
	    LinkedList<Object> params = new LinkedList<>();
	    LOG.debug("query>>>>" + query);
	    params.add(dto.productName);
	    params.add(dto.brandName);
	    params.add(dto.website);
	    params.add(dto.picture);
	    params.add(dto.shopLink1);
	    params.add(dto.shopLink2);
	    params.add(dto.shopLink3);
	    params.add(dto.shopLink4);
	    params.add(dto.shopLink5);
	    params.add(dto.productDescription);
	    params.add(dto.generalCategory);
	    params.add(dto.sku);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    LOG.debug("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

    }

    public boolean deleteProduct(ProductDTO dto) throws Exception {

	Database db = new Database();
	try {
	    String query = SQLConstants.DELETE_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.productId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    try {
		// We rollback the transaction, atomicity!
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    public boolean updateProduct(ProductDTO dto) throws Exception {
	Database db = new Database();
	try {

	    String query = SQLConstants.UPDATE_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.productName);
	    params.add(dto.brandName);
	    params.add(dto.website);
	    params.add(dto.picture);
	    params.add(dto.shopLink1);
	    params.add(dto.shopLink2);
	    params.add(dto.shopLink3);
	    params.add(dto.shopLink4);
	    params.add(dto.shopLink5);
	    params.add(dto.productDescription);
	    params.add(dto.generalCategory);
	    params.add(dto.sku);
	    params.add(dto.productId);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		return false;
	    }
	    return true;
	} catch (Exception e) {

	    LOG.error(e.getMessage());
	    try {
		LOG.error(e.getMessage());
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}

    }

    public MediaDTO getUserMediaMetadata(SearchMediaRequest dto) throws Exception {
	Database db = new Database();
	MediaDTO mediaDTO = null;
	try {

	    String query = SQLConstants.GET_USER_MEDIA_META_DATA.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.mediaId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		mediaDTO = new MediaDTO();
		mediaDTO.metadataFilePath = db.cRowSet.getString("METADATA_FILE_PATH");
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return mediaDTO;
    }

    public String addShopifyProduct(ShopifyProductDTO dto) throws Exception {
	Database db = new Database();

	try {
	    String shopifyProductId = DAOHelper.guidGenerator(Constants.SHOPIFY_PREFIX, "");

	    String query = SQLConstants.INSERT_SHOPIFY_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(shopifyProductId);
	    params.add(dto.shopifyId);
	    params.add(dto.title);
	    params.add(dto.bodyHtml);
	    params.add(dto.vendor);
	    params.add(dto.tags);
	    params.add(dto.imageId);
	    params.add(dto.imageSrc);
	    params.add(dto.businessUsername);
	    params.add(dto.price);
	    params.add(dto.userId);
	    params.add(dto.buyNowNrl);
	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }

	    LOG.error("The transaction was successfully executed");
	    return shopifyProductId;
	} catch (Exception e) {
	    try {
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean updateShopifyProduct(ShopifyProductDTO dto) throws Exception {
	Database db = new Database();

	try {
	    String query = SQLConstants.UPDATE_SHOPIFY_PRODUCT.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.shopifyId);
	    params.add(dto.title);
	    params.add(dto.bodyHtml);
	    params.add(dto.vendor);
	    params.add(dto.tags);
	    params.add(dto.imageId);
	    params.add(dto.imageSrc);
	    params.add(dto.businessUsername);
	    params.add(dto.price);
	    params.add(dto.userId);
	    params.add(dto.buyNowNrl);
	    params.add(dto.shopifyProductId);

	    int count = db.executeUpdate(query, params);
	    if (count <= 0) {
		return false;
	    }

	    LOG.error("The transaction was successfully executed");
	    return true;
	} catch (Exception e) {
	    try {
		LOG.error(e.getMessage());
		LOG.error("The transaction was rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    } catch (Exception e1) {
		LOG.error("There was an error making a rollback");
		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} finally {
	    closeDbConnection(db);
	}
    }

    public boolean deleteShopify(ShopifyProductDTO dto) throws Exception {

	Database db = new Database();
	try {

	    String query = SQLConstants.DELETE_SHOPIFY.toLowerCase();
	    LOG.debug("query>>>>" + query);

	    LinkedList<Object> params = new LinkedList<>();
	    params.add(dto.shopifyProductId);
	    int count = db.executeUpdate(query, params);
	    if (count == 0) {
		return false;
	    }
	    LOG.error("The transaction was successfully executed");
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}

	return true;
    }

    public LinkedList<ShopifyProductDTO> searchShopifyProduct(UserSearchPaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	String query = SQLConstants.SEARCH_SHOPIFY.toLowerCase();
	query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	Database db = new Database();

	LinkedList<ShopifyProductDTO> shopList = new LinkedList<ShopifyProductDTO>();
	try {
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.searchString);
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		ShopifyProductDTO shopify = shopifyProduce(db);
		shopList.add(shopify);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return shopList;
    }

    public LinkedList<ShopifyProductDTO> getShopifyByVendor(UserSearchPaginatedRequest dto) throws Exception {
	validateStartEndRange(dto);
	String query = SQLConstants.GET_SHOPIFY_BY_VENDOR.toLowerCase();
	query += " limit " + (dto.startRange - 1) + "," + (dto.endRange - dto.startRange + 1);
	Database db = new Database();

	LinkedList<ShopifyProductDTO> shopList = new LinkedList<ShopifyProductDTO>();
	try {
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(dto.vendor.toLowerCase());
	    db.executeQuery(query.toString(), paramList);
	    while (db.cRowSet.next()) {
		ShopifyProductDTO shopify = shopifyProduce(db);
		shopList.add(shopify);
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return shopList;
    }

    private ShopifyProductDTO shopifyProduce(Database db) throws SQLException {
	ShopifyProductDTO shopify = new ShopifyProductDTO();
	shopify.shopifyProductId = db.cRowSet.getString("shopify_product_id");
	shopify.shopifyId = db.cRowSet.getString("shopify_id");
	shopify.title = db.cRowSet.getString("title");
	shopify.shopifyId = db.cRowSet.getString("shopify_id");
	shopify.bodyHtml = db.cRowSet.getString("body_html");
	shopify.vendor = db.cRowSet.getString("vendor");
	shopify.tags = db.cRowSet.getString("tags");
	shopify.imageId = db.cRowSet.getString("image_id");
	shopify.imageSrc = db.cRowSet.getString("image_src");
	shopify.businessUsername = db.cRowSet.getString("business_username");
	shopify.price = db.cRowSet.getString("price");
	shopify.createdBy = db.cRowSet.getString("created_by");
	shopify.buyNowNrl = db.cRowSet.getString("buy_now_url");
	return shopify;
    }

    public String getShopifyIdExists(String shopifyId) throws Exception {
	Database db = new Database();
	try {
	    String query = SQLConstants.GET_SHOPIFY_ID.toLowerCase();
	    LOG.debug("query>>>>" + query);
	    LinkedList<Object> paramList = new LinkedList<Object>();
	    paramList.add(shopifyId);
	    db.executeQuery(query, paramList);
	    if (db.cRowSet.next()) {
		String shopifyProductId = db.cRowSet.getString("shopify_product_id");
		return shopifyProductId;
	    }
	} catch (Exception e) {
	    LOG.error("There was an error in DB query " + e.getMessage());
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	} finally {
	    closeDbConnection(db);
	}
	return null;
    }

}
