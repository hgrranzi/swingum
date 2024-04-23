package com.hgrranzi.swingum.model;

import java.util.List;

public interface Interactive {

    String getImageName();

    String getInfo();

    List<String> getOptions();

    Interactive interact(Hero hero);

    default Interactive avoid(Hero hero) {
        return null;
    }

}
