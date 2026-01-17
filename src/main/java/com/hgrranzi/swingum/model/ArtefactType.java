package com.hgrranzi.swingum.model;

public enum ArtefactType {

    WEAPON("weapon.png"),
    ARMOR("armor.png"),
    HELM("helm.png");

    private final String imageName;

    ArtefactType(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
