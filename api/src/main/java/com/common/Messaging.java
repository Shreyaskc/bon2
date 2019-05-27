package com.common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.constants.Constants;
import com.dto.NotificationDTO;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Messaging {

    private static String PROJECT_ID = "bon2-app";
    private static String BASE_URL = "https://fcm.googleapis.com";
    private static String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private static String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static String[] SCOPES = { MESSAGING_SCOPE };

    private static String TITLE = "title";
    private static String NOTIFICATION = "notification";
    private static String BODY = "body";
    private static String MESSAGE_KEY = "message";
    private static String TOKEN = "token";
    private static String DATA = "data";
    private static Logger LOG = LoggerFactory.getLogger(Messaging.class);

    /**
     * Retrieve a valid access token that can be use to authorize requests to the
     * FCM REST API.
     *
     * @return Access token.
     * @throws IOException
     */
    // [START retrieve_access_token]
    private static String getAccessToken() throws IOException {
	try {
	    InputStream is = (Messaging.class.getClassLoader().getResourceAsStream(Constants.FIREBASE_JSON));
	    GoogleCredential googleCredential = GoogleCredential.fromStream(is).createScoped(Arrays.asList(SCOPES));
	    googleCredential.refreshToken();
	    return googleCredential.getAccessToken();
	} catch (Exception e) {
	    LOG.error("Error fetching access token : " + e.getMessage());
	}
	return null;
    }
    // [END retrieve_access_token]

    /**
     * Create HttpURLConnection that can be used for both retrieving and publishing.
     *
     * @return Base HttpURLConnection.
     * @throws IOException
     */
    private static HttpURLConnection getConnection() throws IOException {
	// [START use_access_token]
	URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
	String accessToken = getAccessToken();
	HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	LOG.debug("Access token : " + accessToken);
	httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
	httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
	return httpURLConnection;
	// [END use_access_token]
    }

    /**
     * Send request to FCM message using HTTP.
     *
     * @param fcmMessage Body of the HTTP request.
     * @throws IOException
     */
    private static void sendMessage(JsonObject fcmMessage) throws IOException {
	HttpURLConnection connection = getConnection();
	connection.setDoOutput(true);
	DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
	outputStream.writeBytes(fcmMessage.toString());
	outputStream.flush();
	outputStream.close();

	int responseCode = connection.getResponseCode();
	if (responseCode == 200) {
	    String response = inputstreamToString(connection.getInputStream());
	    LOG.debug("Message sent to Firebase for delivery, response:");
	    LOG.debug(response);
	} else {
	    LOG.error("Unable to send message to Firebase:");
	    String response = inputstreamToString(connection.getErrorStream());
	    LOG.debug(response);
	}
    }

    /**
     * Send notification message to FCM for delivery to registered devices.
     *
     * @throws IOException
     */
    public static void sendCommonMessageNew(String title, String body, Map<String, String> data, String token)
	    throws IOException {
	try {

	    JsonObject notificationMessage = buildNotificationMessage(title, body, data, token);
	    LOG.debug("FCM request body for message using common notification object:");
	    prettyPrint(notificationMessage);
	    sendMessage(notificationMessage);
	} catch (Exception e) {
	    LOG.error("Error sending Push notification : " + e.getMessage());
	    LOG.error("Title : " + title);
	    LOG.error("Body : " + body);
	    LOG.error("Error sending Push notification : " + e.getMessage());

	}
    }

    /**
     * Construct the body of a notification message request.
     *
     * @return JSON of notification message.
     */
    private static JsonObject buildNotificationMessage(String title, String body, Map<String, String> data,
	    String token) {
	JsonObject jNotification = new JsonObject();
	jNotification.addProperty(TITLE, title);
	jNotification.addProperty(BODY, body);
	JsonObject jData = new JsonObject();

	data.forEach((key, value) -> {
	    jData.addProperty(key, value);
	});

	JsonObject jMessage = new JsonObject();
	jMessage.add(NOTIFICATION, jNotification);
	jMessage.add(DATA, jData);
	jMessage.addProperty(TOKEN, token);

	JsonObject jFcm = new JsonObject();
	jFcm.add(MESSAGE_KEY, jMessage);

	return jFcm;
    }

    public static void sendCommonMessage(NotificationDTO notification) throws ClientProtocolException, IOException {
	HttpClient client = new DefaultHttpClient();
	HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
	post.setHeader("Content-type", "application/json");
	post.setHeader("Authorization", "key=AIzaSyCFfDGhm0Ix5z7Yp_cVcahQHZG5aGjHXCc");

	JsonObject message = new JsonObject();
	message.addProperty("to", notification.token);
	message.addProperty("priority", "high");

	if (notification.data != null && !notification.data.isEmpty()) {
	    JsonObject jData = new JsonObject();
	    jData.addProperty(TITLE, notification.title);
	    jData.addProperty(BODY, notification.body);
	    notification.data.forEach((key, value) -> {
		jData.addProperty(key, value);
	    });
	    message.add(DATA, jData);
	}

	JsonObject jNotification = new JsonObject();
	jNotification.addProperty(TITLE, notification.title);
	jNotification.addProperty(BODY, notification.body);

	message.add(NOTIFICATION, jNotification);

	post.setEntity(new StringEntity(message.toString(), "UTF-8"));
	HttpResponse response = client.execute(post);
	LOG.debug("Response : " + response.getStatusLine().toString());
	LOG.debug("Message : " + message);
    }

    /**
     * Read contents of InputStream into String.
     *
     * @param inputStream InputStream to read.
     * @return String containing contents of InputStream.
     * @throws IOException
     */
    private static String inputstreamToString(InputStream inputStream) throws IOException {
	StringBuilder stringBuilder = new StringBuilder();
	Scanner scanner = new Scanner(inputStream);
	while (scanner.hasNext()) {
	    stringBuilder.append(scanner.nextLine());
	}
	return stringBuilder.toString();
    }

    /**
     * Pretty print a JsonObject.
     *
     * @param jsonObject JsonObject to pretty print.
     */
    private static void prettyPrint(JsonObject jsonObject) {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	LOG.debug(gson.toJson(jsonObject) + "\n");
    }

    public static void main(String[] args) throws IOException {
	Map<String, String> dataMap = new HashMap<>();
	dataMap.put(Constants.PAGE, "comment");
	dataMap.put(Constants.ID, "idVal");
//	sendCommonMessage("title app", "body app", dataMap,
//		"f_JRfXJMD6w:APA91bHBCsNjcVTP8AMEO7xj1ejm-ai78lOHRAEsgJrYc7npWVq9GuHv-fu3iGQd5AJ4q2zBakSiX43HD1wuhsnLHEZByP5SYJ1nHz2c2uHCm3nlU6UORAT8MyVFAzngC4zvhYJP8rlm");
//	
	NotificationDTO notificationDTO = new NotificationDTO();
	notificationDTO.title = "title app";
	notificationDTO.body = "body app";
	notificationDTO.data = dataMap;
	notificationDTO.token = "f_JRfXJMD6w:APA91bHBCsNjcVTP8AMEO7xj1ejm-ai78lOHRAEsgJrYc7npWVq9GuHv-fu3iGQd5AJ4q2zBakSiX43HD1wuhsnLHEZByP5SYJ1nHz2c2uHCm3nlU6UORAT8MyVFAzngC4zvhYJP8rlm";
	sendCommonMessage(notificationDTO);

    }

}