package com.painel_bank_ms.painel_ms.geral_configurations.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.painel_bank_ms.painel_ms.account.dtos.UserDataConsumerDto;
import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;



@Component
public class UserDataConsumer {
    private static final Logger log = LoggerFactory.getLogger(UserDataConsumer.class);
    
    @Autowired
    AccountRepository accountRepository;

    @RabbitListener(queues = "${broker.queue.user.data.name}")
    public void listenUserDataQueue(@Payload UserDataConsumerDto dto) {
        if (accountRepository.existsByCpf(dto.getCpf())) {
            log.info("Usuário já existe com CPF: {}", dto.getCpf());
            // não salva, não lança erro → ACK automático
        } else {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(dto, userEntity, "idUser");
            accountRepository.save(userEntity);
            log.info("Usuário salvo: {}", dto.getIdUser());
        }
    }
}
