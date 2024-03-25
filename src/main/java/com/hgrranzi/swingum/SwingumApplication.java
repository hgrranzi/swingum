package com.hgrranzi.swingum;

import com.hgrranzi.swingum.controller.ViewController;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;

public class SwingumApplication {
    public static void main(String[] args) {
        UserInterface ui = args.length > 0 && args[0].equals("console") ? new ConsoleFrame() : new GuiFrame();
        SwingUtilities.invokeLater(() -> {
            ViewController controller = new ViewController(ui);
            controller.switchView("WelcomeView");
        });
    }
}
