package com.hgrranzi.swingum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;
import static com.hgrranzi.swingum.model.ArtefactType.*;

@Getter
@Setter
@Builder
public class Hero {

    @Builder.Default
    @Min(1)
    private Integer id = null;

    @NotBlank
    @Size(min = 4, max = 16)
    private String name;

    private final HeroClass clazz; // todo: custom verify

    @Builder.Default
    @Range(min = 1, max = 10)
    private int level = 1;

    @Builder.Default
    private int xp = 0; // xp >= level * 1000 + (level - 1) * (level - 1) * 450 && xp < (level + 1) * 1000 + level * level * 450

    @Builder.Default
    @Range(min = 1, max = 10)
    private int hitPoints = 10;

    @Builder.Default
    boolean cannotRun = false;

    @Builder.Default
    @Size(max = 3)
    private Artefact[] inventory = new Artefact[ArtefactType.values().length]; // valid Artefacts

    @Builder.Default
    private GameLevel gameLevel = new GameLevel(1); // valid GameLevel

    @Builder.Default
    private Interactive interaction = null; // valid Interactive

    @Builder.Default
    @NotNull
    private String status = ""; // on of the list of possible statuses (need to create that list)

    @JsonCreator
    public Hero(@JsonProperty("id") Integer id,
                @JsonProperty("name") String name,
                @JsonProperty("clazz") HeroClass clazz,
                @JsonProperty("level") int level,
                @JsonProperty("xp") int xp,
                @JsonProperty("hitPoints") int hitPoints,
                @JsonProperty("cannotRun") boolean cannotRun,
                @JsonProperty("inventory") Artefact[] inventory,
                @JsonProperty("gameLevel") GameLevel gameLevel,
                @JsonProperty("interaction") Interactive interaction,
                @JsonProperty("status") String status) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.level = level;
        this.xp = xp;
        this.hitPoints = hitPoints;
        this.cannotRun = cannotRun;
        this.inventory = inventory;
        this.gameLevel = gameLevel;
        this.interaction = interaction;
        this.status = status;
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
        gameLevel = new GameLevel(++level);
    }

    public void move(char direction) {
        status = "GO";
        gameLevel.updateHeroPosition(direction);
        gameLevel.getVillains().forEach(villain -> {
            if (villain.getPosX() == gameLevel.getHeroX() && villain.getPosY() == gameLevel.getHeroY()) {
                status = villain.getInfo();
                interaction = villain;
            }
        });
        if (gameLevel.getHeroX() == -1 || gameLevel.getHeroY() == -1
                || gameLevel.getHeroX() == gameLevel.getMapSize() || gameLevel.getHeroY() == gameLevel.getMapSize()) {
            if (level == 10) {
                status = LevelEndType.WON_GAME.getInfo();
                interaction = LevelEndType.WON_GAME;
            } else {
                status = LevelEndType.WON_LEVEL.getInfo();
                interaction = LevelEndType.WON_LEVEL;
            }
        }
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
                gameLevel.getVillains().remove(villain);
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
            gameLevel.updateHeroPosition();
        } else {
            cannotRun = true;
        }
        return success;
    }

    public void takeArtefact(Artefact artefact) {
        inventory[artefact.getType().ordinal()] = artefact;
    }

}
