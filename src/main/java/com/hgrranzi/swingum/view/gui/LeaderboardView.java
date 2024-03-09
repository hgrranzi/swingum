package com.hgrranzi.swingum.view.gui;

import com.hgrranzi.swingum.controller.GameController;

public class LeaderboardView extends BaseView {

    public LeaderboardView(GameController controller) {
        super(controller);
        addButton("Back", e -> controller.switchView("PreviousView"));
    }
}
