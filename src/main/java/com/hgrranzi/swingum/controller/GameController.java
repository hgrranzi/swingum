package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.model.LevelEndType;
import com.hgrranzi.swingum.persistence.service.PersistenceService;
import com.hgrranzi.swingum.view.*;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;
import com.hgrranzi.swingum.view.UserInterface;

import static com.hgrranzi.swingum.model.LevelEndType.WON_LEVEL;
import static com.hgrranzi.swingum.persistence.service.HeroMapper.validate;

public class GameController {

    private final PersistenceService persistenceService;

    private UserInterface userInterface;

    private Hero hero;

    public GameController(UserInterface userInterface) {
        this.userInterface = userInterface;
        persistenceService = new PersistenceService();
    }

    public void switchView(String viewName) {
        switch (viewName) {
            case "WelcomeView":
                userInterface.setWelcomeView(this);
                break;
            case "NewGameView":
                userInterface.setNewGameView(this, HeroClass.values()); // string?
                break;
            case "LoadGameView":
                userInterface.setLoadGameView(this, persistenceService.loadHeroNames());
                break;
            default:
                userInterface.refreshView();
        }
    }

    public void switchUserInterface() {
        if (userInterface instanceof ConsoleFrame) {
            userInterface = new GuiFrame();
        } else {
            userInterface.closeFrame();
            userInterface = new ConsoleFrame();
        }
        switchView("WelcomeView");
    }

    public void newGame(String name, String heroClass) {
        try {
            if (!persistenceService.isHeroNameAvailable(name)) {
                throw new SwingumException("Hero name already in use");
            }
            hero = Hero.builder()
                       .name(name)
                       .clazz(HeroClass.getClassByName(heroClass))
                       .build();
            validate(hero);
            userInterface.setGameView(this, hero);
        } catch (SwingumException e) {
            userInterface.displayError(e.getMessage());
        }
    }

    public void loadGame(String name) {
        try {
            hero = persistenceService.loadHero(name);
            userInterface.setGameView(this, hero);
        } catch (SwingumException e) {
            userInterface.displayError(e.getMessage());
        }
    }

    public void moveHero(char direction) {
        hero.move(direction);
        userInterface.refreshView();
    }

    public void processAcceptInteraction() {
        if (hero.getInteraction() instanceof LevelEndType) {
            processLevelEnd((LevelEndType) hero.getInteraction());
        }
        hero.acceptInteraction();
        userInterface.refreshView();
    }

    private void processLevelEnd(LevelEndType type) {
        if (type == WON_LEVEL) {
            hero.upgradeLevel();
            userInterface.refreshView();
        } else {
            switchView("WelcomeView");
        }
    }

    public void processRefuseInteraction() {
        hero.refuseInteraction();
        userInterface.refreshView();
    }

    public void saveGame() {
        Integer heroId = persistenceService.saveHero(hero);
        if (hero.getId() == null) {
            hero.setId(heroId);
        }
        System.out.println("Game saved");
    }
}