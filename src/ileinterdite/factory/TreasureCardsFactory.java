package ileinterdite.factory;

import ileinterdite.model.Card;
import ileinterdite.model.TreasureCard;

import java.util.Stack;

public class TreasureCardsFactory {

    public static Stack<Card> treasureCardsFactory() {
        Stack<Card> treasureCards = new Stack<>();

        for (int i = 0; i < 5; i++) {
            treasureCards.add(new TreasureCard("La Pierre sacrée"));
        }

        for (int i = 0; i < 5; i++) {
            treasureCards.add(new TreasureCard("La Statue du zéphyr"));
        }

        for (int i = 0; i < 5; i++) {
            treasureCards.add(new TreasureCard("Le Cristal ardent"));
        }

        for (int i = 0; i < 3; i++) {
            treasureCards.add(new TreasureCard("Montee des eaux"));
        }

        for (int i = 0; i < 5; i++) {
            treasureCards.add(new TreasureCard("Le Calice de l'onde"));
        }

        for (int i = 0; i < 3; i++) {
            treasureCards.add(new TreasureCard("Helicoptère"));
        }

        for (int i = 0; i < 2; i++) {
            treasureCards.add(new TreasureCard("Sacs de sable"));
        }

        return treasureCards;
    }
}
