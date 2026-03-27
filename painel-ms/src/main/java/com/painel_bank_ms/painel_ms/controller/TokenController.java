package com.painel_bank_ms.painel_ms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @GetMapping("/validar-token")
    public ResponseEntity<String> validarToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok("Token válido para usuário: " + jwt.getSubject());
    }
}
