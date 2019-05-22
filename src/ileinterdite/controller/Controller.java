package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
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
    Utils.Action currentAction;

    public Controller() {
        AdventurerView adventurerView = new AdventurerView("Manon", "Explorateur", Utils.Pawn.RED.getColor() );
        adventurerView.addObserver(this);
        adventurerView.setVisible();
    }

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
    public void initMovement(Adventurer adventurer) {
        cellStates = new Utils.State[6][6];
        cellStates = adventurer.getAccessibleCells();
        adventurerView.showSelectableCells(cellStates);
    }

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     * @param adventurer
     */
    public void initDryable(Adventurer adventurer){
        int x;
        int y;
        cellStates = new Utils.State[Grid.HEIGHT][Grid.WIDTH];
        cellStates = adventurer.getDryableCells();
        //TODO declancher interaction avec joueurs
    }

    /**
     *	Renvoie un boolean si la case choisie par l'utilisateur est accesible
     * @param x,y
     * @return boolean
     */
    public boolean isCellAvailable(int x, int y) {
        return cellStates[y][x] == Utils.State.ACCESSIBLE;
    }
    
    /**
     *
     * @param x
     * @param y
     *
     * deplacement de l'avanturier en X,Y et actualisation de la vue
     */
    public void movement(int x, int y){
        if (isCellAvailable(x,y)){
            this.currentAdventurer.move(x,y);
            adventurerView.updateAdventurer(currentAdventurer);
        }
    }

    /**
     * Split the message contents to retrieve the position
     * @param m Message The message received from the view
     * @return Tuple&lt;Integer, Integer&gt;
     */
    private Tuple<Integer, Integer> getPositionFromMessage(Message m) {
        String[] coords = m.message.split("\\s");
        int x = Integer.valueOf(coords[0]);
        int y = Integer.valueOf(coords[1]);
        return new Tuple<>(x, y);
    }

    /**
     * Validate the movement or drying chosen
     * @param m Message The message received from the view
     */
    private void validateCell(Message m) {
        Tuple<Integer, Integer> pos;
        switch (currentAction) {
            case DRY:
                pos = getPositionFromMessage(m);

                break;
            case MOVE:
                pos = getPositionFromMessage(m);
                movement(pos.x, pos.y);
                break;
        }
    }

    /**
     * Asséchement de la grille en X,Y et actualisation de la vue
     * @param x
     * @param y
     */
    public void dry(int x, int y){
        if (isCellAvailable(x,y)){
            this.getGrid().dry(x,y);
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
        Message m = (Message) arg;
        switch (m.action) {
            case DRY:
                currentAction = Utils.Action.DRY;
                break;

            case MOVE:
                currentAction = Utils.Action.MOVE;
                initMovement(currentAdventurer);
                break;

            case VALIDATE_CELL:
                if (currentAction != null) {
                    validateCell(m);
                }
                break;

            case CANCEL_ACTION:
                currentAction = null;
                break;

            default:
                break;
        }
    }

    public static void main(String [] args) {
        // Instanciation de la fenêtre
        Controller c = new Controller();
    }

    private Grid getGrid() {
        return grid;
    }
}