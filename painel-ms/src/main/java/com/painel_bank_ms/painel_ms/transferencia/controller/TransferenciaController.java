package com.painel_bank_ms.painel_ms.transferencia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.painel_bank_ms.painel_ms.transferencia.dto.FeedTransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.dto.TransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.service.TransferenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transferencias")
@Tag(name = "Transferencias", description = "Operacoes de transferencias bancarias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar transferencia")
    public ResponseEntity<Void> criarTransferencia(@RequestBody @Valid TransferenciaDto transferenciaDto) {
        transferenciaService.criarTransferencia(transferenciaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar transferencias do usuario autenticado")
    public ResponseEntity<FeedTransferenciaDto> getAllTransferencias(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
        JwtAuthenticationToken token
    ) {
        return ResponseEntity.ok(transferenciaService.getAllTranferencia(page, pageSize, token));
    }
}
