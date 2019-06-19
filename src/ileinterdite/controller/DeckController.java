package ileinterdite.controller;

import ileinterdite.factory.DeckFactory;
import ileinterdite.factory.DiscardPileFactory;
import ileinterdite.model.*;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Parameters;
import ileinterdite.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class DeckController {

    private GameController controller; //< A reference to the main controller

    private HashMap<Utils.CardType, Deck> decks;
    private HashMap<Utils.CardType, DiscardPile> discardPiles;

    public DeckController(GameController c) {
        this.controller = c;

        initDecks(controller.getGridController().getGrid());
    }

    /**
     * Create all of the deck and cards
     *
     * @param grid
     */
    private void initDecks(Grid grid) {
        Deck deckTmp;
        DiscardPile discardPileTmp;
        this.decks = new HashMap<>();
        deckTmp = DeckFactory.deckFactory(Utils.CardType.Flood, grid);
        deckTmp.shuffle();
        decks.put(deckTmp.getCardType(), deckTmp);

        this.discardPiles = new HashMap<>();
        discardPileTmp = DiscardPileFactory.discardPileFactory(Utils.CardType.Flood);
        deckTmp.setDiscardPile(discardPileTmp);
        discardPiles.put(discardPileTmp.getCardType(), discardPileTmp);

        deckTmp = DeckFactory.deckFactory(Utils.CardType.Treasure, grid);
        deckTmp.shuffle();
        decks.put(deckTmp.getCardType(), deckTmp);

        discardPileTmp = DiscardPileFactory.discardPileFactory(Utils.CardType.Treasure);
        deckTmp.setDiscardPile(discardPileTmp);
        discardPiles.put(discardPileTmp.getCardType(), discardPileTmp);
    }

    public void drawTreasureCards(int nbCard) {
        ArrayList<Card> drawedCards = this.decks.get(Utils.CardType.Treasure).drawCards(nbCard);
        ArrayList<Card> tempAdventurerHandCards = new ArrayList<>(controller.getCurrentAdventurer().getCards());
        controller.getCurrentAdventurer().getHand().clearHand();
        for (Card drawedCard : drawedCards) {
            if (!drawedCard.getCardName().equalsIgnoreCase("Montée des eaux")) {
                tempAdventurerHandCards.add(drawedCard);
            } else {
                controller.getWaterScaleController().increaseWaterScale();
                if (!discardPiles.get(Utils.CardType.Flood).getCards().isEmpty()) {
                    this.discardPiles.get(Utils.CardType.Flood).shuffle();
                    ArrayList<Card> discardFloodCards = this.discardPiles.get(Utils.CardType.Flood).getCards();
                    this.decks.get(Utils.CardType.Flood).addAtTheTop(discardFloodCards);
                    this.discardPiles.get(Utils.CardType.Flood).clearPile();
                }
                this.discardPiles.get(Utils.CardType.Treasure).addCard(drawedCard);
            }
        }
        if (tempAdventurerHandCards.size() > Hand.NB_MAX_CARDS) {
            controller.getInterruptionController().initDiscard(controller.getCurrentAdventurer(), tempAdventurerHandCards);
        } else {
            for (Card card : tempAdventurerHandCards) {
                controller.getAdventurerController().giveCard(controller.getCurrentAdventurer(), card);
            }
        }
    }

    /**
     * Change state of card et put in the discard pile if needed.
     * @param nbCard
     */
    public void drawFloodCards(int nbCard) {
        ArrayList<Card> drawedCard = decks.get(Utils.CardType.Flood).drawCards(nbCard);
        ArrayList<Adventurer> rescueList = new ArrayList<>();
        Utils.State state;
        for (Card card : drawedCard) {
            FloodCard floodCard;
            floodCard = (FloodCard) card;
            Cell linkedCell = floodCard.getLinkedCell();
            state = linkedCell.getState();
            switch (state) {
                case NORMAL:
                    floodCard.getLinkedCell().setState(Utils.State.FLOODED);
                    discardPiles.get(Utils.CardType.Flood).addCard(floodCard);
                    break;
                case FLOODED:
                    floodCard.getLinkedCell().setState(Utils.State.SUNKEN);
                    rescueList.addAll(floodCard.getLinkedCell().getAdventurers());
                    break;
                case SUNKEN:
                    if (Parameters.LOGS){
                        System.out.println("Carte inondation : " + card.getCardName() + " supprimée : Tuile déjà coulée");
                    }
                    break;
                default:
                    throw new RuntimeException();
            }

            if (!rescueList.isEmpty()) {
                controller.getInterruptionController().setRescueList(rescueList);
            }
            controller.getGridController().getGridView().updateCell(linkedCell.getName(), linkedCell.getState());
        }
    }

    public DiscardPile getDiscardPile(Utils.CardType type) {
        return discardPiles.get(type);
    }
}
