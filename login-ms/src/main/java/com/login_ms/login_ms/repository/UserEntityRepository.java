package com.login_ms.login_ms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login_ms.login_ms.entity.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
}
