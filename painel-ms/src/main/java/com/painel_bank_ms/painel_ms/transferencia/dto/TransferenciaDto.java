package com.painel_bank_ms.painel_ms.transferencia.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransferenciaDto(
    @NotNull UUID enviadorId,
    @NotNull UUID recebedorId,
    @NotNull @DecimalMin(value = "0.01", message = "O valor da transferencia deve ser maior que zero") BigDecimal valorTransferencia
) {
}
