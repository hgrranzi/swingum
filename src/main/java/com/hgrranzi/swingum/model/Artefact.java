package com.hgrranzi.swingum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.hgrranzi.swingum.config.ApplicationConfig.getRandom;

@Getter
@AllArgsConstructor
public class Artefact implements Interactive {

    private final ArtefactType type;

    @Setter
    private int effect;

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
}
