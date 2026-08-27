package com.example.talisman.domain.repository;

import com.example.talisman.domain.entity.UserSaju;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSajuRepository extends JpaRepository<UserSaju,Long> {
    // JpaRepository를 상속받으면 기본 CRUD 메서드가 내장되어 있어 생략 가능
}
