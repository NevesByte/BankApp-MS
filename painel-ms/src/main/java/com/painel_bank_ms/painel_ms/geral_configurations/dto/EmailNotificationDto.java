package com.painel_bank_ms.painel_ms.geral_configurations.dto;

import java.util.UUID;

public record EmailNotificationDto(
    UUID userId,
    String emailTo,
    String subject,
    String text
) {}
