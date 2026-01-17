package com.hgrranzi.swingum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.util.List;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;
import static com.hgrranzi.swingum.model.ArtefactType.*;

@JsonIgnoreProperties({"interaction", "mapSize", "cannotRun"})
public class Hero {

    @Positive
    private Integer id = null;

    @NotBlank
    @Size(min = 3, max = 16)
    private String name;

    private final HeroClass clazz;

    @Range(min = 1, max = 10)
    private int level = 1;

    private int mapSize = 10;

    private int x = 5;

    private int y = 5;

    private List<Villain> villains = Villain.createVillains(10, 1);

    @Min(0)
    private int xp = 0;

    @Range(min = 1, max = 10)
    private int hitPoints = 10;

    boolean cannotRun = false;

    @Size(max = 3)
    @Valid
    private Artefact[] inventory = new Artefact[ArtefactType.values().length];

    private Interactive interaction = null;

    private String status = "";

    @JsonCreator
    public Hero(@JsonProperty("id") Integer id,
                @JsonProperty("name") String name,
                @JsonProperty("clazz") HeroClass clazz,
                @JsonProperty("level") int level,
                @JsonProperty("mapSize") int mapSize,
                @JsonProperty("x") int x,
                @JsonProperty("y") int y,
                @JsonProperty("villains") List<Villain> villains,
                @JsonProperty("xp") int xp,
                @JsonProperty("hitPoints") int hitPoints,
                @JsonProperty("cannotRun") boolean cannotRun,
                @JsonProperty("inventory") Artefact[] inventory,
                @JsonProperty("interaction") Interactive interaction,
                @JsonProperty("status") String status) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.level = level;
        this.mapSize = (level - 1) * 5 + 10;
        this.x = x;
        this.y = y;
        if (isOutOfMap()) {
            updateHeroPosition();
        }
        this.villains = villains;
        this.xp = xp;
        this.hitPoints = hitPoints;
        this.cannotRun = false;
        this.inventory = inventory;
        this.interaction = interaction;
        this.status = status == null ? "" : status;
    }

    public String getInfo() {
        return String.format("attack: %d+%d | defence: %d+%d | hit points: %d+%d",
                clazz.getAttack(), getWeaponEffect(), clazz.getDefense(), getArmorEffect(), hitPoints, getHelmEffect());
    }

    public int getWeaponEffect() {
        return inventory[WEAPON.ordinal()] == null ? 0 : inventory[WEAPON.ordinal()].getEffect();
    }

    public int getArmorEffect() {
        return inventory[ARMOR.ordinal()] == null ? 0 : inventory[ARMOR.ordinal()].getEffect();
    }

    public int getHelmEffect() {
        return inventory[HELM.ordinal()] == null ? 0 : inventory[HELM.ordinal()].getEffect();
    }

    public void upgradeLevel() {
        xp = level * 1000 + (level - 1) * (level - 1) * 450;
        level++;
        mapSize = (level - 1) * 5 + 10;
        x = this.mapSize / 2;
        y = this.mapSize / 2;
        villains = Villain.createVillains(mapSize, level);
    }

    public void move(char direction) {
        status = "GO";
        updateHeroPosition(direction);
        getVillains().forEach(villain -> {
            if (villain.getPosX() == getX() && villain.getPosY() == getY()) {
                status = villain.getInfo();
                interaction = villain;
            }
        });
        if (isOutOfMap()) {
            if (level == 10) {
                status = LevelEndType.WON_GAME.getInfo();
                interaction = LevelEndType.WON_GAME;
            } else {
                status = LevelEndType.WON_LEVEL.getInfo();
                interaction = LevelEndType.WON_LEVEL;
            }
        }
    }

    private boolean isOutOfMap() {
        return getX() == -1 || getY() == -1 || getX() == getMapSize() || getY() == getMapSize();
    }

    private void updateHeroPosition(char direction) {
        switch (direction) {
            case 'n': {
                y--;
                break;
            }
            case 's': {
                y++;
                break;
            }
            case 'e': {
                x++;
                break;
            }
            case 'w': {
                x--;
                break;
            }
        }
    }

    private void updateHeroPosition() {
        x = this.mapSize / 2;
        y = this.mapSize / 2;

    }

    public void acceptInteraction() {
        interaction = interaction.interact(this);
        status = interaction == null ? "YOU WON THE BATTLE" : interaction.getInfo();
        if (hitPoints <= 0) {
            status = LevelEndType.LOST.getInfo();
            interaction = LevelEndType.LOST;
        }
        if (interaction == null && xp == level * 1000 + (level - 1) * (level - 1) * 450) {
            if (level == 10) {
                status = LevelEndType.WON_GAME.getInfo();
                interaction = LevelEndType.WON_GAME;
            } else {
                status = LevelEndType.WON_LEVEL.getInfo();
                interaction = LevelEndType.WON_LEVEL;
            }
        }
    }

    public void refuseInteraction() {
        interaction = interaction.avoid(this);
        status = interaction == null ? "YOU RUN AWAY" : interaction.getInfo();
        if (interaction == null && xp == level * 1000 + (level - 1) * (level - 1) * 450) {
            status = LevelEndType.WON_LEVEL.getInfo();
            interaction = LevelEndType.WON_LEVEL;
        }
    }


    public boolean fight(Villain villain) {
        cannotRun = false;
        int villainAttack = Math.max(villain.getAttack() - (clazz.getDefense() + getArmorEffect()), 0);
        while (true) {
            villain.setHitPoints(villain.getHitPoints() - (clazz.getAttack() + getWeaponEffect()));
            if (villain.getHitPoints() <= 0) {
                xp += villain.getXp();
                getVillains().remove(villain);
                return true;
            }
            int damage = resist(villainAttack);
            hitPoints -= damage;
            if (hitPoints <= 0) {
                return false;
            }
        }
    }

    private int resist(int attack) {
        int damageLeft = Math.max(attack - getHelmEffect(), 0);
        int effectLeft = getHelmEffect() - attack;
        if (effectLeft > 0) {
            inventory[HELM.ordinal()].setEffect(effectLeft);
        } else {
            inventory[HELM.ordinal()] = null;
        }
        return damageLeft;
    }

    public boolean run() {
        if (cannotRun) {
            return false;
        }
        boolean success = getRandom().nextInt(0, 10) % 2 == 0;
        if (success) {
            updateHeroPosition();
        } else {
            cannotRun = true;
        }
        return success;
    }

    public void takeArtefact(Artefact artefact) {
        inventory[artefact.getType().ordinal()] = artefact;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HeroClass getClazz() {
        return clazz;
    }

    public int getLevel() {
        return level;
    }

    public int getMapSize() {
        return mapSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Villain> getVillains() {
        return villains;
    }

    public int getXp() {
        return xp;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public boolean isCannotRun() {
        return cannotRun;
    }

    public Artefact[] getInventory() {
        return inventory;
    }

    public Interactive getInteraction() {
        return interaction;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVillains(List<Villain> villains) {
        this.villains = villains;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setCannotRun(boolean cannotRun) {
        this.cannotRun = cannotRun;
    }

    public void setInventory(Artefact[] inventory) {
        this.inventory = inventory;
    }

    public void setInteraction(Interactive interaction) {
        this.interaction = interaction;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Builder
    public static HeroBuilder builder() {
        return new HeroBuilder();
    }

    public static class HeroBuilder {
        private Integer id = null;
        private String name;
        private HeroClass clazz;
        private int level = 1;
        private int mapSize = 10;
        private int x = 5;
        private int y = 5;
        private List<Villain> villains = Villain.createVillains(10, 1);
        private int xp = 0;
        private int hitPoints = 10;
        private boolean cannotRun = false;
        private Artefact[] inventory = new Artefact[ArtefactType.values().length];
        private Interactive interaction = null;
        private String status = "";

        public HeroBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public HeroBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HeroBuilder clazz(HeroClass clazz) {
            this.clazz = clazz;
            return this;
        }

        public HeroBuilder level(int level) {
            this.level = level;
            return this;
        }

        public HeroBuilder mapSize(int mapSize) {
            this.mapSize = mapSize;
            return this;
        }

        public HeroBuilder x(int x) {
            this.x = x;
            return this;
        }

        public HeroBuilder y(int y) {
            this.y = y;
            return this;
        }

        public HeroBuilder villains(List<Villain> villains) {
            this.villains = villains;
            return this;
        }

        public HeroBuilder xp(int xp) {
            this.xp = xp;
            return this;
        }

        public HeroBuilder hitPoints(int hitPoints) {
            this.hitPoints = hitPoints;
            return this;
        }

        public HeroBuilder cannotRun(boolean cannotRun) {
            this.cannotRun = cannotRun;
            return this;
        }

        public HeroBuilder inventory(Artefact[] inventory) {
            this.inventory = inventory;
            return this;
        }

        public HeroBuilder interaction(Interactive interaction) {
            this.interaction = interaction;
            return this;
        }

        public HeroBuilder status(String status) {
            this.status = status;
            return this;
        }

        public Hero build() {
            return new Hero(id, name, clazz, level, mapSize, x, y, villains, xp, hitPoints, cannotRun, inventory, interaction, status);
        }
    }

    // ToString
    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clazz=" + clazz +
                ", level=" + level +
                ", mapSize=" + mapSize +
                ", x=" + x +
                ", y=" + y +
                ", villains=" + villains +
                ", xp=" + xp +
                ", hitPoints=" + hitPoints +
                ", cannotRun=" + cannotRun +
                ", inventory=" + java.util.Arrays.toString(inventory) +
                ", interaction=" + interaction +
                ", status='" + status + '\'' +
                '}';
    }

}
