package com.example.talisman.domain.service;

import com.example.talisman.domain.client.SajuClient;
import com.example.talisman.domain.dto.ManseryeokRequest;
import com.example.talisman.domain.dto.ManseryeokResponse;
import com.example.talisman.domain.dto.SajuRequest;
import com.example.talisman.domain.dto.SajuResponse;
import com.example.talisman.domain.entity.SajuResult;
import com.example.talisman.domain.entity.UserSaju;
import com.example.talisman.domain.repository.UserSajuRepository;
import com.example.talisman.global.config.openai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SajuService {

    private final SajuClient sajuClient;
    private final UserSajuRepository userSajuRepository;
    private final OpenAiClient openAiClient;

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

        // 부족한 오행 추출
        missingElements = resultMap.entrySet().stream()
                .filter(entry -> entry.getValue() == minValue)
                .map(Map.Entry::getKey)
                .toList();

        // OpenAI API 연결해서 멘트 가져오기
        String str = String.join(", ", missingElements);
        String ment = callOpenAi(str, request.getInterest());

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

    // 부족한 오행 + 고민거리에 따른 부적 멘트 생성
    private String callOpenAi(String str, Integer interest) {
        String systemPrompt = """
                너는 유쾌하고 위트 있는 부적 문구 전문가야.
                입력된 부족한 오행과 고민거리를 바탕으로, 직장인부터 학생까지 누구나 보고 피식 웃을 수 있는 뻔뻔하고 당당한 부적 멘트를 2문장 이내로 작성해 줘.

                [필수 금지 규칙]
                1. '물의 기운', '불꽃', '나무의 열매'처럼 오행 단어를 1차원적/문학적으로 직접 언급하며 억지 텐션을 올리지 말 것. (유치함 절대 금지)
                2. '~하게 해주세요', '~하길 바래!' 같은 시적이고 소원 빌기식 말투 금지.
                3. 일시적인 억지 밈이나 유행어(얍, 럭키비키 등) 사용 금지.

                [문장 작성 스타일]
                - 은근히 뻔뻔하고 담백한 확신형 어조 (~임, ~ 완료, ~거절함)
                - 예시 A: "기운 충전 완료. 오늘의 주인공은 바로 나, 일단 나한테 불만 있는 사람? 그 사람 찬물샤워 각임."
                - 예시 B: "통장 방어막 시전 완료. 들어온 돈은 절대 나가지 않는 무적의 부적 작동 중."
                """;    // 고정

        String userPrompt = null;   // 고민거리 유무에 따라 변동

        // 부족한 오행 정제
        String formattedElements = str;

        // 고민거리 정제
        String strInterest = switch(interest != null ? interest : 0) {
            case 1 -> "연애";
            case 2 -> "결혼";
            case 3 -> "취업";
            case 4 -> "이직";
            default -> null;
        };

        // 고민거리를 선택한 경우 : 고민거리 맞춤형 User Prompt 생성
        if(strInterest != null) {
            userPrompt = String.format("""
                    부족한 오행: %s
                    고민거리: %s
                    부족한 오행인 '%s'의 기운을 보완하면서 고민거리인 '%s'을(를) 완벽하게 해결해 줄 킹받고 귀여운 부적 멘트를 작성해줘.
                    """, formattedElements, strInterest, formattedElements, strInterest);
        }
        // 선택하지 않은 경우 : 오행 보완 적용 User Prompt 생성
        else {
            userPrompt = String.format("""
                    부족한 오행: %s
                    부족한 '%s' 기운을 보완하면서 고민거리인 '%s' 문제를 유쾌하게 타파해 줄 부적 멘트를 작성해 줘.
                    
                    [필수 포함 조건]
                    1. 고민 해결에 도움을 줄 '오늘의 행운 팁(추천 컬러, 장소, 아이템, 행동 중 1가지)'을 자연스럽게 1개 포함할 것.
                    2. 시적이거나 오글거리는 비유(불꽃, 사랑의 물줄기 등)는 절대 금지하고, 뻔뻔하지만 센스 있는 2문장 이내의 평서문으로 작성할 것.
                    """, formattedElements, formattedElements);
        }

        // API 호출 및 예외처리
        try {
            return openAiClient.generateText(systemPrompt, userPrompt);
        } catch(Exception e) {
            // 오류 발생해도 서비스 전체가 멈추지 않도록 예외 처리
            return "오늘부터 너의 운은 최고야! 모든 액운을 거절할게!";
        }
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
