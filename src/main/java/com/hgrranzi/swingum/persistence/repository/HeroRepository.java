package com.hgrranzi.swingum.persistence.repository;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;

import java.util.List;

public interface HeroRepository {

    void save(HeroEntity hero);

    List<HeroEntity> findAll();

    HeroEntity findByName(String name);

    // other methods
}
