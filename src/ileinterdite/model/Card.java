package ileinterdite.model;

public abstract class Card{
	Grid board;

	public Card(Grid board) {
		this.board = board;
	}

	public abstract void setCellName(String cellName);
}