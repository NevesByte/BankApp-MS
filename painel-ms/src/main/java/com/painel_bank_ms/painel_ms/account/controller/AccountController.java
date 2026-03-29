package com.painel_bank_ms.painel_ms.account.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.FeedEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.ItemEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.entity.EmprestimoEntity;
import com.painel_bank_ms.painel_ms.emprestimo.service.EmprestimoService;

@RestController
public class AccountController{
    @Autowired
    private AccountRepository repository;

    /*Saldo*/
    @GetMapping("/account/saldo")
    public ResponseEntity<String> getSaldo(JwtAuthenticationToken token){
        return repository.findById(UUID.fromString(token.getName()))
                .map(user -> ResponseEntity.ok(user.getBalance().toString()))
                .orElse(ResponseEntity.notFound().build());
    }

    



}