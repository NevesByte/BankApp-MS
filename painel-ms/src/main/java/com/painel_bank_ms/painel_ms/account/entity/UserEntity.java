package com.painel_bank_ms.painel_ms.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.painel_bank_ms.painel_ms.account.enums.AccountStatus;
import com.painel_bank_ms.painel_ms.account.enums.AccountTypeEnum;
import com.painel_bank_ms.painel_ms.emprestimo.entity.EmprestimoEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_accounts")
@Getter
@Setter
public class UserEntity {
    //Cadastro de usuário
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID idUser;
    private String name;
    @Column(unique = true)
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
    private AccountTypeEnum accountType;
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

    
    //Emprestimos
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<EmprestimoEntity> emprestimo;
}
