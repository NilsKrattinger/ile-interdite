package ileinterdite.controller;

import ileinterdite.model.Card;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.TreasureCard;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.helper.ActionControllerHelper;
import ileinterdite.util.helper.InterruptionControllerHelper;

import java.util.ArrayList;

public class InterruptionController {

    private GameController controller; //< A reference to the main controller

    private Utils.Action currentAction; //< The current action in the interruption
    private Adventurer currentActionAdventurer; //< The adventurer doing the current action

    // Interruption specific variables
    private ArrayList<Adventurer> adventurersToRescue;

    // Action specific variables
    private Utils.State[][] cellStates; //< The state of all states, if needed by the action

    public InterruptionController(GameController c) {
        this.controller = c;
        adventurersToRescue = new ArrayList<>();
    }

    public void handleMessage(Message m) {
        switch (currentAction) {
            case DISCARD:
                discard(currentActionAdventurer.getHand().getCard(m.message), currentActionAdventurer);
                break;
            case RESCUE:
                rescue(ActionControllerHelper.getPositionFromMessage(m.message));
                break;
            case NAVIGATOR_CHOICE:
                /*currentAction = Utils.Action.MOVE;
                // The message contains a string with the format "ClassName (PlayerName)"
                currentActionAdventurer = findAdventurerByClassName(m.message.substring(0, m.message.indexOf(' ')));
                if (currentActionAdventurer != null) {
                    initMovement(currentActionAdventurer);
                }
                break;*/
        }
    }

    /* *************** *
     * PREPARE ACTIONS *
     * *************** */
    public void setRescueList(ArrayList<Adventurer> rescueList) {
        this.adventurersToRescue = rescueList;
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
     * @param card
     */
    public void initDiscard(Adventurer adventurer, Card card) {
        ArrayList<Card> handCards = adventurer.getCards();
        adventurer.getHand().clearHand();
        if (card != null) {
            handCards.add(card);
        }

        currentAction = Utils.Action.DISCARD;
        //adventurerView.askCardToDiscard(handCards);
        // TODO method askCardToDiscard()
    }

    public void startNavigatorInterruption() {
        //controller.getAdventurerController().getCurrentView().showAdventurers(controller.getAdventurers());
        currentAction = Utils.Action.NAVIGATOR_CHOICE;
        controller.getActionController().endInterruption();
        // TODO implements navigator interaction
    }

    /* ************** *
     * HANDLE ACTIONS *
     * ************** */

    /**
     *
     */
    public void discard(Card card, Adventurer adventurer) {
        DiscardPile discardTreasureCards;
        if (card instanceof TreasureCard) {
            discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.Treasure);
        } else {
            discardTreasureCards = controller.getDeckController().getDiscardPile(Utils.CardType.Flood);
        }

        discardTreasureCards.addCard(card);
        adventurer.getCards().remove(card);
        if (adventurer.getCards().size() <= 5) {
            controller.getActionController().endInterruption();
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
