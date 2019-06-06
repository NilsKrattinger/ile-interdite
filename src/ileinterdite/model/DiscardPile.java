package ileinterdite.model;

import ileinterdite.util.Utils;

import java.util.*;

public class DiscardPile {

	ArrayList<Card> cards;
	Utils.CardType cardType;

	public DiscardPile(ArrayList<Card> cards, Utils.CardType cardType) {
		this.cards = cards;
		this.cardType = cardType;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void clearPile(){
		this.cards = new ArrayList<>();
	}
}