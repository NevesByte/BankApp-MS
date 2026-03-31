package com.login_ms.login_ms.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.login_ms.login_ms.entity.UserEntity;

@Component
public class UserDataProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.user.data.name}")
    private String routingQueue;

    public UserDataProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void userData(UserEntity userEntity) {
        rabbitTemplate.convertAndSend("", routingQueue, userEntity);
    }
}
