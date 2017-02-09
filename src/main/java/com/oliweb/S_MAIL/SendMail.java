package com.oliweb.S_MAIL;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {

	public static void executeTLS(final PropertiesMail propertiesMail, String to, String object, String text) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", propertiesMail.getHost());
		props.put("mail.smtp.host", propertiesMail.getHost());
		props.put("mail.smtp.port", propertiesMail.getPort());

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(propertiesMail.getUsername(), propertiesMail.getPassword());
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(propertiesMail.getFrom()));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(object);
			message.setText(text);

			Transport.send(message);

			System.out.println("Send email to " + to + " done.");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void executeSSL(final PropertiesMail propertiesMail, String to, String object, String text) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(propertiesMail.getUsername(), propertiesMail.getPassword());
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(propertiesMail.getFrom()));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(object);
			message.setText(text + "SSL");

			Transport.send(message);

			System.out.println("Send email to " + to + " done.");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
