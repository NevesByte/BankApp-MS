package com.painel_bank_ms.painel_ms.emprestimo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.painel_bank_ms.painel_ms.emprestimo.entity.EmprestimoEntity;

public interface EmprestimoRepository extends JpaRepository<EmprestimoEntity, UUID>{
    Page<EmprestimoEntity> findByUserId(UUID userId, Pageable pageable);
    List<EmprestimoEntity> findByUserId(UUID userId);
    Optional<EmprestimoEntity> findByIdEmprestimoAndUserId(UUID idEmprestimo, UUID userId);
}
