package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.controller.GameController;

public interface UserInterface {

    void setView(String viewName, GameController controller);

    void closeFrame();
}
