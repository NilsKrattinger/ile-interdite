package ileinterdite;

import ileinterdite.controller.Controller;
import ileinterdite.controller.ControllerMainMenu;
import ileinterdite.util.Utils;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;
import ileinterdite.view.MainMenuView;

public class Main {
    public static void main(String [] args) {
        MainMenuView mainMenuView = new MainMenuView();
        ControllerMainMenu cm = new ControllerMainMenu();
        mainMenuView.addObserver(cm);
        mainMenuView.setVisible();
    }
}
