package com.star.bank.exception;

public class DuplicateRuleException extends RuntimeException {

    public DuplicateRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRuleException(Throwable cause) {
        super("Duplicate rule error: A rule with the same unique constraints already exists", cause);
    }
}