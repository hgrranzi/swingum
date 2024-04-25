package com.hgrranzi.swingum.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
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

    private static Villain createVillain(int posX, int posY, int mapSize) {
        Random random = new Random();
        VillainType type = VillainType.values()[random.nextInt(VillainType.values().length)];
        int attack = random.nextInt(1, mapSize / 2);
        int hitPoints = random.nextInt(1, mapSize / 4 * 3);

        Artefact artefact = random.nextBoolean() ? Artefact.createArtefact(attack + hitPoints) : null;

        return new Villain(type, posX, posY, attack, hitPoints, artefact);
    }

    public static List<Villain> createVillains(int mapSize) {
        List<Villain> villains = new ArrayList<>();
        int numberOfVillains = mapSize * mapSize / 10;
        HashSet<String> occupiedPositions = new HashSet<>();
        Random random = new Random();

        while (villains.size() < numberOfVillains) {
            int posX = random.nextInt(0, mapSize);
            int posY = random.nextInt(0, mapSize);
            String positionKey = posX + "," + posY;

            if ((posX != mapSize / 2 || posY != mapSize / 2) && occupiedPositions.add(positionKey)) {
                Villain villain = createVillain(posX, posY, mapSize);
                villains.add(villain);
            }
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
