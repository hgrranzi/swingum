package com.hgrranzi.swingum.persistence.repository.impl;

import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.persistence.repository.HeroRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileHeroRepository implements HeroRepository {

    private final File file;
    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_HEADER = "id,name,lastUpdated,serializedData";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public FileHeroRepository(File file) {
        this.file = file;
    }

    @Override
    public Integer save(HeroEntity hero) {
        List<HeroEntity> heroes = findAll();
        Optional<HeroEntity> existingHero = heroes.stream()
                .filter(h -> h.getId() != null && h.getId().equals(hero.getId()))
                .findFirst();

        if (existingHero.isPresent()) {
            int index = heroes.indexOf(existingHero.get());
            hero.setLastUpdated(LocalDateTime.now());
            heroes.set(index, hero);
        } else {
            int maxId = heroes.stream()
                    .filter(h -> h.getId() != null)
                    .mapToInt(HeroEntity::getId)
                    .max()
                    .orElse(0);
            hero.setId(maxId + 1);
            hero.setLastUpdated(LocalDateTime.now());
            heroes.add(hero);
        }
        writeHeroes(heroes);
        return hero.getId();
    }

    private void writeHeroes(List<HeroEntity> heroes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(CSV_HEADER);
            
            for (HeroEntity hero : heroes) {
                writer.println(serializeHeroEntity(hero));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write heroes to file: " + e.getMessage(), e);
        }
    }

    private String serializeHeroEntity(HeroEntity hero) {
        StringBuilder sb = new StringBuilder();
        sb.append(hero.getId() != null ? hero.getId() : "");
        sb.append(CSV_SEPARATOR);
        sb.append(escapeCsv(hero.getName()));
        sb.append(CSV_SEPARATOR);
        sb.append(hero.getLastUpdated() != null ? hero.getLastUpdated().format(DATE_FORMATTER) : LocalDateTime.now().format(DATE_FORMATTER));
        sb.append(CSV_SEPARATOR);
        sb.append(escapeCsv(hero.getSerializedData()));
        return sb.toString();
    }

    @Override
    public List<HeroEntity> findAll() {
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || !line.equals(CSV_HEADER)) {
                return new ArrayList<>();
            }
            
            List<HeroEntity> heroes = new ArrayList<>();
            Set<Integer> uniqueIds = new HashSet<>();
            Set<String> uniqueNames = new HashSet<>();
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    HeroEntity hero = deserializeHeroEntity(line);
                    if (hero != null && 
                        (hero.getId() == null || uniqueIds.add(hero.getId())) && 
                        uniqueNames.add(hero.getName())) {
                        heroes.add(hero);
                    }
                } catch (Exception e) {
                    // Skip invalid lines
                }
            }
            
            heroes.sort(Comparator.comparing(HeroEntity::getLastUpdated));
            return heroes;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private HeroEntity deserializeHeroEntity(String line) {
        String[] parts = parseCsvLine(line);
        if (parts.length < 4) {
            return null;
        }
        
        try {
            Integer id = parts[0].isEmpty() ? null : Integer.parseInt(parts[0]);
            String name = unescapeCsv(parts[1]);
            LocalDateTime lastUpdated = parts[2].isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(unescapeCsv(parts[2]), DATE_FORMATTER);
            String serializedData = unescapeCsv(parts[3]);
            
            return HeroEntity.builder()
                    .id(id)
                    .name(name)
                    .lastUpdated(lastUpdated)
                    .serializedData(serializedData)
                    .build();
        } catch (Exception e) {
            return null;
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
                .findFirst()
                .orElse(null);
    }

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++;
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString());
        
        return fields.toArray(new String[0]);
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            return value.replace("\"\"", "\"");
        }
        return value;
    }
}
