package com.necor.log.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogbackProperties.class)
public class LogbackConfiguration {

    private final LogbackProperties properties;

    public LogbackConfiguration(LogbackProperties properties) {
        this.properties = properties;
    }

    @Bean
    public LogbackService logbackService() {
        return new LogbackService(properties);
    }
}
