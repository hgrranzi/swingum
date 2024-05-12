package com.hgrranzi.swingum.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class HeroMapper {

    @Getter
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.findAndRegisterModules();
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setVisibility(ALL, NONE);
        objectMapper.setVisibility(FIELD, ANY);
    }

    public static HeroEntity toEntity(Hero hero) {
        try {
            return HeroEntity.builder()
                    .id(hero.getId())
                    .name(hero.getName())
                    .serializedData(objectMapper.writeValueAsString(hero))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error serializing hero", e);
        }
    }

    public static Hero toHero(HeroEntity entity) {
        try {
            Hero hero = objectMapper.readValue(entity.getSerializedData(), Hero.class);
            hero.setId(entity.getId());
            hero.setName(entity.getName());
            return hero;
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing hero", e);
        }
    }
}
