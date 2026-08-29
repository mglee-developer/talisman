package com.example.talisman.domain.service;

import com.example.talisman.domain.client.SajuClient;
import com.example.talisman.domain.dto.ManseryeokRequest;
import com.example.talisman.domain.dto.ManseryeokResponse;
import com.example.talisman.domain.dto.SajuRequest;
import com.example.talisman.domain.dto.SajuResponse;
import com.example.talisman.domain.entity.SajuResult;
import com.example.talisman.domain.entity.UserSaju;
import com.example.talisman.domain.repository.UserSajuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SajuService {

    private final SajuClient sajuClient;
    private final UserSajuRepository userSajuRepository;

    public SajuResponse getSajuResult(SajuRequest request) {
        // 1. 만세력 API 요청 조건에 맞게 dto 변환
        int year = Integer.valueOf(request.getBirthday().substring(0, 4));
        int month = Integer.valueOf(request.getBirthday().substring(4, 6));
        int day = Integer.valueOf(request.getBirthday().substring(6, 8));
        int hour = Integer.valueOf(request.getBirthtime().substring(0, 2));
        int minute = Integer.valueOf(request.getBirthtime().substring(2, 4));
        boolean isLunar = request.getSolarOrLunar() == 1 ? false : true;   // 양력/음력
        boolean isLeapMonth = calculateLeapMonth(year);
        String dayBoundary = calculateDayBoundar(hour, request.getNightOrMorning());

        ManseryeokRequest manseryeokRequest = ManseryeokRequest.builder()
                .year(year)
                .month(month)
                .day(day)
                .hour(hour)
                .minute(minute)
                .isLunar(isLunar)
                .isLeapMonth(isLeapMonth)
                .dayBoundary(dayBoundary)
                .build();

        // 2. 만세력 api 호출
        ManseryeokResponse manseryeokResponse = sajuClient.requestManseryeok(manseryeokRequest);

        // 3. 만세력 응답 결과
        int woodCount = manseryeokResponse.getResultData().getWoodCount();
        int earthCount = manseryeokResponse.getResultData().getEarthCount();
        int metalCount = manseryeokResponse.getResultData().getMetalCount();
        int waterCount = manseryeokResponse.getResultData().getWaterCount();
        int fireCount = manseryeokResponse.getResultData().getFireCount();

        Map<String, Integer> resultMap = Map.of(
                "목", woodCount,
                "토", earthCount,
                "금", metalCount,
                "수", waterCount,
                "화", fireCount
        );

        List<String> missingElements = new ArrayList<>();
        int minValue = Collections.min(resultMap.values());
        missingElements = resultMap.entrySet().stream()
                .filter(entry -> entry.getValue() == minValue)
                .map(Map.Entry::getKey)
                .toList();

        String str = String.join(", ", missingElements);
        String ment = str + " (이)가 부족합니다.";

        // 4. ResponseDTO에 담기
        SajuResponse sajuResponse = SajuResponse.builder()
                .sajuResult(resultMap)
                .missingElement(str)
                .ment(ment)
                .build();

        // 5. 엔티티 생성
        UserSaju userSaju = UserSaju.builder()
                .name(request.getName())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .solarOrLunar(request.getSolarOrLunar())
                .birthtime(request.getBirthtime())
                .timeCheck(request.getTimeCheck())
                .nightOrMorning(request.getNightOrMorning())
                .interest(request.getInterest())
                .build();

        // 5-1. SajuResult 엔티티 생성
        SajuResult sajuResult = SajuResult.builder()
                .woodCount(woodCount)
                .earthCount(earthCount)
                .metalCount(metalCount)
                .waterCount(waterCount)
                .fireCount(fireCount)
                .missingElement(str)
                .ment(ment)
                .build();

        // 5-2. DB 저장
        UserSaju savedUserSaju = userSajuRepository.save(userSaju);
        // 5-3. 양방향 연결
        savedUserSaju.setSajuResult(sajuResult);

        // 6. 컨트롤러 반환
        return SajuResponse.from(sajuResult);
    }

    /*
        야자시/조자시 분리
        midnight : 기본
        jasi : 23:00 ~ 23:59에 태어났고 야자시/조자시 여부를 클릭한 경우
        splitJasi : 날짜는 당일이나, 자정이 지난 시점, midnight으로 보내면 api에서 자동으로 조자시로 계산
     */
    private String calculateDayBoundar(int hour, int option) {
        String returnDayBoundary = "midnight";

        if(hour == 23 && option == 1) {
            returnDayBoundary = "jasi";
        }

        return returnDayBoundary;
    }

    // 윤달 계산
    private boolean calculateLeapMonth(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
