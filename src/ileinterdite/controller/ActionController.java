package ileinterdite.controller;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Engineer;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.*;
import ileinterdite.util.helper.ActionControllerHelper;
import ileinterdite.view.PawnsSelectionView;

import java.util.ArrayList;

public class ActionController implements IObserver<Message> {

    private GameController controller; //< A reference to the main controller

    // View specific Variables
    private PawnsSelectionView pawnsSelectionView;

    // Variables used to handle all actions
    private Utils.Action currentAction; //< The action being processed
    private Utils.Action selectedAction; //< The selected action
    private static final int NB_ACTIONS_PER_TURN = 3; //< The maximum number of actions the player can make during a turn
    private int remainingActions; //< The number of actions remaining before the turn ends

    // Action specific variables
    private Utils.State[][] cellStates; //< The state of all states, if needed by the action
    private boolean isInterrupted; //< Whether the actions are interrupted or not
    private boolean engineerPower; //< The power of the engineer to dry two cells

    /**
     * Creates the controller that receives interactions made with the window
     * @param c A reference to the GameController
     */
    public ActionController(GameController c) {
        this.controller = c;
        this.isInterrupted = false;
        this.engineerPower = false;

        pawnsSelectionView = new PawnsSelectionView();
        pawnsSelectionView.addObserver(this);
    }

    @Override
    public void update(IObservable<Message> o, Message message) {
        if (isInterrupted) {
            controller.getInterruptionController().handleMessage(message);
            return;
        }

        currentAction = message.action;
        switch (currentAction) {
            case MOVE:
                setEngineerPower(false);
                if (controller.getCurrentAdventurer() instanceof Navigator) {
                    startInterruption();
                    controller.getInterruptionController().startNavigatorInterruption();
                    break; // In case the adventurer is a navigator, interrupts the action
                }
            case DRY: // MOVE case comes also here if the current adventurer is not a Navigator
                cellStates = controller.getAdventurerController().startCellAction(message);
                break;

            case START_GIVE_CARD: case GET_TREASURE:
                setEngineerPower(false);
                controller.startAdventurerAction(message);
                break;

            case GIVE_CARD_CARD_CHOICE:
                controller.getAdventurerController().setSelectedCard(message);
                break;

            case GIVE_CARD_RECEIVER_CHOICE:
                controller.getAdventurerController().setSelectedAdventurer(message);
                break;

            case VALIDATE_ACTION:
                if (selectedAction != null) {
                    validateAction(message);
                }
                break;

            case END_TURN:
                controller.endTurn();
                break;

            case CANCEL_ACTION:
                currentAction = null;
                break;
            case ADVENTURER_CHOICE:
                validateAction(message);
                break;

        }
        selectedAction = currentAction;

        if (remainingActions == 0) {
            controller.endTurn();
        }
    }

    /**
     * Starts a new turn
     */
    public void newTurn() {
        currentAction = null;
        selectedAction = null;
        setNbActions(NB_ACTIONS_PER_TURN);
    }

    /**
     * Handles the validation of an action from update
     * @param message The message containing necessary information for handling
     */
    private void validateAction(Message message) {
        switch (selectedAction) {
            case MOVE: case DRY:
                validateCellAction(message, selectedAction);
                break;
            case START_GIVE_CARD: //TODO Donner la carte
                break;
        }
    }

    /**
     * Handles the validation of an action using the cells
     * @param message The message containing necessary information for handling
     */
    private void validateCellAction(Message message, Utils.Action action) {
        Tuple<Integer, Integer> pos = ActionControllerHelper.getPositionFromMessage(message.message);
        if (ActionControllerHelper.checkPosition(pos, cellStates)) {
            switch (action) {
                case MOVE: // Adventurer action -> move
                    controller.getAdventurerController().movement(pos);
                    reduceNbActions();
                    break;
                case DRY: // Grid action -> dry
                    controller.getGridController().dry(pos);
                    if (controller.getCurrentAdventurer() instanceof Engineer) {
                        setEngineerPower(!engineerPower);
                    } else {
                        reduceNbActions();
                    }
                    break;
            }

            currentAction = null;
        }
    }

    /* **************** *
     * ACTIONS HANDLING *
     * **************** */

    /**
     * Set the number of actions remaining during this turn
     * @param nb The number to set, between 0 and NB_ACTIONS_PER_TURN
     */
    public void setNbActions(int nb) {
        this.remainingActions = Math.max(nb, NB_ACTIONS_PER_TURN);
        controller.getAdventurerController().getCurrentView().setNbActions(this.remainingActions);
    }

    /**
     * Reduce the number of actions by one
     */
    public void reduceNbActions() {
        this.remainingActions--;
        controller.getAdventurerController().getCurrentView().setNbActions(this.remainingActions);
    }

    /* ************* *
     * INTERRUPTIONS *
     * ************* */

    /**
     * Start an interruption action. Nothing else will work
     */
    public void startInterruption() {
        this.isInterrupted = true;
    }

    /**
     * End an interruption action. Everything is back at normal
     */
    public void endInterruption() {
        this.isInterrupted = false;
    }

    /* *********************** *
     * ACTION SPECIFIC METHODS *
     * *********************** */

    /**
     * Sets the power of the engineer (to dry twice).
     * @param power false to remove the ability to dry a second cell, it will reduce the number of actions if it was
     *              set to true
     */
    private void setEngineerPower(boolean power) {
        if (!power && engineerPower) {
            reduceNbActions();
        }

        engineerPower = power;
    }

    /* *********************** *
     * PAWN CHOICE METHODS     *
     * *********************** */

    public void choiceAdventuer(ArrayList<Adventurer> adventurers){
        pawnsSelectionView.update(adventurers);

    }

}
