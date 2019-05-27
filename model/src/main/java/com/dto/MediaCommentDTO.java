package com.dto;

import java.sql.Date;

public class MediaCommentDTO extends AuthDTO {
    public String mediaId;
    public String commentId;
    public String comment;
    public String tags;
    public Date commentDate;
    public RegisterUserDTO user;
}
