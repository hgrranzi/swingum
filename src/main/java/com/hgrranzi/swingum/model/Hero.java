package com.hgrranzi.swingum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

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
    private int experience = 0;

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

    public void upgradeLevel() {
        this.experience++;
        gameLevel = new GameLevel(++level);
    }

    public void move(char direction) {
        gameLevel.updateHeroPosition(direction);
        gameLevel.getVillains().forEach(villain -> {
            if (villain.getPosX() == gameLevel.getHeroX() && villain.getPosY() == gameLevel.getHeroY()) {
                interaction = villain;
            }
        });
        if (gameLevel.getHeroX() == -1 || gameLevel.getHeroY() == -1
                || gameLevel.getHeroX() == gameLevel.getMapSize() || gameLevel.getHeroY() == gameLevel.getMapSize()) {
            interaction = LevelEndType.WON;
        }
    }

    public void acceptInteraction() {
        // verify if hero.getInteraction is of levelendtype
        interaction = interaction.interact(this);
        if (hitPoints <= 0) {
            interaction = LevelEndType.LOST;
        }
    }

    public void refuseInteraction() {
        interaction = interaction.avoid(this);
    }


    public boolean fight(Villain villain) {
        cannotRun = false;
        // todo: depending on luck hero attacks first or second
        int armorEffect = inventory[ArtefactType.ARMOR.ordinal()] == null ? 0 : inventory[ArtefactType.ARMOR.ordinal()].getEffect();
        int weaponEffect = inventory[ArtefactType.WEAPON.ordinal()] == null ? 0 : inventory[ArtefactType.WEAPON.ordinal()].getEffect();
        int defenceReserve = this.clazz.defense + armorEffect;
        while (true) {
            villain.setHitPoints(villain.getHitPoints() - (this.clazz.attack + weaponEffect));
            if (villain.getHitPoints() <= 0) {
                gameLevel.getVillains().remove(villain);
                return true;
            }

            int attack = defenceReserve > 0 ? Math.max(villain.getAttack() - defenceReserve, 0) : villain.getAttack();
            defenceReserve -= villain.getAttack();
            this.hitPoints -= attack;
            if (this.hitPoints <= 0) {
                return false;
            }
        }
    }

    public boolean run() {
        if (cannotRun) {
            return false;
        }
        boolean success = new Random().nextInt(0, 10) % 2 == 0;
        if (success) {
            gameLevel.updateHeroPosition();
        } else {
            cannotRun = true;
        }
        return success;
    }

    public void takeArtefact(Artefact artefact) {
        dropArtefact(artefact.getType());
        if (artefact.getType() == ArtefactType.HELM) {
            this.hitPoints += artefact.getEffect();
        }
        inventory[artefact.getType().ordinal()] = artefact;
    }

    public void dropArtefact(ArtefactType type) {
        int index = type.ordinal();
        if (inventory[index] == null) {
            return;
        }
        if (type == ArtefactType.HELM) {
            this.hitPoints = Math.max(hitPoints - inventory[index].getEffect(), 1);
        }
        inventory[index] = null;
    }

}
