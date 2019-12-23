package com.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.ConfigReader;
import com.common.RandomString;
import com.constants.ErrorCodes;
import com.dao.helper.DAOHelper;
import com.dto.ShopifyProductDTO;
import com.exception.SystemException;

public class UrlIdGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(UrlIdGenerator.class);

	public static void main(String[] args) throws Exception {
    	RandomString gen = new RandomString(12, ThreadLocalRandom.current());

		UrlIdGenerator obj = new UrlIdGenerator();
		obj.updateUrlId(gen);

	}
	 private void closeDbConnection(Database db) {
			try {
			    db.closeConnection();
			} catch (Exception e) {
			    System.out.println("Error While Closing connection.");
			}
		    }

    
    public void updateUrlId(RandomString gen) throws Exception {
    	String query = "select media_id, url_id from media_master limit 2500";
    	
    	Database db = new Database();
    	List<String> mediaList = new LinkedList<>();
    	LinkedList<ShopifyProductDTO> shopList = new LinkedList<ShopifyProductDTO>();
    	try {
    	    LOG.debug("query>>>>" + query);
    	    LinkedList<Object> paramList = new LinkedList<Object>();
    	    db.executeQuery(query.toString(), paramList);
    	    while (db.cRowSet.next()) {
    	    	String mediaId = db.cRowSet.getString("media_id");
    	    	String urlId = db.cRowSet.getString("url_id");
    	    	if(StringUtils.isEmpty(urlId)) {
    	    		mediaList.add(mediaId);
    	    	}
    		
    	    }
    	} catch (Exception e) {
    	    LOG.error("There was an error in DB query " + e.getMessage());
    	    throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
    		    ErrorCodes.StatusCodes.FAILURE, null);
    	} finally {
    	    closeDbConnection(db);
    	}
    	mediaList.parallelStream().forEach(mediaId ->{
    		try {
      			 LOG.debug("Id : "+mediaId);
   				editUserMediaUrlId(mediaId,gen);
   			} catch (Exception e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
    	});
    	
    	
        }
    
    public boolean editUserMediaUrlId(String mediaId, RandomString gen) throws Exception {
    	
    	Database db = new Database();

    	try {
    	    String urlId = gen.nextString();

    	    String query = "UPDATE MEDIA_MASTER SET url_id = ? where MEDIA_ID=?";
    	      LinkedList<Object> params = new LinkedList<>();
    	    params.add(urlId);
    	    params.add(mediaId);
    	    LOG.debug("query>>>>" + query);

    	    int count = db.executeUpdate(query.toLowerCase(), params);
    	    if (count <= 0) {
    		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
    			ErrorCodes.StatusCodes.FAILURE, null);
    	    }

    	    return true;
    	} catch (Exception e) {
    	    try {
    		// We rollback the transaction, atomicity!

    		LOG.error(e.getMessage());
    		LOG.error("The transaction was rollback");
    		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
    			ErrorCodes.StatusCodes.FAILURE, null);
    	    } catch (Exception e1) {
    		LOG.error("There was an error making a rollback");
    		throw new SystemException(ErrorCodes.GENERIC_EXCEPTION, ConfigReader.getObject().getErrorConfig(),
    			ErrorCodes.StatusCodes.FAILURE, null);
    	    }
    	} finally {
    	    closeDbConnection(db);
    	}
        }


}
