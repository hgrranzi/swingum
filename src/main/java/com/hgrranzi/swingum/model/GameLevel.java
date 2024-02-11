package com.hgrranzi.swingum.model;

import java.util.List;

public class GameLevel {
    private final int mapSize;
    private int heroX;
    private int heroY;
    private boolean[][] exploredArea;
    private final List<GameObject> objects;

    public GameLevel(int level) {
        this.mapSize = (level - 1) * 5 + 10;
        this.heroX = this.mapSize / 2;
        this.heroY = this.mapSize / 2;
        this.exploredArea = new boolean[mapSize][mapSize];
        this.exploredArea[heroX][heroY] = true;
        objects = GameObject.createObjects(mapSize);
    }

    public boolean moveHero(char direction) {
        switch (direction) {
            case 'n': {
                heroY--;
                break;
            }
            case 's': {
                heroY++;
                break;
            }
            case 'e': {
                heroX++;
                break;
            }
            case 'w': {
                heroX--;
                break;
            }
            default:
                return false; // Invalid direction
        }
        // todo: check if object is present
        objects.forEach(object -> {
            if (object.getPosX() == heroX && object.getPosY() == heroY) {
                object.interactWithHero(); // todo: logic for
            }
        });
        this.exploredArea[heroX][heroY] = true;
        // Check for win condition: reaching any border of the map, hero moves in this level while true
        return heroX != 0 && heroX != mapSize - 1 && heroY != 0 && heroY != mapSize - 1;
    }

}