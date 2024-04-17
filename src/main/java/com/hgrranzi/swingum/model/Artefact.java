package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Artefact {

    private final ArtefactType type;
    private final int effect;

    public static Artefact createArtefact() {
        ArtefactType type = ArtefactType.WEAPON; // todo: Randomize or determine based on villain;
        int effect = 1; // todo: Randomize or determine based on villain;
        return new Artefact(type, effect);

    }
}
