package com.login_ms.login_ms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.br.CPF;

import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.enums.AccountStatus;
import com.login_ms.login_ms.enums.AccountTypeEnum;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
public class UserEntity {
    //UserDatas
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_id", length = 36)
    private UUID idUser;
    private String name;
    @Column(unique = true)
    @CPF(message = "CPF inválido")
    private String cpf;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;
    private String motherName;
    @Column(unique = true)
    private String email;
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountTypeEnum accountType;
    //DefaultValues
    private BigDecimal balance;
    @Column(name = "account_status")
    private AccountStatus accountStatus;
    @Column(name = "login_attempts")
    private int loginAttempts;
    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(unique = true, name = "account_number")
    private String accountNumber;
    private String agency;
    @Column(name = "email_verified")
    private boolean emailVerified;
    @Column(name = "active")
    private boolean active;

    /*Getter & Setters => Lombok*/
}
