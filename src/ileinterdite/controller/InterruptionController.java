package ileinterdite.controller;

import ileinterdite.model.Card;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Hand;
import ileinterdite.model.TreasureCard;
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

    private ArrayList<Adventurer> adventurersToRescue;
    private Utils.State[][] cellStates; //< The state of all states, if needed by the action


    public InterruptionController(GameController c) {
        this.controller = c;
        adventurersToRescue = new ArrayList<>();
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
                    String[] adventurerClass = InterruptionControllerHelper.splitAdventurerClassName(m.message);
                    currentActionAdventurer = findAdventurerByClassName(adventurerClass[0]);
                    if (currentActionAdventurer != null) {
                        if (currentActionAdventurer instanceof Navigator) {
                            cellStates = controller.getAdventurerController().initMove(currentActionAdventurer);
                        } else {
                            cellStates = controller.getAdventurerController().initPowerNavigatorMovement(currentActionAdventurer);
                        }
                    }
                } else {
                    controller.getActionController().stopInterruption();

                }
                break;
            case MOVE:
                Tuple<Integer, Integer> pos = ActionControllerHelper.getPositionFromMessage(m.message);
                if (ActionControllerHelper.checkPosition(pos, cellStates)) {
                    controller.getAdventurerController().movement(pos, currentActionAdventurer);
                    controller.getActionController().reduceNbActions();
                    controller.getActionController().endInterruption();
                }
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
            //TODO FONCTION PERDUUUUUUU T'es NULLLLL
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
                if (card.getCardName().equals(cardName) && cardAdded == false) {
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
        controller.getActionController().choiceAdventuer(controller.getAdventurers());
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
            discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.Treasure);
        } else {
            discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.Flood);
        }
    }

    public void rescue(Tuple<Integer, Integer> pos) {
        if (pos != null) {
            if (ActionControllerHelper.checkPosition(pos, cellStates)) {
                controller.getAdventurerController().movement(pos, currentActionAdventurer);
            }
        }
        adventurersToRescue.remove(0);
        if (!adventurersToRescue.isEmpty()){
            initRescue();
        } else {
            controller.newTurn();
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
