package com.example.talisman.domain.client;

import com.example.talisman.domain.dto.ManseryeokRequest;
import com.example.talisman.domain.dto.ManseryeokResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SajuClient {
    private final WebClient webClient;

    @Cacheable(
            value = "manseryeok",
            key = "#request.year + '-' + #request.month + '-' + #request.day + '-' + " +
                    "#request.hour + '-' + #request.minute + '-' + #request.gender + '-' + " +
                    "#request.isLunar + '-' + #request.timeCheck + '-' + #request.dayBoundary"
    )
    public ManseryeokResponse requestManseryeok(ManseryeokRequest request) {
        // 캐시가 히트(Hit)되면 이 로그는 두 번째 요청부터 출력되지 않습니다!
        System.out.println(">>> [Cache Miss] 외부 만세력 API를 진짜로 호출합니다!");

        return webClient.post()
                .uri("/api/v1/manseryeok")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ManseryeokResponse.class)
                .block();   // 백엔드 애플리케이션 아키텍처가 동기식(MVC) 구조여서 block 호출
    }
}
