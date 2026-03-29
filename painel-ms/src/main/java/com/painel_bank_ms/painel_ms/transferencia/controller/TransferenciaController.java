package com.painel_bank_ms.painel_ms.transferencia.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.painel_bank_ms.painel_ms.transferencia.dto.TransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.entity.TransferenciaEntity;
import com.painel_bank_ms.painel_ms.transferencia.repository.TransferenciaRepository;
import com.painel_bank_ms.painel_ms.transferencia.service.TransferenciaService;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import jakarta.validation.Valid;

@RestController
public class TransferenciaController {
    @Autowired
    TransferenciaService transferenciaService;
    /*
    Transferência
     => Post (Criar transferência) // FEITO !
     => GetAll
    Histórico de transações
    */

    @PostMapping("/criar-transferencia")
    public ResponseEntity<TransferenciaEntity> criarTransferencia(@RequestBody @Valid TransferenciaDto transferenciaDto) {
        transferenciaService.criarTransferencia(transferenciaDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all-transferencias")
    public ResponseEntity<TransferenciaEntity> getAllTransferencias() {

        // Implementation for getting all transferencias
        return ResponseEntity.ok().build();
    }

    


}
