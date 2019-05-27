package com.dto;

import java.sql.Timestamp;
import java.util.List;

public class ArtistDTO extends AuthDTO {
    public String artistId;
    public String firstName;
    public String lastName;
    public String nickName;
    public String artistDescription;
    public Timestamp birthday;
    public String tags;
    public String nationality;
    public String age;
    public String genre;
    public String profilePicture;
    public String fbUrl;
    public String instagramUrl;
    public String pintrestUrl;
    public String youtubeUrl;
    public String bon2Url;
    public List<ArtistMediaDTO> mediaList;
    public Boolean following;
    public String uploader;
    public String artistUserId;
}
