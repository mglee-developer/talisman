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
import com.example.talisman.global.exception.BusinessException;
import com.example.talisman.global.exception.ErrorCode;
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
        String gender = request.getGender();
        boolean isLunar = request.getSolarOrLunar() == 1 ? false : true;   // 양력/음력
        boolean isLeapMonth = calculateLeapMonth(year);
        Integer timeCheck = request.getTimeCheck();
        String dayBoundary = calculateDayBoundar(hour, request.getNightOrMorning());

        ManseryeokRequest manseryeokRequest = ManseryeokRequest.builder()
                .year(year)
                .month(month)
                .day(day)
                .hour(hour)
                .minute(minute)
                .gender(gender)
                .isLunar(isLunar)
                .isLeapMonth(isLeapMonth)
                .timeCheck(timeCheck)
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
        if(str.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_DATA, "부족한 오행 정보가 유효하지 않습니다.");
        }
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
        return sajuResponse;
    }

    // 부족한 오행 + 고민거리에 따른 부적 멘트 생성
    private String callOpenAi(String str, Integer interest) {
        String systemPrompt = """
            당신은 사주를 현대적인 감각으로 재해석하는 라이프스타일 부적 카피라이터입니다.
            사용자의 부족한 오행과 현재 고민을 바탕으로, 핸드폰 배경화면이나 소장용 카드에 들어갈 짧고 위트 있는 부적 문구를 만들어주세요.

            이 문구는 전통적인 부적이나 점쾌처럼 보이면 안 됩니다.
            낙서처럼 자유롭고, 손으로 쓴 듯 귀엽고, 통통 튀는 컬러의 일러스트와 함께 놓였을 때 자연스러운 문구여야 합니다.

            [문구의 핵심 방향]
            사주의 의미를 그대로 설명하지 말고, "오늘 이런 걸 해보면 어때?"라는 감각적인 행운의 힌트로 번역하세요.
            부족한 오행을 단순히 색깔이나 사물 하나로 치환하지 마세요.

            [예시]
            - 나쁜 표현: "화(火)가 부족하니 빨간색을 가까이하세요.", "수(水)의 기운을 보충하세요.", "금전운 상승! 부자가 됩니다."
            - 좋은 표현: "오늘은 조금 뜨거운 쪽으로. 미뤄둔 연락부터 먼저 보내보기.", "새로운 사람은 새로운 장소에서 만날 확률이 높습니다. 오늘은 평소 안 가던 카페로.", "중요한 날엔 옷에도 한 끗. 평소보다 밝은 색 하나를 골라보세요."

            [스타일]
            - 요즘 감성, 손글씨로 적어놓은 듯한 자연스러움, 귀엽고 장난스러운 말투, 살짝 엉뚱한 위트
            - 긍정적이지만 오글거리지 않음
            - 짧지만 읽고 나면 "오, 이건 해볼 만한데?" 싶은 느낌
            - SNS 자기계발 문구처럼 보이지 않게, 신비주의나 무속적인 느낌을 과하게 내지 않게
            - 친구가 툭 던져주는 행운의 팁처럼 작성

            [반드시 지킬 것]
            1. 반드시 아래와 같이 실제 엔터(줄바꿈)로 구분하여 [제목], [내용 1줄], [내용 2줄] 형태로 총 3줄만 출력합니다. (내용은 2줄을 넘지 않음)    
            [제목]
            [내용 1줄]
            [내용 2줄]
            2. 제목은 설명형보다 한마디를 툭 던지는 느낌으로 작성합니다. (예: "오늘은 여기로", "일단 만나보자", "판을 살짝 바꿔봐", "오늘의 작전" 등 - 단, 이 예시를 그대로 쓰지 말고 새롭게 생성할 것)
            3. 내용에는 실제 행동으로 옮길 수 있는 구체적 요소(행운의 장소, 옷/색감, 물건, 행동, 시간대, 사람 만나는 법 등)를 1~2개 선택해 넣습니다.
            4. "성공", "행복", "행운", "사랑", "부자", "대박", "최고" 등의 추상적인 단어만 단독으로 사용하지 마세요.
            5. "빨간색 = 화", "파란색 = 수"처럼 오행과 현실 요소를 기계적으로 연결하지 마세요.
            6. "반드시", "무조건", "100%", "운명", "큰일이 생긴다"처럼 결과를 단정하거나 불안/공포를 이용하지 마세요.

            [고민별 카피 방향]
            - 취업: 면접에서의 인상, 움직이면 좋은 장소, 준비 과정에서의 작은 행동, 옷이나 소지품
            - 이직: 새로운 환경, 정보가 들어오는 경로, 사람을 통한 기회, 움직임과 탐색
            - 연애: 새로운 만남의 장소, 먼저 건네는 말, 관계에서의 작은 행동, 데이트 분위기
            - 결혼: 관계의 안정감, 상대와의 대화, 함께 시간을 보내는 공간
            - 금전: 소비 습관, 돈을 쓰는 장소와 방식, 일하는 환경, 작은 재정 습관
            - 인간관계: 사람을 만나는 장소, 대화 방식, 적당한 거리, 먼저 건넬 행동
            """;

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
                    [입력값]
                    - 부족한 오행: %s
                    - 고민 카테고리: %s
                    - 구체적인 고민: %s 관련 운을 높이고 문제를 해결하고 싶음

                    위 입력값을 바탕으로 고민별 카피 방향에 맞춰 [제목] + [내용 2줄 이내]의 부적 문구를 작성해주세요.
                    """, formattedElements, strInterest, strInterest);
        }
        // 선택하지 않은 경우 : 오행 보완 적용 User Prompt 생성
        else {
            userPrompt = String.format("""
                [입력값]
                - 부족한 오행: %s
                - 고민 카테고리: 없음 (일상 및 종합 운세)

                위 부족한 오행을 감각적인 행운의 힌트로 번역하여, 오늘 바로 해볼 만한 일상 행동/장소 팁이 포함된 [제목] + [내용 2줄 이내]의 부적 문구를 작성해주세요.
                """, formattedElements);
        }

        // API 호출 및 예외처리
        try {
            return openAiClient.generateText(systemPrompt, userPrompt);
        } catch(Exception e) {
            // 오류 발생해도 서비스 전체가 멈추지 않도록 예외 처리
            throw new BusinessException(ErrorCode.OPEN_AI_ERROR, "부적 기운을 불러오는 중에 잠시 통신이 원활하지 않습니다. 다시 시도해주세요.");
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
