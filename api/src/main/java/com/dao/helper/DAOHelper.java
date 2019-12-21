package com.dao.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.dgc.VMID;

import com.constants.Constants;

/**
 * A helper class used in conversion during data entry and retrieval.
 * @author Shreyas
 *
 */
public class DAOHelper {
	public String getDbDetailsXml() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		BufferedReader br = null;
		String sCurrentLine;
		StringBuffer xmlBuffer = new StringBuffer();
		String path = classLoader.getResource(Constants.DB_DETAILS_PATH).getFile();
		br = new BufferedReader(new FileReader(path));
		while ((sCurrentLine = br.readLine()) != null) {
			xmlBuffer.append(sCurrentLine);
		}
		String xml = xmlBuffer.toString();
		return xml;
	}
	public static String guidGenerator(String prefix,String postFix){
		VMID guid= new VMID();
		prefix = prefix==null?"":prefix;
		postFix = postFix==null?"":postFix;
		String id =prefix+guid.toString()+postFix;
		//System.out.println(id);
		return id;
	}
}
