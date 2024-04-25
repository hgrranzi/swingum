package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@Getter
@RequiredArgsConstructor
public class Artefact implements Interactive {

    private final ArtefactType type;

    private final int effect;

    public static Artefact createArtefact(int coefficient) {
        ArtefactType type = ArtefactType.values()[new Random().nextInt(ArtefactType.values().length)];
        int effect = coefficient / 4;
        return new Artefact(type, effect == 0 ? 1 : effect);
    }

    @Override
    public String getImageName() {
        return this.type.getImageName();
    }

    @Override
    public String getInfo() {
        return this.type.toString() + " " + this.effect;
    }

    @Override
    public List<String> getOptions() {
        return List.of("TAKE", "LEAVE");
    }

    @Override
    public Interactive interact(Hero hero) {
        hero.takeArtefact(this);
        return null;
    }
}
