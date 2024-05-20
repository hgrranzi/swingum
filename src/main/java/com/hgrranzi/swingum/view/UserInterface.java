package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;

import java.util.List;

public interface UserInterface {

    void setWelcomeView(GameController controller);

    void setNewGameView(GameController controller, HeroClass[] heroClasses);

    void setLoadGameView(GameController controller, List<String> heroNames);

    void setGameView(GameController controller, Hero hero);

    void refreshView();

    void closeFrame();
}
