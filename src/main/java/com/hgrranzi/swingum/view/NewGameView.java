package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.HeroClass;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.hgrranzi.swingum.view.gui.ImageManager.getImage;
import static com.hgrranzi.swingum.view.gui.ImageManager.scaleImage;

public class NewGameView extends BaseView {

    private final List<HeroClass> heroClasses;

    private JTextField nameField;

    private final ButtonGroup heroClassButtonGroup = new ButtonGroup();

    public NewGameView(GameController controller, HeroClass[] heroClasses) {
        super(controller);
        this.heroClasses = List.of(heroClasses);
        addButton("Start", e -> controller.newGame(nameField.getText(), getHeroClass()));
        addButton("Back", e -> controller.switchView("WelcomeView"));

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
    public void displayGuiButtons() {
        super.displayGuiButtons();

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        titlePanel.add(new JLabel("Choose your hero and give him a name:"));
        nameField = new JTextField(20);
        titlePanel.add(nameField);
        displayScrollRadioButtonList(titlePanel,
                                     heroClasses.stream().map(HeroClass::toString).collect(Collectors.toList()),
                                     heroClassButtonGroup);

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
