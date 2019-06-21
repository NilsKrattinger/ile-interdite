package ileinterdite.controller;

import ileinterdite.model.*;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.helper.ActionControllerHelper;
import ileinterdite.util.helper.InterruptionControllerHelper;
import ileinterdite.view.DiscardView;

import java.util.ArrayList;

public class InterruptionController {

    private GameController controller; //< A reference to the main controller

    private Utils.Action currentAction; //< The current action in the interruption
    private Adventurer currentActionAdventurer; //< The adventurer doing the current action

    // Interruption specific variables
    private DiscardView discardView; //< The view to show when a discard action is going

    private ArrayList<Adventurer> adventurersToRescue; //< The list of adventurers that are drowning and must be moved before next turn
    private Utils.State[][] cellStates; //< The state of all states, if needed by the action

    private ArrayList<Adventurer> helicopterList;


    public InterruptionController(GameController c) {
        this.controller = c;
        adventurersToRescue = new ArrayList<>();
        helicopterList = new ArrayList<>();
        discardView = new DiscardView();
    }

    public void handleMessage(Message m) {
        switch (currentAction) {
            case DISCARD:
                discard(currentActionAdventurer.getHand().getCard(m.message));
                break;
            case RESCUE:
                rescue(ActionControllerHelper.getPositionFromMessage(m.message));
                break;
            case NAVIGATOR_CHOICE:
                if(m.action != Utils.Action.CANCEL_ACTION) {
                    currentAction = Utils.Action.MOVE;
                    String[] adventurerClass = ActionControllerHelper.splitSelectionViewNames(m.message);
                    currentActionAdventurer = findAdventurerByClassName(adventurerClass[0]);
                    if (currentActionAdventurer != null) {
                        if (currentActionAdventurer instanceof Navigator) {
                            cellStates = controller.getAdventurerController().initMove(currentActionAdventurer);
                        } else {
                            cellStates = controller.getAdventurerController().initPowerNavigatorMovement(currentActionAdventurer);
                        }
                    }

                    if (!InterruptionControllerHelper.isMovementPossible(cellStates)) {
                        controller.getActionController().endInterruption();
                    }
                } else {
                    controller.getActionController().stopInterruption();
                }
                break;
            case MOVE:
                Tuple<Integer, Integer> pos = ActionControllerHelper.getPositionFromMessage(m.message);
                if (ActionControllerHelper.checkPosition(pos, cellStates)) {
                    controller.getAdventurerController().movement(pos, currentActionAdventurer);
                    controller.getActionController().endInterruption();
                    controller.getActionController().reduceNbActions();
                }
                break;
            case HELICOPTER_CARD_ADVENTURER_CHOICE:
                selectHelicopterPassengers(m);
                break;
            case SAND_CARD_ACTION: case HELICOPTER_CARD_CELL_CHOICE:
                validateTreasureCardCell(m);
                break;
        }
    }

    /* *************** *
     * PREPARE ACTIONS *
     * *************** */
    public void setRescueList(ArrayList<Adventurer> rescueList) {
        this.adventurersToRescue = rescueList;
    }

    /**
     * Get the adventurer with the given class name.
     *
     * @return Null if not found
     */
    private Adventurer findAdventurerByClassName(String name) {
        Adventurer adv;
        int i = 0;
        do {
            adv = controller.getAdventurers().get(i);
            i++;
        } while (i < controller.getAdventurers().size() && !name.equalsIgnoreCase(adv.getClassName()));

        return (name.equalsIgnoreCase(adv.getClassName())) ? adv : null;
    }

    /* ************ *
     * INIT ACTIONS *
     * ************ */

    public void initRescue() {
        currentActionAdventurer = adventurersToRescue.get(0);
        currentAction = Utils.Action.RESCUE;
        cellStates = currentActionAdventurer.getRescueCells();
        if(InterruptionControllerHelper.isSavePossible(cellStates)) {
            Utils.showInformation("ATTENTION l'aventurier " + currentActionAdventurer.getName() + " boit la tasse, Choisissez vite une case jusqu'à laquelle il va nager !");
            controller.getGridController().getGridView().showSelectableCells(cellStates);
        } else {
            controller.defeat();
        }
    }

    /**
     *
     * @param adventurer
     */
    public void initDiscard(Adventurer adventurer, ArrayList<Card> cards) {

        ArrayList<String> cardNamesToDiscard = discardView.getCardsToDiscard(cards,cards.size() - Hand.NB_MAX_CARDS);
        ArrayList<Card> cardsToDiscard = new ArrayList<>();
        boolean cardAdded;
        for (String cardName : cardNamesToDiscard) {
            cardAdded = false;
            for (Card card : cards) {
                if (card.getCardName().equals(cardName) && !cardAdded) {
                    cardsToDiscard.add(card);
                    cardAdded = true;
                }
            }
        }
        for (Card card : cardsToDiscard) {
            discard(card);
            cards.remove(card);
        }

        for (Card card : cards) {
            controller.getAdventurerController().giveCard(adventurer,card);
        }

        controller.getActionController().endInterruption();
    }

    public void startNavigatorInterruption() {
        currentAction = Utils.Action.NAVIGATOR_CHOICE;
        controller.getActionController().chooseAdventurers(controller.getAdventurers(), 1, false, true);
    }

    public void startCardInterruption(Message m) {
        controller.getActionController().startInterruption();
        cellStates = controller.getGridController().getGrid().getStateOfCells();
        currentActionAdventurer = m.adventurer;

        Card c = InterruptionControllerHelper.getTreasureCard(m.adventurer, Integer.valueOf(m.message));
        if (c.getCardName().equalsIgnoreCase("Sacs de sable")) {
            startSandCardInterruption();
        } else if (c.getCardName().equalsIgnoreCase("Helicoptère")) {
            if (InterruptionControllerHelper.checkVictory(controller.getGridController().getGrid(), m.adventurer, controller.getAdventurers())) {
                controller.victory();
            } else {
                startHelicopterCardInterruption(m.adventurer,
                        controller.getGridController().getGrid().getCell(m.adventurer.getX(), m.adventurer.getY()).getAdventurers());
            }
        }

        controller.getDeckController().getDiscardPile(Utils.CardType.TREASURE).addCard(c);
    }

    private void startSandCardInterruption() {
        currentAction = Utils.Action.SAND_CARD_ACTION;
        ArrayList<Utils.State> acceptableStates = new ArrayList<>();
        acceptableStates.add(Utils.State.FLOODED);
        InterruptionControllerHelper.getTreasureCardCells(cellStates, acceptableStates);
        controller.getGridController().getGridView().showSelectableCells(cellStates);
    }

    private void startHelicopterCardInterruption(Adventurer currAdv, ArrayList<Adventurer> adventurersOnCell) {
        helicopterList.clear();
        helicopterList.add(currAdv);

        currentAction = Utils.Action.HELICOPTER_CARD_ADVENTURER_CHOICE;
        ArrayList<Adventurer> selectableAdventurers = new ArrayList<>(adventurersOnCell);
        selectableAdventurers.remove(currAdv);
        if (selectableAdventurers.size() > 0) {
            controller.getActionController().chooseAdventurers(selectableAdventurers, selectableAdventurers.size(), true, false);
        } else {
            showHelicopterCells();
        }
    }

    /* ************** *
     * HANDLE ACTIONS *
     * ************** */

    /**
     *
     */
    public void discard(Card card) {
        DiscardPile discardTreasureCards;
        if (card instanceof TreasureCard) {
            discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.TREASURE);
        } else {
            discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.FLOOD);
        }
    }

    public void rescue(Tuple<Integer, Integer> pos) {
        if (ActionControllerHelper.checkPosition(pos, cellStates)) {
            controller.getAdventurerController().movement(pos, currentActionAdventurer);

            adventurersToRescue.remove(0);
            if (!adventurersToRescue.isEmpty()){
                initRescue();
            } else {
                controller.newTurn();
                controller.getActionController().endInterruption();
            }
        }
    }

    private void selectHelicopterPassengers(Message m) {
        String[] names = ActionControllerHelper.splitSelectionViewNames(m.message);
        for (String name : names) {
            helicopterList.add(findAdventurerByClassName(name));
        }

        showHelicopterCells();
    }

    private void showHelicopterCells() {
        currentAction = Utils.Action.HELICOPTER_CARD_CELL_CHOICE;
        ArrayList<Utils.State> acceptableStates = new ArrayList<>();
        acceptableStates.add(Utils.State.FLOODED);
        acceptableStates.add(Utils.State.NORMAL);
        InterruptionControllerHelper.getTreasureCardCells(cellStates, acceptableStates);
        cellStates[currentActionAdventurer.getY()][currentActionAdventurer.getX()] = Utils.State.INACCESSIBLE; // The adventurer cannot move on the same cell
        controller.getGridController().getGridView().showSelectableCells(cellStates);
    }

    private void validateTreasureCardCell(Message m) {
        Tuple<Integer, Integer> pos = ActionControllerHelper.getPositionFromMessage(m.message);
        if (ActionControllerHelper.checkPosition(pos, cellStates)) {
            switch (currentAction) {
                case HELICOPTER_CARD_CELL_CHOICE:
                    for (Adventurer adv : helicopterList) {
                        controller.getAdventurerController().movement(pos, adv);
                    }
                    break;
                case SAND_CARD_ACTION:
                    controller.getGridController().dry(pos);
                    break;
            }

            controller.getAdventurerController().getHandViewFor(currentActionAdventurer).update(currentActionAdventurer);
            controller.getActionController().endInterruption();
        }
    }

    /* ***************** *
     * GETTERS & SETTERS *
     * ***************** */
    public ArrayList<Adventurer> getAdventurersToRescue() {
        return adventurersToRescue;
    }
}
