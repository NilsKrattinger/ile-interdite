package ileinterdite.controller;

import ileinterdite.model.Card;
import ileinterdite.model.Cell;
import ileinterdite.model.Hand;
import ileinterdite.model.*;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Messager;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.helper.ActionControllerHelper;
import ileinterdite.util.helper.AdventurerControllerHelper;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.HandView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdventurerController {

    // Information about other controllers
    private GameController controller; //< The controller controlling the whole game

    // The lists containing all information about the adventurers
    private ArrayList<Adventurer> adventurers; //< The list of adventurers in the game (aka. players)
    private HashMap<Adventurer, AdventurerView> adventurerViews; //< An association between adventurers and their view
    private HashMap<Adventurer, HandView> adventurerHandViews; //< An association between adventurers and their view representing their hand

    // The objects containing the information for the current turn
    private AdventurerView currentView; //< The view currently visible
    private HandView currentHandView; //< The hand currently visible
    private Adventurer currentAdventurer; //< The adventurer acting during this turn
    private Adventurer currentActionAdventurer; //< The adventurer making the current action (may differ from currentAdventurer)

    // Action-specific variables
    private Card selectedCard; //< The card selected by the player during a GIVE_CARD action
    private ArrayList<Adventurer> giveCardList; //< The list of adventurers that the player can send cards to

    /**
     * Creates the controller that handles everything the Adventurers do
     * @param c A reference to the GameController
     * @param adventurers The list of adventurers in the game
     * @param playerNames The names to be given to the adventurers
     */
    public AdventurerController(GameController c, ArrayList<Adventurer> adventurers, ArrayList<String> playerNames) {
        this.controller = c;
        this.adventurers = AdventurerControllerHelper.getPlayers(adventurers, playerNames);

        AdventurerControllerHelper.createViews(adventurers, controller.getActionController());
        this.adventurerViews = AdventurerControllerHelper.getAdventurerViews();
        this.adventurerHandViews = AdventurerControllerHelper.getAdventurerHandViews();
    }

    public void finishAdventurerInit() {
        Deck treasureCardsDeck = controller.getDeckController().getDeck(Utils.CardType.TREASURE);

        for (Adventurer adv : adventurers) {
            ArrayList<Card> advCards = adv.getHand().getCards();
            while (advCards.size() != 2) {
                Card card = treasureCardsDeck.drawCards(1).get(0);
                if (card.getCardName().equalsIgnoreCase("Montee des eaux")) {
                    ArrayList<Card> tempList = new ArrayList<>();
                    tempList.add(card);
                    treasureCardsDeck.addAtTheTop(tempList);
                    treasureCardsDeck.shuffle();
                } else {
                    advCards.add(card);
                }
            }
            adventurerHandViews.get(adv).update(adv);
        }

        controller.getWindow().setHandViews(this.adventurerHandViews);

        nextAdventurer();
    }

    /* ************* *
     * TURN HANDLING *
     * ************* */

    /**
     * Switch actions to the next adventurer
     */
    public void nextAdventurer() {
        changeCurrentAdventurer();
        currentAdventurer.newTurn();
    }

    /**
     * Change the current adventurer with the new adventurer and update views
     */
    private void changeCurrentAdventurer() {
        if (currentAdventurer != null) {
            currentHandView.update(currentAdventurer);
        }

        adventurers.add(adventurers.remove(0));
        currentAdventurer = adventurers.get(0);
        currentView = adventurerViews.get(currentAdventurer);
        currentHandView = adventurerHandViews.get(currentAdventurer);
        controller.getWindow().setAdventurerView(currentView);
    }

    /* **************** *
     * RECEIVE MESSAGES *
     * **************** */

    /**
     * Handles a message received from the ActionController
     * @param m The message sent by the view
     */
    public void handleAction(Message m) {

        switch (m.action) {
            case GIVE_CARD:
                initGiveCard(currentAdventurer);
                break;

            case GET_TREASURE:
                controller.getGridController().collectTreasure(currentAdventurer);
                break;
        }
    }

    /* ************* *
     * INIT ACTIONS  *
     * ************* */

    /**
     * Start an action that impacts a cell
     * @param message The message received from the view
     * @return
     */
    public Utils.State[][] startCellAction(Message message) {
        return (message.action == Utils.Action.MOVE) ? initMove(currentAdventurer) : initDry(currentAdventurer);
    }

    /**
     * Start the movement action
     * @param adventurer The adventurer that will start the movement
     * @return The list of states with either ACCESSIBLE or INACCESSIBLE
     */
    public Utils.State[][] initMove(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Utils.State[][] states = adventurer.getAccessibleCells();
        controller.getGridController().getGridView().showSelectableCells(states);

        return states;
    }

    public Utils.State[][] initPowerNavigatorMovement(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Utils.State[][] states = adventurer.getPowerNavigatorAccessibleCells();
        controller.getGridController().getGridView().showSelectableCells(states);

        return states;
    }

    /**
     * Starts the action to dry a cell
     * @param adventurer The adventurer that will dry the cell
     * @return The list of states with either ACCESSIBLE or INACCESSIBLE
     */
    public Utils.State[][] initDry(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Utils.State[][] states = adventurer.getDryableCells();
        controller.getGridController().getGridView().showSelectableCells(states);

        return states;
    }


    /**
     * Starts the action to give a card
     * @param adventurer The adventurer that will give one of its cards
     */
    public void initGiveCard(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Cell adventurerCell = controller.getGridController().getGrid().getCell(adventurer.getX(), adventurer.getY());
        int nbOfAdventurersOnCell = adventurerCell.getAdventurers().size();
        if ((nbOfAdventurersOnCell >= 2 || adventurer instanceof Messager) && adventurer.getNumberOfCards() > 0) {
            giveCardList = new ArrayList<>((adventurer instanceof Messager) ? getAdventurers() : adventurerCell.getAdventurers());
            giveCardList.remove(adventurer);
            controller.getActionController().chooseCards(currentAdventurer.getCards(), 1, "donner");
        }
    }

    /* ************ *
     * MAKE ACTIONS *
     * ************ */

    /**
     * Move the current adventurer
     * @param pos The position where the adventurer will move
     */
    public void movement(Tuple<Integer, Integer> pos) {
        movement(pos, currentActionAdventurer);
    }

    /**
     * Move an adventurer
     * @param pos The position where the adventurer will move
     * @param adv The adventurer to move
     */
    public void movement(Tuple<Integer, Integer> pos, Adventurer adv) {
        adv.move(pos.x, pos.y);
        controller.getGridController().getGridView().updateAdventurer(adv);
    }

    public void selectGiveCard(Message m) {
        String selectedCardName = ActionControllerHelper.splitSelectionViewNames(m.message)[0];
        for (Card card : currentActionAdventurer.getCards()) {
            if (card.getCardName().equals(selectedCardName)) {
                selectedCard = card;
                break;
            }
        }

        controller.getActionController().chooseAdventurers(giveCardList, 1, false, true);
    }

    public void selectGiveAdventurer(Message m) {
        String selectedAdventurerName = ActionControllerHelper.splitSelectionViewNames(m.message)[0];
        for (Adventurer adv : giveCardList) {
            if (adv.getClassName().equalsIgnoreCase(selectedAdventurerName)) {
                currentActionAdventurer.getCards().remove(selectedCard);
                adventurerHandViews.get(currentActionAdventurer).update(currentActionAdventurer);
                this.controller.getActionController().reduceNbActions();

                int nbOfCardsInReceiverHand = adv.getNumberOfCards();
                if (nbOfCardsInReceiverHand == 5) {
                    ArrayList<Card> tempAdventurerHandCards = new ArrayList<>(adv.getCards());
                    tempAdventurerHandCards.add(selectedCard);
                    adv.getHand().clearHand();
                    this.controller.getInterruptionController().initDiscard(adv, tempAdventurerHandCards);
                } else {
                    giveCard(adv, selectedCard);
                    adventurerHandViews.get(adv).update(adv);
                }
            }
        }
    }

    /**
     * Ajoute la carte card à la main de aventurier adventurer s'il a moins du nombre max de cartes dans sa main
     * @param adventurer
     * @param card
     */
    public void giveCard(Adventurer adventurer, Card card) {
        if (adventurer != null && card != null && adventurer.getNumberOfCards() < Hand.NB_MAX_CARDS) {
            adventurer.getCards().add(card);
        }
    }

    /* ***************** *
     * GETTERS & SETTERS *
     * ***************** */
    public ArrayList<Adventurer> getAdventurers() {
        return adventurers;
    }

    public Adventurer getAdventurerFromName(String adventurerName) {
        for (Adventurer adventurer : this.getAdventurers()) {
            if (adventurer.getName().equals(adventurerName)) {
                return adventurer;
            }
        }
        return null;
    }

    public Adventurer getCurrentAdventurer() {
        return currentAdventurer;
    }

    public AdventurerView getCurrentView() {
        return currentView;
    }

    public HandView getHandViewFor(Adventurer adv) {
        return adventurerHandViews.get(adv);
    }
}
