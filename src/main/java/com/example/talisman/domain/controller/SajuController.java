package com.example.talisman.domain.controller;

import com.example.talisman.domain.dto.SajuRequest;
import com.example.talisman.domain.dto.SajuResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/saju")
public class SajuController {
    @PostMapping
    public ResponseEntity<SajuResponse> createSaju(@Valid @RequestBody SajuRequest request) {
        Map<String, Integer> sajuResult = Map.of("WOOD", 1, "FIRE", 2, "EARTH", 0, "METAL", 1, "WATER", 0);
        SajuResponse response = new SajuResponse(sajuResult, "물 기운이 부족합니다");
        return ResponseEntity.ok(response);
    }
}
