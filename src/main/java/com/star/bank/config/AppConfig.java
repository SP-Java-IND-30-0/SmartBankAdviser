package com.star.bank.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${rules.active-user-transaction}")
    private int activeUserTransaction;

    private static int staticActiveUserTransaction;

    @PostConstruct
    public void init() {
        staticActiveUserTransaction = activeUserTransaction;
    }

    public static int getActiveUserTransaction() {
        return staticActiveUserTransaction;
    }
}
