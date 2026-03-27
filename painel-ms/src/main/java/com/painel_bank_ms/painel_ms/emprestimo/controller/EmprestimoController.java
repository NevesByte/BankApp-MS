package com.painel_bank_ms.painel_ms.emprestimo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.painel_bank_ms.painel_ms.emprestimo.dtos.FeedEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.ItemEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.service.EmprestimoService;

@RestController
public class EmprestimoController {
    @Autowired
    EmprestimoService emprestimoService;

    @PostMapping("/emprestimo/criar-emprestimo")
    public ResponseEntity<Void> criarEmprestimo(@RequestBody ItemEmprestimoDto itemEmprestimoDto){
        emprestimoService.emprestimoCriar(itemEmprestimoDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emprestimo")
    public ResponseEntity<FeedEmprestimoDto> emprestimoGetAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            JwtAuthenticationToken token){
        return ResponseEntity.ok(emprestimoService.getAllEmprestimos(page, pageSize, token));
    }

    /*Pagar*/
    @DeleteMapping("/emprestimo/pagar-parcela")
    public ResponseEntity<Void> pagarParcela(JwtAuthenticationToken token, UUID id){
        //Pagar
        return ResponseEntity.ok().build();
    }

    /*Mostrar as parcelas*/

    

    @GetMapping("/emprestimo/{id}")
    public ResponseEntity<ItemEmprestimoDto> emprestimoGetById(@PathVariable("id") UUID id,
                                                               JwtAuthenticationToken token){
        return ResponseEntity.ok(emprestimoService.emprestimoGetById(token, id));
    }
}
