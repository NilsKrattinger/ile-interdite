package ileinterdite.model;

import java.util.*;

public class Hand {

	private ArrayList<Card> cards;

	public Hand() {
		this.cards = new ArrayList<>();
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public Card getCard(String cardName) {
		for (Card card : this.getCards()) {
			if (card.getCardName().equals(cardName)) {
				return card;
			}
		}
        return null;
	}

	public void clearHand() {
		this.cards.clear();
	}

}