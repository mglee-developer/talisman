package com.example.talisman.domain.dto;

import com.example.talisman.domain.entity.SajuResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SajuResponse {
    private Map<String, Integer> sajuResult;
    private String ment;

    public static SajuResponse from(SajuResult sajuResult){
        Map<String, Integer> resultMap = Map.of(
                "WOOD", sajuResult.getWoodCount(),
                "EARTH", sajuResult.getEarthCount(),
                "METAL", sajuResult.getMetalCount(),
                "WATER", sajuResult.getWaterCount(),
                "FIRE", sajuResult.getFireCount()
        );

        return SajuResponse.builder()
                .sajuResult(resultMap)
                .ment(sajuResult.getMent())
                .build();
    }
}
