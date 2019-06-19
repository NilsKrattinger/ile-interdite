package ileinterdite.controller;

import ileinterdite.factory.BoardFactory;
import ileinterdite.factory.DeckFactory;
import ileinterdite.factory.DiscardPileFactory;
import ileinterdite.model.*;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Engineer;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.*;
import ileinterdite.util.Utils.Action;
import ileinterdite.view.GameView;

import java.util.ArrayList;
import java.util.HashMap;

public class GameController {

    private MainMenuController mainMenuController;
    private ActionController actionController;
    private AdventurerController adventurerController;
    private GridController gridController;
    private InterruptionController interruptionController;
    private WaterScaleController waterScaleController;
    private DeckController deckController;

    private GameView mainView;

    private boolean powerEngineer = false;

    public GameController(MainMenuController cm) {
        BoardFactory.initBoardFactory();
        this.mainView = new GameView(1280, 720);

        this.mainMenuController = cm;
        this.actionController = new ActionController(this);
        this.adventurerController = new AdventurerController(this, BoardFactory.getAdventurers(), mainMenuController.getPlayersName());
        this.gridController = new GridController(this);
        this.interruptionController = new InterruptionController(this);
        this.waterScaleController = new WaterScaleController(1);
        this.deckController = new DeckController(this);

        this.mainView.setVisible();
        this.newTurn();
    }

    public GameView getWindow() {
        return mainView;
    }

    public ActionController getActionController() {
        return actionController;
    }

    public AdventurerController getAdventurerController() {
        return adventurerController;
    }

    public GridController getGridController() {
        return gridController;
    }

    public WaterScaleController getWaterScaleController() {
        return waterScaleController;
    }

    public InterruptionController getInterruptionController() {
        return interruptionController;
    }

    public DeckController getDeckController() {
        return deckController;
    }

    /* ********* *
     * SHORTCUTS *
     * ********* */
    public ArrayList<Adventurer> getAdventurers() {
        return adventurerController.getAdventurers();
    }

    public Adventurer getCurrentAdventurer() {
        return adventurerController.getCurrentAdventurer();
    }

    public void startAdventurerAction(Message m) {
        adventurerController.handleAction(m);
    }

    /* ************* *
     * TURN HANDLING *
     * ************* */

    private void newTurn() {
        testDefeat();
        adventurerController.nextAdventurer();
        gridController.newTurn();
        actionController.newTurn();
    }

    public void endTurn() {
        deckController.drawTreasureCards(2);
        deckController.drawFloodCards(waterScaleController.getFloodedCardToPick());
        if (!interruptionController.getAdventurersToRescue().isEmpty()){
            interruptionController.initRescue();
            actionController.startInterruption();
        } else {
            this.newTurn();
        }
    }

    /* **************** *
     * VICTORY & DEFEAT *
     * **************** */

    /**
     * declenche la victoire
     */
    private void victory() {
        //adventurerView.displayVictory()
        //TODO adventurerView.displayVictory()

        //this.endGame()
        //TODO this.endGame()
    }

    /**
     * declenche la défaite
     */
    private void defeat() {
        //adventurerView.displayDefeat()
        //TODO adventurerView.displayVictory()

        //this.endGame()
        //TODO this.endGame()
    }

    /**
     * Check if its dead to win
     */
    public void testDefeat() {
        if (waterScaleController.isDeadly() || this.treasureSink() || this.heliCellSink()) {
            this.defeat();
        }
    }

    /**
     * Check si les tresors restants ont coulés
     * @return
     */
    private boolean treasureSink() {
        ArrayList<Treasure> treasuresNotFound = gridController.getGrid().getTreasures();
        HashMap<Cell, Treasure> notFoundTreasureCells = new HashMap<>();
        for (Cell[] cells : gridController.getGrid().getCells()) {
            for (Cell cell : cells) {
                if (cell instanceof TreasureCell && cell.getState() != Utils.State.SUNKEN && treasuresNotFound.contains(((TreasureCell) cell).getTreasure())) {
                    notFoundTreasureCells.put(cell, ((TreasureCell) cell).getTreasure());
                }
            }
        }
        for (Treasure treasure : treasuresNotFound) {
            if (!notFoundTreasureCells.containsValue(treasure)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check si l'heliport a coulé
     * @return
     */
    private boolean heliCellSink() {
        Cell heliCell = null;
        for (Cell[] cells : gridController.getGrid().getCells()) {
            for (Cell cell : cells) {
                if (cell.getName() != null && cell.getName().equals("Heliport")) {
                    heliCell = cell;
                }
            }
        }
        return heliCell.getState() == Utils.State.SUNKEN;
    }
}
