package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.dgc.VMID;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.constants.Constants;

public class TestMain {

    /**
     * @param args
     */
	static String URL = "https://kindness1billion.ocde.us/api/mobiledevice";
    public static void main(String[] args) {
//	try {
//	    TestMain testMain = new TestMain();
//	    ClassLoader classLoader = testMain.getClass().getClassLoader();
//	    InputStream is = (TestMain.class.getClassLoader().getResourceAsStream(Constants.MAIL_TEMPLATE_FILE));
//
//	    String mailTemplate = IOUtils.toString(is, StandardCharsets.UTF_8);
//	    System.out.println(mailTemplate);
//	} catch (Exception e) {
//	    e.printStackTrace();
//	    // TODO: handle exception
//	}
    	
    	
    	  try {
			
              URL url = new URL(URL);
              HttpURLConnection connection = (HttpURLConnection) url.openConnection();
              connection.setRequestMethod("POST");
              connection.setDoOutput(true);
              connection.setRequestProperty("Content-Type", "application/json");
              connection.setRequestProperty("Accept", "application/json");
              OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
              osw.write((" {\"UniqueID\": \"16\", \"Story\": \"Short story\"}"));
              osw.flush();
              osw.close();
              System.err.println(connection.getResponseCode());

			  BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			  String inputLine;
			  StringBuffer response = new StringBuffer();

			  while ((inputLine = in.readLine()) != null) {
			      response.append(inputLine);
			  }
			  in.close();
			  String result = null;

			  result = response.toString();
			  System.out.println(result);
			  
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    static void dateFormatter() {
	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	Date date = new Date();
	System.out.println(dateFormat.format(date));
    }

    static String guidGenerator(String prefix, String postFix) {
	VMID guid = new VMID();
	prefix = prefix == null ? "" : prefix;
	postFix = postFix == null ? "" : postFix;
	String id = prefix + guid.toString() + postFix;
	System.out.println(id);
	return id;
    }

    static void specialCharHandler() {
	String aString = "Shreyas Export";
	System.out.println(aString);
	Date dateObj = new Date();
	String dateString = new Timestamp(dateObj.getTime()).toString();
	String fileName = aString + dateString;
	fileName = fileName.replaceAll(":", "_");
	fileName = fileName.replaceAll("\\.", "_");
	fileName = fileName.replaceAll("-", "_");
	fileName = fileName.replaceAll("&", "_");
	fileName = fileName.replaceAll("\\\\", "_");
	fileName = fileName.replaceAll("/", "_");
	aString = aString.replaceAll("\\\\", "_");
	System.out.println(aString);
    }
}
