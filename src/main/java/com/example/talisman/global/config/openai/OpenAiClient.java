package com.example.talisman.global.config.openai;

import com.example.talisman.global.config.openai.dto.OpenAiRequest;
import com.example.talisman.global.config.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiClient {
    private final RestClient restClient;
    @Value(("${spring.ai.openai.api.model}"))
    private String model;

    public String generateText(String systemPrompt, String userPrompt) {
        OpenAiRequest request = new OpenAiRequest(
                model,
                List.of(
                        new OpenAiRequest.Message("system", systemPrompt),
                        new OpenAiRequest.Message("user", userPrompt)
                )
        );

        OpenAiResponse response = restClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(OpenAiResponse.class);

        if(response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        throw new RuntimeException("OpenAI API 응답을 가져오지 못했습니다.");
    }

}
