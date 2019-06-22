package ileinterdite.controller;

import ileinterdite.model.Card;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Hand;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.helper.ActionControllerHelper;
import ileinterdite.util.helper.InterruptionControllerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InterruptionController {

    private GameController controller; //< A reference to the main controller

    private Utils.Action currentAction; //< The current action in the interruption
    private Adventurer currentActionAdventurer; //< The adventurer doing the current action

    // Interruption specific variables
    private ArrayList<Adventurer> adventurersToRescue; //< The list of adventurers that are drowning and must be moved before next turn
    private Utils.State[][] cellStates; //< The state of all states, if needed by the action

    private boolean isTurnEnd; //< Check if the discard action is called at the end of a turn
    private ArrayList<Adventurer> selectableAdventurers; //< The list of adventurers that can be chosen for the helicopter group
    private ArrayList<Adventurer> helicopterList; //< The list of passengers in the helicopter
    private ArrayList<Card> cardsToDiscard; //< The cards chosen to discard


    public InterruptionController(GameController c) {
        this.controller = c;
        adventurersToRescue = new ArrayList<>();
        helicopterList = new ArrayList<>();
    }

    public void handleMessage(Message m) {
        if (currentAction == null) {
            controller.getActionController().endInterruption();
            return;
        }

        switch (currentAction) {
            case DISCARD:
                discard(m);
                break;
            case RESCUE:
                rescue(m, ActionControllerHelper.getPositionFromMessage(m.message));
                break;
            case NAVIGATOR_CHOICE:
                if (m.action != Utils.Action.CANCEL_ACTION) {
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
                    controller.getActionController().endInterruption();
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
            case SAND_CARD_ACTION:
            case HELICOPTER_CARD_CELL_CHOICE:
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
        controller.getActionController().startInterruption();
        if (InterruptionControllerHelper.isSavePossible(cellStates)) {
            Utils.showWarning("ATTENTION l'aventurier " + currentActionAdventurer.getName() + " boit la tasse, Choisissez vite une case jusqu'à laquelle il va nager !");
            controller.getGridController().getGridView().showSelectableCells(cellStates);
        } else {
            controller.defeat(false, false, false, true);
        }
    }

    public void initDiscard(Adventurer adventurer, ArrayList<Card> cards, boolean endTurn) {
        this.isTurnEnd = endTurn;
        currentAction = Utils.Action.DISCARD;
        controller.getActionController().startInterruption();

        currentActionAdventurer = adventurer;

        cardsToDiscard = cards;
        controller.getActionController().chooseCards(cards, cards.size() - Hand.NB_MAX_CARDS, "défausser");
    }

    public void startNavigatorInterruption() {
        currentAction = Utils.Action.NAVIGATOR_CHOICE;
        controller.getActionController().chooseAdventurers(controller.getAdventurers(), 1, false, true);
    }

    public void startCardInterruption(Message m) {
        cellStates = controller.getGridController().getGrid().getStateOfCells();
        currentActionAdventurer = m.adventurer;

        Card c = InterruptionControllerHelper.getTreasureCard(m.adventurer, Integer.valueOf(m.message));
        if (c.getCardName().equalsIgnoreCase("Sacs de sable")) {
            startSandCardInterruption();
        } else if (c.getCardName().equalsIgnoreCase("Helicoptère")) {
            if (InterruptionControllerHelper.checkVictory(controller.getGridController().getGrid(), m.adventurer, controller.getAdventurers())) {
                controller.victory();
            } else {
                startHelicopterCardInterruption(controller.getGridController().getGrid().getCell(m.adventurer.getX(), m.adventurer.getY()).getAdventurers());
            }
        }

        controller.getActionController().startInterruption();
        controller.getDeckController().getDiscardPile(Utils.CardType.TREASURE).addCard(c);
    }

    private void startSandCardInterruption() {
        currentAction = Utils.Action.SAND_CARD_ACTION;
        ArrayList<Utils.State> acceptableStates = new ArrayList<>();
        acceptableStates.add(Utils.State.FLOODED);
        InterruptionControllerHelper.getTreasureCardCells(cellStates, acceptableStates);
        controller.getGridController().getGridView().showSelectableCells(cellStates);
    }

    private void startHelicopterCardInterruption(ArrayList<Adventurer> adventurersOnCell) {
        helicopterList.clear();

        currentAction = Utils.Action.HELICOPTER_CARD_ADVENTURER_CHOICE;
        selectableAdventurers = new ArrayList<>(adventurersOnCell);
        controller.getActionController().chooseAdventurers(selectableAdventurers, selectableAdventurers.size(), true, false);
    }

    /* ************** *
     * HANDLE ACTIONS *
     * ************** */

    /**
     *
     */
    private void discard(Message m) {
        if (m.action == Utils.Action.CARD_CHOICE) {
            DiscardPile discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.TREASURE);

            ArrayList<Card> cardsInHand = new ArrayList<>(cardsToDiscard);
            List<String> cardNames = new LinkedList<>(Arrays.asList(ActionControllerHelper.splitSelectionViewNames(m.message)));

            for (Card card : cardsInHand) {
                int index = cardNames.indexOf(card.getCardName());
                if (index != -1) {
                    cardNames.remove(index);
                    cardsToDiscard.remove(card);
                    discardTreasureCards.addCard(card);
                }
            }

            currentActionAdventurer.getCards().addAll(cardsToDiscard);
            controller.getAdventurerController().getHandViewFor(currentActionAdventurer).update(currentActionAdventurer);
            controller.getActionController().endInterruption();
            if (isTurnEnd) {
                controller.drawFloodCards();
            }
        }
    }

    private void rescue(Message m, Tuple<Integer, Integer> pos) {
        if (m.action == Utils.Action.VALIDATE_ACTION) {
            if (ActionControllerHelper.checkPosition(pos, cellStates)) {
                controller.getAdventurerController().movement(pos, currentActionAdventurer);

                adventurersToRescue.remove(0);
                if (!adventurersToRescue.isEmpty()) {
                    initRescue();
                } else {
                    controller.newTurn();
                    controller.getActionController().endInterruption();
                }
            }
        }
    }

    private void selectHelicopterPassengers(Message m) {
        if (m.action == Utils.Action.ADVENTURER_CHOICE) {
            String[] names = ActionControllerHelper.splitSelectionViewNames(m.message);
            if (names.length > 0) {
                for (String name : names) {
                    helicopterList.add(findAdventurerByClassName(name));
                }

                showHelicopterCells();
            } else {
                controller.getActionController().chooseAdventurers(selectableAdventurers, selectableAdventurers.size(), true, false);
            }
        }
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
        if (m.action == Utils.Action.VALIDATE_ACTION) {
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
    }

    /* ***************** *
     * GETTERS & SETTERS *
     * ***************** */
    public ArrayList<Adventurer> getAdventurersToRescue() {
        return adventurersToRescue;
    }
}
