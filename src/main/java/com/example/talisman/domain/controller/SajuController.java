package com.example.talisman.domain.controller;

import com.example.talisman.domain.dto.SajuRequest;
import com.example.talisman.domain.dto.SajuResponse;
import com.example.talisman.domain.service.SajuService;
import com.example.talisman.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "부적(Talisman) API", description = "사주 만세력 계산 및 GPT 부적 멘트 생성 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/saju")
public class SajuController {
    private final SajuService sajuService;

    @PostMapping
    @Operation(
            summary = "내운내뽑 부적 생성",
            description = "사용자의 생년월일과 고민을 바탕으로 오행을 연산(Redis 캐싱)하고 GPT 기반 부적 문구를 반환합니다."
    )
    public ApiResponse<SajuResponse> getSajuResult(@Valid @RequestBody SajuRequest request) {
        // 사주정보 입력 후 결과 반환하기
        SajuResponse response = sajuService.getSajuResult(request);

        return ApiResponse.success("API 부적 멘트가 성공적으로 생성되었습니다.", response);
    }
}
