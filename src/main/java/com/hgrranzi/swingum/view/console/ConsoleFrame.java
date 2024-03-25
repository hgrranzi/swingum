package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.BaseView;

import java.util.Scanner;

public class ConsoleFrame implements UserInterface {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void setView(BaseView view) {
        view.displayConsoleButtons();
        String input = scanner.nextLine();
        view.getButtons().get(input).actionPerformed(null);
    }

    @Override
    public void closeFrame() {
        scanner.close();
        System.out.println("Closing console view");
    }
}
