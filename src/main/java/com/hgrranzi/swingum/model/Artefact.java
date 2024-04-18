package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@Getter
@RequiredArgsConstructor
public class Artefact {

    private final ArtefactType type;

    private final int effect;

    public static Artefact createArtefact() {
        ArtefactType type = ArtefactType.values()[new Random().nextInt(ArtefactType.values().length)];
        int effect = 1; // todo: Randomize or determine based on villain;
        return new Artefact(type, effect);
    }
}
