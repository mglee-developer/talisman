package com.example.talisman.domain.client;

import com.example.talisman.domain.dto.ManseryeokRequest;
import com.example.talisman.domain.dto.ManseryeokResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SajuClient {
    private final WebClient webClient;

    public ManseryeokResponse requestManseryeok(ManseryeokRequest request) {
        return webClient.post()
                .uri("/api/v1/manseryeok")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ManseryeokResponse.class)
                .block();
    }
}
