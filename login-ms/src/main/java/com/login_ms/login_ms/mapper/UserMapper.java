package com.login_ms.login_ms.mapper;
import java.math.BigDecimal;

import com.login_ms.login_ms.entity.UserEntity;
import com.login_ms.login_ms.enums.AccountStatus;

public class UserMapper{
    public void mapping(UserEntity userEntity, String password){
        userEntity.setPassword(password);
        userEntity.setLoginAttempts(0);
        userEntity.setBlockedUntil(null);
        userEntity.setBalance(new BigDecimal("0.00"));
        userEntity.setActive(true);
        userEntity.setAccountStatus(AccountStatus.ACTIVE);
        userEntity.setCreatedAt(java.time.LocalDateTime.now());
        userEntity.setAccountNumber(java.util.UUID.randomUUID().toString());
        userEntity.setAgency("0001");
        userEntity.setEmailVerified(false);
    }
}