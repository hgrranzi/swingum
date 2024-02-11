package com.hgrranzi.swingum.model;

import lombok.Getter;

import java.util.Random;

@Getter
public class Artefact extends GameObject {

    private final ArtefactType type;
    private final int effect;

    private Artefact(ArtefactType type, int effect, int posX, int posY) {
        super(posX, posY);
        this.type = type;
        this.effect = effect;
    }

    public static Artefact createArtefact(int mapSize) {
        Random random = new Random();
        int posX;
        int posY;
        do {
            posX = random.nextInt(mapSize);
            posY = random.nextInt(mapSize);
        } while (posX == mapSize / 2 && posY == mapSize / 2);
        ArtefactType type = ArtefactType.WEAPON; // todo: Randomize or determine based on mapSize;
        int effect = 1; // todo: Randomize or determine based on mapSize;
        return new Artefact(type, effect, posX, posY);

    }

    @Override
    String interactWithHero() {
        return ("ARTIFACT");
    }
}
