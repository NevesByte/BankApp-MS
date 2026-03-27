package com.login_ms.login_ms.dto;

public record UserLoginResponseDto(String token,
                                  Long expiresIn) {
    
}
