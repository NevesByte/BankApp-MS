package com.painel_bank_ms.painel_ms.geral_configurations.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.painel_bank_ms.painel_ms.account.dtos.UserDataConsumerDto;
import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.account.enums.AccountStatus;
import com.painel_bank_ms.painel_ms.account.enums.AccountTypeEnum;
import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;

@Component
public class UserDataConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserDataConsumer.class);

    private final AccountRepository accountRepository;

    public UserDataConsumer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RabbitListener(queues = "${broker.queue.user.data.name}")
    public void listenUserDataQueue(@Payload UserDataConsumerDto dto) {
        if (accountRepository.existsByCpf(dto.getCpf())) {
            log.info("Usuario ja existe com CPF: {}", dto.getCpf());
            return;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setIdUser(dto.getIdUser());
        userEntity.setName(dto.getName());
        userEntity.setCpf(dto.getCpf());
        userEntity.setBirthDate(dto.getBirthDate());
        userEntity.setMotherName(dto.getMotherName());
        userEntity.setEmail(dto.getEmail());
        userEntity.setPhone(dto.getPhone());
        userEntity.setPassword(dto.getPassword());
        userEntity.setAccountType(AccountTypeEnum.valueOf(dto.getAccountType()));
        userEntity.setBalance(dto.getBalance());
        userEntity.setAccountStatus(AccountStatus.valueOf(dto.getAccountStatus()));
        userEntity.setLoginAttempts(dto.getLoginAttempts());
        userEntity.setBlockedUntil(dto.getBlockedUntil());
        userEntity.setCreatedAt(dto.getCreatedAt());
        userEntity.setAccountNumber(dto.getAccountNumber());
        userEntity.setAgency(dto.getAgency());
        userEntity.setEmailVerified(dto.isEmailVerified());
        userEntity.setActive(dto.isActive());

        accountRepository.save(userEntity);
        log.info("Usuario salvo: {}", dto.getIdUser());
    }
}
