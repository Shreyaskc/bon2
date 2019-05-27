package com.dto;

import java.sql.Timestamp;

public class ActivityMasterDTO {
    public String activityId;
    public String activityName;
    public String activityDescription;
    public String stationId;
    public Timestamp createdDate;
    public String activityType;
    public Object data;
    public String referenceId;
    public RegisterUserDTO user;

    public ActivityMasterDTO() {
	user = new RegisterUserDTO();
    }

}
