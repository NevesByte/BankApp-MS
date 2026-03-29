package com.painel_bank_ms.painel_ms.transferencia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.emprestimo.entity.ParcelaEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusEmprestimo;

public record ItemTransferenciaDto(UUID idTransferencia,
                                LocalDateTime dataTransferencia,
                                BigDecimal valorTranferenciaor,
                                UserEntity enviador,
                                UserEntity recebedor) {
    
}