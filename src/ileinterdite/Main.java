package ileinterdite;

import ileinterdite.controller.Controller;
import ileinterdite.util.Utils;
import ileinterdite.view.AdventurerView;

public class Main {
    public static void main(String [] args) {
        // Instanciation de la fenêtre
        AdventurerView adventurerView = new AdventurerView("Manon", "Explorateur", Utils.Pawn.RED.getColor() );
        Controller c = new Controller(adventurerView, 4);
        adventurerView.addObserver(c);
        adventurerView.setVisible();
    }
}
