package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.util.Utils;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;
import ileinterdite.model.adventurers.Adventurer;

import java.util.*;

public class Controller implements Observer {

	Grid grid;
	Collection<Adventurer> players;
	GridView gridView;
	AdventurerView adventurerView;
	Collection<Deck> decks;
	Collection<DiscardPile> discardPiles;

	public void beginTurn() {
		// TODO - implement ileinterdite.controller.Controller.beginTurn
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param nb
	 */
	public void setNbActions(int nb) {
		// TODO - implement ileinterdite.controller.Controller.setNbActions
		throw new UnsupportedOperationException();
	}

	public void reduceNbActions() {
		// TODO - implement ileinterdite.controller.Controller.reduceNbActions
		throw new UnsupportedOperationException();
	}

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void main(String [] args) {
        // Instanciation de la fenêtre
        AdventurerView adventurerView = new AdventurerView("Manon", "Explorateur", Utils.Pawn.RED.getColor() );
        Controller c = new Controller();
        adventurerView.addObserver(c);
        adventurerView.setVisible();
    }
}