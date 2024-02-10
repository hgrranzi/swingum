package com.hgrranzi.swingum.model;

import lombok.Getter;

@Getter
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

    public boolean takeArtefact(Artefact artefact) {
        int index = artefact.getType().ordinal();
        if (inventory[index] != null) {
            return false;
        }
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
        inventory[index] = artefact;
        return true;
    }

    public boolean dropArtefact(ArtefactType type) {
        int index = type.ordinal();
        if (inventory[index] == null) {
            return false;
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
        return true;
    }

}
