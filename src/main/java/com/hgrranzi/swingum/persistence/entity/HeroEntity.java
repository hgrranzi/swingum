package com.hgrranzi.swingum.persistence.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HeroEntity {

    private Integer id;

    private String name;

    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Builder.Default
    private String serializedData = null;

}
