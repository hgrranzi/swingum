package com.hgrranzi.swingum.persistence.service;

import com.google.gson.Gson;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.persistence.entity.HeroEntity;

import java.util.ArrayList;
import java.util.List;

public class HeroMapper {

    private final static Gson gsonMapper = new Gson();

    public static HeroEntity toEntity(Hero hero) {
        System.out.println(gsonMapper.toJson(hero));

        return HeroEntity.builder()
                   .id(hero.getId())
                   .name(hero.getName())
                   .serializedData(gsonMapper.toJson(hero)) // todo: serialize hero data
                   .build();
    }

    public static Hero toHero(HeroEntity entity) {
        Hero hero = gsonMapper.fromJson(entity.getSerializedData(), Hero.class);
        hero.setId(entity.getId());
        return hero;
    }

    public static List<Hero> toHeroList(List<HeroEntity> entityList) {
        List<Hero> heroList = new ArrayList<>();
        for (HeroEntity entity : entityList) {
            heroList.add(toHero(entity));
        }
        return heroList;
    }
}
