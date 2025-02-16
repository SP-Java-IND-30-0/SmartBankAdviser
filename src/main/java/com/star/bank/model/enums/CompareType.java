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

    public static CompareType findByValue(String s) {
        return switch (s) {
            case "<" -> LT;
            case ">" -> GT;
            case "=" -> EQ;
            case "<=" -> LE;
            case ">=" -> GE;
            default -> throw new IllegalArgumentException("Unknown compare type");
        };
    }
}
