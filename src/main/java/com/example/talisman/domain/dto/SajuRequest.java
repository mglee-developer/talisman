package com.example.talisman.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SajuRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;    // 이름(별명)

    @NotBlank(message = "성별은 필수입니다.")
    private String gender;  // 성별

    @NotNull(message = "양력/음력은 필수입니다.")
    private int solarOrLunar;

    @NotBlank(message = "생년월일은 필수입니다.")
    private String birthday;       // 생년월일

    private String birthtime;      // 태어난 시간

    private Integer timeCheck;  // 태어난 시간 아는지 모르는지

    private Integer nightOrMorning; // 야자시/조자시 여부

    private Integer interest;   // 관심사(1: 학업, 2: 연애, 3: 결혼, 4: 이직/취업)
}
