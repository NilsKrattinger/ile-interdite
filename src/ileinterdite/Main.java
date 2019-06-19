package ileinterdite;

import ileinterdite.controller.ControllerMainMenu;
import ileinterdite.view.MainMenuView;
import ileinterdite.view.MainMenuView;

public class Main {
    public static void main(String [] args) {
        MainMenuView MainMenuView = new MainMenuView();
        ControllerMainMenu cm = new ControllerMainMenu();
        MainMenuView.addObserver(cm);
        MainMenuView.setVisible();
    }
}
