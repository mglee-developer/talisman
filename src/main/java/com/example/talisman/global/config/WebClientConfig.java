package com.example.talisman.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${manseryeok.api.url:http://localhost:3000}")
    private String apiUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }
}
