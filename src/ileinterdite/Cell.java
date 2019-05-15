package ileinterdite;

import java.util.*;

public class Cell {

	Collection<Adventurer> adventurers;
	private State state;

	public Cell(State state) {
		this.state = state;
	}

	/**
	 * 
	 * @param adv
	 */
	public void removeAdventurer(Adventurer adv) {
		// TODO - implement ileinterdite.Cell.removeAdventurer
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param adv
	 */
	public void addAdventurer(Adventurer adv) {
		// TODO - implement ileinterdite.Cell.addAdventurer
		throw new UnsupportedOperationException();
	}

	public State getState() {
		return this.state;
	}

}