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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDatabaseAccessException(DatabaseAccessException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DatabaseSaveException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDatabaseSaveException(DatabaseSaveException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicateRuleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateRuleException(DuplicateRuleException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidProductIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidProductIdException(InvalidProductIdException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidQueryArgumentsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidQueryArgumentsException(InvalidQueryArgumentsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidRuleDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidRuleDataException(InvalidRuleDataException ex) {
        return ex.getMessage();
    }
}