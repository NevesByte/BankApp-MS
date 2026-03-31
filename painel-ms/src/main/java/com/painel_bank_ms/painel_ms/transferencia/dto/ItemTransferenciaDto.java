package com.painel_bank_ms.painel_ms.transferencia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;

public record ItemTransferenciaDto(
    UUID idTransferencia,
    LocalDateTime dataTransferencia,
    BigDecimal valorTransferencia,
    UserEntity enviador,
    UserEntity recebedor
) {
}
