package com.emailms.services;

import com.emailms.email.EmailModel;
import com.emailms.email.enums.StatusEmail;
import com.emailms.exception.EmailException;
import com.emailms.repositories.EmailRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailService {
    EmailRepository emailRepository;
    private JavaMailSender emailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    public void sendEmail(EmailModel emailModel) throws EmailException {
        emailModel.setSendDateEmail(
                LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
            logger.error("error message", e);
            throw new EmailException(e.getMessage());
        } finally {
            emailRepository.save(emailModel);
        }
    }
}