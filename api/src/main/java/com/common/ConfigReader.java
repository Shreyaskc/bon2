package com.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

import com.constants.Constants;
import com.exception.SystemException;

/**
 * Class to load the properties from the database during the first call.
 * 
 * @author Shreyas
 *
 */
public class ConfigReader {
    public static boolean hasObject = false;
    public static String hostName = "";
    public static String portNumber = "";
    public static String dbName = "";
    public static String userName = "";
    public static String password = "";

    public static String mail_username = "";
    public static String mail_password = "";
    public static String mail_smtphost = "";
    public static String mail_smtpport = "";
    public static boolean executeQueryService = false;
    public static ConfigReader configReaderObject = null;
    private static HashMap<String, String> errorConfig = null;
    private static HashMap<String, String> appConfig = null;
    private static String mailTemplate = "";
    private static String welcomeMailTemplate = "";

    /**
     * Singleton class that instantiates once during the server starts.
     * 
     * @return The config object created once and stored in the server cache.
     * @throws FileParsingException
     * @throws DALException
     * @throws SQLException
     * @throws SystemException
     * @throws IOException
     * @throws NumberFormatException
     */
    public synchronized static ConfigReader getObject() throws Exception {
	if (hasObject) {
	    return configReaderObject;
	} else {
	    appConfig = new HashMap<>();
	    executeQueryService = true;
	    errorConfig = getErrorMessages();
	    configReaderObject = new ConfigReader();
	    hasObject = true;
	    setMailTemplate();
	    setWelcomeMailTemplate();
	    Properties props = new Properties();
	    InputStream is = (ConfigReader.class.getClassLoader().getResourceAsStream(Constants.APP_PROPS));
	    props.load(is);
	    String prop = props.getProperty(Constants.MAIL_SMTP);
	    appConfig.put(Constants.MAIL_SMTP, prop);
	    prop = props.getProperty(Constants.MAIL_HEADER);
	    appConfig.put(Constants.MAIL_HEADER, prop);
	    prop = props.getProperty(Constants.MAIL_ADDRESS);
	    appConfig.put(Constants.MAIL_ADDRESS, prop);
	    prop = props.getProperty(Constants.MAIL_PORT);
	    appConfig.put(Constants.MAIL_PORT, prop);
	    prop = props.getProperty(Constants.MAIL_USER);
	    appConfig.put(Constants.MAIL_USER, prop);
	    prop = props.getProperty(Constants.MAIL_PASSWORD);
	    appConfig.put(Constants.MAIL_PASSWORD, prop);
	    prop = props.getProperty(Constants.ARTIST_VERIFICATION_CONTEXT);
	    appConfig.put(Constants.ARTIST_VERIFICATION_CONTEXT, prop);
	    prop = props.getProperty(Constants.ARTIST_VERIFICATION_EMAIL);
	    appConfig.put(Constants.ARTIST_VERIFICATION_EMAIL, prop);
	    prop = props.getProperty(Constants.APP_URL);
	    appConfig.put(Constants.APP_URL, prop);
	    prop = props.getProperty(Constants.ARTIST_VERIFICATION_TOKEN);
	    appConfig.put(Constants.ARTIST_VERIFICATION_TOKEN, prop);
	    prop = props.getProperty(Constants.BUSINESS_PRODUCT_DISTANCE);
	    appConfig.put(Constants.BUSINESS_PRODUCT_DISTANCE, prop);
	    prop = props.getProperty(Constants.DEFAULT_PROFILE_PIC);
	    appConfig.put(Constants.DEFAULT_PROFILE_PIC, prop);
	    prop = props.getProperty(Constants.MYSQL_DB_URL);
	    appConfig.put(Constants.MYSQL_DB_URL, prop);
	    prop = props.getProperty(Constants.MYSQL_DB_USERNAME);
	    appConfig.put(Constants.MYSQL_DB_USERNAME, prop);
	    prop = props.getProperty(Constants.MYSQL_DB_PASSWORD);
	    appConfig.put(Constants.MYSQL_DB_PASSWORD, prop);
	    prop = props.getProperty(Constants.REJECT_FLAG);
	    appConfig.put(Constants.REJECT_FLAG, prop);
	    prop = props.getProperty(Constants.APPROVE_STATION_PARTNER);
	    appConfig.put(Constants.APPROVE_STATION_PARTNER, prop);
	    prop = props.getProperty(Constants.BANTU_MAIL);
	    appConfig.put(Constants.BANTU_MAIL, prop);

	    return configReaderObject;
	}

    }

    /**
     * Set the mail template.
     */
    private static void setMailTemplate() {
	File file = new File(ConfigReader.class.getClassLoader().getResource(Constants.MAIL_TEMPLATE_FILE).getFile());

	StringBuilder result = new StringBuilder("");

	try (Scanner scanner = new Scanner(file)) {

	    while (scanner.hasNextLine()) {
		String line = scanner.nextLine();
		result.append(line).append("\n");
	    }
	    scanner.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	mailTemplate = result.toString();
    }

    public static String getMailTemplate() {
	if (StringUtils.isEmpty(mailTemplate)) {
	    setMailTemplate();
	}
	return mailTemplate;
    }

    private static void setWelcomeMailTemplate() {
	File file = new File(
		ConfigReader.class.getClassLoader().getResource(Constants.WELCOME_MAIL_TEMPLATE_FILE).getFile());

	StringBuilder result = new StringBuilder("");

	try (Scanner scanner = new Scanner(file)) {

	    while (scanner.hasNextLine()) {
		String line = scanner.nextLine();
		result.append(line).append("\n");
	    }
	    scanner.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	welcomeMailTemplate = result.toString();
    }

    public static String getWelcomeMailTemplate() {
	if (StringUtils.isEmpty(welcomeMailTemplate)) {
	    setMailTemplate();
	}
	return welcomeMailTemplate;
    }

    /**
     * Method to set the error config values.
     * 
     * @param errorConfig
     */
    public void setErrorConfig(HashMap<String, String> errorConfig) {
	ConfigReader.errorConfig = errorConfig;
    }

    /**
     * Method to get the error config values.
     * 
     * @return the error config hash map.
     */
    public HashMap<String, String> getErrorConfig() {
	return errorConfig;
    }

    public String getAppConfig(String key) {
	return appConfig.get(key);
    }

    private static HashMap<String, String> getErrorMessages() {
	HashMap<String, String> errorConfig = new HashMap<String, String>();
	errorConfig.put("10001", "System Error Occurred");
	errorConfig.put("100000", "Model already exists.");
	errorConfig.put("100001", "QC name already exists for the selected model.");
	errorConfig.put("100002", "Failed to delete.");
	errorConfig.put("100003", "Model does not exists.");
	errorConfig.put("100004", "User Name already exists.");
	errorConfig.put("100005", "Invalid credentials.");
	errorConfig.put("100006", "Invalid User.");
	errorConfig.put("100007", "Invalid Request.");
	errorConfig.put("100008", "User Email already exists.");
	errorConfig.put("100009", Constants.InvalidPassword);
	errorConfig.put("100010", "Invalid User Id.");
	errorConfig.put("100011", "User Id already exists.");
	errorConfig.put("100012", "Social media id is empty.");
	errorConfig.put("100013", "Empty List.");
	errorConfig.put("100014", "false");
	errorConfig.put("100015", "Cannot Execute query service.");
	errorConfig.put("100016", "Playlist doesn't exist.");
	errorConfig.put("100017", "Station doesn't exist.");
	errorConfig.put("100018", "Invalid Co-ordinates.");
	errorConfig.put("100019", "Invalid Station item.");
	errorConfig.put("100020", "The logged user is blocked by the above user.");
	errorConfig.put("100021", "The search string is empty.");
	errorConfig.put("100022", "Insufficient values.");
	errorConfig.put("100023", "Invalid request format.");
	errorConfig.put("100024", "The category is empty.");
	errorConfig.put("100025", "The selected media is not scheduled.");
	errorConfig.put("100026", "The selected artist is already linked to a user.");
	errorConfig.put("100027", "The account is not activated yet.");
	return errorConfig;
    }

}
