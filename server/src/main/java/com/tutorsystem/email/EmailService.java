package com.tutorsystem.email;

import javax.mail.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EmailService {
    public static void sendmail() throws MessagingException, IOException {

        String subject = "Hello from Java";
        String message = "Whats up";
        String sender = "noreply@oktatutor.com";
        String recipient = "daniel.szabo99@outlook.com";

        runCommand("echo", "\"" + message +"\"" , "|", "mailx", "-r", sender, "-s",  "\"" + subject +"\"", recipient);
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                return new javax.mail.PasswordAuthentication("", "");
//            }
//        });
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress("daniel.tailor99@gmail.com", false));
//
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(""));
//        msg.setSubject("Hello bello");
//        msg.setContent("This is an email", "text/html");
//        msg.setSentDate(new Date());
//
////        MimeBodyPart messageBodyPart = new MimeBodyPart();
////        messageBodyPart.setContent("Tutorials point email", "text/html");
////        Multipart multipart = new MimeMultipart();
////        multipart.addBodyPart(messageBodyPart);
////        MimeBodyPart attachPart = new MimeBodyPart();
////        multipart.addBodyPart(attachPart);
////        msg.setContent(multipart);
//
//        Transport.send(msg);
    }

    private static void runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);

        try {
            Process process = processBuilder.start();

            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = null;
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println(output);
            }

            //wait for the process to complete
            process.waitFor();

            //close the resources
            bufferedReader.close();
            process.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
