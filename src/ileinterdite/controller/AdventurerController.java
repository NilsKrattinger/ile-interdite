package ileinterdite.controller;

import ileinterdite.model.Card;
import ileinterdite.model.Cell;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.helper.AdventurerControllerHelper;
import ileinterdite.view.AdventurerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdventurerController {

    // Information about other controllers
    private GameController controller; //< The controller controlling the whole game

    // The lists containing all information about the adventurers
    private ArrayList<Adventurer> adventurers; //< The list of adventurers in the game (aka. players)
    private HashMap<Adventurer, AdventurerView> adventurerViews; //< An association between adventurers and their views

    // The objects containing the information for the current turn
    private AdventurerView currentView; //< The view currently visible
    private Adventurer currentAdventurer; //< The adventurer acting during this turn
    private Adventurer currentActionAdventurer; //< The adventurer making the current action (may differ from currentAdventurer)

    // Action-specific variables
    Card selectedCard; //< The card selected by the player during a GIVE_CARD action
    Adventurer selectedAdventurer; //< The adventurer selected by the player during a GIVE_CARD action

    public AdventurerController(GameController c, ArrayList<Adventurer> adventurers, ArrayList<String> playerNames) {
        this.controller = c;
        this.adventurers = AdventurerControllerHelper.getPlayers(adventurers, playerNames);
        this.adventurerViews = AdventurerControllerHelper.createViews(adventurers, controller.getActionController());

        nextAdventurer();
    }

    /* ************* *
     * TURN HANDLING *
     * ************* */

    public void nextAdventurer() {
        changeCurrentAdventurer();
        currentAdventurer.newTurn();
    }

    private void changeCurrentAdventurer() {
        adventurers.add(adventurers.remove(0));
        currentAdventurer = adventurers.get(0);
        currentView = adventurerViews.get(currentAdventurer);
        controller.getWindow().setAdventurerView(currentView);
    }

    /* **************** *
     * RECEIVE MESSAGES *
     * **************** */

    public void handleAction(Message m) {

        switch (m.action) {
            case START_GIVE_CARD:
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

    public Utils.State[][] startCellAction(Message message) {
        return (message.action == Utils.Action.MOVE) ? initMove(currentAdventurer) : initDry(currentAdventurer);
    }

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     *
     * @param adventurer
     */
    public Utils.State[][] initMove(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Utils.State[][] states = adventurer.getAccessibleCells();
        controller.getGridController().getGridView().showSelectableCells(states);

        return states;
    }

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     *
     * @param adventurer
     */
    public Utils.State[][] initDry(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Utils.State[][] states = adventurer.getDryableCells();
        controller.getGridController().getGridView().showSelectableCells(states);

        return states;
    }


    /**
     *  Lance les actions pour le don d'une carte par l'aventurier adventurer (vérification qu'il y a
     *  bien un autre aventurier sur sa tuile, et lancement du choix de la carte à donner
     * @param adventurer : aventurier initiant le don de carte
     */
    public void initGiveCard(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Cell adventurerCell = controller.getGridController().getGrid().getCell(adventurer.getX(),adventurer.getY());
        int nbOfAdventurersOnCell = adventurerCell.getAdventurers().size();
        if (nbOfAdventurersOnCell >= 2) {
            ArrayList<Card> giverCards = currentAdventurer.getCards();
            //adventurerView.showTradableCards(giverCards);
            // TODO showTradableCards() method
        }
    }

    /* ************ *
     * MAKE ACTIONS *
     * ************ */

    public void movement(Tuple<Integer, Integer> pos) {
        movement(pos, currentActionAdventurer);
    }

    public void movement(Tuple<Integer, Integer> pos, Adventurer adv) {
        adv.move(pos.x, pos.y);
        controller.getGridController().getGridView().updateAdventurer(adv);
    }

    public void setSelectedCard(Message m) {
        selectedCard = this.currentAdventurer.getHand().getCard(m.message);
        if (selectedCard != null) {
            //adventurerView.chooseCardReceiver();
            // TODO chooseCardReceiver() method
        }
    }

    public void setSelectedAdventurer(Message m) {
        Adventurer receiver = this.getAdventurerFromName(m.message);
        int nbOfCardsInReceiverHand = receiver.getNumberOfCards();
        if (nbOfCardsInReceiverHand == 5 && selectedCard != null) {
            controller.getInterruptionController().initDiscard(receiver,selectedCard);
            // TODO method initDiscard() (already in feature-discard-treasure-cards
        } else {
            if (selectedCard != null) {
                currentAdventurer.getCards().remove(selectedCard);
                giveCard(receiver,selectedCard);
            }
        }
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
