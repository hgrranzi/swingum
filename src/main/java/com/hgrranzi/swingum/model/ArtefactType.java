package com.hgrranzi.swingum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArtefactType {

    WEAPON("weapon.png"),
    ARMOR("armor.png"),
    HELM("helm.png");

    private final String imageName;
}
