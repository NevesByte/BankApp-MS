package com.painel_bank_ms.painel_ms.transferencia.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.InsufficientBalanceException;
import com.painel_bank_ms.painel_ms.transferencia.dto.TransferenciaDto;
import com.painel_bank_ms.painel_ms.transferencia.repository.TransferenciaRepository;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private UUID senderId;
    private UUID receiverId;

    @BeforeEach
    void setup() {
        senderId = UUID.randomUUID();
        receiverId = UUID.randomUUID();
    }

    @Test
    void shouldThrowWhenSenderHasInsufficientBalance() {
        UserEntity sender = new UserEntity();
        sender.setIdUser(senderId);
        sender.setBalance(new BigDecimal("50.00"));

        UserEntity receiver = new UserEntity();
        receiver.setIdUser(receiverId);
        receiver.setBalance(new BigDecimal("0.00"));
        receiver.setActive(true);

        when(accountRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        var dto = new TransferenciaDto(senderId, receiverId, new BigDecimal("100.00"));

        assertThrows(InsufficientBalanceException.class, () -> transferenciaService.criarTransferencia(dto));
    }

    @Test
    void shouldTransferAndPersist() {
        UserEntity sender = new UserEntity();
        sender.setIdUser(senderId);
        sender.setBalance(new BigDecimal("200.00"));

        UserEntity receiver = new UserEntity();
        receiver.setIdUser(receiverId);
        receiver.setBalance(new BigDecimal("10.00"));
        receiver.setActive(true);

        when(accountRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        var dto = new TransferenciaDto(senderId, receiverId, new BigDecimal("100.00"));
        transferenciaService.criarTransferencia(dto);

        verify(accountRepository).save(sender);
        verify(accountRepository).save(receiver);
        verify(transferenciaRepository).save(any());
    }
}
