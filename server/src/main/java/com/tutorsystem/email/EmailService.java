package com.tutorsystem.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Properties;

@Configuration
public class EmailService {

    @Autowired
    private Environment environment;

    private String template = null;

    public void sendmail(String recipient, String subject, String content) throws MessagingException, IOException {

        File resource = new ClassPathResource("/email-template.html").getFile();
        template = new String(Files.readAllBytes(resource.toPath()));

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", environment.getProperty("email.smtp.host"));
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(environment.getProperty("email.address"),
                        environment.getProperty("email.password"));
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(environment.getProperty("email.sender"), false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        msg.setSubject(subject);
        content = content.replace("\n", "<br>");
        msg.setContent(template.replace("$subject$", subject).replace("$content$", content).replace("$optouturl$",
                environment.getProperty("email.optouturl")).replace("$loginurl$", environment.getProperty("email" +
                        ".loginurl"))
                , "text/html");

        msg.setSentDate(new Date());

        new Thread(() -> {
            try {
                Transport.send(msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

    }
}
