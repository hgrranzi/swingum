package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HeroClass {
    DOTNET_BOT("dotnet.png", 4, 1, 0),
    GOFER("go.png", 1, 3, 1),
    DUKE("java.png", 1, 1, 3),
    ELEPHANT("php.png", 2, 2, 1),
    FERRIS("rust.png", 2, 3, 0);

    @Getter
    private final String imageName;

    public final int attack;

    public final int defense;

    public final int luck;

}
