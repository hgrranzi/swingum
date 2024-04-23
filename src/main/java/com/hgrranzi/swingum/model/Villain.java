package com.hgrranzi.swingum.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Villain implements Interactive {

    private final VillainType type;

    private final int posX;

    private final int posY;

    private final int attack;

    @Setter
    private int hitPoints;

    private final Artefact artefact;

    public static Villain createVillain(int start, int end, int mapSize) {
        Random random = new Random();
        VillainType type = VillainType.values()[random.nextInt(VillainType.values().length)];
        int posX, posY;
        do {
            posX = random.nextInt(0, mapSize);
            posY = random.nextInt(start, end);
        } while (posX == mapSize / 2 && posY == mapSize / 2);
        int attack = 1; // todo: Randomize or determine based on mapSize;
        int hitPoints = 1; // todo: Randomize or determine based on mapSize;
        Artefact artefact = random.nextInt() % 2 == 0 ? null : Artefact.createArtefact();
        return new Villain(type, posX, posY, attack, hitPoints, artefact);
    }

    public static List<Villain> createVillains(int mapSize) {
        List<Villain> villains = new ArrayList<>();

        int numberOfVillains = mapSize * mapSize / 10;
        int sectionSize = mapSize / numberOfVillains;
        int startOfSection = 0;
        // todo: determine section size
        for (int i = 0; i < numberOfVillains; i++) {
            villains.add(createVillain(startOfSection, startOfSection + sectionSize, mapSize));
            startOfSection += sectionSize;
        }
        return villains;
    }

    @Override
    public String getImageName() {
        return this.type.getImageName();
    }

    @Override
    public String getInfo() {
        return this.type.toString() + " " + this.attack + " " + this.hitPoints;
    }

    @Override
    public List<String> getOptions() {
        return List.of("FIGHT", "RUN");
    }

    @Override
    public Interactive interact(Hero hero) {
        boolean heroWins = hero.fight(this);
        if (heroWins) {
            return artefact;
        }
        return null;
    }

    @Override
    public Interactive avoid(Hero hero) {
        if (hero.run()) {
            return null;
        }
        return this;
    }
}
