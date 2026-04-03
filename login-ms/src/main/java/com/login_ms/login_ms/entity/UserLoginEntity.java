package com.login_ms.login_ms.entity;

import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.login_ms.login_ms.dto.UserLoginDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserLoginEntity {
    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Id
    @Column(name = "user_id")
    private UUID idUser;

    @CPF(message = "CPF inválido")
    private String cpf;

    public boolean isLoginCorrect(UserLoginDto loginDto, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(loginDto.password(), this.password);
    }
}
