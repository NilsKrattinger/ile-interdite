package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Action;
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
    Utils.State[][] cellStates;
    Adventurer currentAdventurer;

    // Turn state
    private Action selectedAction;
    private static final int NB_ACTIONS_PER_TURN = 3;

	public void beginTurn() {
        for (int i = NB_ACTIONS_PER_TURN; i > 0; i--) {

            // TODO - boucle d'attente

            switch (selectedAction){
                case MOVE:
                    // TODO - méthode pour se déplacer
                    break;
                case DRY:
                    // TODO - méthode pour assécher
                    break;
                case GIVE_CARD:
                    // TODO - méthode pour donner une carte
                    break;
                case GET_TREASURE:
                    // TODO - méthode pour donner un trésor
                    break;
            }
        }
	}

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     * @param adventurer
     */
    public void initMovement(Adventurer adventurer){
        int x;
        int y;
        cellStates = new Utils.State[6][6];
        cellStates = adventurer.getAccessibleCells();
        //TODO declancher snteraction avec joueurs
    }

    /**
     *	Renvoie un boolean si la case choisie par l'utilisateur est accesible
     * @param x,y
     * @return boolean
     */
    public boolean isMovementAvailable(int x, int y){
        return cellStates[x][y] == Utils.State.ACCESSIBLE;
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

	/**
	 *
	 * @param nb
	 */
	public void setNbActions(int nb) {
		// TODO - implement ileinterdite.Controller.setNbActions
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