package com.kamsan.notificationservice.sharedkernel.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
