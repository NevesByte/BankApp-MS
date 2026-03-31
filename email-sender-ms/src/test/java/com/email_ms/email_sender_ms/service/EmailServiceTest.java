package com.email_ms.email_sender_ms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.email_ms.email_sender_ms.dtos.EmailRecordDto;
import com.email_ms.email_sender_ms.exceptions.EmailDispatchException;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void shouldSendEmailSuccessfully() {
        ReflectionTestUtils.setField(emailService, "emailFrom", "noreply@bankapp.local");
        EmailRecordDto dto = new EmailRecordDto("user@test.com", "Assunto", "Mensagem");

        emailService.sendEmail(dto);

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldThrowEmailDispatchExceptionWhenSendFails() {
        ReflectionTestUtils.setField(emailService, "emailFrom", "noreply@bankapp.local");
        EmailRecordDto dto = new EmailRecordDto("user@test.com", "Assunto", "Mensagem");

        doThrow(new MailSendException("error")).when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThrows(EmailDispatchException.class, () -> emailService.sendEmail(dto));
    }
}
