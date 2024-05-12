package com.hgrranzi.swingum.persistence.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;
import com.hgrranzi.swingum.persistence.service.HeroMapper;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FileHeroRepository implements HeroRepository {

    private final File file;

    @Override
    public Integer save(HeroEntity hero) {
        List<HeroEntity> heroes = findAll();
        Optional<HeroEntity> existingHero = heroes.stream()
                .filter(h -> h.getId().equals(hero.getId()))
                .findFirst();

        if (existingHero.isPresent()) {
            int index = heroes.indexOf(existingHero.get());
            heroes.set(index, hero);
        } else {
            int maxId = heroes.stream().mapToInt(HeroEntity::getId).max().orElse(0);
            hero.setId(maxId + 1);
            heroes.add(hero);
        }
        writeHeroes(heroes);
        return hero.getId();
    }

    private void writeHeroes(List<HeroEntity> heroes) {
        try {
            HeroMapper.getObjectMapper().writeValue(file, heroes);
        } catch (IOException e) {
            // todo: handle write exceptions ?
            throw new RuntimeException("Failed to write heroes to file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HeroEntity> findAll() {
        try {
            String jsonData = new String(Files.readAllBytes(file.toPath()));
            return HeroMapper.getObjectMapper().readValue(jsonData, new TypeReference<>() {
            });
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> findAllNames() {
        return findAll().stream().map(HeroEntity::getName).toList();
    }

    @Override
    public HeroEntity findByName(String name) {
        return findAll().stream()
                .filter(h -> h.getName().equals(name))
                .findFirst().orElse(null);
    }
}
