package com.painel_bank_ms.painel_ms.consumer;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import com.painel_bank_ms.painel_ms.dto.UserDataConsumerDto;
import com.painel_bank_ms.painel_ms.entity.UserEntity;
import com.painel_bank_ms.painel_ms.repository.AccountRepository;

@org.springframework.stereotype.Component
public class UserDataConsumer {
    @Autowired
    AccountRepository accountRepository;

    @RabbitListener(queues = "${broker.queue.user.data.name}")
    public void listenUserDataQueue(@Payload UserDataConsumerDto dto) {

        System.out.println("Recebido: " + dto);

        if (accountRepository.existsById(dto.getIdUser())) {
            System.out.println("Já existe → update");

            UserEntity existing = accountRepository.findById(dto.getIdUser()).get();
            BeanUtils.copyProperties(dto, existing, "idUser", "version");

            accountRepository.save(existing);
        } else {
            System.out.println("Novo → insert");

            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(dto, userEntity, "idUser", "version");

            accountRepository.save(userEntity);
        }
    }
}
