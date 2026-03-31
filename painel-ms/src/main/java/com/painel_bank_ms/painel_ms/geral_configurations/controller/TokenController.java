package com.painel_bank_ms.painel_ms.geral_configurations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacao", description = "Validacao de token JWT")
public class TokenController {

    @GetMapping("/validar-token")
    @Operation(summary = "Validar token")
    public ResponseEntity<String> validarToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok("Token valido para usuario: " + jwt.getSubject());
    }
}
