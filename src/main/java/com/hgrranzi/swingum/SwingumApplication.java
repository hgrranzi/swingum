package com.hgrranzi.swingum;

import com.hgrranzi.swingum.controller.ViewController;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.console.ConsoleFrame;
import com.hgrranzi.swingum.view.gui.GuiFrame;

import javax.swing.*;
import java.sql.Connection;

import static java.sql.DriverManager.getConnection;

public class SwingumApplication {
    public static void main(String[] args) {
        try (Connection connection = getConnection("jdbc:postgresql://localhost:5432/swingum",
                                                   "postgres",
                                                   "postgres")) {
            System.out.println("Database connection established");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            //todo: if no database connection, use file storage or do not load/save game
        }
        UserInterface ui = args.length > 0 && args[0].equals("console") ? new ConsoleFrame() : new GuiFrame();
        SwingUtilities.invokeLater(() -> {
            ViewController controller = new ViewController(ui);
            controller.switchView("WelcomeView");
        });
    }
}
