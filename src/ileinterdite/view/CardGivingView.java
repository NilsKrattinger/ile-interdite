package ileinterdite.view;

import ileinterdite.controller.GameController;
import ileinterdite.model.Card;
import ileinterdite.model.adventurers.Adventurer;

import java.util.ArrayList;
import java.util.Scanner;

public class CardGivingView {
    private GameController controller;

    public CardGivingView(GameController controller) {
        this.controller = controller;
    }


    public void showTradableCards(ArrayList<Card> giverCards) {
        System.out.println("Voici les cartes parmis lesquelles vous pouvez choisir pour donner une carte : ");
        for (Card card : giverCards) {
            System.out.println(card.getCardName());
        }
    }

    public void showPotentialReceivers(ArrayList<Adventurer> potentielReceivers) {
        System.out.println("Voici les aventuriers parmis lesquels vous pouvez choisir : ");
        for (Adventurer adventurer : potentielReceivers) {
            System.out.println(adventurer.getName());
        }
    }

    public String getSelectedCardName() {
        Scanner entry = new Scanner(System.in);
        System.out.println("Carte : ");
        String cardName = entry.nextLine();
        return cardName;
    }

    public String getReceiverName() {
        Scanner entry = new Scanner(System.in);
        System.out.println("Aventurier recepteur de la carte : ");
        String receiverName = entry.nextLine();
        return receiverName;
    }
}
