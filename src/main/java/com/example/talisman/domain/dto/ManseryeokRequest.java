package com.example.talisman.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManseryeokRequest {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean isLunar;
    private boolean isLeapMonth;
    private String dayBoundary; // 야자시 적용

    @Builder
    public ManseryeokRequest(int year, int month, int day, int hour, int minute, boolean isLunar, boolean isLeapMonth, String dayBoundary) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.isLunar = isLunar;
        this.isLeapMonth = isLeapMonth;
        this.dayBoundary = dayBoundary;
    }

    private class SolarTime {
        private String longitude;
        private boolean applyEquationOfTime;
        private boolean applyHistoricalDst;
        /*
        ongitude: 126.978,         // 출생지 경도(동경). 기본 127.5 (한반도 평균)
        applyEquationOfTime: true,  // 균시차 보정 (기본 true)
        applyHistoricalDst: true,   // 과거 표준시/서머타임 보정 (기본 true)

         */
    }
}
