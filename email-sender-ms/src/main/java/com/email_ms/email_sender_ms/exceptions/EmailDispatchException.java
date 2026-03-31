package com.email_ms.email_sender_ms.exceptions;

public class EmailDispatchException extends RuntimeException {
    public EmailDispatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
