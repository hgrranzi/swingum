package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.SwingumException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;
import static com.hgrranzi.swingum.view.gui.ImageManager.scaleImage;

public class NewGameView extends BaseView {

    private JTextField nameField;

    private final ButtonGroup heroClassButtonGroup = new ButtonGroup();

    public NewGameView(GameController controller, HeroClass[] heroClasses) {
        super(controller);
        List<HeroClass> heroClasses1 = List.of(heroClasses);
        addButton("Start", e -> controller.newGame(nameField.getText(), getHeroClass()));
        addButton("Back", e -> controller.switchView("WelcomeView"));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        titlePanel.add(new JLabel("Choose your hero and give him a name:"));
        nameField = new JTextField(20);
        titlePanel.add(nameField);
        createScrollRadioButtonList(titlePanel,
                heroClasses1.stream().map(HeroClass::toString).collect(Collectors.toList()),
                heroClassButtonGroup);
    }

    private HeroClass getHeroClass() {
        String heroClassName = heroClassButtonGroup.getSelection().getActionCommand();
        if (nameField.getText().isEmpty() || heroClassName.isEmpty()) {
            throw new SwingumException("No hero name or class provided.");
        }
        try {
            return HeroClass.valueOf(heroClassName);
        } catch (IllegalArgumentException e) {
            throw new SwingumException("No hero class * " + heroClassName + " * available.");
        }
    }

    @Override
    void drawCenterPanel(Graphics g) {
    }

    @Override
    protected ImageIcon getRadioIcon(String identifier, int iconSize) {
        try {
            HeroClass heroClass = HeroClass.valueOf(identifier);
            return new ImageIcon(scaleImage(getImage(heroClass.getImageName()), iconSize, iconSize));
        } catch (IllegalArgumentException e) {
            return super.getRadioIcon(identifier, iconSize);
        }
    }
}
