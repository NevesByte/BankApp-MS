package com.login_ms.login_ms.dto;

import java.util.UUID;

public record UserLoginDto(
    UUID id,
    String email,
    String password,
    String cpf
) {
}
