package com.example.talisman.domain.entity;

import com.example.talisman.domain.dto.SajuResponse;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "saju_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SajuResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "wood_count")
    private int woodCount;
    @Column(name = "earth_count")
    private int earthCount;
    @Column(name = "metal_count")
    private int metalCount;
    @Column(name = "water_count")
    private int waterCount;
    @Column(name = "fire_count")
    private int fireCount;
    @Column(name = "missing_element")
    private String missingElement;
    @Column
    private String ment;

    @OneToOne
    @JoinColumn(name = "id")
    private UserSaju userSaju;
}