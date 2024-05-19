package com.hgrranzi.swingum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.util.List;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;
import static com.hgrranzi.swingum.model.ArtefactType.*;

@Getter
@Setter
@Builder
@JsonIgnoreProperties({"interaction", "mapSize", "cannotRun"})
@ToString
public class Hero {

    @Builder.Default
    @Positive
    private Integer id = null;

    @NotBlank
    @Size(min = 3, max = 16)
    private String name;

    private final HeroClass clazz;

    @Builder.Default
    @Range(min = 1, max = 10)
    private int level = 1;

    @Builder.Default
    private int mapSize = 10;

    @Builder.Default
    private int x = 5;

    @Builder.Default
    private int y = 5;

    @Builder.Default
    private List<Villain> villains = Villain.createVillains(10, 1);

    @Builder.Default
    @Positive
    private int xp = 0;

    @Builder.Default
    @Range(min = 1, max = 10)
    private int hitPoints = 10;

    @Builder.Default
    boolean cannotRun = false;

    @Builder.Default
    @Size(max = 3)
    @Valid
    private Artefact[] inventory = new Artefact[ArtefactType.values().length];

    @Builder.Default
    private Interactive interaction = null;

    @Builder.Default
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

}
