package com.constants;

/**
 * Target Group SQL Constants(DAL query keys).
 * 
 * @author Shreyas
 *
 */
public interface SQLConstants {
    public String GET_TEST_LIST = "SELECT abc FROM test where abc=?";
    public String MEDIA_COLUMNS = "m.MEDIA_ID,m.FILE_NAME,m.FILE_TYPE,m.MUSIC_PREF_ID,m.UPLOADER,m.TAGS,m.PATH,m.IS_AD,m.SKIP_FLAG,m.LOCATION,m.PRIVILEGE,m.LATITUDE,m.LONGITUDE,m.ARTIST,m.THUMBNAIL,m.DESCRIPTION,m.ARTIST_ID,m.ALBUM_ID,m.MEDIA_LENGTH,m.RANK,m.TITLE,m.SONG_TITLE,m.EDL_FILE_PATH,m.IS_SKIT,m.MEDIA_DATE,m.STATION_LIST,m.IS_FEATURED,m.PROJECT_NAME,m.POST_TYPE,m.posted_as,m.posted_id,m.scheduled_Start_Time,m.scheduled_End_Time,m.is_Scheduled,m.FLAG,m.FLAGGED_BY,m.FLAGGED_REASON,m.episode_number,m.series_id ";
    public String GET_MUSIC_PREF_LIST = "SELECT MUSIC_PREF_ID,MUSIC_PREF_NAME, CATEGORY FROM music_pref_master";
    public String INSERT_USER_MASTER = "INSERT INTO USER_MASTER(USER_ID,PASSWORD,ACCESS_TOKEN,LAST_LOGIN,ACCESS_TOKEN_REFRESH_TIME,FIRST_NAME,LAST_NAME,EMAIL,"
	    + "PHONE,PROFILE_PICTURE,USER_TYPE,LOCATION,GOOGLE_ID,FB_ID,CATEGORY,BUSINESS_USER_ID,ARTIST_USER_ID, "
	    + "BUSINESS_EMAIL, BUSINESS_ADDRESS, BUSINESS_NAME,BUSINESS_PHONE ,BUSINESS_BIO, BUSINESS_WEBSITE,BUSINESS_FACEBOOK,BUSINESS_YOUTUBE,"
	    + "BUSINESS_TWITTER,BUSINESS_INSTAGRAM,BUSINESS_PROFILE_PICTURE, KEYWORDS,artist_reference,shopify_shop_name,shopify_access_token,is_station_partner,partner_station_name,is_active,privacy_setting ) VALUES (?,?,?,NOW(),NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public String INSERT_USER_MUSIC_PREF = "INSERT INTO USER_MUSIC_PREF(USER_ID,MUSIC_PREF_ID,PREF_DATE) VALUES (?,?,NOW())";
    public String INSERT_USER_FOLLOWING = "INSERT INTO USER_FOLLOWING(USER_ID,FOLLOWING_USER_ID,FOLLOWING_DATE,FOLLOW_TYPE) VALUES (?,?,NOW(),?)";
    public String INSERT_BUSINESS_FOLLOWING = "INSERT INTO USER_BUSINESS_FOLLOWING(USER_ID,FOLLOWING_USER_ID,FOLLOWING_DATE,FOLLOW_TYPE) VALUES (?,?,NOW(),?)";

    public String INSERT_ARTIST_FOLLOWING = "INSERT INTO user_artist_following(USER_ID,FOLLOWING_ARTIST_ID,FOLLOWING_DATE,FOLLOW_TYPE) VALUES (?,?,NOW(),?)";
    public String DELETE_USER_MUSIC_PREF = "DELETE FROM USER_MUSIC_PREF WHERE USER_ID=?";
    public String DELETE_USER_FOLLOWING = "DELETE FROM USER_FOLLOWING WHERE USER_ID=?";
    public String DELETE_BUSINESS_FOLLOWING = "DELETE FROM USER_BUSINESS_FOLLOWING WHERE USER_ID=?";
    public String DELETE_ARTIST_FOLLOWING = "DELETE FROM user_artist_following WHERE USER_ID=?";
    public String DELETE_SPECIFIC_USER_FOLLOWING = "DELETE FROM USER_FOLLOWING WHERE USER_ID=? AND FOLLOWING_USER_ID=?";
    public String DELETE_SPECIFIC_BUSINESS_FOLLOWING = "DELETE FROM USER_BUSINESS_FOLLOWING WHERE USER_ID=? AND FOLLOWING_USER_ID=?";
    public String DELETE_SPECIFIC_ARTIST_FOLLOWING = "DELETE FROM user_artist_following WHERE USER_ID=? AND FOLLOWING_ARTIST_ID=?";
    public String GET_USER_MUSIC_PREF = "SELECT MUSIC_PREF_ID FROM USER_MUSIC_PREF WHERE USER_ID=?";
    public String GET_USER_DETAILS = "SELECT *  FROM USER_MASTER WHERE USER_ID=? ";
    public String GET_SOCIAL_MEDIA_USER_DETAILS = "SELECT USER_ID,PASSWORD,ACCESS_TOKEN,LAST_LOGIN,ACCESS_TOKEN_REFRESH_TIME,FIRST_NAME,LAST_NAME,EMAIL,PHONE,PROFILE_PICTURE,USER_TYPE,LOCATION,GOOGLE_ID,FB_ID FROM USER_MASTER WHERE GOOGLE_ID=? OR FB_ID=? ";
    public String GET_SOCIAL_MEDIA_USER_FROM_EMAIL = "SELECT USER_ID,PASSWORD,ACCESS_TOKEN,LAST_LOGIN,ACCESS_TOKEN_REFRESH_TIME,FIRST_NAME,LAST_NAME,EMAIL,PHONE,PROFILE_PICTURE,USER_TYPE,LOCATION,GOOGLE_ID,FB_ID FROM USER_MASTER WHERE EMAIL=? ";
    public String USER_EMAIL_EXISTS = "SELECT USER_ID,ACCESS_TOKEN FROM USER_MASTER WHERE EMAIL=? ";
    public String USER_ID_EXISTS = "SELECT LOWER(USER_ID) FROM USER_MASTER WHERE USER_ID=? ";
    public String USER_FOLLOWING_EXISTS = "SELECT LOWER(USER_ID) FROM USER_FOLLOWING WHERE USER_ID=? AND FOLLOWING_USER_ID=? ";
    public String ARTIST_FOLLOWING_EXISTS = "SELECT LOWER(USER_ID) FROM user_artist_following WHERE USER_ID=? AND FOLLOWING_ARTIST_ID=? ";
    public String VALIDATE_USER = "SELECT USER_ID FROM USER_MASTER WHERE USER_ID=? AND ACCESS_TOKEN=? ";
    public String VALIDATE_USER_WITH_TOKEN = "SELECT * FROM USER_MASTER WHERE USER_ID=? AND ACCESS_TOKEN=? ";
    public String SEARCH_USER_CATEGORY = "SELECT * FROM USER_MASTER WHERE concat_ws(' ' , CATEGORY, BUSINESS_BIO,KEYWORDS) REGEXP ?  ";
    public String SEARCH_USER_CATEGORY_KEYWORD_3 = "SELECT * FROM USER_MASTER WHERE category =?  and concat_ws(' ' , BUSINESS_BIO,KEYWORDS) REGEXP ? ";
    public String SEARCH_USER_CATEGORY_KEYWORD = "select * from (select * from USER_MASTER  where category =?  and match(BUSINESS_BIO) against(?)  union select * from USER_MASTER  where category =?  and match(KEYWORDS) against(?) ) as t1   ";
    public String SEARCH_USER_BUSINESS_PRODUCT_DIST_3 = "select *,(DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST from business_product_master where  concat_ws('',product_name, brand_name,  keywords, tags, description,category) REGEXP ? having  DIST < ? order by dist  ";
    public String SEARCH_USER_BUSINESS_PRODUCT_DIST = "select * from (select *,(DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST from business_product_master where MATCH(product_name) AGAINST(? )  and category like ?   UNION select *,(DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST from business_product_master where MATCH(product_name) AGAINST(? )  and category like ?    UNION select *,(DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST from business_product_master where MATCH(keywords) AGAINST(? )  and category like ?  UNION select *,(DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST from business_product_master where MATCH(tags) AGAINST(? )  and category like ?    UNION select *,(DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST from business_product_master where MATCH(description) AGAINST(? )   and category like ? ) as t1 where  t1.DIST < ? order by t1.dist  ";

    public String SEARCH_USER_BUSINESS_PRODUCT_3 = "select * from business_product_master where  concat_ws('',product_name, brand_name,  keywords, tags, description,category) REGEXP ?   ";
    public String SEARCH_USER_BUSINESS_PRODUCT = "SELECT * FROM (SELECT * FROM business_product_master WHERE MATCH(product_name) AGAINST(?) and category like ?  UNION SELECT * FROM business_product_master WHERE MATCH(brand_name) AGAINST(?) and category like ?  UNION SELECT * FROM business_product_master WHERE MATCH(keywords) AGAINST(?)  and category like ?  UNION SELECT * FROM business_product_master WHERE MATCH(tags) AGAINST(?)  and category like ?  UNION SELECT * FROM business_product_master WHERE MATCH(description) AGAINST(?) and category like ?  ) as t1 ";

    public String GET_FRIEND_LIST = "SELECT USER_ID,FIRST_NAME,LAST_NAME,EMAIL,PHONE,PROFILE_PICTURE,LOCATION FROM USER_MASTER WHERE upper(EMAIL) in ('EMAIL_PLACEHOLDER') OR upper(PHONE) in ('PHONE_PLACEHOLDER')";
    public String EMAIL_PLACEHOLDER = "'EMAIL_PLACEHOLDER'";
    public String PHONE_PLACEHOLDER = "'PHONE_PLACEHOLDER'";
    public String USER_FOLLOWS = "select user_id from user_following where FOLLOWING_USER_ID= ? and USER_ID = ? ";
    public String GET_FOLLOWING_FEED = "select distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id,m.created_date as cr_date from  media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?, user_master u , "
	    + "(select media_id,user_id from user_media where user_id in (select following_user_id from user_following where user_id = ?) "
	    + "and ( privilege = '" + Constants.PUBLIC_VISIBILITY + "' OR privilege = '"
	    + Constants.FOLLOWERS_VISIBILITY
	    + "'))  a where m.media_id = a.media_id and lower(m.file_type)!= 'video' and u.user_id = a.user_id  ";
    public String MEDIA_FLAG = " and m.flag!= 'Y' ";

    public String USER_FEED_PREFIX = "select distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id from media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?, user_master u , (select media_id,user_id from user_media where user_id =? and ( privilege = '"
	    + Constants.PUBLIC_VISIBILITY + "'";
    public String USER_FEED_FOLLOW = "OR privilege = '" + Constants.FOLLOWERS_VISIBILITY + "'";
    public String USER_FEED_POSTFIX = "))  a where m.media_id = a.media_id and m.flag!= 'Y' and u.user_id = a.user_id and m.file_type != '"
	    + Constants.VIDEO + "' ";
    public String GET_PUBLIC_FEED_PREFIX = "select distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id from media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?, user_master u , "
	    + "(select media_id,user_id from user_media where ( privilege = '" + Constants.PUBLIC_VISIBILITY
	    + "'))  a where m.media_id = a.media_id  and m.UPLOADER = u.USER_ID ";
    public String GET_PUBLIC_FEED_FOR_BUSINESS = " and u.user_type = ? ";

    public String GET_PUBLIC_FEED_POSTFIX = " order by m.created_date desc ";
    public String GET_PLAYLIST_FOR_USER = "select * from playlist_master where user_id =? ";
    public String GET_PLAYLIST_FOR_USER_FOLLOWING = "SELECT * FROM playlist_master WHERE user_id IN  (SELECT user_id FROM user_following WHERE following_user_id = ? ) AND privilege != ? ";
    public String INSERT_MEDIA = "INSERT INTO MEDIA_MASTER(MEDIA_ID,FILE_NAME,FILE_TYPE,MUSIC_PREF_ID,UPLOADER,TAGS,PATH,IS_AD,SKIP_FLAG,LOCATION,PRIVILEGE,LATITUDE,LONGITUDE,ARTIST,THUMBNAIL,DESCRIPTION,ARTIST_ID,ALBUM_ID,MEDIA_LENGTH,RANK,TITLE,SONG_TITLE,EDL_FILE_PATH,METADATA_FILE_PATH,IS_SKIT,MEDIA_DATE,STATION_LIST,IS_FEATURED,PROJECT_NAME,POST_TYPE,posted_as,posted_id,scheduled_Start_Time,scheduled_End_Time,is_Scheduled,series_id,episode_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?)";
    public String INSERT_USER_MEDIA = "INSERT INTO USER_MEDIA(USER_ID,MEDIA_ID,MEDIA_DATE,PRIVILEGE,ACTIVITY_ID) VALUES (?,?,NOW(),?,?)";
    public String INSERT_USER_MEDIA_ACTIVITY = "INSERT INTO ACTIVITY_MASTER(ACTIVITY_ID,ACTIVITY_NAME,ACTIVITY_DESCRIPTION,CREATED_DATE,USER_ID,ACTIVITY_TYPE,MEDIA_ID) VALUES (?,?,?,NOW(),?,?,?)";
    public String GET_TRENDING_MEDIA_FEED = "select distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id from media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and m.flag!= 'Y' and lm.USER_ID = ?,"
	    + " user_master u where (rank != '-1' or uploader = ?) and (m.uploader = u.user_id) AND "
	    + "(m.uploader NOT IN   (SELECT user_id  FROM user_blocked   WHERE blocked_user_id =?  )) "
	    + "AND (m.uploader NOT IN   (SELECT blocked_user_id FROM user_blocked WHERE user_id =?  )) "
	    + "order by CAST(rank AS SIGNED) desc";
    public String UPDATE_GOOGLE_ID_FOR_USER = "UPDATE USER_MASTER SET GOOGLE_ID=? WHERE USER_ID = ? ";
    public String UPDATE_GOOGLE_ID_FOR_EMAIL = "UPDATE USER_MASTER SET GOOGLE_ID=? WHERE EMAIL = ? ";
    public String UPDATE_FB_ID_FOR_EMAIL = "UPDATE USER_MASTER SET FB_ID=? WHERE EMAIL = ? ";
    public String UPDATE_FB_ID_FOR_USER = "UPDATE USER_MASTER SET FB_ID=? WHERE USER_ID = ? ";
    public String UPDATE_CATEGORY_FOR_USER = "UPDATE USER_MASTER SET CATEGORY=?,USER_TYPE=? , BUSINESS_EMAIL=?, BUSINESS_ADDRESS=?, BUSINESS_NAME=?,BUSINESS_PHONE=? ,BUSINESS_BIO=?, BUSINESS_WEBSITE=?,BUSINESS_FACEBOOK=?,BUSINESS_YOUTUBE=?,BUSINESS_TWITTER=?,BUSINESS_INSTAGRAM=?,BUSINESS_PROFILE_PICTURE=?, KEYWORDS=?,shopify_shop_name=?,shopify_access_token=? WHERE USER_ID = ? ";
    public String UPDATE_USER_TO_ARTIST = "UPDATE USER_MASTER SET artist_reference=?,USER_TYPE=? WHERE USER_ID = ? ";
    public String UPDATE_ARTIST_REFERENCE = "UPDATE USER_MASTER SET artist_reference=? WHERE USER_ID = ? ";
    public String REMOVE_ARTIST_ASSOCIATION = "UPDATE USER_MASTER SET artist_reference=?,USER_TYPE=?,CATEGORY=? WHERE USER_ID = ? ";

    public String GET_FOLLOWING_USER_COUNT = "select count(1) from user_following where user_id = ?";
    public String GET_FOLLOWING_BUSINESS_COUNT = "select count(1) from user_business_following where user_id = ?";
    public String GET_FOLLOWING_ARTIST_COUNT = "select count(1) from user_artist_following where user_id = ?";
    public String GET_ARTIST_FOLLOWER_COUNT = "select count(1) from user_artist_following where FOLLOWING_ARTIST_ID = ?";
    public String GET_BUSINESS_FOLLOWER_COUNT = "select count(1) from user_business_following where following_user_id = ?";

    public String GET_MEDIA_UPLOADER_PROJECT = "select media_id from media_master where lower(uploader) = ? and lower(project_name)= ?";
    public String GET_FOLLOWERS_USER_COUNT = "select count(1) from user_following where following_user_id = ?";
    public String GET_FOLLOWING_USERS = "select   i_uf.user_id as follows ,u_m.*,count_val  from  user_following u_f left join  user_following i_uf on i_uf.user_id = ? and i_uf.FOLLOWING_USER_ID = u_f.FOLLOWING_USER_ID, user_master u_m,(select count(1)  count_val  from user_following where user_id = ?) cv where u_f.user_id = ? and u_m.user_id = u_f.following_user_id ";
    public String GET_FOLLOWING_BUSINESS = "select u_m.*,count_val  from user_business_following u_f, user_master u_m,(select count(1)  count_val  from user_business_following where user_id = ?) cv where u_f.user_id = ? and u_m.user_id = u_f.following_user_id ";
    public String GET_FOLLOWING_ARTISTS = "select  i_af.USER_ID as follows,a_m.*,count_val  from user_artist_following u_f left join user_artist_following i_af on i_af.USER_ID = ? and i_af.FOLLOWING_ARTIST_ID = u_f.FOLLOWING_ARTIST_ID, artist_master a_m,(select count(1)  count_val  from user_artist_following where user_id = ?) cv where u_f.user_id = ? and a_m.ARTIST_ID = u_f.FOLLOWING_ARTIST_ID ";
    public String SUGGEST_FOLLOWING_USERS = "SELECT * FROM user_master where user_id!= ? and user_id not in (select FOLLOWING_USER_ID from user_following where USER_ID =?  ) and user_id in (select FOLLOWING_USER_ID from  user_following where USER_ID in (select FOLLOWING_USER_ID from user_following where USER_ID=? ))  ";
    public String SUGGEST_FOLLOWING_BUSINESS = "SELECT * FROM user_master where user_id not in (select FOLLOWING_USER_ID from user_business_following where USER_ID =?  ) and user_id in (select FOLLOWING_USER_ID from  user_business_following where USER_ID in (select FOLLOWING_USER_ID from user_following where USER_ID=? ))  ";
    public String SUGGEST_FOLLOWING_ARTISTS = "SELECT * FROM artist_master where ARTIST_ID not in (select FOLLOWING_ARTIST_ID from user_artist_following where USER_ID =? ) and ARTIST_ID in (select FOLLOWING_ARTIST_ID from  user_artist_following where USER_ID in (select FOLLOWING_USER_ID from user_following where USER_ID=? ))  ";
    public String GET_FOLLOWERS = "select   i_uf.user_id AS follows,u_m.*,count_val  from user_following u_f LEFT JOIN user_following i_uf ON i_uf.user_id = ? AND i_uf.FOLLOWING_USER_ID = u_f.USER_ID, user_master u_m,(select count(1)  count_val  from user_following where following_user_id = ?) cv where u_f.following_user_id = ? and u_m.user_id = u_f.user_id ";
    public String GET_ALL_ACTIVITY = "select lm.USER_ID as like_user_id,m.*,am.* from"
	    + " ( activity_master am left join( media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ? )on am.MEDIA_ID = m.MEDIA_ID)"
	    + " where am.user_id =?  order by am.created_date desc  ";
    public String GET_FOLLOWER_ACTIVITY = "select lm.USER_ID as like_user_id,am.created_date as cr_date,m.*,am.*,u.* from "
	    + " (activity_master am left join ( media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ? ) on am.MEDIA_ID = m.MEDIA_ID, "
	    + "user_master u)  where am.user_id  in (select user_id from user_following where FOLLOWING_USER_ID =? )  and am.USER_ID = u.USER_ID  order by am.created_date desc    ";
    public String GET_FOLLOWING_ACTIVITY = "select lm.USER_ID as like_user_id,am.created_date as cr_date,m.*,am.*,u.* from "
	    + " (activity_master am left join ( media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ? ) on am.MEDIA_ID = m.MEDIA_ID, "
	    + "user_master u)  where am.user_id  in (select FOLLOWING_USER_ID from user_following where USER_ID =? )  and am.USER_ID = u.USER_ID  order by am.created_date desc    ";
    public String GET_PLAYLIST_OF_ACTIVITY = "select * from playlist_master where playlist_id = ? ";
    public String GET_STATION_OF_ACTIVITY = "select * from station_master where station_id = ? ";
    public String GET_PLAYLIST_ITEMS = "select distinct " + MEDIA_COLUMNS
	    + ", p.*,lm.USER_ID as like_user_id from (media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?) inner join playlist_items p where playlist_id = ? and m.media_id = p.media_id  and m.flag!= 'Y' order by CAST(p.rank AS SIGNED) ";
    public String EDIT_MEDIA_PREFIX = "UPDATE MEDIA_MASTER SET FILE_NAME=?,FILE_TYPE=?,MUSIC_PREF_ID=?,UPLOADER=?,TAGS=?,PATH=?,IS_AD=?,SKIP_FLAG=?,LOCATION=?"
	    + ",PRIVILEGE=?,LATITUDE=?,LONGITUDE=?,ARTIST=?,THUMBNAIL=?,DESCRIPTION=?,ARTIST_ID=?,ALBUM_ID=?,MEDIA_LENGTH=?,RANK=?,TITLE=?,SONG_TITLE=?,EDL_FILE_PATH=?,"
	    + "IS_SKIT=?,STATION_LIST=?,IS_FEATURED=?, PROJECT_NAME=?,POST_TYPE=?,posted_as=?,posted_id=?,scheduled_Start_Time=?,"
	    + "scheduled_End_Time=?,is_Scheduled=?";
    public String WHERE_MEDIA_ID = " WHERE MEDIA_ID=?";
    public String EDIT_MEDIA = EDIT_MEDIA_PREFIX + WHERE_MEDIA_ID;
    public String EDIT_MEDIA_WITH_METADATA = EDIT_MEDIA_PREFIX + ",METADATA_FILE_PATH=?  " + WHERE_MEDIA_ID;
    public String EDIT_USER_MEDIA = "UPDATE USER_MEDIA SET MEDIA_DATE = NOW(),PRIVILEGE=? WHERE USER_ID=? AND MEDIA_ID=?";
    public String GET_ACTIVITY_ID_FOR_USER_MEDIA = "SELECT ACTIVITY_ID FROM USER_MEDIA WHERE USER_ID=? AND MEDIA_ID=? ";
    public String EDIT_USER_MEDIA_ACTIVITY = "UPDATE ACTIVITY_MASTER SET ACTIVITY_NAME=?,ACTIVITY_DESCRIPTION=?,CREATED_DATE=NOW(),ACTIVITY_TYPE=? WHERE ACTIVITY_ID =?";
    public String INSERT_PLAYLIST = "INSERT INTO PLAYLIST_MASTER(PLAYLIST_ID,USER_ID,PLAYLIST_NAME,CREATED_DATE,privilege,THUMBNAIL,ACTIVITY_ID) VALUES (?,?,?,NOW(),?,?,?)";
    public String INSERT_PLAYLIST_ITEM = "INSERT INTO PLAYLIST_ITEMS(PLAYLIST_ID,MEDIA_ID,RANK) VALUES (?,?,?)";
    public String COPY_PLAYLIST_ITEM = " insert into playlist_items(PLAYLIST_ID, media_id, rank )  select ?,media_id, rank from playlist_items where playlist_id = ? ";
    public String DELETE_PLAYLIST_ITEM = "DELETE FROM PLAYLIST_ITEMS WHERE PLAYLIST_ID=? AND MEDIA_ID=?";
    public String PLAYLIST_EXISTS = "SELECT PLAYLIST_ID,USER_ID FROM PLAYLIST_MASTER WHERE PLAYLIST_ID=? ";
    public String PLAYLIST_FOLLOWER_EXISTS = "select playlist_id from playlist_master where (PLAYLIST_ID = ?) AND ( (user_id = ?) or ((privilege = ?) or ((privilege !=?) and user_id in (select user_id from user_following where following_user_id = ?) ))) ";
    public String DELETE_PLAYLIST_ITEMS = "DELETE FROM PLAYLIST_ITEMS WHERE PLAYLIST_ID=?";
    public String DELETE_PLAYLIST = "DELETE FROM PLAYLIST_MASTER WHERE PLAYLIST_ID=? AND USER_ID=? ";
    public String GET_STATION_FOR_USER = "SELECT sm.STATION_ID,sm.USER_ID,sm.MUSIC_PREF_ID,sm.ACTIVITY_ID,sm.STATION_NAME,sm.PRIVILEGE, m.THUMBNAIL FROM station_master sm left join media_master m on m.MEDIA_ID = (select MEDIA_ID from media_master where lower(EDL_FILE_PATH) like concat('%',lower(sm.STATION_NAME),'%') order by created_date desc limit 1) where user_id= ? or STATION_ID in (select station_id from station_share where user_id = ?) ";
    public String DELETE_STATION_ITEM = "DELETE FROM STATION_ITEMS WHERE STATION_ID=? AND MEDIA_ID=?";
    public String STATION_EXISTS_FOR_USER = "SELECT STATION_ID FROM STATION_MASTER WHERE (STATION_ID=? AND USER_ID=?) OR STATION_ID in (select station_id from station_share where station_id = ? AND USER_ID=?) ";
    public String STATION_EXISTS = "SELECT STATION_ID FROM STATION_MASTER WHERE STATION_ID= ?";
    public String DELETE_STATION_ITEMS = "DELETE FROM STATION_ITEMS WHERE STATION_ID=?";
    public String DELETE_CAMEO_MEDIA = "DELETE FROM CAMEO_MASTER WHERE MEDIA_ID=?";
    public String DELETE_LIKE_MEDIA = "DELETE FROM LIKE_MASTER WHERE MEDIA_ID=?";
    public String DELETE_COMMENTS_MEDIA = "DELETE FROM media_comments WHERE MEDIA_ID=?";
    public String DELETE_LOOP_MEDIA = "DELETE FROM media_loop WHERE MEDIA_ID=?";
    public String DELETE_MEDIA = "DELETE FROM MEDIA_MASTER WHERE MEDIA_ID=?";
    public String DELETE_MEDIA_METADATA = "DELETE FROM MEDIA_METADATA WHERE MEDIA_ID=?";
    public String DELETE_PLAYLIST_ITEMS_MEDIA = "DELETE FROM PLAYLIST_ITEMS WHERE MEDIA_ID=?";
    public String DELETE_STATION_ITEMS_MEDIA = "DELETE FROM STATION_ITEMS WHERE MEDIA_ID=?";
    public String DELETE_USER_MEDIA = "DELETE FROM USER_MEDIA WHERE MEDIA_ID=?";
    public String GET_SPECIFIC_MEDIA = "SELECT distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id FROM media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?,user_master u "
	    + "WHERE m.MEDIA_ID=? and m.UPLOADER = u.USER_ID  ";

    public String DELETE_STATION = "DELETE FROM STATION_MASTER WHERE STATION_ID=? AND USER_ID=? ";
    public String INSERT_STATION = "INSERT INTO STATION_MASTER(STATION_ID,USER_ID,CREATED_DATE,MUSIC_PREF_ID,ACTIVITY_ID,STATION_NAME,PRIVILEGE,THUMBNAIL,TAGS) VALUES (?,?,NOW(),?,?,?,?,?,?)";

    public String INSERT_SHOPIFY_PRODUCT = "INSERT INTO shopify_product_master(shopify_product_id, shopify_id, title, body_html, vendor, tags, image_id, image_src, business_username, price, created_by,buy_now_url) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    public String UPDATE_SHOPIFY_PRODUCT = "update shopify_product_master set shopify_id = ? , title = ? , body_html = ? , vendor = ? , tags = ? , image_id = ? , image_src = ? , business_username = ? , price = ? , created_by = ?,buy_now_url=? where shopify_product_id = ? ";
    public String GET_SHOPIFY_ID = "select shopify_product_id  from shopify_product_master where shopify_id = ?";
    public String DELETE_SHOPIFY = "DELETE FROM shopify_product_master WHERE shopify_product_id=?";
    public String SEARCH_SHOPIFY = "SELECT * FROM shopify_product_master WHERE concat_ws(' ' , title, body_html,tags,vendor) REGEXP ?  ";
    public String GET_SHOPIFY_BY_VENDOR = "SELECT * FROM shopify_product_master WHERE lower(business_username) = ?   ";
    public String INSERT_STATION_ITEM = "INSERT INTO STATION_ITEMS(STATION_ID,MEDIA_ID,RANK) VALUES (?,?,?)";
    public String SEARCH_STATION = "SELECT * FROM station_master where ( (user_id not in (select user_id from user_blocked where blocked_user_id =? )) AND  (user_id not in (select blocked_user_id from user_blocked where user_id =? ))) ";
    public String SEARCH_STATION_CONDITION = "and concat_ws(' ' , STATION_NAME, TAGS) REGEXP ? ";
    // Search User media query
    // #blockImplemented
    public String SEARCH_MEDIA = "SELECT distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id FROM media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and  m.flag!= 'Y' and lm.USER_ID = ?,user_master u "
	    + "where UPLOADER = u.USER_ID and UPLOADER = ?  ";
    public String SEARCH_USER_MEDIA = "select distinct " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id from media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and m.flag!= 'Y' and lm.USER_ID = ?, user_master u "
	    + "where (concat_ws(' ' , song_title, file_name,STATION_LIST,tags,description,title) REGEXP ? or"
	    + " artist_id in (select artist_id from artist_master where  concat_ws(' ' , ARTIST_NAME, ARTIST_DESCRIPTION,TAGS,NATIONALITY,GENERE) REGEXP ?  )  "
	    + "or album_id in ( select album_id from album_master where concat_ws(' ' , album_NAME,DESCRIPTION,STUDIO ) REGEXP ? "
	    + "or artist_id in ( select artist_id from artist_master where    concat_ws(' ' , ARTIST_NAME, ARTIST_DESCRIPTION,TAGS,NATIONALITY,GENERE) REGEXP ?"
	    + " ) ) ) and (m.uploader = u.user_id) AND (m.uploader not in (select user_id from user_blocked where blocked_user_id =? )) "
	    + "AND (m.uploader not in (select blocked_user_id from user_blocked where user_id =? ))  and u.IS_ACTIVE!='N' and lower(u.privacy_setting)!='private' ";
    String SEARCH_MEDIA_FEATURED = " and IS_FEATURED =  ";

    String POST_TYPE_FILTER = " and FILE_TYPE = ?  ";
    public String GET_NEARBY_FEEDS = "SELECT distinct  " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id, (DEGREES(ACOS(sin(RADIANS(LATITUDE)) * sin(RADIANS(?)) + cos(RADIANS(LATITUDE)) * cos(RADIANS(?)) * cos(RADIANS(LONGITUDE - ?))))*60*1.1515) AS DIST FROM  media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID , user_master u  where  (m.uploader = u.user_id) and m.flag!= 'Y' AND PRIVILEGE = ? order by DIST ";
    public String GET_NEXT_STATION_ITEMS = "SELECT distinct " + MEDIA_COLUMNS
	    + ",m_s.media_id as id_from_station,lm.USER_ID as like_user_id FROM media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ?, "
	    + "(SELECT MEDIA_ID, MUSIC_PREF_ID, ARTIST, ARTIST_ID, ALBUM_ID  FROM media_master  WHERE media_id IN "
	    + "(SELECT media_id    FROM station_items WHERE station_id = ? )  ) m_s WHERE m.flag!= 'Y' and "
	    + "((m.MEDIA_ID      = m_s.MEDIA_ID) OR (m_s.MUSIC_PREF_ID is not NULL AND m_s.MUSIC_PREF_ID != '' AND m.MUSIC_PREF_ID    = m_s.MUSIC_PREF_ID)"
	    + " OR ( m_s.ARTIST  is not NULL AND m_s.ARTIST !='' AND m.ARTIST = m_s.ARTIST) "
	    + "OR (m_s.ARTIST_ID    is not NULL AND m_s.ARTIST_ID     !='' AND m.ARTIST_ID        = m_s.ARTIST_ID)"
	    + " OR (m_s.ALBUM_ID      is not NULL AND m_s.ALBUM_ID      !='' AND m.ALBUM_ID         = m_s.ALBUM_ID ))"
	    + " AND (m.UPLOADER = ? OR m.PRIVILEGE!= ? ) ";
    public String GET_USER_DETAILS_FOLLOWING = "select um.*,uf.following_user_id,following_count,follower_count      from user_master um left outer join (select * from user_following where user_id = ? and following_user_id  = ? ) uf on um.user_id =uf.user_id ,(select count(1) as following_count from user_following where user_id = ?) fc1 ,(select count(1) as follower_count   from user_following where following_user_id  = ?) fc2 where um.user_id = ?";
    // #blockImplemented
    public String GET_SELF_MEDIA = "select  distinct " + MEDIA_COLUMNS
	    + " from media_master m where media_id in (select media_id from user_media where user_id = ? ) or media_id in (select media_id from media_loop where user_id = ? )  ";
    // #blockImplemented
    public String GET_FOLLOWING_USER_MEDIA = "select  DISTINCT " + MEDIA_COLUMNS
	    + " ,lm.USER_ID as like_user_id from media_master m left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID where m.flag!= 'Y' and m.media_id in (select media_id from user_media where user_id = ? and privilege != ? )  or m.media_id in (select media_id from media_loop where user_id = ? ) ";
    // #blockImplemented
    public String GET_PUBLIC_USER_MEDIA = "select distinct " + MEDIA_COLUMNS
	    + " from media_master m where m.flag!= 'Y' and  media_id in (select media_id from user_media where user_id = ? and privilege = ? )  or media_id in (select media_id from media_loop where user_id = ? ) ";
    public String GET_MEDIA_LIKE_COUNT = "select count(1) like_count  from like_master where media_id = ? ";
    public String GET_USER_MEDIA_LIKE_COUNT = "select count(1) like_count  from like_master where media_id = ? and USER_ID =? ";
    public String GET_MEDIA_DATA = "select METADATA_FILE_PATH   from media_master where media_id = ? ";
    public String GET_MEDIA_DETAILS = "select " + MEDIA_COLUMNS + "   from media_master m where m.media_id = ?";
    public String GET_SCHEDULED_DETAILS = "select " + MEDIA_COLUMNS
	    + ",u.*,datediff(scheduled_End_Time,current_date()) as days from media_master m, user_master u  where m.flag!= 'Y' and (m.uploader = u.user_id) AND m.is_Scheduled = 'Y' "
	    + "having (  days >= 0 and days < 8) and EDL_FILE_PATH like '%STATION_NAME%' ";
    public String GET_SCHEDULED_MEDIA = "select m.media_id,datediff(scheduled_end_time,current_date()) as days from media_master m where m.is_scheduled = 'y' and media_id = ? having (  days > 0 and days < 8) ";
    public String SCHEDULED_MEDIA_EXISTS = "select schedule_id from schedule_master where user_id = ? and media_id =?";
    public String INSERT_SCHEDULED_MEDIA = " insert into schedule_master (schedule_id,user_id,media_id,schedule_time,created_by) values (?,?,?,(select scheduled_End_Time from media_master where media_id =?) , ?)";
    public String GET_USER_SCHEDULED_MEDIA = "select * from schedule_master where user_id = ? ";

    public String GET_SERIES = "select * from series_master ";
    public String DELETE_SCHEDULED_MEDIA = "DELETE FROM schedule_master WHERE schedule_id=? ";
    public String INSERT_SERIES = " insert into series_master (series_id, title, description, thumbnail, director, producer, cast,is_featured,featured_image,genre) values (?,?,?,?,?,?,?,?,?,?)";

    // #blockImplemented
    public String GET_MEDIA_COMMENT_COUNT = "select count(1) comment_count  from media_comments where media_id = ?  group by  media_id ";
    public String GET_MEDIA_LOOP_COUNT = "select count(1) loop_count  from media_loop where media_id = ?  group by  media_id ";
    public String GET_MEDIA_CAMEO_USERS = "select * from user_master where user_id in (select user_id from cameo_master where media_id  = ?) ";
    public String INSERT_MEDIA_COMMENT = "INSERT INTO MEDIA_COMMENTS(COMMENT_ID,MEDIA_ID,USER_ID,COMMENT,TAGS,ACTIVITY_ID,COMMENT_DATE) VALUES (?,?,?,?,?,?,NOW())";
    public String CREATE_PRODUCT_WISHLIST = "INSERT INTO wishlist_master(wishlist_id,user_id,product_id,product_source,product_store,product_handle,created_by) VALUES (?,?,?,?,?,?,?)";
    public String GET_COMMENT_ACTIVITY = "select * from media_comments where COMMENT_ID = ? ";
    public String GET_MEDIA_COMMENTS = "SELECT um.*,mc.* FROM (media_comments mc left join user_master um on um.USER_ID = mc.USER_ID) where mc.media_id = ? order by mc.comment_date desc  ";
    public String DELETE_COMMENT = "DELETE FROM MEDIA_COMMENTS WHERE COMMENT_ID=? AND USER_ID =? ";
    public String LIKE_MEDIA = "INSERT INTO LIKE_MASTER(USER_ID,MEDIA_ID) VALUES (?,?)";
    public String UNLIKE_MEDIA = "DELETE FROM LIKE_MASTER WHERE USER_ID =? AND MEDIA_ID=? ";
    public String GET_BLOCKED_USER = "select blocked_user_id from user_blocked where user_id = ? and blocked_user_id= ? ";
    public String BLOCK_USER = "INSERT INTO user_blocked(user_id,blocked_user_id) VALUES (?,?)";
    public String UN_BLOCK_USER = "delete from user_blocked where user_id = ? and blocked_user_id= ? ";
    public String CREATE_ARTIST = "INSERT INTO ARTIST_MASTER(`ARTIST_ID`, `ARTIST_NAME`, `ARTIST_DESCRIPTION`, `BIRTHDAY`, `TAGS`, `NATIONALITY`, `AGE`, `GENERE`, `FIRST_NAME`, `LAST_NAME`,`NICK_NAME`, `PROFILE_PICTURE`, `FB_URL`, `INSTAGRAM_URL`, `PINTREST_URL`, `YOUTUBE_URL`, `BON2_URL`,UPLOADER,`GENERAL_CATEGORY`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public String SEARCH_ARTIST = "SELECT * FROM artist_master  where concat_ws(' ', `ARTIST_NAME`, `TAGS`, `NATIONALITY`,  `GENERE`,`FIRST_NAME`, `LAST_NAME`,`NICK_NAME`) REGEXP ? ";
    public String SEARCH_ARTIST_MEDIA = "SELECT * FROM artist_master  where ARTIST_ID in (select ARTIST_ID from artist_media where  concat_ws(' ', `media_name`, `TAGS`) REGEXP ?) ";
    public String UPDATE_ARTIST = "UPDATE ARTIST_MASTER set  `ARTIST_NAME` =? , `ARTIST_DESCRIPTION` = ?, `BIRTHDAY` = ? , `TAGS` =? , `NATIONALITY` =? , `AGE` =? , `GENERE` = ? , `FIRST_NAME`=? , `LAST_NAME`= ? ,`NICK_NAME`=?, `PROFILE_PICTURE`= ?, `FB_URL`= ?, `INSTAGRAM_URL`= ?, `PINTREST_URL`= ?, `YOUTUBE_URL`= ?, `BON2_URL`= ?,`GENERAL_CATEGORY`=? where `ARTIST_ID`= ?";
    public String UPDATE_ARTIST_USER = "UPDATE ARTIST_MASTER set  artist_user_id = ? where `ARTIST_ID`= ?";
    public String FORGOT_PASSWORD = "SELECT email,password FROM user_master  where email = ?";
    public String RESET_PASSWORD = "UPDATE user_master set password = ? where email = ? and password=?";
    public String DELETE_ARTIST = "DELETE FROM ARTIST_MASTER WHERE ARTIST_ID=? ";
    public String DELETE_ARTIST_MEDIA = "DELETE FROM ARTIST_MEDIA WHERE ARTIST_ID=? ";

    public String SEARCH_PRODUCT_3 = "SELECT * FROM product_master  where concat_ws(' ', `PRODUCT_NAME`, `BRAND_NAME`, `keywords`) REGEXP ? ";
    public String SEARCH_PRODUCT = "SELECT * FROM product_master  where MATCH(PRODUCT_NAME,BRAND_NAME,keywords) AGAINST(?)   ";
    public String CREATE_PRODUCT = "INSERT INTO product_master( `product_name`, `brand_name`, `website`, `picture`, `shop_link_1`, `shop_link_2`, `shop_link_3`, `shop_link_4`, `shop_link_5`, `description`,general_category,sku,keywords) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public String UPDATE_PRODUCT = "UPDATE product_master set  `product_name`=?, `brand_name`=?, `website`=?, `picture`=?, `shop_link_1`=?, `shop_link_2`=?, `shop_link_3`=?, `shop_link_4`=?, `shop_link_5`=?, `description`=? , general_category= ?,sku=?,keywords=? where `product_id` = ?";
    public String DELETE_PRODUCT = "DELETE FROM product_master WHERE PRODUCT_ID=? ";
    public String GET_USER_BLOCKED_USER = "select blocked_user_id from user_blocked where (user_id = ? and blocked_user_id= ?) or (user_id = ? and blocked_user_id= ?)  ";

    public String INSERT_ARTIST_MEDIA = "INSERT INTO ARTIST_MEDIA(artist_media_id,artist_id,media_name,uploader,description,tags) VALUES (?,?,?,?,?,?)";
    public String CREATE_BUSINESS_PRODUCT = "INSERT INTO business_product_master( product_id,user_id, product_name, `brand_name`, `website`, `picture`, `thumbnail`, `keywords`, `tags`, `description`, `url`, `latitude`,longitude,category,sku) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public String GET_ARTIST_DETAILS = "SELECT * FROM artist_master  where ARTIST_ID =  ? ";
    public String GET_ARTIST_MEDIA = "SELECT * FROM artist_media  where ARTIST_ID =  ? ";
    public String DELETE_BUSINESS_PRODUCT = "DELETE FROM business_product_master WHERE user_id = ?";
    public String DELETE_SPECIFIC_BUSINESS_PRODUCT = "DELETE FROM business_product_master WHERE user_id = ? and product_id =? ";
    public String GET_USER_BUSINESS_PRODUCT = "SELECT * FROM business_product_master WHERE user_id = ? ";
    public String GET_USER_BUSINESS_PRODUCT_BY_ID = "SELECT * FROM business_product_master WHERE product_id = ? ";
    public String UPDATE_BUSINESS_PRODUCT = "UPDATE business_product_master set  product_name=? , `brand_name`=? , `website`=? , `picture`=? , `thumbnail`=? , `keywords`=? , `tags`=? , `description`=? , `url`=? , `latitude`=? ,longitude=? ,category=?,sku=? where product_id=? and user_id=? ";
    public String GET_USER_MEDIA_META_DATA = "SELECT METADATA_FILE_PATH FROM media_master WHERE MEDIA_ID=?  ";
    public String GET_MEDIA_TARGET_USER = "select um1.user_id m_user_id, um1.first_name as m_fname,um1.NOTIFICATION_TOKEN, um1.last_name as m_lname, um1.email, um2.first_name  as  c_fname,  um2.last_name as  c_lname, m.file_type, m.TITLE,  um1.NOTIFICATION_PREF  from user_master um1,user_master um2, media_master m where m.MEDIA_ID= ? and um1.user_id = (select uploader from media_master where media_id = m.MEDIA_ID) and um2.user_id = ?  ";
    public String GET_TARGET_USER = "select um.NOTIFICATION_TOKEN,um.user_id m_user_id, um.first_name as m_fname, um.last_name as m_lname, um.email,um.NOTIFICATION_PREF from user_master um where um.user_id = ? ";
    public String UPDATE_BUSINESS_USER_ID = "UPDATE USER_MASTER SET BUSINESS_USER_ID=? WHERE USER_ID = ? ";
    public String UPDATE_ARTIST_USER_ID = "UPDATE USER_MASTER SET ARTIST_USER_ID=? WHERE USER_ID = ? ";
    public String FLAG_MEDIA = "UPDATE MEDIA_MASTER SET FLAG=?,FLAGGED_BY=?,FLAGGED_REASON=? WHERE MEDIA_ID = ? ";
    public String UPDATE_USER_NOTIFICATION_PREF = "UPDATE USER_MASTER SET NOTIFICATION_PREF=? WHERE USER_ID = ? ";
    public String UPDATE_BUSINESS_USER = "UPDATE user_master set  CATEGORY =? , BUSINESS_EMAIL =? , BUSINESS_ADDRESS=? , BUSINESS_NAME=?,BUSINESS_PHONE=? ,BUSINESS_BIO=?, BUSINESS_WEBSITE =? ,BUSINESS_FACEBOOK =? ,BUSINESS_YOUTUBE =? ,BUSINESS_TWITTER =?,BUSINESS_INSTAGRAM=?,BUSINESS_PROFILE_PICTURE=?,shopify_shop_name=?,shopify_access_token=? where user_id=? ";
    public String GET_TARGET_USER_NOTIFICATION = "SELECT um2.EMAIL, um2.NOTIFICATION_TOKEN , um1.FIRST_NAME,um1.LAST_NAME,um2.NOTIFICATION_PREF FROM user_master um1, user_master um2 where um1.USER_ID=? and um2.USER_ID=? ";
    public String GET_ARTIST_FOLLOWERS = "SELECT um.* FROM user_master um, user_artist_following u_af where FOLLOWING_ARTIST_ID = ? and um.USER_ID = u_af.USER_ID ";
    public String GET_WISH_LIST = "SELECT w_m.*, s_pm.title,s_pm.image_src ,s_pm.business_username,s_pm.buy_now_url , b_pm.picture as b_picture,b_pm.thumbnail b_thumbnail,pm.picture as p_picture,b_pm.product_name as b_product_name,pm.product_name as p_product_name from wishlist_master w_m left join business_product_master b_pm on b_pm.product_id = w_m.product_id left join  product_master pm on  pm.product_id = w_m.product_id left join shopify_product_master s_pm  on s_pm.shopify_product_id=w_m.product_id   where w_m.user_id = ?   order by w_m.created_date desc  ";
    public String DELETE_WISH_LIST = "DELETE FROM wishlist_master WHERE wishlist_id=?  ";
    public String ACTIVATE_USER = "UPDATE USER_MASTER SET IS_ACTIVE=? WHERE USER_ID = ? ";
    public String UPDATE_USER_DETAILS = "update user_master set  password = ? ,first_name = ?, last_name = ?, email = ? ,privacy_setting =?,PROFILE_PICTURE=?,PHONE=?,LOCATION=?,BIO=?  where user_id = ? ";
    public String UPDATE_USER_NOTIFICATION_TOKEN = "update user_master set  NOTIFICATION_TOKEN =?  where user_id = ? ";
    public String GET_POSTS_USER_TAGGED_IN = "select " + MEDIA_COLUMNS
	    + ",u.*,lm.USER_ID as like_user_id  from media_master m  left join like_master lm on lm.MEDIA_ID = m.MEDIA_ID and lm.USER_ID = ? , user_master u  where m.flag!= 'y' and (m.METADATA_FILE_PATH like '%USER_ID_OBJ%' or m.TITLE like '%user_id_tag%' or m.DESCRIPTION like '%user_id_tag%') and m.file_type !='video' and m.UPLOADER = u.USER_ID ";
    public String GET_MEDIA_IN_SERIES = "select " + MEDIA_COLUMNS
	    + ",u.* from media_master m, user_master u  where m.flag!= 'y' and m.UPLOADER = u.USER_ID and m.series_id = ?";

}
