package com.painel_bank_ms.painel_ms.transferencia.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.InsufficientBalanceException;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.InvalidOperationException;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.ResourceNotFoundException;
import com.painel_bank_ms.painel_ms.transferencia.dto.FeedTransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.dto.ItemTransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.dto.TransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.entity.TransferenciaEntity;
import com.painel_bank_ms.painel_ms.transferencia.repository.TransferenciaRepository;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final AccountRepository accountRepository;

    public TransferenciaService(TransferenciaRepository transferenciaRepository, AccountRepository accountRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.accountRepository = accountRepository;
    }

    public FeedTransferenciaDto getAllTranferencia(int page, int pageSize, JwtAuthenticationToken token) {
        var userId = UUID.fromString(token.getName());
        Page<ItemTransferenciaDto> transferencias = transferenciaRepository.findByUserId(
            userId,
            PageRequest.of(page, pageSize, Sort.Direction.DESC, "dataTransferencia"))
            .map(transferencia -> new ItemTransferenciaDto(
                transferencia.getIdTransferencia(),
                transferencia.getDataTransferencia(),
                transferencia.getValorTransferencia(),
                transferencia.getEnviador(),
                transferencia.getRecebedor()
            ));

        return new FeedTransferenciaDto(
            transferencias.getContent(),
            page,
            pageSize,
            transferencias.getTotalPages(),
            (int) transferencias.getTotalElements()
        );
    }

    @Transactional
    public void criarTransferencia(TransferenciaDto transferenciaDto) {
        var enviador = accountRepository.findById(transferenciaDto.enviadorId())
            .orElseThrow(() -> new ResourceNotFoundException("Enviador nao encontrado."));

        var recebedor = accountRepository.findById(transferenciaDto.recebedorId())
            .orElseThrow(() -> new ResourceNotFoundException("Recebedor nao encontrado."));

        if (!recebedor.isActive()) {
            throw new InvalidOperationException("A conta recebedora esta inativa.");
        }

        if (enviador.getBalance().compareTo(transferenciaDto.valorTransferencia()) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para transferencia.");
        }

        enviador.setBalance(enviador.getBalance().subtract(transferenciaDto.valorTransferencia()));
        recebedor.setBalance(recebedor.getBalance().add(transferenciaDto.valorTransferencia()));
        accountRepository.save(enviador);
        accountRepository.save(recebedor);

        TransferenciaEntity transferenciaEntity = new TransferenciaEntity();
        transferenciaEntity.setEnviador(enviador);
        transferenciaEntity.setRecebedor(recebedor);
        transferenciaEntity.setValorTransferencia(transferenciaDto.valorTransferencia());
        transferenciaEntity.setDataTransferencia(LocalDateTime.now());
        transferenciaEntity.setUserId(enviador.getIdUser());
        transferenciaRepository.save(transferenciaEntity);
    }
}
