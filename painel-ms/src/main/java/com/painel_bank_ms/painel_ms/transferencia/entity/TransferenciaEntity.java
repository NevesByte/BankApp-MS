package com.painel_bank_ms.painel_ms.transferencia.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.painel_bank_ms.painel_ms.account.entity.UserEntity;

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
@Table(name = "tb_transferencias")
@Getter
@Setter
public class TransferenciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_transferencia")
    private UUID idTransferencia;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "enviador_id")
    private UserEntity enviador;

    @ManyToOne
    @JoinColumn(name = "recebedor_id")
    private UserEntity recebedor;

    @Column(name = "valor_transferencia")
    private BigDecimal valorTranferencia;

    @Column(name = "data_transferencia")
    private LocalDateTime dataTransferencia;
}
