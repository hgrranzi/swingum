package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum LevelEndType implements Interactive {

    WON("won.png", "YOU WON THE LEVEL", List.of("NEXT LEVEL")),
    LOST("lost.png", "YOU LOST", List.of("MAIN MENU"));

    private final String imageName;

    private final String info;

    private final List<String> interactions;

}
