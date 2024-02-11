package com.hgrranzi.swingum.model;

import lombok.Getter;

import java.util.Random;

@Getter
public class Villain extends GameObject {

    private final int attack;
    private int hitPoints;

    private Villain(int attack, int hitPoints, int posX, int posY) {
        super(posX, posY);
        this.attack = attack;
        this.hitPoints = hitPoints;
    }

    public static Villain createVillain(int mapSize) {
        Random random = new Random();
        int posX, posY;
        do {
            posX = random.nextInt(mapSize);
            posY = random.nextInt(mapSize);
        } while (posX == mapSize / 2 && posY == mapSize / 2);
        int attack = 1; // todo: Randomize or determine based on mapSize;
        int hitPoints = 1; // todo: Randomize or determine based on mapSize;
        return new Villain(attack, hitPoints, posX, posY);
    }

    @Override
    void interactWithHero() {
        System.out.println("Encountered a villain!");
    }
}
