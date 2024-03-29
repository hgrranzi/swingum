package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.ViewController;
import com.hgrranzi.swingum.model.HeroClass;

import javax.swing.*;
import java.util.List;

public class NewGameView extends BaseView {

    private final List<HeroClass> heroClasses;
    private JTextField nameField;
    private final ButtonGroup heroClassButtonGroup = new ButtonGroup();

    public NewGameView(ViewController controller, HeroClass[] heroClasses) {
        super(controller);
        this.heroClasses = List.of(heroClasses);
        addButton("Start", e -> controller.newGame(nameField.getText(), getHeroClass()));
        addButton("Back", e -> controller.switchView("WelcomeView"));

    }

    private HeroClass getHeroClass() {
        String heroClassName = heroClassButtonGroup.getSelection().getActionCommand();
        if (nameField.getText().isEmpty() || heroClassName.isEmpty()){
            throw new SwingumException("No hero name or class provided.");
        }
        try {
            return HeroClass.valueOf(heroClassName);
        } catch (IllegalArgumentException e) {
            throw new SwingumException("No hero class * " + heroClassName + " * available.");
        }
    }

    @Override
    public void displayGuiButtons() {
        super.displayGuiButtons();
        nameField = new JTextField(20);
        add(nameField);
        for (HeroClass heroClass : heroClasses) {
            JRadioButton radioButton = new JRadioButton(heroClass.toString());
            radioButton.setActionCommand(heroClass.toString());
            heroClassButtonGroup.add(radioButton);
            add(radioButton);
        }
    }

    @Override
    public void displayConsoleButtons() {
        super.displayConsoleButtons();
        StringBuilder viewBuffer = new StringBuilder();
        viewBuffer.append("Hero classes:\n");
        viewBuffer.append("* ");
        for (HeroClass heroClass : heroClasses) {
            viewBuffer.append(heroClass.toString()).append(" * ");
        }
        viewBuffer.append("\n");
        viewBuffer.append("Choose hero class and type hero name as follows: name CLASS\n");
        System.out.println(viewBuffer);
        String[] inputArray = ScannerProvider.getScanner().nextLine().split(" ");
        nameField = inputArray.length > 0 ? new JTextField(inputArray[0]) : new JTextField("");
        JRadioButton radioButton = new JRadioButton();
        radioButton.setActionCommand(inputArray.length > 1 ? inputArray[1] : "");
        radioButton.setSelected(true);
        heroClassButtonGroup.add(radioButton);
    }
}
