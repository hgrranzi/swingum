package com.hgrranzi.swingum.model;

import java.util.List;

public class LevelMap {
    private final int size;
    private int heroX;
    private int heroY;
    private final List<Villain> villains;

    public LevelMap(int level) {
        this.size = (level - 1) * 5 + 10;
        this.heroX = this.size / 2;
        this.heroY = this.size / 2;
        villains = Villain.createVillains(this.size);
    }

    public boolean moveHero(char direction) {
        switch (direction) {
            case 'n': heroY--; break;
            case 's': heroY++; break;
            case 'e': heroX++; break;
            case 'w': heroX--; break;
            default: return false; // Invalid direction
        }
        // todo: check if villain is present
        // Check for win condition: reaching any border of the map, hero moves in this level while true
        return heroX != 0 && heroX != size - 1 && heroY != 0 && heroY != size - 1;
    }


}

