package com.painel_bank_ms.painel_ms.account.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    @Operation(summary = "Consultar saldo")
    public ResponseEntity<String> getSaldo(JwtAuthenticationToken token) {
        var user = repository.findById(UUID.fromString(token.getName()))
            .orElseThrow(() -> new ResourceNotFoundException("Conta nao encontrada."));

        return ResponseEntity.ok(user.getBalance().toString());
    }
}
