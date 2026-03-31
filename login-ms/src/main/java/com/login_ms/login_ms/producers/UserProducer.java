package com.login_ms.login_ms.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.login_ms.login_ms.dto.EmailDto;
import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.entity.UserEntity;

@Component
public class UserProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.name}")
    private String routingQueue;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMessageEmailSignUp(UserEntity userEntity) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(userEntity.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso");
        emailDto.setText("Ola, " + userEntity.getName() + ", seu cadastro no banco fake foi realizado com sucesso.");
        rabbitTemplate.convertAndSend("", routingQueue, emailDto);
    }

    public void publishMessageEmailLogin(UserLoginDto userEntity) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(userEntity.email());
        emailDto.setSubject("Login realizado com sucesso");
        emailDto.setText("Ola, um login foi identificado em sua conta.");
        rabbitTemplate.convertAndSend("", routingQueue, emailDto);
    }
}
