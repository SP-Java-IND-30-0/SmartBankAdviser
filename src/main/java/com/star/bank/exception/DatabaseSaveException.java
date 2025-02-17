package com.star.bank.exception;

public class DatabaseSaveException extends RuntimeException {

    public DatabaseSaveException(Throwable cause) {
        super("Error saving data to the database", cause);
    }
}