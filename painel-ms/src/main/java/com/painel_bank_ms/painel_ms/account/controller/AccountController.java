package com.painel_bank_ms.painel_ms.account.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Conta", description = "Operacoes de conta bancaria")
public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/saldo")
    public ResponseEntity<String> getSaldo(@AuthenticationPrincipal Jwt jwt) {

        // 1. Pega o subject direto do token
        String userId = jwt.getSubject();

        // 2. Converte pra UUID
        UUID uuid = UUID.fromString(userId);

        // 3. Busca no banco
        var user = repository.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Conta nao encontrada"));

        BigDecimal balance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
        return ResponseEntity.ok(balance.toString());
    }
}
