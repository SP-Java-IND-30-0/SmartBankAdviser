package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.star.bank.model.enums.QueryType;

import java.util.List;

public class SimpleRuleDto {

    @JsonProperty("query")
    private QueryType queryType;
    @JsonProperty("negate")
    private boolean negate;
    @JsonProperty("arguments")
    private List<String> arguments;
}
