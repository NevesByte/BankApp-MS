package com.painel_bank_ms.painel_ms.transferencia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public record TransferenciaDto(UserEntity enviador,
                               UserEntity recebedor,
                               BigDecimal valorTranferencia,
                               LocalDateTime dataTransferencia) {
    
}
