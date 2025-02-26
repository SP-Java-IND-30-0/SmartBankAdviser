package com.star.bank.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AppConfig {

    private final BuildProperties buildProperties;

    @Value("${rules.active-user-transaction}")
    private int activeUserTransaction;

    private static int staticActiveUserTransaction;
    @Getter
    private static String version;
    @Getter
    private static String name;

    public AppConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    public void init() {
        staticActiveUserTransaction = activeUserTransaction;
        version = buildProperties.getVersion();
        name = buildProperties.getName();
    }

    public static int getActiveUserTransaction() {
        return staticActiveUserTransaction;
    }

}
