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

	/**
	 * Remove the adventurer from the cell
	 * @param adv The adventurer to remove
	 */
	public void removeAdventurer(Adventurer adv) {
	    adventurers.remove(adv);
	}

	/**
	 * Add an adventurer to the cell
	 * @param adv The adventurer to add
	 */
	public void addAdventurer(Adventurer adv) {
	    adventurers.add(adv);
	}

    /**
     * Change the state of the cell
     * @param state The new state to set
     */
    public void setState(Utils.State state){
        this.state = state;
    }

    /**
     * Get the state of the cell
     * @return Utils.State
     */
    public Utils.State getState() {
        return this.state;
    }

}