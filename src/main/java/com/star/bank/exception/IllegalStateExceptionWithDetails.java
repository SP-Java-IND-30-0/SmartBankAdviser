package com.star.bank.exception;

public class IllegalStateExceptionWithDetails extends RuntimeException {

    public IllegalStateExceptionWithDetails(String entityName, String entityId) {
        super(entityName + " with ID " + entityId + " is in an invalid state.");
    }

    public IllegalStateExceptionWithDetails(String entityName, String entityId, Throwable cause) {
        super(entityName + " with ID " + entityId + " is in an invalid state.", cause);
    }
}