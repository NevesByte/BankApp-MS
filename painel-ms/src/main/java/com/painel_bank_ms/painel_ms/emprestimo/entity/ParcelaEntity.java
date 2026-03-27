package com.painel_bank_ms.painel_ms.emprestimo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusParcela;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_parcela")
@Getter
@Setter
public class ParcelaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parcela")
    private Long id;
    private Integer numero;
    private BigDecimal valor;
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;
    private StatusParcela status;

    @ManyToOne
    @JoinColumn(name = "emprestimo_id")
    private EmprestimoEntity emprestimo;
}
