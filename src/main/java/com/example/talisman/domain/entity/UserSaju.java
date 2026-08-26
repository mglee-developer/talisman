package com.example.talisman.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_saju")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSaju {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private Integer solarOrLunar;
    @Column(nullable = false)
    private String birthday;
    @Column
    private String birthtime;
    @Column
    private Integer timeCheck;
    @Column
    private Integer nightOrMorning;
    @Column
    private Integer interest;

    @OneToOne(mappedBy = "userSaju", cascade = CascadeType.ALL)
    private SajuResult sajuResult;
}
