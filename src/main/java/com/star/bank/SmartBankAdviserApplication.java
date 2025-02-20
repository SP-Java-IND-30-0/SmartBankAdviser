package com.star.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class SmartBankAdviserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartBankAdviserApplication.class, args);
    }

}