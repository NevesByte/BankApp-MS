package com.painel_bank_ms.painel_ms.emprestimo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


import lombok.Getter;
import lombok.Setter;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;
import com.painel_bank_ms.painel_ms.emprestimo.enums.StatusEmprestimo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import jakarta.persistence.GenerationType;

@Entity
@Table(name = "tb_emprestimo")
@Getter
@Setter
public class EmprestimoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_emprestimo")
    private UUID idEmprestimo;
    @Column(name = "emprestimo_timestamp")
    private LocalDateTime dateTime;
    private BigDecimal valor;
    private BigDecimal taxaJuros;
    private Integer parcelas;
    private BigDecimal saldoDevedor;
    private StatusEmprestimo statusEmprestimo;
    @Column(name = "user_id")
    private UUID userId;
    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private List<ParcelaEntity> parcelaLista;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;
}
