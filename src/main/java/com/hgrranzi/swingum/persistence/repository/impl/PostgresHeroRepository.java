package com.hgrranzi.swingum.persistence.repository.impl;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class PostgresHeroRepository implements HeroRepository {

    private final Connection connection;

    @Override
    public Integer save(HeroEntity hero) {
        try {
            if (hero.getId() == null) {
                return insertHero(hero);
            } else {
                updateHero(hero);
                return hero.getId();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save hero: " + e.getMessage(), e);
        }
    }

    private Integer insertHero(HeroEntity hero) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO hero (name, last_updated, serialized_data) VALUES (?, ?, ?) RETURNING id")) {
            statement.setString(1, hero.getName());
            statement.setObject(2, hero.getLastUpdated());
            statement.setString(3, hero.getSerializedData());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        }
    }

    private void updateHero(HeroEntity hero) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
            "UPDATE hero SET last_updated = ?, serialized_data = ? WHERE id = ?")) {
            statement.setObject(1, hero.getLastUpdated());
            statement.setString(2, hero.getSerializedData());
            statement.setInt(3, hero.getId());
            statement.executeUpdate();
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
