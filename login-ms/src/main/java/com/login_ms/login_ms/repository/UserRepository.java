package com.login_ms.login_ms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login_ms.login_ms.entity.UserLoginEntity;

@Repository
public interface UserRepository extends JpaRepository<UserLoginEntity, UUID> {

    Optional<UserLoginEntity> findByCpf(String cpf);

    Optional<UserLoginEntity> findByEmail(String email);
}
