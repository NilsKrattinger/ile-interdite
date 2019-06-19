package ileinterdite.controller;

import ileinterdite.model.Card;
import ileinterdite.model.Cell;
import ileinterdite.model.adventurers.*;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.helper.AdventurerControllerHelper;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.CardGivingView;
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
    private CardGivingView cardGivingView;
    private Card selectedCard; //< The card selected by the player during a GIVE_CARD action
    private Adventurer selectedAdventurer; //< The adventurer selected by the player during a GIVE_CARD action

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
        adventurers.add(adventurers.remove(0));
        currentAdventurer = adventurers.get(0);
        currentView = adventurerViews.get(currentAdventurer);
        currentHandView = adventurerHandViews.get(currentAdventurer);
        controller.getWindow().setAdventurerView(currentView);
        currentHandView.update(currentAdventurer);
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
        Cell adventurerCell = controller.getGridController().getGrid().getCell(adventurer.getX(),adventurer.getY());
        int nbOfAdventurersOnCell = adventurerCell.getAdventurers().size();
        if ((nbOfAdventurersOnCell >= 2 && adventurer.getNumberOfCards() > 0) || adventurer instanceof Messager) {
            cardGivingView = new CardGivingView(this.controller);
            ArrayList<Card> giverCards = new ArrayList<>();
            giverCards.addAll(currentAdventurer.getCards());

            cardGivingView.showTradableCards(giverCards);

            String selectedCardName = cardGivingView.getSelectedCardName();
            for (Card card : giverCards) {
                if (card.getCardName().equals(selectedCardName)) {
                    selectedCard = card;
                }
            }

            ArrayList<Adventurer> potentialAdventurers = new ArrayList<>();
            for (Adventurer adv : adventurers) {
                if ((currentAdventurer instanceof Messager && adv != currentAdventurer)
                    || (adv != currentAdventurer
                        && adv.getY() == currentAdventurer.getY()
                        && adv.getX() == currentAdventurer.getX())) {
                    potentialAdventurers.add(adv);
                }
            }
            cardGivingView.showPotentialReceivers(potentialAdventurers);

            String receiverName = cardGivingView.getReceiverName();
            selectedAdventurer = getAdventurerFromName(receiverName);

            if (potentialAdventurers.contains(selectedAdventurer)) {
                int nbOfCardsInReceiverHand = selectedAdventurer.getNumberOfCards();
                if (nbOfCardsInReceiverHand == 5) {
                    ArrayList<Card> tempAdventurerHandCards = new ArrayList<>();
                    tempAdventurerHandCards.addAll(selectedAdventurer.getCards());
                    selectedAdventurer.getHand().clearHand();
                    this.controller.getInterruptionController().initDiscard(selectedAdventurer, tempAdventurerHandCards);
                }
                giveCard(selectedAdventurer, selectedCard);
                adventurer.getCards().remove(selectedCard);
                this.controller.getActionController().reduceNbActions();
            }
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

    /**
     * Ajoute la carte card à la main de aventurier adventurer s'il a moins de 5 cartes dans sa main
     * @param adventurer
     * @param card
     */
    public void giveCard(Adventurer adventurer, Card card) {
        if (adventurer != null && card != null && adventurer.getNumberOfCards()<5) {
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
}
