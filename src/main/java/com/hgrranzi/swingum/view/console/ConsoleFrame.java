package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.BaseView;

import java.util.Scanner;

public class ConsoleFrame implements UserInterface {

    Scanner scanner = new Scanner(System.in);

    private BaseView view;

    @Override
    public void setView(BaseView view) {
        this.view = view;
        this.view.displayConsoleButtons();
        this.view.printView();
        String input = scanner.nextLine();
        try {
            view.getButtons().get(input).actionPerformed(null);
        } catch (NullPointerException e) {
            System.out.println("Invalid input");
            setView(view);
        }
    }

    @Override
    public void refreshView() {
        view.printView();
    }

    @Override
    public void closeFrame() {
        scanner.close();
        System.out.println("Closing console view");
    }
}
