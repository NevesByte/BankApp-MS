package com.painel_bank_ms.painel_ms.emprestimo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.FeedEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.ItemEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.entity.EmprestimoEntity;
import com.painel_bank_ms.painel_ms.emprestimo.entity.ParcelaEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusEmprestimo;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusParcela;
import com.painel_bank_ms.painel_ms.emprestimo.repository.EmprestimoRepository;
import com.painel_bank_ms.painel_ms.emprestimo.repository.ParcelaRepository;

@Service
public class EmprestimoService {
    @Autowired
    EmprestimoRepository emprestimoRepository;
    @Autowired
    ParcelaRepository parcelaRepository;
    @Autowired
    AccountRepository accountRepository; 
    
    @Transactional
    public ResponseEntity<Void> pagarParcela(JwtAuthenticationToken token, UUID id) {
        var userId = UUID.fromString(token.getName());

        // Resgatar emprestimo do usuario
        EmprestimoEntity emprestimo = emprestimoRepository.findByIdEmprestimoAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado ou não pertence ao usuário"));

        // Resgatar a primeira parcela pendente
        ParcelaEntity parcela = parcelaRepository
                .findFirstByEmprestimo_IdEmprestimoAndStatus(id, StatusParcela.PENDENTE)
                .orElseThrow(() -> new RuntimeException("Nenhuma parcela pendente encontrada"));

        // Verificar saldo do usuario
        UserEntity user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getBalance().compareTo(parcela.getValor()) < 0) {
            throw new RuntimeException("Saldo insuficiente para pagar a parcela");
        }

        // Debitar saldo e marcar parcela como paga
        user.setBalance(user.getBalance().subtract(parcela.getValor()));
        parcela.setStatus(StatusParcela.PAGA);
        parcela.setDataPagamento(LocalDate.now());
        accountRepository.save(user);
        parcelaRepository.save(parcela);

        // Verificar se ainda existem parcelas pendentes
        var parcelasPendentes = parcelaRepository
                .findByEmprestimo_IdEmprestimoAndStatus(id, StatusParcela.PENDENTE);

        if (parcelasPendentes.isEmpty()) {
            emprestimo.setStatusEmprestimo(StatusEmprestimo.QUITADO);
            emprestimoRepository.save(emprestimo);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> emprestimoCriar(ItemEmprestimoDto dto){
        EmprestimoEntity entity = new EmprestimoEntity();
        org.springframework.beans.BeanUtils.copyProperties(dto, entity);
        emprestimoRepository.save(entity);
        return ResponseEntity.ok().build();
    }

    public ItemEmprestimoDto emprestimoGetById(JwtAuthenticationToken token, UUID id){
        var userId = UUID.fromString(token.getName());
        return emprestimoRepository.findByIdEmprestimoAndUserId(id, userId)
                .map(emprestimo -> new ItemEmprestimoDto(
                    emprestimo.getIdEmprestimo(),
                    emprestimo.getDateTime(),
                    emprestimo.getValor(),
                    emprestimo.getTaxaJuros(),
                    emprestimo.getParcelas(),
                    emprestimo.getSaldoDevedor(),
                    emprestimo.getStatusEmprestimo(),
                    emprestimo.getParcelaLista()
                ))
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado ou não pertence ao usuário"));
    }

    public FeedEmprestimoDto getAllEmprestimos(int page,
                                               int pageSize,
                                               JwtAuthenticationToken token) {
        var userId = UUID.fromString(token.getName());
        Page<ItemEmprestimoDto> emprestimos = emprestimoRepository.findByUserId(
            userId,
            PageRequest.of(page, pageSize, Sort.Direction.DESC, "emprestimo_timestamp"))
                .map(emprestimo -> new ItemEmprestimoDto(
                    emprestimo.getIdEmprestimo(),
                    emprestimo.getDateTime(),
                    emprestimo.getValor(),
                    emprestimo.getTaxaJuros(),
                    emprestimo.getParcelas(),
                    emprestimo.getSaldoDevedor(),
                    emprestimo.getStatusEmprestimo(),
                    emprestimo.getParcelaLista()
                ));
            
        return new FeedEmprestimoDto(
            emprestimos.getContent(),
            page,
            pageSize,
            emprestimos.getTotalPages(),
            (int) emprestimos.getTotalElements()
        );
    }
}
