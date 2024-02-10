package com.hgrranzi.swingum.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Villain {

    private final int attack;
    private int hitPoints;
    private int posX;
    private int posY;

    public static List<Villain> createVillains(int mapSize) {
        List<Villain> villains = new ArrayList<>();
        Random random = new Random();
        int numberOfVillains = 5; // todo: Determine based on mapSize or a fixed number;

        for (int i = 0; i < numberOfVillains; i++) {
            int posX, posY;
            do {
                posX = random.nextInt(mapSize);
                posY = random.nextInt(mapSize);
            } while (posX == mapSize / 2 && posY == mapSize / 2); // Avoid placing a villain on the hero's start position

            int attack = 1; // todo: Randomize or determine based on level;
            int hitPoints = 1; // todo: Randomize or determine based on level;
            villains.add(new Villain(attack, hitPoints, posX, posY));
        }
        return villains;
    }

}
