package com.hgrranzi.swingum.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class GameObject {

    protected final int posX;
    protected final int posY;

    public static List<GameObject> createObjects(int mapSize) {
        List<GameObject> objects = new ArrayList<>();

        int numberOfVillains = 6; // todo: Determine based on mapSize;
        for (int i = 0; i < numberOfVillains; i++) {
            objects.add(Villain.createVillain(mapSize));
        }

        int numberOfArtefacts = 5; // todo: Determine based on mapSize;
        for (int i = 0; i < numberOfArtefacts; i++) {
            objects.add(Artefact.createArtefact(mapSize));
        }

        return objects;
    }

    abstract String interactWithHero();

}
