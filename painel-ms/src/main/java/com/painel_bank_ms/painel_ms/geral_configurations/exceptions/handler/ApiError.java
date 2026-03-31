package com.painel_bank_ms.painel_ms.geral_configurations.exceptions.handler;

import java.time.OffsetDateTime;

public record ApiError(
    int status,
    String error,
    String message,
    String path,
    OffsetDateTime timestamp
) {
}
