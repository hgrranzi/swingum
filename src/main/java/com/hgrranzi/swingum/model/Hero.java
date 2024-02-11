package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Hero {

    private final String name;
    private final HeroClass clazz;
    private int level;
    private int experience;
    private int attack;
    private int defense;
    private final int luck;
    private int hitPoints;
    private final Artefact[] inventory;
    private GameLevel gameLevel;
    private List<String> events;

    public Hero(String name, HeroClass clazz) {
        this.name = name;
        this.clazz = clazz;
        this.level = 1;
        this.experience = 0;
        this.attack = clazz.attack;
        this.defense = clazz.defense;
        this.luck = clazz.luck;
        this.hitPoints = 10;
        this.inventory = new Artefact[ArtefactType.values().length];
    }

    public void upgradeLevel() {
        events.clear();
        this.experience++;
        gameLevel = new GameLevel(++level);
    }

    public void move(char direction) {
        gameLevel.updateHeroPosition(direction);
        events = gameLevel.exploreArea();
    }


    public boolean fight(Villain villain) {
        // todo: depending on luck hero attacks first or second
        int defenceReserve = this.defense;
        while (true) {
            villain.setHitPoints(villain.getHitPoints() - this.attack);
            if (villain.getHitPoints() <= 0) {
                gameLevel.getObjects().remove(villain);
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
        switch (artefact.getType()) {
            case WEAPON: {
                this.attack += artefact.getEffect();
                break;
            }
            case ARMOR: {
                this.defense += artefact.getEffect();
                break;
            }
            case HELM: {
                this.hitPoints += artefact.getEffect();
                break;
            }
        }
        inventory[artefact.getType().ordinal()] = artefact;
    }

    public void dropArtefact(ArtefactType type) {
        int index = type.ordinal();
        if (inventory[index] == null) {
            return;
        }
        switch (type) {
            case WEAPON: {
                this.attack -= inventory[index].getEffect();
                break;
            }
            case ARMOR: {
                this.defense -= inventory[index].getEffect();
                break;
            }
            case HELM: {
                this.hitPoints = Math.max(hitPoints - inventory[index].getEffect(), 1);
                break;
            }
        }
        inventory[index] = null;
    }

}
