package com.painel_bank_ms.painel_ms.emprestimo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.painel_bank_ms.painel_ms.geral_configurations.dto.EmailNotificationDto;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.InsufficientBalanceException;
import com.painel_bank_ms.painel_ms.geral_configurations.exceptions.ResourceNotFoundException;
import com.painel_bank_ms.painel_ms.geral_configurations.producer.EmailProducer;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ParcelaRepository parcelaRepository;
    private final AccountRepository accountRepository;
    private final EmailProducer emailProducer;

    public EmprestimoService(
        EmprestimoRepository emprestimoRepository,
        ParcelaRepository parcelaRepository,
        AccountRepository accountRepository,
        EmailProducer emailProducer
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.parcelaRepository = parcelaRepository;
        this.accountRepository = accountRepository;
        this.emailProducer = emailProducer;
    }

    @Transactional
    public void pagarParcela(JwtAuthenticationToken token, UUID id) {
        var userId = UUID.fromString(token.getName());

        EmprestimoEntity emprestimo = emprestimoRepository.findByIdEmprestimoAndUserId(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Emprestimo nao encontrado para este usuario."));

        ParcelaEntity parcela = parcelaRepository
            .findFirstByEmprestimo_IdEmprestimoAndStatus(id, StatusParcela.PENDENTE)
            .orElseThrow(() -> new ResourceNotFoundException("Nenhuma parcela pendente encontrada."));

        UserEntity user = accountRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado."));

        if (user.getBalance().compareTo(parcela.getValor()) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para pagar a parcela.");
        }

        user.setBalance(user.getBalance().subtract(parcela.getValor()));
        parcela.setStatus(StatusParcela.PAGA);
        parcela.setDataPagamento(LocalDate.now());
        accountRepository.save(user);
        parcelaRepository.save(parcela);

        var parcelasPendentes = parcelaRepository
            .findByEmprestimo_IdEmprestimoAndStatus(id, StatusParcela.PENDENTE);

        if (parcelasPendentes.isEmpty()) {
            emprestimo.setStatusEmprestimo(StatusEmprestimo.QUITADO);
            emprestimoRepository.save(emprestimo);
            emailProducer.sendEmail(new EmailNotificationDto(
                userId,
                user.getEmail(),
                "Emprestimo quitado",
                "Parabens " + user.getName() + ", seu emprestimo foi totalmente quitado."
            ));
            return;
        }

        emailProducer.sendEmail(new EmailNotificationDto(
            userId,
            user.getEmail(),
            "Parcela paga",
            "Ola " + user.getName() + ", sua parcela " + parcela.getNumero() +
                " no valor de R$ " + parcela.getValor() + " foi paga com sucesso. " +
                "Parcelas restantes: " + parcelasPendentes.size()
        ));
    }

    @Transactional
    public void emprestimoCriar(ItemEmprestimoDto dto, JwtAuthenticationToken token) {
        var userId = UUID.fromString(token.getName());

        UserEntity user = accountRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado."));

        EmprestimoEntity entity = new EmprestimoEntity();
        entity.setDateTime(LocalDateTime.now());
        entity.setUserId(userId);
        entity.setValor(dto.valor());
        entity.setTaxaJuros(dto.taxaJuros());
        entity.setParcelas(dto.parcelas());
        entity.setSaldoDevedor(dto.valor());
        entity.setStatusEmprestimo(StatusEmprestimo.PENDENTE);

        List<ParcelaEntity> parcelas = buildParcelas(entity, dto);
        entity.setParcelaLista(parcelas);

        emprestimoRepository.save(entity);

        emailProducer.sendEmail(new EmailNotificationDto(
            userId,
            user.getEmail(),
            "Emprestimo criado",
            "Ola " + user.getName() + ", seu emprestimo de R$ " + dto.valor() +
                " em " + dto.parcelas() + " parcelas foi criado com sucesso."
        ));
    }

    public ItemEmprestimoDto emprestimoGetById(JwtAuthenticationToken token, UUID id) {
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
            .orElseThrow(() -> new ResourceNotFoundException("Emprestimo nao encontrado para este usuario."));
    }

    public FeedEmprestimoDto getAllEmprestimos(int page, int pageSize, JwtAuthenticationToken token) {
        var userId = UUID.fromString(token.getName());
        Page<ItemEmprestimoDto> emprestimos = emprestimoRepository.findByUserId(
            userId,
            PageRequest.of(page, pageSize, Sort.Direction.DESC, "dateTime"))
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

    private List<ParcelaEntity> buildParcelas(EmprestimoEntity entity, ItemEmprestimoDto dto) {
        List<ParcelaEntity> parcelas = new ArrayList<>();
        var valorParcela = dto.valor().divide(java.math.BigDecimal.valueOf(dto.parcelas()), java.math.RoundingMode.HALF_UP);

        for (int i = 1; i <= dto.parcelas(); i++) {
            ParcelaEntity parcela = new ParcelaEntity();
            parcela.setEmprestimo(entity);
            parcela.setNumero(i);
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(LocalDate.now().plusMonths(i));
            parcela.setStatus(StatusParcela.PENDENTE);
            parcelas.add(parcela);
        }

        return parcelas;
    }
}
