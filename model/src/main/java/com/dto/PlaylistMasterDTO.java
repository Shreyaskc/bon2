package com.dto;

import java.sql.Timestamp;
import java.util.LinkedList;

public class PlaylistMasterDTO extends AuthDTO {
    public String playlistId;
    public Timestamp createdDate;
    public String playlistName;
    public String privilege;
    public String thumbnail;
    public LinkedList<String> playlistItems;
}
