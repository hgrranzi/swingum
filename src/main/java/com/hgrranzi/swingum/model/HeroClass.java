package com.hgrranzi.swingum.model;

import com.hgrranzi.swingum.view.SwingumException;

public enum HeroClass {
    DOTNET_BOT("dotnet.png", 4, 2),
    GOFER("go.png", 2, 4),
    DUKE("java.png", 3, 3),
    ELEPHANT("php.png", 5, 1),
    FERRIS("rust.png", 1, 5);

    private final String imageName;

    public final int attack;

    public final int defense;

    HeroClass(String imageName, int attack, int defense) {
        this.imageName = imageName;
        this.attack = attack;
        this.defense = defense;
    }

    public String getImageName() {
        return imageName;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public static HeroClass getClassByName(String name) {
        if (name == null || name.isEmpty())
            name = "WITHOUT NAME";
        try {
            return HeroClass.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SwingumException("No hero class * " + name + " * available.");
        }
    }

}
