package com.painel_bank_ms.painel_ms.account.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;

@Repository
public interface AccountRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByCpf(String cpf);

    java.util.Optional<UserEntity> findByCpf(String cpf);
    
    java.util.Optional<UserEntity> findByEmail(String email);
    
}
