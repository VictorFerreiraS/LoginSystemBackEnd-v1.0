package com.confirmationemailms.services;

import com.confirmationemailms.email.EmailModel;
import com.confirmationemailms.enums.StatusEmail;
import com.confirmationemailms.repositories.EmailRepository;
import lombok.AllArgsConstructor;
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

    public void sendEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(
                LocalDateTime.now());
        try {
            SimpleMailMessage message  = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            emailRepository.save(emailModel);
        }
    }
}