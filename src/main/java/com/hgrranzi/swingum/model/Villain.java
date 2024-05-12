package com.hgrranzi.swingum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;

@Getter
@ToString
public class Villain implements Interactive {

    private final VillainType type;

    private final int posX;

    private final int posY;

    private final int attack;

    @Setter
    private int hitPoints;

    private final Artefact artefact;

    private final int xp;

    @JsonCreator
    public Villain(@JsonProperty("type") VillainType type,
                   @JsonProperty("posX") int posX,
                   @JsonProperty("posY") int posY,
                   @JsonProperty("attack") int attack,
                   @JsonProperty("hitPoints") int hitPoints,
                   @JsonProperty("artefact") Artefact artefact,
                   @JsonProperty("xp") int xp) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.attack = attack;
        this.hitPoints = hitPoints;
        this.artefact = artefact;
        this.xp = xp;
    }

    private static Villain createVillain(int posX, int posY, int xp) {
        VillainType type = VillainType.values()[getRandom().nextInt(VillainType.values().length)];
        int attack = getRandom().nextInt(xp / 100 + 1, xp / 25 + 2);
        int hitPoints = getRandom().nextInt(xp / 40 + 1, xp / 20 + 2);

        Artefact artefact = getRandom().nextBoolean() ? Artefact.createArtefact(attack + hitPoints) : null;

        return new Villain(type, posX, posY, attack, hitPoints, artefact, xp);
    }

    public static List<Villain> createVillains(int mapSize, int level) {
        List<Villain> villains = new ArrayList<>();
        int numberOfVillains = mapSize * mapSize / 10;
        HashSet<String> occupiedPositions = new HashSet<>();
        int previousXpSum = level == 1 ? 0 : (level - 1) * 1000 + (level - 2) * (level - 2) * 450;
        int xpSum = level * 1000 + (level - 1) * (level - 1) * 450 - previousXpSum;

        while (villains.size() < numberOfVillains) {
            int posX = getRandom().nextInt(0, mapSize);
            int posY = getRandom().nextInt(0, mapSize);
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
