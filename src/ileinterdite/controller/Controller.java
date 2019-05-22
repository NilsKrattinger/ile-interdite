package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.*;
import ileinterdite.test.DemoBoardGenerator;
import ileinterdite.util.Message;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Action;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.*;

public class Controller implements Observer {

	Grid grid;
    Utils.State[][] cellStates;

	ArrayList<Adventurer> players;
    Adventurer currentAdventurer;

	Collection<Deck> decks;
	Collection<DiscardPile> discardPiles;

    GridView gridView;
    AdventurerView adventurerView;

    // Turn state
    private Action selectedAction;
    private static final int NB_ACTIONS_PER_TURN = 3;
    private int remainingActions;

    public Controller(AdventurerView view, int nbPlayers) {
        this.adventurerView = view;

        Grid grid = new Grid(DemoBoardGenerator.boardBuilder("res/Case.txt"), null, null);

        players = new ArrayList<>();
        players.add(new Diver(grid));
        players.add(new Engineer(grid));
        players.add(new Explorer(grid));
        players.add(new Messager(grid));
        players.add(new Navigator(grid));
        players.add(new Pilot(grid));

        Collections.shuffle(players);
        while (players.size() > nbPlayers) {
            players.remove(players.size() - 1);
        }
        currentAdventurer = players.get(0);
    }

	public void beginTurn() {
        setNbActions(NB_ACTIONS_PER_TURN);
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
    public boolean isCellAvailable(int x, int y){
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
        if (isCellAvailable(x,y)){
            this.currentAdventurer.move(x,y);
            //TODO actualisation de la vue
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

    public void nextAdventurer() {
        players.add(players.remove(0));
    }

	/**
	 *
	 * @param nb
	 */
	public void setNbActions(int nb) {
        this.remainingActions = nb;
	}

	public void reduceNbActions() {
		this.remainingActions--;
	}

    @Override
    public void update(Observable o, Object arg) {
        Message m = (Message) arg;
        switch (m.action) {
            case MOVE:
                initMovement(currentAdventurer);
                break;
            case DRY:
                initDryable(currentAdventurer);
                break;
            case GIVE_CARD:
                break;
            case GET_TREASURE:
                break;
            case VALIDATE_ACTION:
                reduceNbActions();
                break;
            case END_TURN:
                break;
            case CANCEL_ACTION:
                break;
        }
    }

    private Grid getGrid() {
        return grid;
    }
}