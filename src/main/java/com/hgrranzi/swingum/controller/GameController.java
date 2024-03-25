package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.gui.GameView;
import lombok.Getter;

public class GameController {

    @Getter
    private final GameView gameView;
    private Hero hero;

    public GameController(ViewController viewController) {
        hero = new Hero("lol", HeroClass.CLASS1);
        this.gameView = new GameView(viewController, this, hero.getGameLevel());
    }

    public void moveHero(char direction) {
        hero.move(direction);
        gameView.repaint();
    }

    public void saveGame() {
        //todo: save game state
        System.out.println("Game saved");
    }
}
