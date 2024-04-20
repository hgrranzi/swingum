package com.hgrranzi.swingum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GameLevel {

    private final int mapSize;
    private int heroX;
    private int heroY;
    private final List<Villain> villains;

    public GameLevel(int level) {
        this.mapSize = (level - 1) * 5 + 10;
        this.heroX = this.mapSize / 2;
        this.heroY = this.mapSize / 2;
        villains = Villain.createVillains(mapSize);
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

    List<Interactive> exploreArea() {
        List<Interactive> events = new ArrayList<>();
        villains.forEach(villain -> {
            if (villain.getPosX() == heroX && villain.getPosY() == heroY) {
                events.add(villain);
            }
        });
        if (heroX == -1 || heroY == -1 || heroX == mapSize || heroY == mapSize) {
            events.add(LevelEndType.WON);
        }
        return events;
    }


}