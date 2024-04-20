package com.hgrranzi.swingum.controller;

import com.hgrranzi.swingum.model.Hero;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.persistence.service.PersistenceService;
import com.hgrranzi.swingum.view.BaseView;
import com.hgrranzi.swingum.view.GameView;
import com.hgrranzi.swingum.view.LoadGameView;
import com.hgrranzi.swingum.view.NewGameView;
import com.hgrranzi.swingum.view.SwingumException;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.WelcomeView;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;

public class GameController {

    private final PersistenceService persistenceService;

    private UserInterface userInterface;

    private Hero hero;

    public GameController(UserInterface userInterface) {
        this.userInterface = userInterface;
        persistenceService = new PersistenceService();
    }

    public void switchView(String viewName) {
        BaseView view;
        switch (viewName) {
            case "WelcomeView":
                view = new WelcomeView(this);
                break;
            case "NewGameView":
                view = new NewGameView(this, HeroClass.values());
                break;
            case "LoadGameView":
                view = new LoadGameView(this, persistenceService.loadHeroNames());
                break;
            default:
                userInterface.refreshView();
                return;
        }
        userInterface.setView(view);
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

    public void newGame(String name, HeroClass heroClass) {
        if (!persistenceService.isHeroNameAvailable(name)) {
            throw new SwingumException("Hero name already in use");
        }
        System.out.println("Creating hero " + name + " of class " + heroClass);
        hero = Hero.builder()
                   .name(name)
                   .clazz(heroClass)
                   .build();
        userInterface.setView(new GameView(this, hero));
    }

    public void loadGame(String name) {
        hero = persistenceService.loadHero(name);
        userInterface.setView(new GameView(this, hero));
    }

    public void moveHero(char direction) {
        hero.move(direction);
        // todo: process events if any
        System.out.println("Events:");
        hero.getInteractions().forEach(System.out::println);
        userInterface.refreshView();
    }

    public void processInteraction(String action) {
        System.out.println(action);

    }

    public void saveGame() {
        Integer heroId = persistenceService.saveHero(hero);
        if (hero.getId() == null) {
            hero.setId(heroId);
        }
        System.out.println("Game saved");
    }
}