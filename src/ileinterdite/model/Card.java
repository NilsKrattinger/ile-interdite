package ileinterdite.model;

public abstract class Card{
	private Grid board;
	private String name;

	public Card(Grid board, String name) {
        this.board = board;
        this.name = name;

    }

    public String getCardName() {
        return this.name;
    }
}