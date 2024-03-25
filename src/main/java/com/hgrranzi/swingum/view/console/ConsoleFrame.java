package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.gui.BaseView;

import java.util.Scanner;

public class ConsoleFrame implements UserInterface {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void setView(BaseView view) {
        System.out.println("Setting view to " + view.getName());
        String input = scanner.nextLine();
        if (input.equals("switch")) {
            view.getViewController().switchUserInterface();
        } else {
            view.getViewController().switchView(input);
        }
    }

    @Override
    public void closeFrame() {
        scanner.close();
        System.out.println("Closing console view");
    }

    @Override
    public BaseView getPreviousView() {
        return null;
    }
}
