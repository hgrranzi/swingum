package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.ViewController;

import javax.swing.*;
import java.util.List;

public class LoadGameView extends BaseView {

    private final List<String> heroNames;
    private final ButtonGroup heroClassButtonGroup = new ButtonGroup();

    public LoadGameView(ViewController controller, List<String> heroNames) {
        super(controller);
        this.heroNames = heroNames;
        addButton("Load", e -> controller.switchView("GameView"));
        addButton("Back", e -> controller.switchView("WelcomeView"));
    }

    @Override
    public void displayGuiButtons() {
        super.displayGuiButtons();
        for (String name : heroNames) {
            JRadioButton radioButton = new JRadioButton(name);
            radioButton.setActionCommand(name);
            heroClassButtonGroup.add(radioButton);
            add(radioButton);
        }
    }

}
