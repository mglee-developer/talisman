package com.example.talisman.domain.dto;

import com.example.talisman.domain.entity.UserSaju;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "만세력 연산 및 부적 생성 요청 객체")
public class SajuRequest {
    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "이름", example = "홍길동")
    private String name;    // 이름(별명)

    @NotBlank(message = "성별은 필수입니다.")
    @Schema(description = "성별(male / female)", example = "female")
    private String gender;  // 성별

    @NotNull(message = "양력/음력은 필수입니다.")
    @Schema(description = "음력여부(0 : 음력, 1 : 양력)", example = "1")
    private int solarOrLunar;

    @NotBlank(message = "생년월일은 필수입니다.")
    @Schema(description = "태어난 생년월일", example = "20000204")
    private String birthday;       // 생년월일

    @Schema(description = "태어난 시간", example = "2335")
    private String birthtime;      // 태어난 시간

    @Schema(description = "태어난 시간 모름(0 : 안다, 1 : 모른다)", example = "0")
    private Integer timeCheck;  // 태어난 시간 아는지 모르는지

    @Schema(description = "야자시/조자시 적용(0 : 미적용, 1 : 적용)", example = "1")
    private Integer nightOrMorning; // 야자시/조자시 여부

    @Schema(description = "고민거리(1 : 연애, 2 : 결혼, 3 : 학업, 4 : 취업/이직)", example = "2")
    private Integer interest;   // 관심사(1: 학업, 2: 연애, 3: 결혼, 4: 이직/취업)

    public UserSaju toEntity(UserSaju userSaju) {
        return UserSaju.builder()
                .name(this.name)
                .gender(this.gender)
                .solarOrLunar(this.solarOrLunar)
                .birthday(this.birthday)
                .birthtime(this.birthtime)
                .timeCheck(this.timeCheck)
                .nightOrMorning(this.nightOrMorning)
                .interest(this.interest)
                .build();
    }
}
