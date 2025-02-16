package com.star.bank.exception;

public class DatabaseAccessException extends RuntimeException {

    public DatabaseAccessException(Throwable cause) {
        super("Database access error", cause);
    }
}