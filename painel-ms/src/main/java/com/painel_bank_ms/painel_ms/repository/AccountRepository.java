package com.painel_bank_ms.painel_ms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.painel_bank_ms.painel_ms.entity.UserEntity;

@Repository
public interface AccountRepository extends JpaRepository<UserEntity, UUID> {
    
}
