package com.example.talisman.repository;

import com.example.talisman.domain.entity.SajuResult;
import com.example.talisman.domain.entity.UserSaju;
import com.example.talisman.domain.repository.UserSajuRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Swagger 테스트를 위해 중단")
@DataJpaTest
public class UserSajuRepositoryTest {
    @Autowired
    private UserSajuRepository userSajuRepository;

    @Test
    @DisplayName("사용자 사주 정보를 저장할 때 분석 결과도 함께 연쇄 저장되어야 한다.")
    public void testSave() {
        // given
        UserSaju userSaju = UserSaju.builder()
                .name("test")
                .gender("여")
                .solarOrLunar(1)
                .birthday("1994/02/04")
                .birthtime("23:35")
                .timeCheck(0)
                .nightOrMorning(1)
                .build();

        SajuResult sajuResult = SajuResult.builder()
                .woodCount(2)
                .earthCount(1)
                .metalCount(3)
                .waterCount(0)
                .fireCount(1)
                .missingElement("물")
                .ment("물 기운이 부족합니다.")
                .build();

        userSaju.setSajuResult(sajuResult);

        // when
        UserSaju savedUserSaju = userSajuRepository.save(userSaju);

        // then
        assertThat(savedUserSaju).isNotNull();
        assertThat(savedUserSaju.getSajuResult()).isNotNull();
        assertThat(savedUserSaju.getSajuResult().getMissingElement()).isEqualTo("물");
    }
}
