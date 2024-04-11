package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.persistence.service.PersistenceService;
import com.hgrranzi.swingum.view.GameView;
import com.hgrranzi.swingum.view.SwingumException;
import lombok.Getter;

public class GameController {

    private final PersistenceService persistenceService;

    @Getter
    private final GameView gameView;

    private Hero hero;

    private GameController(PersistenceService persistenceService, ViewController viewController, Hero hero) {
        this.persistenceService = persistenceService;
        this.hero = hero;
        this.gameView = new GameView(viewController, this, hero.getGameLevel());
    }

    public static GameController createGame(ViewController viewController, Hero hero) {
        PersistenceService persistenceService = new PersistenceService();
        if (!persistenceService.isHeroNameAvailable(hero.getName())) {
            throw new SwingumException("Hero name already in use");
        }
        return new GameController(persistenceService, viewController, hero);
    }

    public void moveHero(char direction) {
        hero.move(direction);
        gameView.repaint();
    }

    public void saveGame() {
        persistenceService.saveHero(hero);
        System.out.println("Game saved");
    }
}
