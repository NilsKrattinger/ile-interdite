package ileinterdite.model;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Utils;

import java.util.*;

public class Cell {

	Collection<Adventurer> adventurers;
	private Utils.State state;

	public Cell() {
		this.state = Utils.State.NORMAL;

	}

	public void setState(Utils.State state){
		this.state = state;
	}

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

	public Utils.State getState() {
		return this.state;
	}

}