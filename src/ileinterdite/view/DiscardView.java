package ileinterdite.view;

import ileinterdite.model.Card;
import ileinterdite.util.Parameters;

import java.util.ArrayList;
import java.util.Scanner;

public class DiscardView {

    public ArrayList<String> getCardsToDiscard(ArrayList<Card> cards, int nbCard) {
        if (Parameters.LOGS) {
            System.out.println("Vous devez choisir " + nbCard + " cartes à défausser parmis les cartes : ");
        }
        for (Card card : cards) {
            System.out.println(card.getCardName());
        }
        ArrayList<String> cardNames = new ArrayList<>();
        Scanner entry = new Scanner(System.in);
        for (int i = 0; i < nbCard; i++) {
            if (Parameters.LOGS) {
                System.out.print("Carte n°" + (i + 1) + " : ");
            }
            String cardName = entry.nextLine();
            cardNames.add(cardName);
        }
        return cardNames;
    }
}
