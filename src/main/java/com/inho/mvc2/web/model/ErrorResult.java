package com.inho.mvc2.web.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ErrorResult {
    private String code;
    private String message;

    public ErrorResult() {
    }

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
