package ileinterdite;

import ileinterdite.controller.Controller;
import ileinterdite.util.Utils;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

public class Main {
    public static void main(String [] args) {
        // Instanciation de la fenêtre
        AdventurerView adventurerView = new AdventurerView();
        GridView gridView = new GridView();
        Controller c = new Controller(adventurerView, gridView, 4);
        adventurerView.addObserver(c);
        adventurerView.setVisible();
    }
}
