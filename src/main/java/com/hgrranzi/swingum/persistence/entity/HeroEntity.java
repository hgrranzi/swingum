package com.hgrranzi.swingum.persistence.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HeroEntity {

    private Integer id;

    private String name;

    private String clazz;

    @Builder.Default
    private int level = 1;

    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Builder.Default
    private String serializedData = null;

}
