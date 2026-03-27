package main.java.com.email_ms.email_sender_ms.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.email_ms.email_sender_ms.dto.EmailRecordDto;
import com.email_ms.email_sender_ms.model.EmailModel;
import com.email_ms.email_sender_ms.service.EmailService;

@Component
public class EmailConsumer {
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {
        EmailModel emailModel = new EmailModel();
        emailService.sendEmail(emailRecordDto);
    }
}
