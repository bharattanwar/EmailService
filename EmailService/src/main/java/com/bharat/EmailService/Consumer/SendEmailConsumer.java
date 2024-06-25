package com.bharat.EmailService.Consumer;

import com.bharat.EmailService.DTO.SendMessageDTO;
import com.bharat.EmailService.Utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import javax.mail.Authenticator;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class SendEmailConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "sendEmail",groupId = "emailService")//listens the events with topic=sendEmail
    public void handleSendEmail(String Message){
        try {
            SendMessageDTO sendMessageDTO=objectMapper.readValue(Message, SendMessageDTO.class);

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("bharat01rajput@gmail.com", "bharatt017");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, sendMessageDTO.getTo(), sendMessageDTO.getSubject(), sendMessageDTO.getBody());


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
