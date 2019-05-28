package ileinterdite.controller;

import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.*;
import ileinterdite.test.DemoBoardGenerator;
import ileinterdite.util.Message;
import ileinterdite.util.Parameters;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Action;
import ileinterdite.util.Utils.Pawn;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (Parameters.DEMOMAP) {
            this.grid = new Grid(DemoBoardGenerator.boardBuilder("res/Case.txt"), null);
        }

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
        cellStates = adventurer.getAccessibleCells();
        adventurerView.showSelectableCells(cellStates, grid, new Tuple<>(adventurer.getX(), adventurer.getY()));
    }

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     * @param adventurer
     */
    public void initDryable(Adventurer adventurer) {
        cellStates = adventurer.getDryableCells();
        adventurerView.showSelectableCells(cellStates, grid, new Tuple<>(adventurer.getX(), adventurer.getY()));
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
        this.currentAdventurer.move(x,y);
        adventurerView.updateAdventurer(currentAdventurer);
    }

    /**
     * Asséchement de la grille en X,Y et actualisation de la vue
     * @param x
     * @param y
     */
    public void dry(int x, int y){
        this.getGrid().dry(x,y);
        adventurerView.updateDriedCell(x, y);
    }

    /**
     * Split the message contents to retrieve the position
     * @param msg Message The message received from the view
     * @return Tuple&lt;Integer, Integer&gt;
     */
    private Tuple<Integer, Integer> getPositionFromMessage(String msg) {
        Pattern p = Pattern.compile("(\\d).+(\\d)");
        Matcher m = p.matcher(msg);
        if (m.find()) {
            int x = Integer.valueOf(m.group(1));
            int y = Integer.valueOf(m.group(2));
            return new Tuple<>(x, y);
        } else {
            Utils.showInformation("Les coordonnées entrées sont incorrectes.");
            return null;
        }
    }

    private boolean validateCellAction(int x, int y) {
        if (x >= 0 && y >= 0 && x < Grid.WIDTH && y < Grid.HEIGHT && isCellAvailable(x, y)) {
            reduceNbActions();
            return true;
        } else {
            Utils.showInformation("Les coordonnées sont invalides.");
            return false;
        }
    }

    public void handleAction(String msg) {
        Tuple<Integer, Integer> coords = getPositionFromMessage(msg);
        switch (selectedAction) {
            case MOVE:
                if (coords != null) {
                    int x = coords.x - 1;
                    int y = coords.y - 1;
                    if (validateCellAction(x, y)) {
                        movement(x, y);
                    }
                }
                break;
            case DRY:
                if (coords != null) {
                    int x = coords.x - 1;
                    int y = coords.y - 1;
                    if (validateCellAction(x, y)) {
                        dry(x, y);
                    }
                }
                break;
            case GIVE_CARD:
                break;
            case GET_TREASURE:
                break;
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