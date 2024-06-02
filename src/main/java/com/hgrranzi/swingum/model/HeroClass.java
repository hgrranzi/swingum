package com.hgrranzi.swingum.model;

import com.hgrranzi.swingum.view.SwingumException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HeroClass {
    DOTNET_BOT("dotnet.png", 4, 2),
    GOFER("go.png", 2, 4),
    DUKE("java.png", 3, 3),
    ELEPHANT("php.png", 5, 1),
    FERRIS("rust.png", 1, 5);

    private final String imageName;

    public final int attack;

    public final int defense;

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
