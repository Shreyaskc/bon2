package com.dto;

import java.sql.Timestamp;
import java.util.LinkedList;

public class StationMasterDTO extends AuthDTO {
    public String stationId;
    public Timestamp createdDate;
    public String stationName;
    public String activityId;
    public String musicPrefId;
    public String privilege;
    public String thumbnail;
    public LinkedList<String> stationItems;
    public String tags;
}
