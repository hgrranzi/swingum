package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum LevelEndType implements Interactive {

    WON_LEVEL("won.png", "YOU WON THE LEVEL", List.of("NEXT LEVEL")),
    WON_GAME("won.png", "YOU WON THE GAME", List.of("MAIN MENU")),
    LOST("lost.png", "YOU LOST", List.of("MAIN MENU"));

    private final String imageName;

    private final String info;

    private final List<String> options;

    @Override
    public Interactive interact(Hero hero) {
        return null;
    }
}
