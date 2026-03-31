package com.email_ms.email_sender_ms.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.email_ms.email_sender_ms.dtos.EmailRecordDto;
import com.email_ms.email_sender_ms.service.EmailService;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {
        emailService.sendEmail(emailRecordDto);
    }
}
