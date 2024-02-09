package com.hgrranzi.swingum.model;

public class Hero {

    private final String name;
    private final HeroClass clazz;
    private int level;
    private int experience;
    private int attack;
    private int defense;
    private int luck;
    private int hitPoints;

    public Hero(String name, HeroClass clazz) {
        this.name = name;
        this.clazz = clazz;
        this.level = 1;
        this.experience = 0;
        this.attack = clazz.attack;
        this.defense = clazz.defense;
        this.luck = clazz.luck;
        this.hitPoints = 10;
    }
}
