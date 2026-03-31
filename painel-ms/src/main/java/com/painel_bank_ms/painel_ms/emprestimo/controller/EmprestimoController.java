package com.painel_bank_ms.painel_ms.emprestimo.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.painel_bank_ms.painel_ms.emprestimo.dtos.FeedEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.ItemEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.service.EmprestimoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/emprestimos")
@Tag(name = "Emprestimos", description = "Operacoes de emprestimo e pagamento de parcelas")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar emprestimo")
    public ResponseEntity<Void> criarEmprestimo(@RequestBody @Valid ItemEmprestimoDto itemEmprestimoDto,
                                                JwtAuthenticationToken token) {
        emprestimoService.emprestimoCriar(itemEmprestimoDto, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar emprestimos do usuario")
    public ResponseEntity<FeedEmprestimoDto> emprestimoGetAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(emprestimoService.getAllEmprestimos(page, pageSize, token));
    }

    @DeleteMapping("/parcelas/{id}")
    @Operation(summary = "Pagar proxima parcela pendente")
    public ResponseEntity<Void> pagarParcela(JwtAuthenticationToken token,
                                             @PathVariable UUID id) {
        emprestimoService.pagarParcela(token, id);
        return ResponseEntity.noContent().build();
    }
}
