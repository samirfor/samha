package com.samha.commons;

public class UnexpectedException extends RuntimeException{
    public UnexpectedException(String message, Throwable err) {
        super(message, err);
    }
}
