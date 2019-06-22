package ileinterdite;

import ileinterdite.controller.MainMenuController;
import ileinterdite.view.MainMenuView;

public class Main {
    public static void main(String[] args) {
        MainMenuView mainMenuView = new MainMenuView();
        MainMenuController cm = new MainMenuController();
        mainMenuView.addObserver(cm);
        mainMenuView.setVisible();
    }
}
