package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
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
    Utils.State[][] cellStates;
    Adventurer currentAdventurer;
    Utils.Action currentAction;

    public Controller() {
        AdventurerView adventurerView = new AdventurerView("Manon", "Explorateur", Utils.Pawn.RED.getColor() );
        adventurerView.addObserver(this);
        adventurerView.setVisible();
    }

	public void beginTurn() {
		// TODO - implement ileinterdite.controller.Controller.beginTurn
		throw new UnsupportedOperationException();
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
     *	Renvoie un boolean si la case choisie par l'utilisateur est accessible
     * @param x,y
     * @return boolean
     */
    public boolean isMovementAvailable(int x, int y) {
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
}