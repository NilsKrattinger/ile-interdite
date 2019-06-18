package ileinterdite;

import ileinterdite.controller.ControllerMainMenu;
import ileinterdite.view.MainMenuView;

public class Main {
    public static void main(String [] args) {
        MainMenuView mainMenuView = new MainMenuView();
        ControllerMainMenu cm = new ControllerMainMenu();
        mainMenuView.addObserver(cm);
        mainMenuView.setVisible();
    }
}
