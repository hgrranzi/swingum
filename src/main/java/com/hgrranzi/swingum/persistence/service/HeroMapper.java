package com.hgrranzi.swingum.persistence.service;

import com.hgrranzi.swingum.model.Artefact;
import com.hgrranzi.swingum.model.ArtefactType;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.model.Villain;
import com.hgrranzi.swingum.model.VillainType;
import com.hgrranzi.swingum.persistence.entity.HeroEntity;
import com.hgrranzi.swingum.view.SwingumException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HeroMapper {

    private final static ValidatorFactory validatorFactory;
    private final static Validator validator;

    static {
        validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
        Runtime.getRuntime().addShutdownHook(new Thread(validatorFactory::close));
    }

    public static HeroEntity toEntity(Hero hero) {
        validate(hero);
        String csvData = serializeHeroToCsv(hero);
        return HeroEntity.builder()
                .id(hero.getId())
                .name(hero.getName())
                .serializedData(csvData)
                .build();
    }

    public static Hero toHero(HeroEntity entity) {
        if (entity == null) {
            throw new SwingumException("No existing hero selected");
        }
        Hero hero = deserializeHeroFromCsv(entity.getSerializedData());
        hero.setId(entity.getId());
        hero.setName(entity.getName());
        validate(hero);
        return hero;
    }

    public static void validate(Hero hero) {
        Set<ConstraintViolation<Hero>> violations = validator.validate(hero);
        if (!violations.isEmpty()) {
            throw new SwingumException("Hero validation failed.");
        }
    }

    private static String serializeHeroToCsv(Hero hero) {
        StringBuilder sb = new StringBuilder();
        
        // Основные поля
        sb.append(escapeCsv(hero.getId() != null ? hero.getId().toString() : ""));
        sb.append(",");
        sb.append(escapeCsv(hero.getName()));
        sb.append(",");
        sb.append(escapeCsv(hero.getClazz().name()));
        sb.append(",");
        sb.append(hero.getLevel());
        sb.append(",");
        sb.append(hero.getX());
        sb.append(",");
        sb.append(hero.getY());
        sb.append(",");
        sb.append(hero.getXp());
        sb.append(",");
        sb.append(hero.getHitPoints());
        sb.append(",");
        sb.append(hero.isCannotRun());
        sb.append(",");
        sb.append(escapeCsv(hero.getStatus()));
        sb.append(",");
        
        // Villains
        sb.append(serializeVillains(hero.getVillains()));
        sb.append(",");
        
        // Inventory
        sb.append(serializeInventory(hero.getInventory()));
        
        return sb.toString();
    }

    private static String serializeVillains(List<Villain> villains) {
        if (villains == null || villains.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < villains.size(); i++) {
            if (i > 0) {
                sb.append(";;");
            }
            Villain v = villains.get(i);
            sb.append(v.getType().name());
            sb.append("|");
            sb.append(v.getPosX());
            sb.append("|");
            sb.append(v.getPosY());
            sb.append("|");
            sb.append(v.getAttack());
            sb.append("|");
            sb.append(v.getHitPoints());
            sb.append("|");
            if (v.getArtefact() != null) {
                sb.append(v.getArtefact().getType().name());
                sb.append("|");
                sb.append(v.getArtefact().getEffect());
            } else {
                sb.append("null|null");
            }
            sb.append("|");
            sb.append(v.getXp());
        }
        return escapeCsv(sb.toString());
    }

    private static String serializeInventory(Artefact[] inventory) {
        if (inventory == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inventory.length; i++) {
            if (i > 0) {
                sb.append(";;");
            }
            Artefact a = inventory[i];
            if (a != null) {
                sb.append(a.getType().name());
                sb.append("|");
                sb.append(a.getEffect());
            } else {
                sb.append("null|null");
            }
        }
        return escapeCsv(sb.toString());
    }

    private static Hero deserializeHeroFromCsv(String csvData) {
        if (csvData == null || csvData.trim().isEmpty()) {
            throw new SwingumException("Empty CSV data");
        }
        
        String[] parts = parseCsvLine(csvData);
        if (parts.length < 11) {
            throw new SwingumException("Invalid CSV format: not enough fields");
        }
        
        try {
            Integer id = parts[0].isEmpty() ? null : Integer.parseInt(parts[0]);
            String name = unescapeCsv(parts[1]);
            HeroClass clazz = HeroClass.valueOf(unescapeCsv(parts[2]));
            int level = Integer.parseInt(parts[3]);
            int x = Integer.parseInt(parts[4]);
            int y = Integer.parseInt(parts[5]);
            int xp = Integer.parseInt(parts[6]);
            int hitPoints = Integer.parseInt(parts[7]);
            boolean cannotRun = Boolean.parseBoolean(parts[8]);
            String status = unescapeCsv(parts[9]);
            List<Villain> villains = deserializeVillains(unescapeCsv(parts[10]));
            Artefact[] inventory = deserializeInventory(unescapeCsv(parts[11]));
            
            return new Hero(id, name, clazz, level, 0, x, y, villains, xp, hitPoints, cannotRun, inventory, null, status);
        } catch (Exception e) {
            throw new SwingumException("Error deserializing hero from CSV: " + e.getMessage());
        }
    }

    private static List<Villain> deserializeVillains(String data) {
        List<Villain> villains = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            return villains;
        }
        
        String[] villainStrings = data.split(";;");
        for (String villainStr : villainStrings) {
            if (villainStr.isEmpty()) {
                continue;
            }
            String[] parts = villainStr.split("\\|");
            if (parts.length < 8) {
                continue;
            }
            
            try {
                VillainType type = VillainType.valueOf(parts[0]);
                int posX = Integer.parseInt(parts[1]);
                int posY = Integer.parseInt(parts[2]);
                int attack = Integer.parseInt(parts[3]);
                int hitPoints = Integer.parseInt(parts[4]);
                Artefact artefact = null;
                if (!parts[5].equals("null") && !parts[6].equals("null")) {
                    ArtefactType artefactType = ArtefactType.valueOf(parts[5]);
                    int effect = Integer.parseInt(parts[6]);
                    artefact = new Artefact(artefactType, effect);
                }
                int xp = Integer.parseInt(parts[7]);
                
                villains.add(new Villain(type, posX, posY, attack, hitPoints, artefact, xp));
            } catch (Exception e) {
                // Skip invalid villain
            }
        }
        return villains;
    }

    private static Artefact[] deserializeInventory(String data) {
        ArtefactType[] types = ArtefactType.values();
        Artefact[] inventory = new Artefact[types.length];
        
        if (data == null || data.isEmpty()) {
            return inventory;
        }
        
        String[] artefactStrings = data.split(";;");
        for (String artefactStr : artefactStrings) {
            if (artefactStr.isEmpty() || artefactStr.equals("null|null")) {
                continue;
            }
            
            String[] parts = artefactStr.split("\\|");
            if (parts.length >= 2 && !parts[0].equals("null") && !parts[1].equals("null")) {
                try {
                    ArtefactType type = ArtefactType.valueOf(parts[0]);
                    int effect = Integer.parseInt(parts[1]);
                    inventory[type.ordinal()] = new Artefact(type, effect);
                } catch (Exception e) {
                    // Skip invalid artefact
                }
            }
        }
        return inventory;
    }

    private static String[] parseCsvLine(String line) {
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

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String unescapeCsv(String value) {
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
