package com.hgrranzi.swingum;

import com.hgrranzi.swingum.config.ApplicationConfig;
import com.hgrranzi.swingum.controller.ViewController;
import com.hgrranzi.swingum.persistence.DBConnectionManager;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;
import java.sql.Connection;

public class SwingumApplication {
    public static void main(String[] args) {
        ApplicationConfig.loadProperties();
       try ( Connection connection = DBConnectionManager.open()) {
           System.out.println("Database connection established");
       } catch (Exception e) {
              System.out.println("Database connection failed: " + e.getMessage());
       }
        UserInterface ui = args.length > 0 && args[0].equals("console") ? new ConsoleFrame() : new GuiFrame();
        SwingUtilities.invokeLater(() -> {
            ViewController controller = new ViewController(ui);
            controller.switchView("WelcomeView");
        });
    }
}
