package com.star.bank.exception;

public class NullRecommendationInList extends RuntimeException{

    public NullRecommendationInList() {
    }

    public NullRecommendationInList(String message) {
        super(message);
    }

    public NullRecommendationInList(Throwable cause) {
        super(cause);
    }

    public NullRecommendationInList(String message, Throwable cause) {
        super(message, cause);
    }

    public NullRecommendationInList(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
