package com.painel_bank_ms.painel_ms.emprestimo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.painel_bank_ms.painel_ms.emprestimo.entity.ParcelaEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusParcela;

public interface ParcelaRepository extends JpaRepository<ParcelaEntity, Long> {
    Optional<ParcelaEntity> findFirstByEmprestimo_IdEmprestimoAndStatus(UUID idEmprestimo, StatusParcela status);
    List<ParcelaEntity> findByEmprestimo_IdEmprestimoAndStatus(UUID idEmprestimo, StatusParcela status);
}
