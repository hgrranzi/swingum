package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

public class LeaderboardView extends BaseView {

    public LeaderboardView(GameController controller, BaseView previousView) {
        super(controller);
        addButton("Back", e -> controller.showTheView(previousView));
    }
}
