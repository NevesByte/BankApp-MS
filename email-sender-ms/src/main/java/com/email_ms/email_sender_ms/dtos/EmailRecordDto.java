package com.email_ms.email_sender_ms.dtos;


public record EmailRecordDto(
    String emailTo,
    String subject,
    String text
) {}
