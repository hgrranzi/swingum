package com.hgrranzi.swingum;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;

public class SwingumApplication {
    public static void main(String[] args) {
        UserInterface ui = args.length > 0 && args[0].equals("console") ? new ConsoleFrame() : new GuiFrame();
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController(ui);
            controller.switchView("WelcomeView");
        });
    }
}
