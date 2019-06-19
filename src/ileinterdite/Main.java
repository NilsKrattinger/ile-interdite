package ileinterdite;

import ileinterdite.controller.ControllerMainMenu;
import ileinterdite.view.MainMenuView;
import ileinterdite.view.MainMenuView2;

public class Main {
    public static void main(String [] args) {
        MainMenuView2 mainMenuView2 = new MainMenuView2();
        ControllerMainMenu cm = new ControllerMainMenu();
        mainMenuView2.addObserver(cm);
        mainMenuView2.setVisible();
    }
}
