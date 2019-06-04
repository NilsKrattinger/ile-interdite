package ileinterdite.model;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Utils;

import java.util.ArrayList;

public class Cell {

	private ArrayList<Adventurer> adventurers;
	private Utils.State state;
	private String name;

	public Cell() {
		this.state = Utils.State.NORMAL;
        this.adventurers = new ArrayList<>();
	}

	public Cell(String name) {
		this.state = Utils.State.NORMAL;
		this.setName(name);
		this.adventurers = new ArrayList<>();
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
     * Check if the adventurer is present on the cell
     * @param adv Adventurer The adventurer to find
     * @return boolean
     */
	public boolean isAdventurerOnCell(Adventurer adv) {
	    return adventurers.contains(adv);
    }

    public void spawnAdventurer(int x, int y){}

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

	public void setName(String name){
    	this.name = name;
	}

	public String getName(){
    	return this.name;
	}

	public Adventurer getAdventurerSpawn(){
    	return null;
	}

}