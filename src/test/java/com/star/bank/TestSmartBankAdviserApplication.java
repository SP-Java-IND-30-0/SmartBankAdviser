package com.star.bank;

import org.springframework.boot.SpringApplication;

public class TestSmartBankAdviserApplication {

    public static void main(String[] args) {
        SpringApplication.from(SmartBankAdviserApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
