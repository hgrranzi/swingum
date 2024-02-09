package com.hgrranzi.swingum.model;

public enum HeroClass {
    CLASS1(4, 1, 0),
    CLASS2(1, 3, 1),
    CLASS3(1, 1, 3),
    CLASS4(2, 2, 1),
    CLASS5(2, 3, 0);

    public final int attack;
    public final int defense;
    public final int luck;

    HeroClass(int attack, int defense, int luck) {
        this.attack = attack;
        this.defense = defense;
        this.luck = luck;
    }
}
