package ileinterdite.model;

import ileinterdite.util.State;
import ileinterdite.model.adventurers.Adventurer;

import java.util.*;

public class Cell {

	Collection<Adventurer> adventurers;
	private State state;

	/**
	 * 
	 * @param adv
	 */
	public void removeAdventurer(Adventurer adv) {
		// TODO - implement ileinterdite.model.Cell.removeAdventurer
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param adv
	 */
	public void addAdventurer(Adventurer adv) {
		// TODO - implement ileinterdite.model.Cell.addAdventurer
		throw new UnsupportedOperationException();
	}

	public State getState() {
		return this.state;
	}

}