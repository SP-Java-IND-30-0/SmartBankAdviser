package com.star.bank.exception;

public class InvalidProductIdException extends RuntimeException {

    public InvalidProductIdException(String productId) {
        super("Invalid product ID format: " + productId);
    }
}