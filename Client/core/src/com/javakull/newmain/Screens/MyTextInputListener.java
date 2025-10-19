package com.javakull.newmain.Screens;

import com.badlogic.gdx.Input;

public class MyTextInputListener implements Input.TextInputListener {

    private MenuScreen menu;

    public MyTextInputListener(MenuScreen menuScreen) {
        this.menu = menuScreen;

    }

    @Override
    public void input(String s) {
        this.menu.setPlayerName(s);

    }

    @Override
    public void canceled() {

    }
}
