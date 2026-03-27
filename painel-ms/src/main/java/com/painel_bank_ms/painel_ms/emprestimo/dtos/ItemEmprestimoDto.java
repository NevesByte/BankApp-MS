package com.painel_bank_ms.painel_ms.emprestimo.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.painel_bank_ms.painel_ms.emprestimo.entity.ParcelaEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusEmprestimo;

public record ItemEmprestimoDto(UUID idEmprestimo,
                            LocalDateTime dateTime,
                            BigDecimal valor,
                            BigDecimal taxaJuros,
                            Integer parcelas,
                            BigDecimal saldoDevedor,
                            StatusEmprestimo statusEmprestimo,
                            List<ParcelaEntity> parcelaLista) {
    
}
