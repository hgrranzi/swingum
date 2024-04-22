package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoadGameView extends BaseView {

    private final List<String> heroNames;

    private final ButtonGroup heroClassButtonGroup = new ButtonGroup();

    public LoadGameView(GameController controller, List<String> heroNames) {
        super(controller);
        this.heroNames = heroNames;
        addButton("Load", e -> controller.loadGame(getSelectedHeroName()));
        addButton("Back", e -> controller.switchView("WelcomeView"));
    }

    private String getSelectedHeroName() {
        String selectedHeroName = heroClassButtonGroup.getSelection().getActionCommand();
        if (selectedHeroName.isEmpty()) {
            throw new SwingumException("No hero name selected.");
        }
        return selectedHeroName;
    }

    @Override
    void drawCenterPanel(Graphics g) {

    }

    @Override
    public void displayGuiButtons() {
        super.displayGuiButtons();

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        titlePanel.add(new JLabel("Choose your hero:"));
        displayScrollRadioButtonList(titlePanel, heroNames, heroClassButtonGroup);
    }


}
