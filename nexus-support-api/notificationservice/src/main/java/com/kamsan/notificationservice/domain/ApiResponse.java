package com.kamsan.notificationservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ApiResponse<T>(
        int code,
        String message,
        T data) {
}
