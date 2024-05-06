package com.hgrranzi.swingum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;
import static com.hgrranzi.swingum.model.ArtefactType.*;

@Getter
@Setter
@Builder
public class Hero {

    @Builder.Default
    private Integer id = null;

    private final String name;

    private final HeroClass clazz;

    @Builder.Default
    private int level = 1;

    @Builder.Default
    private int xp = 0;

    @Builder.Default
    private int hitPoints = 10;

    @Builder.Default
    boolean cannotRun = false;

    @Builder.Default
    private final Artefact[] inventory = new Artefact[ArtefactType.values().length];

    @Builder.Default
    private GameLevel gameLevel = new GameLevel(1);

    @Builder.Default
    private Interactive interaction = null;

    @Builder.Default
    private String status = "";

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
