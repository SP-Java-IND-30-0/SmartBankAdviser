package com.star.bank.model.enums;

import lombok.Getter;

@Getter
public enum CompareType {
    LT("<"),
    GT(">"),
    EQ("="),
    LE("<="),
    GE(">=");

    private final String value;

    CompareType(String value) {
        this.value = value;
    }
}
