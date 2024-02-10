package com.hgrranzi.swingum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Artefact {

    private String name;
    private ArtefactType type;
    private int effect;

}
