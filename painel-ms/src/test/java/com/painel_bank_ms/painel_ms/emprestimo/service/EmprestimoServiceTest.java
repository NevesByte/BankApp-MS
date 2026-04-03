package com.painel_bank_ms.painel_ms.emprestimo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.account.repository.AccountRepository;
import com.painel_bank_ms.painel_ms.emprestimo.dtos.ItemEmprestimoDto;
import com.painel_bank_ms.painel_ms.emprestimo.entity.EmprestimoEntity;
import com.painel_bank_ms.painel_ms.emprestimo.entity.ParcelaEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusParcela;
import com.painel_bank_ms.painel_ms.emprestimo.repository.EmprestimoRepository;
import com.painel_bank_ms.painel_ms.emprestimo.repository.ParcelaRepository;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.InsufficientBalanceException;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.ResourceNotFoundException;
import com.painel_bank_ms.painel_ms.geral_configurations.producer.EmailProducer;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;
    @Mock
    private ParcelaRepository parcelaRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmailProducer emailProducer;
    @Mock
    private JwtAuthenticationToken token;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        when(token.getName()).thenReturn(userId.toString());
    }

    @Test
    void shouldThrowWhenUserNotFoundOnPayInstallment() {
        UUID emprestimoId = UUID.randomUUID();
        EmprestimoEntity emprestimo = new EmprestimoEntity();
        emprestimo.setIdEmprestimo(emprestimoId);
        emprestimo.setUserId(userId);

        ParcelaEntity parcela = new ParcelaEntity();
        parcela.setValor(new BigDecimal("100.00"));
        parcela.setStatus(StatusParcela.PENDENTE);

        when(emprestimoRepository.findByIdEmprestimoAndUserId(emprestimoId, userId))
            .thenReturn(Optional.of(emprestimo));
        when(parcelaRepository.findFirstByEmprestimo_IdEmprestimoAndStatus(emprestimoId, StatusParcela.PENDENTE))
            .thenReturn(Optional.of(parcela));
        when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> emprestimoService.pagarParcela(token, emprestimoId));
    }

    @Test
    void shouldThrowWhenInsufficientBalanceOnPayInstallment() {
        UUID emprestimoId = UUID.randomUUID();
        EmprestimoEntity emprestimo = new EmprestimoEntity();
        emprestimo.setIdEmprestimo(emprestimoId);
        emprestimo.setUserId(userId);

        ParcelaEntity parcela = new ParcelaEntity();
        parcela.setValor(new BigDecimal("500.00"));
        parcela.setStatus(StatusParcela.PENDENTE);

        UserEntity user = new UserEntity();
        user.setIdUser(userId);
        user.setBalance(new BigDecimal("100.00"));

        when(emprestimoRepository.findByIdEmprestimoAndUserId(emprestimoId, userId))
            .thenReturn(Optional.of(emprestimo));
        when(parcelaRepository.findFirstByEmprestimo_IdEmprestimoAndStatus(emprestimoId, StatusParcela.PENDENTE))
            .thenReturn(Optional.of(parcela));
        when(accountRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(InsufficientBalanceException.class, () -> emprestimoService.pagarParcela(token, emprestimoId));
    }

    @Test
    void shouldThrowWhenLoanNotFoundOnPayInstallment() {
        UUID emprestimoId = UUID.randomUUID();
        when(emprestimoRepository.findByIdEmprestimoAndUserId(emprestimoId, userId))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> emprestimoService.pagarParcela(token, emprestimoId));
        verify(parcelaRepository, never()).findFirstByEmprestimo_IdEmprestimoAndStatus(any(), any());
    }

    @Test
    void shouldThrowWhenUserNotFoundOnCreateLoan() {
        when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        ItemEmprestimoDto dto = new ItemEmprestimoDto(
            null,
            LocalDateTime.now(),
            new BigDecimal("1000.00"),
            new BigDecimal("0.02"),
            10,
            null,
            null,
            null
        );

        assertThrows(ResourceNotFoundException.class, () -> emprestimoService.emprestimoCriar(dto, token));
    }

    @Test
    void shouldCreateLoanAndSendEmail() {
        UserEntity user = new UserEntity();
        user.setIdUser(userId);
        user.setEmail("user@bank.com");
        user.setName("User");

        when(accountRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemEmprestimoDto dto = new ItemEmprestimoDto(
            null,
            LocalDateTime.now(),
            new BigDecimal("1000.00"),
            new BigDecimal("0.02"),
            10,
            null,
            null,
            null
        );

        emprestimoService.emprestimoCriar(dto, token);

        verify(emprestimoRepository).save(any());
        verify(emailProducer).sendEmail(any());
    }
}
