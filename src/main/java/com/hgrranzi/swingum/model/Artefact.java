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

    public static Artefact createArtefact() {
        ArtefactType type = ArtefactType.values()[new Random().nextInt(ArtefactType.values().length)];
        int effect = 1; // todo: Randomize or determine based on villain;
        return new Artefact(type, effect);
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
    public List<String> getInteractions() {
        return List.of("TAKE", "LEAVE");
    }
}
