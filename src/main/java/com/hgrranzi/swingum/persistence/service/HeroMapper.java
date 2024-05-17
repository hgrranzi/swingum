package com.hgrranzi.swingum.persistence.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.view.SwingumException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class HeroMapper {

    @Getter
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final static ValidatorFactory validatorFactory;

    private final static Validator validator;

    static {
        objectMapper.findAndRegisterModules();
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setVisibility(ALL, NONE);
        objectMapper.setVisibility(FIELD, ANY);

        validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
        Runtime.getRuntime().addShutdownHook(new Thread(validatorFactory::close));
    }

    public static HeroEntity toEntity(Hero hero) {
        try {
            validate(hero);
            return HeroEntity.builder()
                    .id(hero.getId())
                    .name(hero.getName())
                    .serializedData(objectMapper.writeValueAsString(hero))
                    .build();
        } catch (JsonProcessingException e) {
            throw new SwingumException("Error serializing hero.");
        }
    }

    public static Hero toHero(HeroEntity entity) {
        try {
            Hero hero = objectMapper.readValue(entity.getSerializedData(), Hero.class);
            hero.setId(entity.getId());
            hero.setName(entity.getName());
            validate(hero);
            return hero;
        } catch (JsonProcessingException e) {
            throw new SwingumException("Error deserializing hero.");
        }
    }

    public static void validate(Hero hero) {
        Set<ConstraintViolation<Hero>> violations = validator.validate(hero);
        if (!violations.isEmpty()) {
            throw new SwingumException("Hero validation failed.");
        }
    }
}
