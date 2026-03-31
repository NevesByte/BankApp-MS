package com.email_ms.email_sender_ms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.email_ms.email_sender_ms.dtos.EmailRecordDto;
import com.email_ms.email_sender_ms.exceptions.EmailDispatchException;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(EmailRecordDto emailDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(emailDto.emailTo());
            message.setSubject(emailDto.subject());
            message.setText(emailDto.text());
            emailSender.send(message);
        } catch (MailException e) {
            throw new EmailDispatchException("Erro ao enviar email.", e);
        }
    }
}
