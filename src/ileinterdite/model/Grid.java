package ileinterdite.model;

import ileinterdite.util.State;
import ileinterdite.model.adventurers.Adventurer;

import java.util.*;

public class Grid {

	Collection<Cell> cells;
	Collection<Adventurer> pawn;
	Collection<Treasure> treasures;

	public Collection<State> getEtatCases() {
		// TODO - implement ileinterdite.model.Grid.getEtatCases
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param x_old
	 * @param y_old
	 * @param adv
	 */
	public void move(int x, int y, int x_old, int y_old, Adventurer adv) {
		// TODO - implement ileinterdite.model.Grid.move
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void getTuile(int x, int y) {
		// TODO - implement ileinterdite.model.Grid.getTuile
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void dry(int x, int y) {
		// TODO - implement ileinterdite.model.Grid.dry
		throw new UnsupportedOperationException();
	}

}