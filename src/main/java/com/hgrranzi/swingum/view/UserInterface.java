package com.hgrranzi.swingum.view;

import com.hgrranzi.swingum.view.gui.BaseView;

public interface UserInterface {

    void setView(BaseView view);

    void closeFrame();

    BaseView getPreviousView();
}
