package com.painel_bank_ms.painel_ms.emprestimo.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.painel_bank_ms.painel_ms.emprestimo.entity.ParcelaEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusEmprestimo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemEmprestimoDto(
    UUID idEmprestimo,
    LocalDateTime dateTime,
    @NotNull @DecimalMin("0.01") BigDecimal valor,
    @NotNull @DecimalMin("0.00") BigDecimal taxaJuros,
    @NotNull @Min(1) Integer parcelas,
    BigDecimal saldoDevedor,
    StatusEmprestimo statusEmprestimo,
    List<ParcelaEntity> parcelaLista
) {
}
