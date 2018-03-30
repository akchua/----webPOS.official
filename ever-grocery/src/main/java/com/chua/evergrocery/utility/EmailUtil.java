package com.chua.evergrocery.utility;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 2, 2016
 */
@Component
public class EmailUtil {
	
	@Value("${enable.send.email}")
	private Boolean enableSendEmail;
	
	private final String smtpHost;
	
	private final String smtpPort;
	
	private final String smtpFromAddress;
	
	private final String smtpUsername;
	
	private final String smtpPassword;
	
	private final String defaultEmail;
	
	private Session session = null;
	
	@Autowired
	public EmailUtil(@Value("${mail.smtp.host}") String smtpHost,
			@Value("${mail.smtp.port}") String smtpPort,
			@Value("${mail.smtp.fromAddress}") String smtpFromAddress,
			@Value("${mail.smtp.username}") String smtpUsername,
			@Value("${mail.smtp.password}") String smtpPassword,
			@Value("${mail.defaultEmail}") String defaultEmail) {
		validateEmailValue(smtpFromAddress, "SMTP From Address", true);
		validateEmailValue(smtpUsername, "SMTP Username", true);
		validateEmailValue(defaultEmail, "Default Email", false);
		
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.smtpFromAddress = smtpFromAddress;
		this.smtpUsername = smtpUsername;
		this.smtpPassword = smtpPassword;
		this.defaultEmail = defaultEmail;
		connect();
	}
	
	private void validateEmailValue(String email, String emailField, boolean notNull) {
		if(notNull && (email == null || email.isEmpty())) {
			throw new IllegalArgumentException(emailField + ": " + " cannot be null.");
		} else if(!email.isEmpty() && !EmailUtil.validateEmail(email)) {
			throw new IllegalArgumentException(emailField + ": " + email + " is not a valid email.");
		}
	}
	
	public boolean send(String to, String subject, String content) {
		return send(to, subject, content, null);
	}
	
	public boolean send(String to, String subject, String content, String[] attachments) {
		return send(to, null, null, subject, content, attachments);
	}
	
	public boolean send(String to, String cc, String bcc, String subject, String content, String[] attachments) {
		boolean success = true;

		if(enableSendEmail) {
			try {
				Message message = new MimeMessage(session);
				Multipart multipart = new MimeMultipart();
				
				message.setFrom(new InternetAddress(smtpFromAddress));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				if(cc != null) message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				if(bcc != null) message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc + ", " + defaultEmail));
				message.setSubject(subject);
				
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(message, "text/html");
				messageBodyPart.setText(content);
				multipart.addBodyPart(messageBodyPart);
				
				if(attachments != null && attachments.length > 0) {
					for(String filePath : attachments) {
						MimeBodyPart attachPart = new MimeBodyPart();
						try {
							attachPart.attachFile(filePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
						multipart.addBodyPart(attachPart);
					}
				}
	
				message.setContent(multipart);
				Transport.send(message);
			} catch (MessagingException e) {
				success = false;
				throw new RuntimeException(e);
			}
		}
		
		return success;
	}

	public void connect() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", smtpPort);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		
		session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUsername, smtpPassword);
				}
			});
	}
	
	public static boolean validateEmail(String emailAddress) {
		boolean valid = true;
		String[] emails = emailAddress.split(",");
		
		for(String email : emails) {
			if(!email.trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
										+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
				valid = false;
				break;
			}
		}
		
		return valid;
	}
}
