package com.star.bank.aspect;

import com.star.bank.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DatabaseAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleDatabaseAccessException(DatabaseAccessException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DatabaseSaveException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String handleDatabaseSaveException(DatabaseSaveException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({
            InvalidProductIdException.class,
            InvalidQueryArgumentsException.class,
            InvalidRuleDataException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestExceptions(RuntimeException ex) {
        return ex.getMessage();
    }
}