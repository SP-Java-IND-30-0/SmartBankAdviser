package com.star.bank.exception;

public class InvalidRuleDataException extends RuntimeException {

    public InvalidRuleDataException(Throwable cause) {
        super("Invalid rule data error: The provided rule data is incorrect or incomplete", cause);
    }
}