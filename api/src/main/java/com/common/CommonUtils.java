package com.common;

import org.apache.commons.lang.StringUtils;

import com.constants.Constants;
import com.dto.ArtistDTO;
import com.dto.RegisterUserDTO;

public class CommonUtils {
    public static String getArtistVerificationEmail(RegisterUserDTO validate, ArtistDTO artistDTO) throws Exception {
	String mail = "User : " + validate.firstName + " " + validate.lastName + " has been linked to artist : "
		+ artistDTO.firstName + " " + artistDTO.lastName
		+ "\nIf valid, please click on the below link to remove association\n";
	ConfigReader config = ConfigReader.getObject();
	String app = config.getAppConfig(Constants.APP_URL);
	String context = config.getAppConfig(Constants.ARTIST_VERIFICATION_CONTEXT);
	String token = config.getAppConfig(Constants.ARTIST_VERIFICATION_TOKEN);
	String link = app + context + "/" + validate.userId + "/" + token;
	return mail + link;
    }

    public static String getName(String fName, String lName, String userId) {
	if (StringUtils.isEmpty(fName) && StringUtils.isEmpty(lName) && StringUtils.isEmpty(userId)) {
	    return "User";
	}
	if (StringUtils.isEmpty(fName) && StringUtils.isEmpty(lName)) {
	    return userId;
	}
	if (StringUtils.isEmpty(fName)) {
	    return lName;
	}
	if (StringUtils.isEmpty(lName)) {
	    return fName;
	}
	return fName + " " + lName;

    }

    public static String getNotificationPref(String comment, String like, String follow, String message) {
	String pref = "";
	if ("Y".equalsIgnoreCase(comment)) {
	    pref += "C_";
	}
	if ("Y".equalsIgnoreCase(like)) {
	    pref += "L_";
	}
	if ("Y".equalsIgnoreCase(follow)) {
	    pref += "F_";
	}
	if ("Y".equalsIgnoreCase(message)) {
	    pref += "M_";
	}
	return pref;
    }

    public static boolean sendCommentNotification(String pref) {
	if (StringUtils.isEmpty(pref)) {
	    return false;
	}
	if (pref.contains("C")) {
	    return true;
	}
	return false;
    }

    public static boolean sendLikeNotification(String pref) {
	if (StringUtils.isEmpty(pref)) {
	    return false;
	}
	if (pref.contains("L")) {
	    return true;
	}
	return false;
    }

    public static boolean sendFollowNotification(String pref) {
	if (StringUtils.isEmpty(pref)) {
	    return false;
	}
	if (pref.contains("F")) {
	    return true;
	}
	return false;
    }

    public static boolean sendMessageNotification(String pref) {
	if (StringUtils.isEmpty(pref)) {
	    return false;
	}
	if (pref.contains("M")) {
	    return true;
	}
	return false;
    }

    public static boolean isUserAdmin(String email) throws Exception {
	ConfigReader config = ConfigReader.getObject();
	String adminSubstring = config.getAppConfig(Constants.ADMIN_SUBSTRING);
	if (!StringUtils.isEmpty(email) && email.contains(adminSubstring)) {
	    return true;
	}
	return false;
    }

    public static void main(String args[]) throws Exception {
	String email = "shreyas@bon2.com";
	System.out.println(isUserAdmin(email) + "");
    }
}
