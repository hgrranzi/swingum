package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VillainType {

    BUG_BEETLE("bug_beetle.png"),

    BURNOUT("burnout.png"),

    LEGACY_GHOST("legacy_ghost.png"),

    MEMORY_LEACH("memory_leach.png");


    private final String imageName;

}
