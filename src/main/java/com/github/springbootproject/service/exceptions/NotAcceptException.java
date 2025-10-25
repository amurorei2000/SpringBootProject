package com.github.springbootproject.service.exceptions;

public class NotAcceptException extends RuntimeException {
    public NotAcceptException(String message) {
        super(message);
    }
}
