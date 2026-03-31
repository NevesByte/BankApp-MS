package com.painel_bank_ms.painel_ms.geral_configurations.exceptions;

public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
