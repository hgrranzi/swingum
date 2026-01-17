package com.hgrranzi.swingum.model;

import java.util.List;

public enum LevelEndType implements Interactive {

    WON_LEVEL("won.png", "YOU WON THE LEVEL", List.of("NEXT")),
    WON_GAME("won.png", "YOU WON THE GAME", List.of("MENU")),
    LOST("lost.png", "YOU LOST", List.of("MENU"));

    private final String imageName;

    private final String info;

    private final List<String> options;

    LevelEndType(String imageName, String info, List<String> options) {
        this.imageName = imageName;
        this.info = info;
        this.options = options;
    }

    public String getImageName() {
        return imageName;
    }

    public String getInfo() {
        return info;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public Interactive interact(Hero hero) {
        return null;
    }
}
