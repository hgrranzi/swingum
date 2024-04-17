package com.hgrranzi.swingum.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Villain {

    private final int posX;
    private final int posY;
    private final int attack;
    private int hitPoints;
    private final Artefact artefact;

    public static Villain createVillain(int mapSize) {
        Random random = new Random();
        int posX, posY;
        do {
            posX = random.nextInt(mapSize);
            posY = random.nextInt(mapSize);
        } while (posX == mapSize / 2 && posY == mapSize / 2);
        int attack = 1; // todo: Randomize or determine based on mapSize;
        int hitPoints = 1; // todo: Randomize or determine based on mapSize;
        Artefact artefact = random.nextInt() % 2 == 0 ? null : Artefact.createArtefact();
        return new Villain(attack, hitPoints, posX, posY, artefact);
    }

    public static List<Villain> createVillains(int mapSize) {
        List<Villain> villains = new ArrayList<>();

        int numberOfVillains = 6; // todo: Determine based on mapSize;
        for (int i = 0; i < numberOfVillains; i++) {
            villains.add(createVillain(mapSize));
        }
        return villains;
    }

}
