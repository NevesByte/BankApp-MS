package com.email_ms.email_sender_ms.dtos;

import java.util.UUID;

public record EmailRecordDto(
    UUID userId,
    String emailTo,
    String subject,
    String text
) {}
