package com.dto;

import java.sql.Date;
import java.util.LinkedList;

public class MediaMetadataDTO {
    public LinkedList<RegisterUserDTO> cameoUsers;
    public int likeCount;
    public Boolean userLikeFlag;
    public int commentCount;
    public int loopCount;
    public Date mediaDate;
}
