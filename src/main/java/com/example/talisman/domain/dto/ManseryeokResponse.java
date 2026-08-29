package com.example.talisman.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManseryeokResponse {
    private String status;
    @JsonProperty("data")
    private ResultData resultData;

    @Getter
    @NoArgsConstructor
    public static class ResultData{
        // 사주 팔자 간지
        private String yearGanji;
        private String monthGanji;
        private String dayGanji;
        private String timeGanji;

        // 오행 카운트
        private int woodCount;
        private int earthCount;
        private int metalCount;
        private int waterCount;
        private int fireCount;

        // 오행 중 가장 부족한 기운
        private String missingElement;

        @Builder
        public ResultData(String yearGanji, String monthGanji, String dayGanji, String timeGanji,
                          int woodCount, int earthCount, int metalCount, int waterCount, int fireCount, String missingElement) {
            this.yearGanji = yearGanji;
            this.monthGanji = monthGanji;
            this.dayGanji = dayGanji;
            this.timeGanji = timeGanji;
            this.woodCount = woodCount;
            this.earthCount = earthCount;
            this.metalCount = metalCount;
            this.waterCount = waterCount;
            this.fireCount = fireCount;
            this.missingElement = missingElement;
        }
    }
}
