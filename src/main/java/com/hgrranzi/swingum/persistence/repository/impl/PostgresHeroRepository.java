package com.hgrranzi.swingum.persistence.repository.impl;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class PostgresHeroRepository implements HeroRepository {

    private final Connection connection;

    @Override
    public void save(HeroEntity hero) { // todo: save hero entity to database
        String sql = """
            INSERT INTO hero (name, class, level, last_updated, serialized_data)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save hero: " + e.getMessage());
        }
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
