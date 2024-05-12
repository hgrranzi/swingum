package com.hgrranzi.swingum.persistence.repository.impl;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * This repository does not perform any save/load activities
 */
public class MockHeroRepository implements HeroRepository {

    @Override
    public Integer save(HeroEntity hero) {
        return 1;
    }

    @Override
    public List<HeroEntity> findAll() {
        return new ArrayList<>();
    }

    @Override
    public List<String> findAllNames() {
        return new ArrayList<>();
    }

    @Override
    public HeroEntity findByName(String name) {
        return null;
    }
}
