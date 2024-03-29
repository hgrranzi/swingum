package com.hgrranzi.swingum.view.console;

import com.hgrranzi.swingum.view.SwingumException;
import com.hgrranzi.swingum.view.ScannerProvider;
import com.hgrranzi.swingum.view.UserInterface;
import com.hgrranzi.swingum.view.BaseView;

public class ConsoleFrame implements UserInterface {

    private BaseView view;

    @Override
    public void setView(BaseView view) {
        this.view = view;
        this.view.displayConsoleButtons();
        String input = ScannerProvider.getScanner().nextLine();
        try {
            view.getButtonListener(input).actionPerformed(null);
        } catch (SwingumException e) {
            System.out.println("Invalid input: " + e.getMessage());
            setView(view);
        }
    }

    @Override
    public void refreshView() {
        view.printView();
    }

    @Override
    public void closeFrame() {
        ScannerProvider.closeScanner();
        System.out.println("Closing console view");
    }
}
