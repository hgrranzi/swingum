package com.hgrranzi.swingum.persistence.repository.impl;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FileHeroRepository implements HeroRepository {

    @Override
    public void save(HeroEntity hero) {
        // logic for saving to file
    }

    @Override
    public List<HeroEntity> findAll() {
        return List.of();
    }

    @Override
    public HeroEntity findByName(String name) {
        return null;
    }
}
