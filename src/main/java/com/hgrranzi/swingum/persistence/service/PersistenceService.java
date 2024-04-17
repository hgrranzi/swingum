package com.hgrranzi.swingum.persistence.service;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.persistence.DBConnectionManager;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;
import com.hgrranzi.swingum.persistence.repository.impl.FileHeroRepository;
import com.hgrranzi.swingum.persistence.repository.impl.PostgresHeroRepository;
import com.hgrranzi.swingum.view.SwingumException;

import java.sql.Connection;
import java.util.List;

public class PersistenceService {

    private final HeroRepository heroRepository;

    public PersistenceService() {
        HeroRepository repository;
        try {
            Connection connection = DBConnectionManager.open();
            repository = new PostgresHeroRepository(connection);
        } catch (SwingumException e) {
            repository = new FileHeroRepository();
        }
        heroRepository = repository;
    }

    public boolean isHeroNameAvailable(String name) {
        return heroRepository.findByName(name) == null;
    }

    public Integer saveHero(Hero hero) {
        return heroRepository.save(HeroMapper.toEntity(hero));
    }

    public List<String> loadHeroNames() {
        return heroRepository.findAllNames();
    }

    public Hero loadHero(String name) {
        return HeroMapper.toHero(heroRepository.findByName(name));
    }

    // other methods

}


