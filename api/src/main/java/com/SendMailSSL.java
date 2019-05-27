package com;

import java.util.Base64;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.ConfigReader;
import com.constants.Constants;
import com.dto.MailDTO;

public class SendMailSSL {
    private static final Logger LOG = LoggerFactory.getLogger(SendMailSSL.class);

    public static void sendMail(String toEmail, String subject, String body) {
	try {
	    ConfigReader config = ConfigReader.getObject();
	    final String username = config.getAppConfig(Constants.MAIL_USER);
	    final String password = config.getAppConfig(Constants.MAIL_PASSWORD);
	    final String fromMail = config.getAppConfig(Constants.MAIL_ADDRESS);

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", config.getAppConfig(Constants.MAIL_SMTP));
	    props.put("mail.smtp.port", config.getAppConfig(Constants.MAIL_PORT));

	    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(username, password);
		}
	    });

	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(fromMail));
	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	    message.setSubject(subject);
	    message.setText(body);

	    Transport.send(message);

	} catch (Exception e) {
	    LOG.error("Stack Trace : " + ExceptionUtils.getFullStackTrace(e));
	    LOG.error("Error : " + e.getMessage());

	}
    }

    public static void sendMailFromTemplate(MailDTO mail) {
	try {
	    ConfigReader config = ConfigReader.getObject();
	    final String username = config.getAppConfig(Constants.MAIL_USER);
	    final String password = config.getAppConfig(Constants.MAIL_PASSWORD);
	    final String fromMail = config.getAppConfig(Constants.MAIL_ADDRESS);

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", config.getAppConfig(Constants.MAIL_SMTP));
	    props.put("mail.smtp.port", config.getAppConfig(Constants.MAIL_PORT));
	    String msg = config.getMailTemplate();
	    if (mail.isWelcome) {
		msg = config.getWelcomeMailTemplate();
	    }
	    if (!StringUtils.isEmpty(mail.btnText)) {
		msg = msg.replaceAll(Constants.VIEW_ON_BON2, mail.btnText);
	    }
	    if (!StringUtils.isEmpty(mail.btnUrl)) {
		msg = msg.replaceAll(Constants.URL_REPLACEMENT, mail.btnUrl);
	    }
	    msg = msg.replaceAll(Constants.ACTIVITY_TEXT, mail.activityText);
	    msg = msg.replaceAll(Constants.POSTFIX_REPLACEMENT, mail.urlPostfix);
	    msg = msg.replaceAll(Constants.ACTIVITY_TITLE, mail.activityTitle);
	    if (StringUtils.isEmpty(mail.profilePic)) {
		mail.profilePic = config.getAppConfig(Constants.DEFAULT_PROFILE_PIC);
	    }
	    msg = msg.replaceAll(Constants.TARGET_PROFILE_PICTURE, mail.profilePic);
	    msg = msg.replaceAll(Constants.TARGET_EMAIL_ADDRESS, mail.email);

	    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(username, password);
		}
	    });

	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(fromMail));
	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.email));
	    message.setSubject(mail.subject);
	    message.setContent(msg, "text/html");
	    Transport.send(message);

	} catch (Exception e) {
	    LOG.error("Stack Trace : " + ExceptionUtils.getFullStackTrace(e));
	    LOG.error("Error : " + e.getMessage());

	}
    }

    public static void main(String[] args) throws Exception {
	MailDTO mailDTO = new MailDTO();
	mailDTO.email = "shreyas@troomobile.com";
	mailDTO.subject = "Test";
	mailDTO.activityTitle = "Test Title";
	mailDTO.activityText = "Test Text";
	// mailDTO.profilePic = "https://www.gstatic.com/webp/gallery3/1.png";
	sendMailFromTemplate(mailDTO);
    }
}
