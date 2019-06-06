package ileinterdite.model;

import ileinterdite.util.Utils;
import java.util.*;

public class DiscardPile {

	ArrayList<Card> cards;
	Utils.CardType cardType;

	public DiscardPile(Utils.CardType cardType) {
		this.cards = new ArrayList<>();
		this.cardType = cardType;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void clearPile(){
		this.cards = new ArrayList<>();
	}

	public void addCard(Card card){
		this.cards.add(card);
	}

	public Utils.CardType getCardType() {
		return cardType;
	}
}