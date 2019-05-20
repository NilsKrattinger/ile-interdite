package ileinterdite;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.State;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.*;

public class Controller extends Observer {

	Grid grid;
	Collection<Adventurer> players;
	GridView gridView;
	AdventurerView advemoventurerView;
	Collection<Deck> decks;
	Collection<DiscardPile> discardPiles;
	State[][] cellStates;
	Adventurer currentAdventurer;

	public void beginTurn() {
		// TODO - implement ileinterdite.Controller.beginTurn
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param nb
	 */
	public void setNbActions(int nb) {
		// TODO - implement ileinterdite.Controller.setNbActions
		throw new UnsupportedOperationException();
	}

	public void reduceNbActions() {
		// TODO - implement ileinterdite.Controller.reduceNbActions
		throw new UnsupportedOperationException();
	}

	/**
	 * Lance les actions pour le deplacement de l'aventurier.
	 * puis l'interaction avec l'interface
	 * @param adventurer
	 */
	public void initMovement(Adventurer adventurer){
		int x;
		int y; 
		cellStates = new State[6][6];
		cellStates = adventurer.getAccessibleCells();
		//TODO declancher snteraction avec joueurs
	}

	/**
	 *	Renvoie un boolean si la case choisie par l'utilisateur est accesible
	 * @param x,y
	 * @return boolean
	 */
	public boolean isMovementAvailable(int x, int y){
		return cellStates[x][y] == State.ACCESSIBLE;
	}

	/**
	 *
	 * @param x
	 * @param y
	 *
	 * deplacement de l'avanturier en X,Y et actualisation de la vue
	 */
	public void movement(int x, int y){
		if (isMovementAvailable(x,y)){
			this.currentAdventurer.move(x,y);
			//TODO actualisation de la vue
		}
	}

    @Override
    public void update(Observable o, Object arg) {
        
    }
}