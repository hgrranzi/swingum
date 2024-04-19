package com.hgrranzi.swingum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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
    private final Artefact[] inventory = new Artefact[ArtefactType.values().length];

    @Builder.Default
    private GameLevel gameLevel = new GameLevel(1);

    @Builder.Default
    private List<Interactive> interactions = List.of();

    public void upgradeLevel() {
        interactions.clear();
        this.experience++;
        gameLevel = new GameLevel(++level);
    }

    public void move(char direction) {
        gameLevel.updateHeroPosition(direction);
        interactions = gameLevel.exploreArea();
    }


    public boolean fight(Villain villain) {
        // todo: depending on luck hero attacks first or second
        int defenceReserve = this.clazz.defense + inventory[ArtefactType.ARMOR.ordinal()].getEffect();
        while (true) {
            villain.setHitPoints(villain.getHitPoints() -
                                     (this.clazz.attack + inventory[ArtefactType.WEAPON.ordinal()].getEffect()));
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
        boolean success = new Random().nextBoolean();
        if (success) {
            // todo: randomly move hero to one of the explored area
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
