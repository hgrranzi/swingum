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

    private final int xp;

    private static Villain createVillain(int posX, int posY, int xp) {
        Random random = new Random();
        VillainType type = VillainType.values()[random.nextInt(VillainType.values().length)];
        int attack = random.nextInt(xp / 100 + 1, xp / 25 + 2);
        int hitPoints = random.nextInt(xp / 40 + 1, xp / 20 + 2) ;

        Artefact artefact = random.nextBoolean() ? Artefact.createArtefact(attack + hitPoints) : null;

        return new Villain(type, posX, posY, attack, hitPoints, artefact, xp);
    }

    public static List<Villain> createVillains(int mapSize, int level) {
        List<Villain> villains = new ArrayList<>();
        int numberOfVillains = mapSize * mapSize / 10;
        HashSet<String> occupiedPositions = new HashSet<>();
        Random random = new Random();
        int previousXpSum = level == 1 ? 0 : (level - 1) * 1000 + (level - 2) * (level - 2) * 450;
        int xpSum = level * 1000 + (level - 1) * (level - 1) * 450 - previousXpSum;

        while (villains.size() < numberOfVillains) {
            int posX = random.nextInt(0, mapSize);
            int posY = random.nextInt(0, mapSize);
            String positionKey = posX + "," + posY;

            if ((posX != mapSize / 2 || posY != mapSize / 2) && occupiedPositions.add(positionKey)) {
                int xp;
                if (villains.size() == numberOfVillains - 1) {
                    xp = xpSum;
                } else {
                    xp = xpSum / (level * 5) + 1;
                }
                xpSum -= xp;
                Villain villain = createVillain(posX, posY, xp);
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
        return this.type.toString() + " | attack: " + this.attack + " | hit points: " + this.hitPoints;
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
