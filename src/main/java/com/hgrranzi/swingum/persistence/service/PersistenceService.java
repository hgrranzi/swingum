package com.hgrranzi.swingum.persistence.service;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.persistence.DBConnectionManager;
import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;
import com.hgrranzi.swingum.persistence.repository.impl.FileHeroRepository;
import com.hgrranzi.swingum.persistence.repository.impl.PostgresHeroRepository;
import com.hgrranzi.swingum.view.SwingumException;

import java.sql.Connection;
import java.util.ArrayList;
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
        return heroRepository.findByName(name) != null;
    }

    public void saveHero(Hero hero) {
        HeroEntity entity = HeroEntity.builder()
                                .name(hero.getName())
                                .clazz(hero.getClazz().name())
                                .level(hero.getLevel())
                                .serializedData("") // todo: serialize hero data
                                .build();
        heroRepository.save(entity);
    }

    public List<Hero> loadHeroes() {
        List<HeroEntity> entities = heroRepository.findAll();
        // convert entities to dto or directly to Hero
        return new ArrayList<>();
    }

    // other methods

}


