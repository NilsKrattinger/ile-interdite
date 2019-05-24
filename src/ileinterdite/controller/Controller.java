package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.*;
import ileinterdite.test.DemoBoardGenerator;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Action;
import ileinterdite.util.Utils.Pawn;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.*;

public class Controller implements Observer {

	private Grid grid;
    private Utils.State[][] cellStates;

    private ArrayList<Adventurer> players;
    private Adventurer currentAdventurer;

    private Collection<Deck> decks;
    private Collection<DiscardPile> discardPiles;

    private GridView gridView;
    private AdventurerView adventurerView;

    // Turn state
    private Action selectedAction;
    private static final int NB_ACTIONS_PER_TURN = 3;
    private int remainingActions;

    public Controller(AdventurerView view, int nbPlayers) {
        this.adventurerView = view;

        Grid grid = new Grid(DemoBoardGenerator.boardBuilder("res/Case.txt"), null);

        players = new ArrayList<>();
        players.add(new Diver(grid, 3, 4));
        players.add(new Engineer(grid, 3, 4));
        players.add(new Explorer(grid, 3, 4));
        players.add(new Messager(grid, 3, 4));
        players.add(new Navigator(grid, 3, 4));
        players.add(new Pilot(grid, 3, 4));

        Collections.shuffle(players);
        while (players.size() > nbPlayers) {
            players.remove(players.size() - 1);
        }
        nextAdventurer();
    }

    private void changeCurrentAdventurer() {
        players.add(players.remove(0));
        currentAdventurer = players.get(0);
        Pawn currentPawn = currentAdventurer.getPawn();
        adventurerView.setColor(currentPawn.getColor(), currentPawn.getTextColor());
        adventurerView.setText(currentAdventurer.getClass().getSimpleName(), currentAdventurer.getClass().getSimpleName());
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
        adventurerView.showSelectableCells(cellStates);
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
     * Asséchement de la grille en X,Y et actualisation de la vue
     * @param x
     * @param y
     */
    public void dry(int x, int y){
        if (isCellAvailable(x,y)){
            this.getGrid().dry(x,y);
            adventurerView.updateDriedCell(x, y);
        }
    }

    public void handleAction(String msg) {
        String[] coords = msg.split("(?<=\\d).+(?=\\d)");
        System.out.println(coords[0] + ' ' + coords[1]);
        switch (selectedAction) {
            case MOVE:
                movement(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
                break;
            case DRY:
                dry(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
                break;
            case GIVE_CARD:
                break;
            case GET_TREASURE:
                break;
            default:
                setNbActions(remainingActions + 1);
        }

        if (remainingActions == 0) {
            nextAdventurer();
        }
    }

    public void drawTreasureCards() {

    }

    public void drawFloodCards() {

    }

    public void nextAdventurer() {
        changeCurrentAdventurer();
        setNbActions(NB_ACTIONS_PER_TURN);
        selectedAction = null;
    }

	/**
	 *
	 * @param nb
	 */
	public void setNbActions(int nb) {
        this.remainingActions = Math.max(nb, NB_ACTIONS_PER_TURN);
	}

	public void reduceNbActions() {
		this.remainingActions--;
	}

    @Override
    public void update(Observable o, Object arg) {
        Message m = (Message) arg;
        switch (m.action) {
            case MOVE:
                selectedAction = Action.MOVE;
                initMovement(currentAdventurer);
                break;
            case DRY:
                selectedAction = Action.DRY;
                initDryable(currentAdventurer);
                break;
            case GIVE_CARD:
                selectedAction = Action.GIVE_CARD;
                break;
            case GET_TREASURE:
                selectedAction = Action.GET_TREASURE;
                break;
            case VALIDATE_ACTION:
                if (selectedAction != null) {
                    reduceNbActions();
                    handleAction(m.message);
                }
                selectedAction = null;
                break;
            case END_TURN:
                nextAdventurer();
                break;
            case CANCEL_ACTION:
                selectedAction = null;
                break;
        }
    }

    private Grid getGrid() {
        return grid;
    }
}