package com.painel_bank_ms.painel_ms.emprestimo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.painel_bank_ms.painel_ms.emprestimo.repository.EmprestimoRepository;
import com.painel_bank_ms.painel_ms.emprestimo.repository.ParcelaRepository;
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
