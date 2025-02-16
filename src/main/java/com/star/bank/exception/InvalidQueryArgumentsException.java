package com.star.bank.exception;

import com.star.bank.model.enums.QueryType;

import java.util.List;

public class InvalidQueryArgumentsException extends RuntimeException {
    public InvalidQueryArgumentsException(QueryType queryType, List<String> arguments) {
        super(String.format("Invalid query type %s with arguments %s", queryType.name(), arguments.toString()));
    }
}
