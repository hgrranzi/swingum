package com.hgrranzi.swingum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

import java.util.List;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;

public class Artefact implements Interactive {

    private final ArtefactType type;

    @Positive
    private int effect;

    @JsonCreator
    public Artefact(@JsonProperty("type") ArtefactType type, @JsonProperty("effect") int effect) {
        this.type = type;
        this.effect = effect;
    }

    public static Artefact createArtefact(int coefficient) {
        ArtefactType type = ArtefactType.values()[getRandom().nextInt(ArtefactType.values().length)];
        int effect = coefficient / 4;
        return new Artefact(type, effect == 0 ? 1 : effect);
    }

    @Override
    public String getImageName() {
        return this.type.getImageName();
    }

    @Override
    public String getInfo() {
        return this.type.toString() + " | effect: " + this.effect;
    }

    @Override
    public List<String> getOptions() {
        return List.of("TAKE", "LEAVE");
    }

    @Override
    public Interactive interact(Hero hero) {
        hero.takeArtefact(this);
        return null;
    }

    // Getters
    public ArtefactType getType() {
        return type;
    }

    public int getEffect() {
        return effect;
    }

    // Setter
    public void setEffect(int effect) {
        this.effect = effect;
    }
}
