package com.hgrranzi.swingum.persistence.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HeroEntity {

    @Min(1)
    private Integer id;

    @NotBlank
    @Size(min = 4, max = 16)
    private String name;

    @Builder.Default
    @PastOrPresent
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Builder.Default
    private String serializedData = null;

}
