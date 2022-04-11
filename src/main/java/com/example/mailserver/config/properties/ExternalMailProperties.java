package com.example.mailserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("external")
public class ExternalMailProperties {
    private String ip;
    private String apiKey;
}