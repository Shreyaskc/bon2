package com.dto;

import java.sql.Timestamp;
import java.util.LinkedList;

public class RegisterUserDTO extends AuthDTO {

    public String googleId;
    public String fbId;
    public String password;
    public Timestamp lastLogin;
    public Timestamp accessTokenRefreshTime;
    public String firstName;
    public String lastName;
    public String bio;
    public String email;
    public String phone;
    public String profilePicture;
    public String userType;
    public String location;
    public LinkedList<String> musicPrefList;
    public LinkedList<String> followingUserIdList;
    public LinkedList<String> followingArtistIdList;
    public Boolean following;
    public String followerCount;
    public String followingCount;
    public String newPassword;
    public String category;
    public LinkedList<BusinessProductDTO> businessProductList;
    public String artistReference;
    public String artistUserId;
    public String businessEmail;
    public String businessName;
    public String businessUserId;
    public String businessAddress;
    public String businessPhone;
    public String businessBio;
    public String businessWebsite;
    public String businessFacebook;
    public String businessTwitter;
    public String businessInstagram;
    public String businessYoutube;
    public String businessProfilePicture;
    public String keywords;
    public RegisterUserDTO businessUser;
    public RegisterUserDTO artistUser;
    public String sendCommentNotification;
    public String sendLikeNotification;
    public String sendFollowNotification;
    public String sendMessageNotification;
    public String shopifyAccessToken;
    public String shopifyShopName;
    public String isStationPartner;
    public String partnerStationName;
    public String isActive;
    public String privacySetting;
    public String notificationToken;
}
