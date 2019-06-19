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
}
