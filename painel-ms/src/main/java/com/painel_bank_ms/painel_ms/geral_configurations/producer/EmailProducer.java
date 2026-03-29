package com.painel_bank_ms.painel_ms.geral_configurations.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.painel_bank_ms.painel_ms.geral_configurations.dto.EmailNotificationDto;

@Component
public class EmailProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.name}")
    private String emailQueue;

    public void sendEmail(EmailNotificationDto dto) {
        rabbitTemplate.convertAndSend(emailQueue, dto);
    }
}
