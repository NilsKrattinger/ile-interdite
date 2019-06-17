package ileinterdite.factory;

import ileinterdite.model.Card;
import ileinterdite.model.Deck;
import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

import java.util.Stack;

public class DeckFactory {
    private static Stack<Card> cards;

    public static Deck deckFacotry(Utils.CardType type, Grid grid) {
        if (type == Utils.CardType.Flood) {
            cards = FloodCardFactory.floodCardFactory(grid);
        } else {
            //TODO IMPLEMENT TREASURE CARD FACTORY
        }
        return new Deck(cards, type);
    }
}
