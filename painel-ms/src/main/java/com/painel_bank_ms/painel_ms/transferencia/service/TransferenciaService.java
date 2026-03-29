package com.painel_bank_ms.painel_ms.transferencia.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.transferencia.dto.FeedTransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.dto.ItemTransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.dto.TransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.entity.TransferenciaEntity;
import com.painel_bank_ms.painel_ms.transferencia.repository.TransferenciaRepository;

@Service
public class TransferenciaService {
    @Autowired
    TransferenciaRepository transferenciaRepository;
    @Autowired
    AccountRepository accountRepository;

    public FeedTransferenciaDto getAllTranferencia(int page, int pageSize, JwtAuthenticationToken token) {
        var userId = UUID.fromString(token.getName());
        Page<ItemTransferenciaDto> tranferencias = transferenciaRepository.findByUserId(
            userId,
            PageRequest.of(page, pageSize, Sort.Direction.DESC, "data_transferencia"))
                .map(transferencia -> new ItemTransferenciaDto(
                    transferencia.getIdTransferencia(),
                    transferencia.getDataTransferencia(),
                    transferencia.getValorTranferencia(),
                    transferencia.getEnviador(),
                    transferencia.getRecebedor()
                ));

        return new FeedTransferenciaDto(
            tranferencias.getContent(),
            page,
            pageSize,
            tranferencias.getTotalPages(),
            (int) tranferencias.getTotalElements()
        );
    }

    @Transactional
    public ResponseEntity<Void> criarTransferencia(TransferenciaDto transferenciaDto) {
        if (transferenciaDto == null) {
            return ResponseEntity.badRequest().build();
        }

        var enviador = accountRepository.findById(transferenciaDto.enviador().getIdUser())
                .orElseThrow(() -> new RuntimeException("Enviador não encontrado"));

        var recebedor = accountRepository.findById(transferenciaDto.recebedor().getIdUser())
                .orElseThrow(() -> new RuntimeException("Recebedor não encontrado"));

        if (!recebedor.isActive()) {
            return ResponseEntity.badRequest().build();
        }

        if (enviador.getBalance().compareTo(transferenciaDto.valorTranferencia()) < 0) {
            return ResponseEntity.badRequest().build();
        }

        enviador.setBalance(enviador.getBalance().subtract(transferenciaDto.valorTranferencia()));
        recebedor.setBalance(recebedor.getBalance().add(transferenciaDto.valorTranferencia()));
        accountRepository.save(enviador);
        accountRepository.save(recebedor);

        TransferenciaEntity transferenciaEntity = new TransferenciaEntity();
        transferenciaEntity.setEnviador(enviador);
        transferenciaEntity.setRecebedor(recebedor);
        transferenciaEntity.setValorTranferencia(transferenciaDto.valorTranferencia());
        transferenciaEntity.setDataTransferencia(LocalDateTime.now());
        transferenciaEntity.setUserId(enviador.getIdUser());
        transferenciaRepository.save(transferenciaEntity);

        return ResponseEntity.ok().build();
    }
}
