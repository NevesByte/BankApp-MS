package com.login_ms.login_ms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.login_ms.login_ms.enums.AccountTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignUpDto(
    @NotBlank String name,
    @NotBlank String cpf,
    @NotBlank String motherName,
    @NotBlank String email,
    @NotBlank String phone,
    @NotBlank String password,
    @NotNull LocalDateTime birthDate,
    @NotNull AccountTypeEnum accountType
) {}
