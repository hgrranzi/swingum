package com.hgrranzi.swingum;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SwingumApplication {
    public static void main(String[] args) {
        disableLogging();
        UserInterface ui = args.length > 0 && args[0].equals("console") ? new ConsoleFrame() : new GuiFrame();
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController(ui);
            controller.switchView("WelcomeView");
        });
    }

    private static void disableLogging() {
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger("");
        logger.setLevel(Level.OFF);
    }
}
