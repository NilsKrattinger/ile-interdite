package ileinterdite.model;

import ileinterdite.util.Parameters;
import ileinterdite.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {

    private DiscardPile discardPile;
    private Stack<Card> cards;
    private Utils.CardType cardType;

    public Deck(Stack<Card> cards, Utils.CardType type) {
        this.cards = cards;
        this.cardType = type;
    }

    public void setDiscardPile(DiscardPile discard) {
        this.discardPile = discard;
    }

    public Utils.CardType getCardType() {
        return cardType;
    }

    public Stack<Card> getCards() {
        return cards;
    }

    /**
     * permet de picher un cetain nombre de cartes
     *
     * @param nbCards nb de cartes a piocher
     */
    public ArrayList<Card> drawCards(int nbCards) {
        ArrayList<Card> drewCards = new ArrayList<>();

        for (int i = 0; i < nbCards; i++) {
            if (cards.size() == 0) {
                discardPile.shuffle();
                addAtTheTop(discardPile.getCards());
                discardPile.clearPile();
            }

            drewCards.add(this.cards.pop());
        }
        return drewCards;
    }

    /**
     * Ajoute toutes les carte en haut de la pioche
     */
    public void addAtTheTop(ArrayList<Card> newCards) {
        cards.addAll(newCards);

    }

    public void shuffle() {
        if (Parameters.RANDOM) {
            Collections.shuffle(cards);
        }
    }

}