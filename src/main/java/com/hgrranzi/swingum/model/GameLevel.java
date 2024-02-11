package com.hgrranzi.swingum.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    public void updateHeroPosition(char direction) {
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
        }
    }

    List<String> exploreArea() {
        List<String> events = new ArrayList<>();
        this.exploredArea[heroX][heroY] = true;
        objects.forEach(object -> {
            if (object.getPosX() == heroX && object.getPosY() == heroY) {
                events.add(object.interactWithHero());
            }
        });
        if (heroX == 0 || heroY == 0 || heroX == mapSize - 1 || heroY == mapSize - 1) {
            events.add("WIN LEVEL");
        }
        return events;
    }


}