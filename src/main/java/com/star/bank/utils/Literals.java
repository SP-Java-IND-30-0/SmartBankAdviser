package com.star.bank.utils;

public class Literals {

    private Literals() {
    }

    public static final String QUERY_PREFIX = "SELECT (";
    public static final String QUERY_SUFFIX = """
            ) AS result FROM PUBLIC.TRANSACTIONS T
                    JOIN PUBLIC.PRODUCTS P ON P.ID = T.PRODUCT_ID
            WHERE T.USER_ID = ?
            """;
}
