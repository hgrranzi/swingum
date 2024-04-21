package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.model.HeroClass;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        int iconSize = (GuiFrame.getFrameHeight() - northPanel.getPreferredSize().height * 3) / (heroClasses.size() + 1);
        centerPanel.add(new JLabel("Choose your hero and give him a name"));
        nameField = new JTextField(20);
        centerPanel.add(nameField);
        for (HeroClass heroClass : heroClasses) {
            JRadioButton radioButton = new JRadioButton(heroClass.toString(), new ImageIcon(
                scaleImage(getImage(heroClass.getImageName()), iconSize, iconSize)));
            radioButton.setActionCommand(heroClass.toString());
            radioButton.setSelectedIcon(new ImageIcon(scaleImage(getImage("yes.png"), iconSize, iconSize)));
            radioButton.setPreferredSize(new Dimension(GuiFrame.getFrameWidth() / 4, iconSize));
            JLabel infoLabel = new JLabel("attack=" + heroClass.getAttack() +
                                          "  defense=" + heroClass.getDefense() +
                                          "  luck=" + heroClass.getLuck());
            heroClassButtonGroup.add(radioButton);
            centerPanel.add(radioButton);
            centerPanel.add(infoLabel);
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
