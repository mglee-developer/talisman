package com.example.talisman.domain.repository;

import com.example.talisman.domain.entity.SajuResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SajuResultRepository extends JpaRepository<SajuResult,Long> {
    // SajuResult 내부의 UserSaju 객체 및 그 ID를 기준으로 조회하는 쿼리 메서드
    SajuResult findByUserSaju_Id(Long userSajuId);
}
