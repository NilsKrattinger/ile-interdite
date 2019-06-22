package ileinterdite.factory;

import ileinterdite.model.Card;
import ileinterdite.model.Deck;
import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

import java.util.Stack;

public class DeckFactory {

    public static Deck deckFactory(Utils.CardType type, Grid grid) {
        Stack<Card> cards;
        if (type == Utils.CardType.FLOOD) {
            cards = FloodCardFactory.floodCardFactory(grid);
        } else {
            cards = TreasureCardsFactory.treasureCardsFactory();
        }
        return new Deck(cards, type);
    }
}
