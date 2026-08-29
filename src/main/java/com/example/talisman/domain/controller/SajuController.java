package com.example.talisman.domain.controller;

import com.example.talisman.domain.dto.SajuRequest;
import com.example.talisman.domain.dto.SajuResponse;
import com.example.talisman.domain.service.SajuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/saju")
public class SajuController {
    private final SajuService sajuService;

    @PostMapping
    public ResponseEntity<SajuResponse> getSajuResult(@Valid @RequestBody SajuRequest request) {

        SajuResponse response = sajuService.getSajuResult(request);

        return ResponseEntity.ok(response);
    }
}
