package com.hgrranzi.swingum.persistence.repository;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;

import java.util.List;

public interface HeroRepository {

    Integer save(HeroEntity hero);

    List<HeroEntity> findAll();

    List<String> findAllNames();

    HeroEntity findByName(String name);

    // other methods
}
