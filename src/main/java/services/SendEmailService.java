package services;

import javax.mail.*;
import javax.servlet.ServletContext;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailService {
    private ServletContext config;
    private String username;
    private String password;
    private String emailHost;
    private int hostPort;
    private String sentFromMail;
    private Properties emailProperties;

    public SendEmailService(ServletContext config) {
        this.config = config;
    }

    public void Init(ServletContext config) {
        emailHost = "smtp.sendgrid.net";//config.Config.getSMTPhost(config);
        hostPort = 587;//config.Config.getSMTPhostPort(config);
        username = "apikey";//config.Config.getSMTPusername(config);
        password = "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";//config.Config.getSMTPpassword(config);
        sentFromMail = "noreply@mmh55ladder.com";//config.Config.getSentFromMail(config);

        emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", emailHost);
        emailProperties.put("mail.smtp.port", Integer.toString(hostPort));
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
    }

    public boolean SendMail(Multipart content, String subject, String addressToSend) {
        boolean success = false;
        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sentFromMail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(addressToSend));
            message.setSubject(subject);
            message.setContent(content);

            Transport.send(message);
            success = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }
}
