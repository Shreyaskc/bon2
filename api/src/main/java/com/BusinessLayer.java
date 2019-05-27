package com;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.common.CommonUtils;
import com.common.ConfigReader;
import com.common.Messaging;
import com.constants.Constants;
import com.constants.ErrorCodes;
import com.dao.DAO;
import com.dao.helper.DAOHelper;
import com.dto.ActivityMasterDTO;
import com.dto.ArtistDTO;
import com.dto.ArtistSearchDTO;
import com.dto.BusinessProductDTO;
import com.dto.FeedResponseDTO;
import com.dto.MailDTO;
import com.dto.MediaCommentDTO;
import com.dto.MediaDTO;
import com.dto.MediaLibraryDTO;
import com.dto.MediaLibraryPaginatedRequest;
import com.dto.MediaMetadataDTO;
import com.dto.MediaPaginatedRequest;
import com.dto.MediaPostDTO;
import com.dto.MediaTargetUserDTO;
import com.dto.MessageDTO;
import com.dto.MusicPrefDto;
import com.dto.NearbyPaginatedRequest;
import com.dto.NotificationDTO;
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
import com.dto.ShopifyProductDTO;
import com.dto.StationItemPaginatedRequest;
import com.dto.StationMasterDTO;
import com.dto.UserContactRequestDTO;
import com.dto.UserMediaPaginatedRequest;
import com.dto.UserSearchPaginatedRequest;
import com.dto.WishlistDTO;
import com.exception.SystemException;
import java.util.Base64;

/**
 * Validation Service busines layer.
 * 
 * @author Shreyas
 *
 */
public class BusinessLayer {

    private DAO B2Dao;

    /**
     * default Constructor initializing the logger and the DAO object
     * 
     * @throws SQLException
     * @throws DALException
     * @throws FileParsingException
     */
    public BusinessLayer() {

	B2Dao = new DAO();

    }

    public LinkedList<String> getTestList() throws Exception {
	return B2Dao.getTestList();
    }

    public LinkedList<MusicPrefDto> getMusicPrefList() throws Exception {
	return B2Dao.getMusicPrefList();
    }

    public RegisterUserDTO registerUser(RegisterUserDTO regDto) throws Exception {
	regDto.accessToken = DAOHelper.guidGenerator("", "");
	boolean isStationPartner = false;
	if (!StringUtils.isEmpty(regDto.isStationPartner) && Constants.Y.equalsIgnoreCase(regDto.isStationPartner)) {
	    isStationPartner = true;
	    regDto.isActive = Constants.N;
	} else {
	    regDto.isStationPartner = Constants.N;
	    regDto.isActive = Constants.Y;
	}
	RegisterUserDTO dto = B2Dao.registerUser(regDto, false);
	System.out.println("User ID :" + dto.userId);
	if (dto.userId != null && dto.userId.length() > 0) {
	    String name = CommonUtils.getName(regDto.firstName, regDto.lastName, dto.userId);
	    String text = "Hi " + name + "!  Thank you for subscribing to BON2!  ";
	    MailDTO mailDTO = new MailDTO();
	    mailDTO.activityText = text;
	    mailDTO.activityTitle = text;
	    mailDTO.subject = text;
	    mailDTO.isWelcome = true;
	    mailDTO.email = regDto.email;
	    SendMailSSL.sendMailFromTemplate(mailDTO);

	    // If is station partner

	    if (isStationPartner) {

		mailDTO = new MailDTO();
		name = dto.firstName + " " + dto.lastName;
		String title = name + Constants.REGISERED_STATION_PARTNER + dto.partnerStationName;

		ConfigReader config = ConfigReader.getObject();
		String app = config.getAppConfig(Constants.APP_URL);
		String context = config.getAppConfig(Constants.REJECT_FLAG);
		String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
		String link = app + context + "/" + dto.userId + "/" + token;
		mailDTO.email = config.getAppConfig(Constants.BANTU_MAIL);
		mailDTO.subject = title;
		mailDTO.activityTitle = title;
		mailDTO.urlPostfix = "";
		mailDTO.activityText = title;
		mailDTO.btnText = Constants.APPROVE;
		mailDTO.btnUrl = link;

		SendMailSSL.sendMailFromTemplate(mailDTO);
	    }
	}
	return dto;
    }

    public boolean updateMusicPref(RegisterUserDTO regDto) throws Exception {
	return B2Dao.updateMusicPref(regDto);
    }

    public boolean updateUserFollowing(RegisterUserDTO regDto) throws Exception {
	if (!validateUser(regDto.userId, regDto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateUserFollowing(regDto);
    }

    public boolean updateBusinessFollowing(RegisterUserDTO regDto) throws Exception {
	if (!validateUser(regDto.userId, regDto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateBusinessFollowing(regDto);
    }

    public boolean updateArtistFollowing(RegisterUserDTO regDto) throws Exception {
	if (!validateUser(regDto.userId, regDto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateArtistFollowing(regDto);
    }

    public boolean addUserFollowing(RegisterUserDTO regDto) throws Exception {
	if (!validateUser(regDto.userId, regDto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean flag = B2Dao.addUserFollowing(regDto);
	try {

	    if (flag) {
		MediaTargetUserDTO user = B2Dao.getUser(regDto.userId);
		if (user.sendFollowNotification) {
		    String name = CommonUtils.getName(user.mFirstName, user.mLastName, user.mUserId);
		    MailDTO mailDTO = new MailDTO();
		    LinkedList<String> followingUserIdList = regDto.followingUserIdList;
		    for (String followingId : followingUserIdList) {
			MediaTargetUserDTO targetUser = B2Dao.getUser(followingId);
			String title = name + Constants.FOLLOWED_YOU;
			mailDTO.email = targetUser.mEmail;
			mailDTO.subject = name + Constants.FOLLOWED_YOU;
			mailDTO.urlPostfix = "";
			mailDTO.activityTitle = title;
			mailDTO.activityText = "";
			SendMailSSL.sendMailFromTemplate(mailDTO);
			Map<String, String> data = new HashMap<>();
			data.put(Constants.PAGE, Constants.FOLLOW);
			data.put(Constants.ID, regDto.userId);
			Messaging messaging = new Messaging();
			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.title = Constants.NEW_FOLLOWER;
			notificationDTO.body = mailDTO.subject;
			notificationDTO.data = data;
			notificationDTO.token = targetUser.notificationToken;
			messaging.sendCommonMessage(notificationDTO);
		    }
		}
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}
	return flag;
    }

    public boolean addBusinessFollowing(RegisterUserDTO regDto) throws Exception {
	if (!validateUser(regDto.userId, regDto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addBusinessFollowing(regDto);
    }

    public boolean addUserArtistFollowing(RegisterUserDTO regDto) throws Exception {
	if (!validateUser(regDto.userId, regDto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addUserArtistFollowing(regDto);
    }

    public boolean deleteUserFollowing(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteUserFollowing(dto);
    }

    public boolean deleteBusinessFollowing(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteBusinessFollowing(dto);
    }

    public boolean deleteArtistFollowing(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteArtistFollowing(dto);
    }

    public boolean validateUser(RegisterUserDTO regDto) throws Exception {
	return validateUser(regDto.userId, regDto.accessToken);
    }

    public boolean validateUser(String userId, String accessToken) throws Exception {
	return B2Dao.validateUser(userId, accessToken);
    }

    public RegisterUserDTO getUserDetailsWithToken(String userId, String accessToken) throws Exception {
	return B2Dao.getUserDetailsWithToken(userId, accessToken);
    }

    public boolean userIdExists(RegisterUserDTO regDto) throws Exception {
	return B2Dao.userIdExists(regDto.userId);
    }

    public boolean userEmailExists(RegisterUserDTO regDto) throws Exception {
	return B2Dao.userEmailExists(regDto);
    }

    public Object getExistingSocialMediaUser(RegisterUserDTO regDto) throws Exception {
	RegisterUserDTO dto = B2Dao.loginSocialMediaUser(regDto);
	if (dto == null) {
	    if (regDto.email != null && regDto.email.length() > 0) {
		RegisterUserDTO emailDto = B2Dao.getUserDetailsForEmail(regDto);
		if (emailDto == null) {
		    throw new SystemException(ErrorCodes.FALSE_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			    ErrorCodes.StatusCodes.FAILURE, null);
		} else {
		    boolean updateFlag = B2Dao.updateSocialIdForEmail(regDto);
		    if (updateFlag) {
			return emailDto;
		    } else {
			throw new SystemException(ErrorCodes.GENERIC_EXCEPTION,
				ConfigReader.getObject().getErrorConfig(), ErrorCodes.StatusCodes.FAILURE, null);
		    }
		}
	    } else {
		throw new SystemException(ErrorCodes.FALSE_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	} else {
	    return dto;
	}
    }

    public LinkedList<String> getUserMusicPrefList(RegisterUserDTO regDto) throws Exception {
	return B2Dao.getUserMusicPrefList(regDto.userId, regDto.accessToken);
    }

    public RegisterUserDTO loginUser(RegisterUserDTO dto) throws Exception {
	RegisterUserDTO regDTO = B2Dao.loginUser(dto, false);
	if (regDTO == null) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	RegisterUserDTO businessUser = new RegisterUserDTO();
	RegisterUserDTO artistUser = new RegisterUserDTO();
	if (!StringUtils.isEmpty(regDTO.businessUserId)) {
	    businessUser.userId = regDTO.businessUserId;
	    businessUser = B2Dao.getUserDetails(businessUser);
	    regDTO.businessUser = businessUser;
	}
	if (!StringUtils.isEmpty(regDTO.artistUserId)) {
	    artistUser.userId = regDTO.artistUserId;
	    artistUser = B2Dao.getUserDetails(artistUser);
	    regDTO.artistUser = artistUser;
	}
	return regDTO;
    }

    public boolean updateUserDetails(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateUserDetails(dto);
    }

    public boolean updateUserNotificationToken(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateUserNotificationToken(dto);
    }

    public MediaMetadataDTO getMediaMetadata(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (StringUtils.isEmpty(dto.targetUserId)) {
	    dto.targetUserId = dto.userId;
	}
	return B2Dao.getMediaMetadata(dto);
    }

    public MediaDTO getMediaData(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getMediaData(dto);
    }

    public boolean addMediaComment(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	MediaTargetUserDTO target = B2Dao.getMediaTargetUser(dto.userId, dto.mediaId);
	boolean flag = B2Dao.addMediaComment(dto, target);
	try {
	    if (flag) {

		if (target.sendCommentNotification) {
		    MailDTO mailDTO = new MailDTO();
		    String name = CommonUtils.getName(target.cFirstName, target.cLastName, dto.userId);
		    if (!dto.userId.equalsIgnoreCase(target.mUserId)) {
			if (!StringUtils.isEmpty(target.fileType)) {
			    target.fileType = target.fileType.replaceAll("-post", "");
			}
			String title = name + Constants.COMMENTED_ON + target.fileType;
			mailDTO.email = target.mEmail;
			mailDTO.subject = name + " Commented on your " + Constants.POST;
			mailDTO.activityTitle = title;
			mailDTO.urlPostfix = "?post=" + dto.mediaId;
			mailDTO.activityText = title + " : <b>" + target.title + "</b>";
			SendMailSSL.sendMailFromTemplate(mailDTO);
			Map<String, String> data = new HashMap<>();
			data.put(Constants.PAGE, Constants.COMMENT);
			data.put(Constants.ID, dto.mediaId);
			Messaging messaging = new Messaging();

			byte[] base64decodedBytes = Base64.getDecoder().decode(dto.comment);
			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.title = title;
			notificationDTO.body = new String(base64decodedBytes, "utf-8");
			notificationDTO.data = data;
			notificationDTO.token = target.notificationToken;
			messaging.sendCommonMessage(notificationDTO);
		    }
		}
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}
	return flag;
    }

    public boolean flagMedia(MediaDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean flag = B2Dao.flagMedia(dto);

	if (flag) {
	    MediaTargetUserDTO target = B2Dao.getMediaTargetUser(dto.userId, dto.mediaId);

	    MailDTO mailDTO = new MailDTO();
	    String name = CommonUtils.getName(target.cFirstName, target.cLastName, dto.userId);
	    if (!dto.userId.equalsIgnoreCase(target.mUserId)) {
		if (!StringUtils.isEmpty(target.fileType)) {
		    target.fileType = target.fileType.replaceAll("-post", "");
		}
		String title = name + Constants.FLAGGED + target.title;

		ConfigReader config = ConfigReader.getObject();
		String app = config.getAppConfig(Constants.APP_URL);
		String context = config.getAppConfig(Constants.REJECT_FLAG);
		String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
		String link = app + context + "/" + dto.mediaId + "/" + token;
		mailDTO.email = config.getAppConfig(Constants.MAIL_ADDRESS);
		mailDTO.subject = title;
		mailDTO.activityTitle = title;
		mailDTO.urlPostfix = "";
		mailDTO.activityText = title + " : <b>" + target.title + "</b>";
		mailDTO.btnText = Constants.REJECT;
		mailDTO.btnUrl = link;

		SendMailSSL.sendMailFromTemplate(mailDTO);
	    }
	}
	return flag;
    }

    public void unFlagMedia(MediaDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	MediaTargetUserDTO target = B2Dao.getMediaTargetUser(dto.userId, dto.mediaId);
	MailDTO mailDTO = new MailDTO();
	if (dto.userId.equalsIgnoreCase(target.mUserId)) {
	    if (!StringUtils.isEmpty(target.fileType)) {
		target.fileType = target.fileType.replaceAll("-post", "");
	    }
	    String title = Constants.UN_FLAG + target.title;

	    ConfigReader config = ConfigReader.getObject();
	    String app = config.getAppConfig(Constants.APP_URL);
	    String context = config.getAppConfig(Constants.REJECT_FLAG);
	    String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
	    String link = app + context + "/" + dto.mediaId + "/" + token;
	    mailDTO.email = config.getAppConfig(Constants.MAIL_ADDRESS);
	    mailDTO.subject = title;
	    mailDTO.activityTitle = title;
	    mailDTO.urlPostfix = "";
	    mailDTO.activityText = title + " : <b>" + target.title + "</b>";
	    mailDTO.btnText = Constants.APPROVE;
	    mailDTO.btnUrl = link;
	    SendMailSSL.sendMailFromTemplate(mailDTO);

	}
    }

    public boolean sendMessageNotification(MessageDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	try {
	    MediaTargetUserDTO target = B2Dao.getTargetUser(dto.userId, dto.toUserId);
	    if (target.sendMessageNotification) {
		MailDTO mailDTO = new MailDTO();
		String name = CommonUtils.getName(target.mFirstName, target.mLastName, dto.userId);
		String title = name + Constants.MESSAGED + " : ";
		mailDTO.email = target.mEmail;
		mailDTO.subject = title;
		mailDTO.activityTitle = title;
		mailDTO.activityText = dto.message;
		mailDTO.urlPostfix = "profile.php?m=" + dto.messageId + "&tab=MQ==";
		SendMailSSL.sendMailFromTemplate(mailDTO);
		Map<String, String> data = new HashMap<>();
		data.put(Constants.PAGE, Constants.MESSAGE);
		data.put(Constants.ID, dto.messageId);

		Messaging messaging = new Messaging();
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.title = name + Constants.MESSAGED;
		notificationDTO.body = dto.message;
		notificationDTO.data = data;
		notificationDTO.token = target.notificationToken;
		messaging.sendCommonMessage(notificationDTO);
		return true;
	    }

	} catch (Exception e) {
	    // TODO: handle exception
	}
	return false;
    }

    public LinkedList<MediaCommentDTO> getMediaComments(MediaPaginatedRequest dto) throws Exception {
	return B2Dao.getMediaComments(dto);
    }

    public boolean deleteComment(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteComment(dto);
    }

    public boolean sendCustomNotification(NotificationDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	Messaging messaging = new Messaging();
	messaging.sendCommonMessage(dto);

	return true;
    }

    public boolean likeMedia(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean flag = B2Dao.likeMedia(dto);
	try {
	    if (flag) {
		MediaTargetUserDTO target = B2Dao.getMediaTargetUser(dto.userId, dto.mediaId);
		if (target.sendLikeNotification) {
		    MailDTO mailDTO = new MailDTO();

		    String name = CommonUtils.getName(target.cFirstName, target.cLastName, dto.userId);
		    if (!dto.userId.equalsIgnoreCase(target.mUserId)) {
			if (!StringUtils.isEmpty(target.fileType)) {
			    target.fileType = target.fileType.replaceAll("-post", "");
			}
			String title = name + Constants.LIKED_YOUR + target.fileType;
			mailDTO.email = target.mEmail;
			mailDTO.subject = name + Constants.LIKED_YOUR + Constants.POST;
			mailDTO.activityTitle = title;
			mailDTO.urlPostfix = "?post=" + dto.mediaId;
			mailDTO.activityText = "";
			SendMailSSL.sendMailFromTemplate(mailDTO);
			Map<String, String> data = new HashMap<>();
			data.put(Constants.PAGE, Constants.LIKE);
			data.put(Constants.ID, dto.mediaId);
			Messaging messaging = new Messaging();
			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.title = title;
			notificationDTO.body = target.title;
			notificationDTO.data = data;
			notificationDTO.token = target.notificationToken;
			messaging.sendCommonMessage(notificationDTO);
		    }
		}
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}
	return flag;
    }

    public boolean unlikeMedia(MediaCommentDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.unlikeMedia(dto);
    }

    public LinkedList<RegisterUserDTO> getUserContactList(UserContactRequestDTO contact) throws Exception {
	return B2Dao.getUserContactList(contact);
    }

    public LinkedList<FeedResponseDTO> getUserFollowingFeedList(PaginatedRequest requestDTO) throws Exception {
	if (!validateUser(requestDTO.userId, requestDTO.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = requestDTO.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = requestDTO.userId;
	}
	requestDTO.targetUserId = targetUserId;
	return B2Dao.getUserFollowingFeedList(requestDTO);
    }

    public LinkedList<FeedResponseDTO> getUserFeedList(PaginatedRequest requestDTO) throws Exception {
	if (!validateUser(requestDTO.userId, requestDTO.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = requestDTO.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = requestDTO.userId;
	}
	requestDTO.targetUserId = targetUserId;
	boolean followFlag = B2Dao.userFollows(requestDTO.userId, requestDTO.targetUserId);
	return B2Dao.getUserFeedList(requestDTO, followFlag);
    }

    /**
     * No Authentication required
     * 
     * @param requestDTO
     * @return
     * @throws Exception
     */
    public LinkedList<FeedResponseDTO> getPublicFeed(UserMediaPaginatedRequest requestDTO) throws Exception {

	return B2Dao.getPublicFeed(requestDTO);
    }

    public LinkedList<FeedResponseDTO> getTrendingFeed(PaginatedRequest requestDTO) throws Exception {
	return B2Dao.getTrendingFeed(requestDTO);
    }

    public LinkedList<FeedResponseDTO> getNearbyFeed(NearbyPaginatedRequest requestDTO) throws Exception {
	try {
	    Double.parseDouble(requestDTO.latitude);
	    Double.parseDouble(requestDTO.longitude);
	} catch (Exception e) {
	    throw new SystemException(ErrorCodes.INVALID_COORDINATES, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getNearbyFeed(requestDTO);
    }

    public LinkedList<PlaylistMasterDTO> getPlaylistForUser(PaginatedRequest user) throws Exception {
	return B2Dao.getPlaylistForUser(user);
    }

    public LinkedList<PlaylistMasterDTO> getPlaylistForUserFollowing(PaginatedRequest user) throws Exception {
	if (!validateUser(user.userId, user.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = user.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = user.userId;
	}
	user.targetUserId = targetUserId;
	return B2Dao.getPlaylistForUserFollowing(user);
    }

    public LinkedList<MediaDTO> getPlaylistItems(PlaylistItemPaginatedRequest user) throws Exception {
	if (!validateUser(user.userId, user.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getPlaylistItems(user);
    }

    public RegisterUserDTO loginRegisterSocialMedia(RegisterUserDTO dto) throws Exception {
	if (dto.googleId == null || dto.googleId.length() <= 0) {
	    if (dto.fbId == null || dto.fbId.length() <= 0) {
		throw new SystemException(ErrorCodes.EMPTY_SOCIAL_MEDIA_ID, ConfigReader.getObject().getErrorConfig(),
			ErrorCodes.StatusCodes.FAILURE, null);
	    }
	}
	dto.accessToken = DAOHelper.guidGenerator("", "");
	RegisterUserDTO regDTO = B2Dao.loginSocialMediaUser(dto);
	if (regDTO == null) {
	    RegisterUserDTO emailRegDto = null;
	    // If the social user with the mail is already registered with
	    // another social media, then we need to add the new social media
	    // id.
	    if (dto.email != null && dto.email.length() > 0) {
		emailRegDto = B2Dao.getUserDetailsForEmail(dto);
		if (emailRegDto == null) {
		    dto.password = "social";
		    dto.accessToken = DAOHelper.guidGenerator("", "");
		    regDTO = B2Dao.registerUser(dto, true);
		} else {
		    regDTO = B2Dao.updateSocialIdForUser(dto);
		}
	    }
	}
	return regDTO;
    }

    public String addUserMedia(MediaPostDTO dto) throws Exception {
	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	Date date = new Date();
	String mediaId = dto.userId + Constants.MEDIA + dateFormat.format(date);
	dto.mediaId = mediaId;
	String visibility = dto.privilege;
	if (visibility == null || visibility.length() <= 0) {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.FOLLOWERS_VISIBILITY)) {
	    dto.privilege = Constants.FOLLOWERS_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.PRIVATE_VISIBILITY)) {
	    // If user marks the media private only he can see and wont be
	    // listed in trending feed.
	    dto.rank = "-1";
	    dto.privilege = Constants.PRIVATE_VISIBILITY;
	} else {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	}
	if (dto.isSkit == null || dto.isSkit.length() <= 0) {
	    dto.isSkit = "true";
	} else if (dto.isSkit.equalsIgnoreCase("false")) {
	    dto.isSkit = "false";
	} else {
	    dto.isSkit = "true";
	}
	String existingMediaId = B2Dao.mediaExists(dto.userId, dto.projectName);
	if (existingMediaId != null) {
	    dto.mediaId = existingMediaId;
	    mediaId = existingMediaId;
	    B2Dao.editUserMedia(dto);
	} else {
	    B2Dao.addUserMedia(dto);
	}
	return mediaId;
    }

    public boolean editUserMedia(MediaPostDTO dto) throws Exception {
	String visibility = dto.privilege;
	if (visibility == null || visibility.length() <= 0) {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.FOLLOWERS_VISIBILITY)) {
	    dto.privilege = Constants.FOLLOWERS_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.PRIVATE_VISIBILITY)) {
	    // If user marks the media private only he can see and wont be
	    // listed in trending feed.
	    dto.rank = "-1";
	    dto.privilege = Constants.PRIVATE_VISIBILITY;
	} else {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	}
	if (dto.isSkit == null || dto.isSkit.length() <= 0) {
	    dto.isSkit = "true";
	} else if (dto.isSkit.equalsIgnoreCase("false")) {
	    dto.isSkit = "false";
	} else {
	    dto.isSkit = "true";
	}
	return B2Dao.editUserMedia(dto);
    }

    public String copyUserPlaylist(PlaylistMasterDTO dto) throws Exception {
	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	Date date = new Date();
	String playlistId = dto.userId + Constants.PLAYLIST + dateFormat.format(date);
	String oldPlaylistId = dto.playlistId;
	dto.playlistId = playlistId;
	String visibility = dto.privilege;
	if (visibility == null || visibility.length() <= 0) {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.FOLLOWERS_VISIBILITY)) {
	    dto.privilege = Constants.FOLLOWERS_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.PRIVATE_VISIBILITY)) {
	    dto.privilege = Constants.PRIVATE_VISIBILITY;
	} else {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	}
	B2Dao.copyUserPlaylist(dto, oldPlaylistId);
	return playlistId;
    }

    public String addUserPlaylist(PlaylistMasterDTO dto) throws Exception {
	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	Date date = new Date();
	String playlistId = dto.userId + Constants.PLAYLIST + dateFormat.format(date);
	dto.playlistId = playlistId;
	String visibility = dto.privilege;
	if (visibility == null || visibility.length() <= 0) {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.FOLLOWERS_VISIBILITY)) {
	    dto.privilege = Constants.FOLLOWERS_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.PRIVATE_VISIBILITY)) {
	    dto.privilege = Constants.PRIVATE_VISIBILITY;
	} else {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	}
	B2Dao.addUserPlaylist(dto);
	return playlistId;
    }

    public boolean addPlaylistItem(PlaylistMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addPlaylistItem(dto);
    }

    public boolean deletePlaylistItem(PlaylistMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deletePlaylistItem(dto);
    }

    public boolean deleteUserPlaylist(PlaylistMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!B2Dao.playListExists(dto.playlistId, dto.userId)) {
	    throw new SystemException(ErrorCodes.PLAYLIST_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteUserPlaylist(dto);
    }

    public String addUserStation(StationMasterDTO dto) throws Exception {
	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	Date date = new Date();
	String stationId = dto.userId + Constants.STATION + dateFormat.format(date);
	dto.stationId = stationId;
	String visibility = dto.privilege;
	if (visibility == null || visibility.length() <= 0) {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.FOLLOWERS_VISIBILITY)) {
	    dto.privilege = Constants.FOLLOWERS_VISIBILITY;
	} else if (visibility.equalsIgnoreCase(Constants.PRIVATE_VISIBILITY)) {
	    dto.privilege = Constants.PRIVATE_VISIBILITY;
	} else {
	    dto.privilege = Constants.PUBLIC_VISIBILITY;
	}
	B2Dao.addUserStation(dto);
	return stationId;
    }

    public String addShopifyProduct(ShopifyProductDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addShopifyProduct(dto);
    }

    public boolean deleteShopify(ShopifyProductDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteShopify(dto);
    }

    public LinkedList<ShopifyProductDTO> searchShopifyProduct(UserSearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.searchShopifyProduct(dto);
    }

    public LinkedList<ShopifyProductDTO> getShopifyByVendor(UserSearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getShopifyByVendor(dto);
    }

    public boolean addStationItem(StationMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addStationItem(dto);
    }

    public String updateShopifyProduct(ShopifyProductDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String shopifyProductId = B2Dao.getShopifyIdExists(dto.shopifyId);
	if (StringUtils.isEmpty(shopifyProductId)) {
	    shopifyProductId = B2Dao.addShopifyProduct(dto);
	    return shopifyProductId;
	}
	B2Dao.updateShopifyProduct(dto);
	return shopifyProductId;
    }

    public boolean deleteUserStation(StationMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!B2Dao.stationExistsForUser(dto.stationId, dto.userId)) {
	    throw new SystemException(ErrorCodes.STATION_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteUserStation(dto);
    }

    public LinkedList<StationMasterDTO> getUserStation(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getUserStation(dto);
    }

    public boolean deleteStationItem(StationMasterDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteStationItem(dto);
    }

    public HashMap<String, Object> getStationItems(StationItemPaginatedRequest user) throws Exception {
	if (!validateUser(user.userId, user.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (user.startRange < 0 || user.startRange >= user.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!B2Dao.stationExistsForUser(user.stationId, user.userId)) {
	    throw new SystemException(ErrorCodes.STATION_DOESNT_EXIST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getStationItems(user);
    }

    public int getFollowingUserCount(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = dto.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = dto.userId;
	}
	dto.targetUserId = targetUserId;
	return B2Dao.getFollowingUserCount(dto);
    }

    public int getFollowingBusinessCount(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getFollowingBusinessCount(dto);
    }

    public int getFollowingArtistCount(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getFollowingArtistCount(dto);
    }

    public int getArtistFollowerCount(ArtistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getArtistFollowerCount(dto);
    }

    public int getBusinessFollowerCount(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getBusinessFollowerCount(dto);
    }

    public int getFollowerUserCount(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = dto.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = dto.userId;
	}
	dto.targetUserId = targetUserId;
	return B2Dao.getFollowerUserCount(dto);
    }

    public HashMap<String, Object> getFollowingFollowerCount(PaginatedRequest dto) throws Exception {
	int followingCount = getFollowingUserCount(dto);
	int followerCount = getFollowerUserCount(dto);
	HashMap<String, Object> map = new HashMap<String, Object>();
	map.put(Constants.FOLLOWER_LIST, followerCount);
	map.put(Constants.FOLLOWING_LIST, followingCount);
	return map;
    }

    public HashMap<String, Object> getFollowingUserList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = dto.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = dto.userId;
	}
	dto.targetUserId = targetUserId;
	return B2Dao.getFollowingUserList(dto);
    }

    public HashMap<String, Object> getFollowingBusinessList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getFollowingBusinessList(dto);
    }

    public LinkedList<RegisterUserDTO> suggestFollowUserList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.suggestFollowUserList(dto);
    }

    public LinkedList<RegisterUserDTO> suggestFollowBusinessList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.suggestFollowBusinessList(dto);
    }

    public HashMap<String, Object> getFollowingArtistList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = dto.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = dto.userId;
	}
	dto.targetUserId = targetUserId;
	return B2Dao.getFollowingArtistList(dto);
    }

    public LinkedList<ArtistDTO> suggestFollowArtistList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.suggestFollowArtistList(dto);
    }

    public RegisterUserDTO getRequestUserDetails(UserMediaPaginatedRequest dto) throws Exception {
	if (dto.userId == null || dto.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean isBlocked = B2Dao.getBlockStatus(dto.userId, dto.requestUserId);
	if (isBlocked) {
	    throw new SystemException(ErrorCodes.USER_BLOCKED, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getRequestUserDetails(dto);
    }

    public LinkedList<RegisterUserDTO> getRequestUserAdmin(UserSearchPaginatedRequest dto) throws Exception {
	return B2Dao.getRequestUserAdmin(dto);
    }

    public LinkedList<RegisterUserDTO> searchCategory(UserSearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (StringUtils.isEmpty(dto.searchString)) {
	    throw new SystemException(ErrorCodes.EMPTY_SEARCH_STRING, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.searchCategory(dto);
    }

    public LinkedList<RegisterUserDTO> searchCategoryByKeywords(UserSearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (StringUtils.isEmpty(dto.searchString)) {
	    throw new SystemException(ErrorCodes.EMPTY_SEARCH_STRING, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (StringUtils.isEmpty(dto.category)) {
	    throw new SystemException(ErrorCodes.EMPTY_CATEGORY, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	return B2Dao.searchCategoryByKeywords(dto);
    }

    public LinkedList<BusinessProductDTO> searchUserBusinessProduct(NearbyPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (StringUtils.isEmpty(dto.searchString)) {
	    throw new SystemException(ErrorCodes.EMPTY_SEARCH_STRING, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.searchString.length() <= 3) {
	    return B2Dao.searchUserBusinessProduct3(dto);
	}
	return B2Dao.searchUserBusinessProduct(dto);
    }

    public LinkedList<Object> getActivityList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<ActivityMasterDTO> activityList = B2Dao.getActivityList(dto);
	return getActivityReturnObject(activityList);
    }

    public boolean updateCategoryForUser(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateCategoryForUser(dto);
    }

    public RegisterUserDTO updateBusinessUserId(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	RegisterUserDTO user = registerUser(dto.businessUser);
	if (StringUtils.isEmpty(user.userId)) {
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean updateFlag = B2Dao.updateBusinessUserId(user.userId, dto.userId);
	if (!updateFlag) {
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return user;
    }

    public boolean updateBusinessUser(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateBusinessUser(dto);
    }

    public boolean editBusinessUserId(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (StringUtils.isEmpty(dto.businessUserId)) {
	    throw new SystemException(ErrorCodes.INSUFFICIENT_VALUES, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateBusinessUserId(dto.businessUserId, dto.userId);

    }

    public RegisterUserDTO updateArtistUserId(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	ArtistDTO artist = new ArtistDTO();
	try {
	    artist.artistId = dto.artistUser.artistReference;
	} catch (Exception e) {
	}
	artist = B2Dao.getArtistDetails(artist);
	if (artist != null && !StringUtils.isEmpty(artist.artistUserId)) {
	    throw new SystemException(ErrorCodes.ARTIST_LINKED, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (artist == null) {
	    RegisterUserDTO artistUser = dto.artistUser;
	    artist = new ArtistDTO();
	    artist.firstName = artistUser.firstName;
	    artist.lastName = artistUser.lastName;
	    artist.artistDescription = artistUser.bio;
	    artist.profilePicture = artistUser.profilePicture;
	    artist.artistId = dto.artistUser.artistReference = B2Dao.createArtist(artist);
	}

	RegisterUserDTO user = registerUser(dto.artistUser);
	if (StringUtils.isEmpty(user.userId)) {
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	artist.artistUserId = user.userId;
	boolean updateFlag = B2Dao.updateArtistUserId(user.userId, dto.userId);
	if (!updateFlag) {
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	B2Dao.updateArtistUser(artist);
	return user;
    }

    public boolean updateArtistUser(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	ArtistDTO artist = new ArtistDTO();
	try {
	    artist.artistId = dto.artistReference;
	} catch (Exception e) {
	}
	artist = B2Dao.getArtistDetails(artist);
	if (artist != null && !StringUtils.isEmpty(artist.artistUserId)) {
	    throw new SystemException(ErrorCodes.ARTIST_LINKED, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	artist.artistUserId = dto.userId;
	B2Dao.updateArtistReference(dto.artistReference, dto.userId);
	return B2Dao.updateArtistUser(artist);
    }

    public boolean editArtistUserId(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	if (StringUtils.isEmpty(dto.userId)) {
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateArtistUserId(dto.artistUserId, dto.userId);

    }

    public boolean updateNotificationPref(RegisterUserDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	if (StringUtils.isEmpty(dto.userId)) {
	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String pref = CommonUtils.getNotificationPref(dto.sendCommentNotification, dto.sendLikeNotification,
		dto.sendFollowNotification, dto.sendMessageNotification);
	return B2Dao.updateNotificationPref(dto.userId, pref);

    }

    public boolean removeArtistAssociation(String userId, String verificationToken) throws Exception {
	ConfigReader config = ConfigReader.getObject();
	String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
	if (!token.equals(verificationToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.removeArtistAssociation(userId);
    }

    public boolean rejectFlag(String verificationToken, String mediaId) throws Exception {
	if (StringUtils.isEmpty(mediaId)) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	ConfigReader config = ConfigReader.getObject();
	String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
	if (!token.equals(verificationToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	return B2Dao.rejectFlag(mediaId);
    }

    public boolean approveStationPartner(String verificationToken, String userId) throws Exception {
	if (StringUtils.isEmpty(userId)) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	ConfigReader config = ConfigReader.getObject();
	String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
	if (!token.equals(verificationToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean flag = B2Dao.activateUser(userId);
	RegisterUserDTO userDTO = new RegisterUserDTO();
	userDTO.userId = userId;
	userDTO = B2Dao.getUserDetails(userDTO);
	try {
	    if (flag) {

		MailDTO mailDTO = new MailDTO();
		String name = CommonUtils.getName(userDTO.firstName, userDTO.lastName, userDTO.userId);

		String title = "Account activated.";
		mailDTO.email = userDTO.email;
		mailDTO.subject = name + " Your account is activated. ";
		mailDTO.activityTitle = title;
		mailDTO.urlPostfix = "";
		mailDTO.activityText = title;
		SendMailSSL.sendMailFromTemplate(mailDTO);

	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}
	return flag;
    }

    public boolean updateUserToArtist(RegisterUserDTO dto) throws Exception {
	RegisterUserDTO validate = getUserDetailsWithToken(dto.userId, dto.accessToken);
	if (validate == null) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean flag = B2Dao.updateUserToArtist(dto);
	ArtistDTO artistDTO = new ArtistDTO();
	artistDTO.artistId = dto.artistReference;
	artistDTO = B2Dao.getArtistDetails(artistDTO);
	String artistVerificationMail = CommonUtils.getArtistVerificationEmail(validate, artistDTO);
	ConfigReader config = ConfigReader.getObject();
	String toEmail = config.getAppConfig(Constants.ARTIST_VERIFICATION_EMAIL);
	SendMailSSL.sendMail(toEmail, "User Artist Verification", artistVerificationMail);
	return flag;
    }

    public LinkedList<Object> getFollowerActivityList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<ActivityMasterDTO> activityList = B2Dao.getFollowerActivityList(dto);
	return getActivityReturnObject(activityList);
    }

    public LinkedList<Object> getFollowingActivityList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	LinkedList<ActivityMasterDTO> activityList = B2Dao.getFollowingActivityList(dto);
	return getActivityReturnObject(activityList);
    }

    private LinkedList<Object> getActivityReturnObject(LinkedList<ActivityMasterDTO> activityList) throws Exception {
	LinkedList<Object> itemList = new LinkedList<Object>();
	if (activityList != null && activityList.size() > 0) {

	    for (ActivityMasterDTO activity : activityList) {
		if (activity != null) {
		    if (activity.referenceId != null && activity.referenceId.length() > 0) {
			if (Constants.NEW_PLAYLIST.equalsIgnoreCase(activity.activityType)) {
			    PlaylistMasterDTO playlist = B2Dao.getPlaylistActiviy(activity.referenceId);
			    activity.data = playlist;
			} else if (Constants.NEW_STATION.equalsIgnoreCase(activity.activityType)) {
			    StationMasterDTO station = B2Dao.getStationActiviy(activity.referenceId);
			    activity.data = station;
			} else if (Constants.NEW_COMMENT.equalsIgnoreCase(activity.activityType)) {
			    MediaCommentDTO comment = B2Dao.getCommentActiviy(activity.referenceId);
			    activity.data = comment;
			}
		    }
		    itemList.add(activity);
		}
	    }

	}
	return itemList;
    }

    public HashMap<String, Object> getRequestedUserMedia(UserMediaPaginatedRequest user) throws Exception {
	if (!validateUser(user.userId, user.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean isBlocked = B2Dao.getBlockStatus(user.userId, user.requestUserId);
	if (isBlocked) {
	    throw new SystemException(ErrorCodes.USER_BLOCKED, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (user.startRange < 0 || user.startRange >= user.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getRequestedUserMedia(user);
    }

    public HashMap<String, Object> getFollowerUserList(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = dto.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = dto.userId;
	}
	dto.targetUserId = targetUserId;
	return B2Dao.getFollowerUserList(dto);
    }

    public String genericDMLExecutor(String query) throws Exception {
	ConfigReader config = ConfigReader.getObject();
	String result = "";
	if (config.executeQueryService) {
	    result = B2Dao.genericDMLExecutor(query);
	} else {
	    throw new SystemException(ErrorCodes.QUERY_SERVICE_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return result;
    }

    public String genericSqlExecutor(String query) throws Exception {
	ConfigReader config = ConfigReader.getObject();
	String fileName = "";
	if (config.executeQueryService) {
	    fileName = B2Dao.genericSqlExecutor(query);
	} else {
	    throw new SystemException(ErrorCodes.QUERY_SERVICE_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return fileName;
    }

    public LinkedList<FeedResponseDTO> searchMedia(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.targetUserId == null || dto.targetUserId.length() <= 0) {
	    dto.targetUserId = dto.userId;
	}
	return B2Dao.searchMedia(dto);
    }

    public LinkedList<FeedResponseDTO> getPostsUserTaggedIn(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto.targetUserId == null || dto.targetUserId.length() <= 0) {
	    dto.targetUserId = dto.userId;
	}
	return B2Dao.getPostsUserTaggedIn(dto);
    }

    public FeedResponseDTO getSpecificMedia(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getSpecificMedia(dto);
    }

    public LinkedList<FeedResponseDTO> getScheduledContent(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getScheduledContent(dto);
    }

    public String addScheduledContent(MediaPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	MediaDTO media = B2Dao.getScheduledMedia(dto);
	if (media == null) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String scheduledId = B2Dao.scheduledMediaExists(dto);
	if (!StringUtils.isEmpty(scheduledId)) {
	    return scheduledId;
	}
	return B2Dao.addScheduledContent(dto);
    }

    public boolean deleteScheduledContent(ScheduledDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteScheduledContent(dto);
    }

    public LinkedList<ScheduledDTO> getUserScheduledContent(PaginatedRequest dto) throws Exception {
	B2Dao.validateStartEndRange(dto);
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getUserScheduledContent(dto);

    }

    public LinkedList<StationMasterDTO> searchStation(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.searchStation(dto);
    }

    public boolean deleteMedia(MediaPostDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (dto == null || dto.mediaId == null || dto.mediaId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	FeedResponseDTO media = B2Dao.getSpecificMedia(dto);
	if (media == null || !dto.userId.equals(media.user.userId)) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteMedia(dto);

    }

    public LinkedList<FeedResponseDTO> getUserMedia(SearchPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (B2Dao.getUserBlockStatus(dto.userId, dto.targetUserId)) {
	    throw new SystemException(ErrorCodes.USER_BLOCKED, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	String targetUserId = dto.targetUserId;
	if (StringUtils.isEmpty(targetUserId)) {
	    targetUserId = dto.userId;
	}
	dto.targetUserId = targetUserId;
	return B2Dao.getUserMedia(dto);
    }

    public MediaDTO getUserMediaMetadata(SearchMediaRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getUserMediaMetadata(dto);
    }

    public LinkedList<ArtistDTO> searchArtist(SearchArtistDTO dto) throws Exception {
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.searchArtist(dto);
    }

    public LinkedList<RegisterUserDTO> getArtistFollowers(ArtistSearchDTO dto) throws Exception {
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getArtistFollowers(dto);
    }

    public ArtistDTO getArtistDetails(ArtistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getArtistDetails(dto);
    }

    public LinkedList<ProductDTO> searchProduct(SearchPaginatedRequest dto) throws Exception {
	if (dto.startRange < 0 || dto.startRange >= dto.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.searchProduct(dto);
    }

    public boolean getBlockStatus(UserMediaPaginatedRequest dto) throws Exception {
	return B2Dao.getBlockStatus(dto.userId, dto.requestUserId);
    }

    public boolean blockUser(UserMediaPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean isBlocked = B2Dao.getBlockStatus(dto.userId, dto.requestUserId);
	if (isBlocked) {
	    return true;
	}
	return B2Dao.blockUser(dto);
    }

    public boolean deleteBusinessProduct(BusinessProductDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteBusinessProduct(dto.userId, dto.productId);
    }

    public boolean updateBusinessProduct(BusinessProductDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateBusinessProduct(dto);
    }

    public String addBusinessProduct(BusinessProductDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addBusinessProduct(dto);
    }

    public LinkedList<BusinessProductDTO> getBusinessProduct(PaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getBusinessProduct(dto);
    }

    public BusinessProductDTO getBusinessProductById(ProductRequestDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getBusinessProductById(dto);
    }

    public boolean unBlockUser(UserMediaPaginatedRequest dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	boolean isBlocked = B2Dao.getBlockStatus(dto.userId, dto.requestUserId);
	if (!isBlocked) {
	    return true;
	}
	return B2Dao.unBlockUser(dto);
    }

    public LinkedList<MediaLibraryDTO> getMediaLibrary(MediaLibraryPaginatedRequest libReq) throws Exception {
	if (libReq.userId == null || libReq.userId.length() == 0) {
	    throw new SystemException(ErrorCodes.INVALID_USER_ID, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (libReq.startRange < 0 || libReq.startRange >= libReq.endRange) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getMediaLibrary(libReq);
    }

    public String createArtist(ArtistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.createArtist(dto);
    }

    public boolean updateArtist(ArtistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateArtist(dto);
    }

    public boolean deleteArtist(ArtistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteArtist(dto);
    }

    public boolean forgotPassword(RegisterUserDTO dTO) throws Exception {
	dTO = B2Dao.forgotPassword(dTO.email);
	if (dTO != null) {
	    SendMailSSL.sendMail(dTO.email, "Forgot Password", "Password :" + dTO.password);
	    return true;
	}
	// TODO Auto-generated method stub
	return false;
    }

    public boolean resetPassword(RegisterUserDTO dTO) throws Exception {
	if (dTO.email == null || dTO.email.length() <= 0 || dTO.password == null || dTO.password.length() <= 0
		|| dTO.newPassword == null || dTO.newPassword.length() <= 0) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}

	return B2Dao.resetPassword(dTO);
    }

    public boolean createProduct(ProductDTO dto) throws Exception {
	if (StringUtils.isEmpty(dto.brandName) || StringUtils.isEmpty(dto.picture)
		|| StringUtils.isEmpty(dto.shopLink1)) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.createProduct(dto);
    }

    public boolean updateProduct(ProductDTO dto) throws Exception {
	if (StringUtils.isEmpty(dto.brandName) || StringUtils.isEmpty(dto.picture) || StringUtils.isEmpty(dto.shopLink1)
		|| StringUtils.isEmpty(dto.productId)) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);

	}
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.updateProduct(dto);
    }

    public boolean deleteProduct(ProductDTO dto) throws Exception {
	if (StringUtils.isEmpty(dto.productId)) {
	    throw new SystemException(ErrorCodes.INVALID_REQUEST, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteProduct(dto);
    }

    public String addProductToWishList(WishlistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.addProductToWishList(dto);
    }

    public LinkedList<WishlistDTO> getWishList(WishlistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.getWishList(dto);
    }

    public boolean deleteWishListItem(WishlistDTO dto) throws Exception {
	if (!validateUser(dto.userId, dto.accessToken)) {
	    throw new SystemException(ErrorCodes.INVALID_USER, ConfigReader.getObject().getErrorConfig(),
		    ErrorCodes.StatusCodes.FAILURE, null);
	}
	return B2Dao.deleteWishListItem(dto);
    }
}