package com.star.bank.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID productId) {
        super("Product with ID " + productId + " not found");
    }

    public ProductNotFoundException(UUID productId, Throwable cause) {
        super("Product with ID " + productId + " not found", cause);
    }
}