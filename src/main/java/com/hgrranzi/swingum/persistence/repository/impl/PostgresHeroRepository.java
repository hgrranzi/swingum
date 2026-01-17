package com.hgrranzi.swingum.persistence.repository.impl;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostgresHeroRepository implements HeroRepository {

    private final Connection connection;

    public PostgresHeroRepository(Connection connection) {
        this.connection = connection;
    }

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
        List<HeroEntity> heroes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                heroes.add(getHeroEntity(resultSet));
            }
            return heroes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all heroes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> findAllNames() {
        List<String> heroNames = new ArrayList<>();
        try (PreparedStatement statement =
                 connection.prepareStatement("SELECT name FROM hero ORDER BY last_updated DESC")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                heroNames.add(resultSet.getString("name"));
            }
            return heroNames;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all hero names: " + e.getMessage(), e);
        }
    }

    private static HeroEntity getHeroEntity(ResultSet resultSet) throws SQLException {
        return HeroEntity.builder()
                   .id(resultSet.getInt("id"))
                   .name(resultSet.getString("name"))
                   .lastUpdated(resultSet.getObject("last_updated", LocalDateTime.class))
                   .serializedData(resultSet.getString("serialized_data"))
                   .build();
    }

    @Override
    public HeroEntity findByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM hero WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getHeroEntity(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find hero by name: " + e.getMessage(), e);
        }
    }

}
