package com.login_ms.login_ms.dto;

import java.time.LocalDateTime;

import com.login_ms.login_ms.enums.AccountTypeEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignUpDto(
    @NotBlank String name,
    @NotBlank String cpf,
    @NotBlank String motherName,
    @NotBlank @Email String email,
    @NotBlank String phone,
    @NotBlank String password,
    @NotNull LocalDateTime birthDate,
    @NotNull AccountTypeEnum accountType
) {
}
