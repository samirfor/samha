package com.samha.commons;

public class BusinessException extends RuntimeException{
    public BusinessException(String message, Throwable err) {
        super(message, err);
    }

    public BusinessException(String message) {
        super(message, new RuntimeException(message));
    }
}
