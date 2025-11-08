package com.github.springbootproject.service.exceptions;

public class CAuthenticationEntryPointException extends RuntimeException {
    public CAuthenticationEntryPointException(String message) {
        super(message);
    }
}
