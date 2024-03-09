package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.controller.GameController;
import com.hgrranzi.swingum.view.UserInterface;

import java.util.Scanner;

public class ConsoleFrame implements UserInterface {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void setView(String viewName, GameController controller) {
        System.out.println("Setting view to " + viewName);
        String input = scanner.nextLine();
        if (input.equals("switch")) {
            controller.switchUserInterface();
        } else {
            controller.switchView(input);
        }
    }

    @Override
    public void closeFrame() {
        scanner.close();
        System.out.println("Closing console view");
    }
}
