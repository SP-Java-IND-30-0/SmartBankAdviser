package com.star.bank.exception;

public class EmptyValueException extends RuntimeException {
    public EmptyValueException(String fieldName) {
        super(String.format("Empty value provided for %s", fieldName));
    }
}