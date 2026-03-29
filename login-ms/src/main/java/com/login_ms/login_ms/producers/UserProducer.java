package com.login_ms.login_ms.producers;

import com.login_ms.login_ms.dto.EmailDto;
import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.entity.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
public class UserProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.name}")
    private String routingQueue;

    public void publishMessageEmailSignUp(UserEntity userEntity){
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(userEntity.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText("Olá, " + userEntity.getName() + ", seu cadastro no banco Fake foi realizado com sucesso!");
        rabbitTemplate.convertAndSend("", routingQueue, emailDto);
    }

    public void publishMessageEmailLogin(UserLoginDto userEntity){
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(userEntity.email());
        emailDto.setSubject("Login Realizado com sucesso !");
        emailDto.setText("Olá, alguém seja você ou não entrou no sua conta do banco :) Mas que bom que esse banco é fake !");
        rabbitTemplate.convertAndSend("", routingQueue, emailDto);
    }
}
