package com.painel_bank_ms.painel_ms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataConsumerDto{
    UUID idUser;
    String name;
    String cpf;
    LocalDateTime birthDate;
    String motherName;
    String email;
    String phone;
    String password;
    String accountType;
    BigDecimal balance;
    String accountStatus;
    int loginAttempts;
    LocalDateTime blockedUntil;
    LocalDateTime createdAt;
    String accountNumber;
    String agency;
    boolean emailVerified;
    boolean active;
}