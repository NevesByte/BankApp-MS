package com.painel_bank_ms.painel_ms.transferencia.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.painel_bank_ms.painel_ms.transferencia.entity.TransferenciaEntity;

@Repository
public interface TransferenciaRepository extends JpaRepository<TransferenciaEntity, UUID> {
    Page<TransferenciaEntity> findByUserId(UUID userId, Pageable pageable);
}
