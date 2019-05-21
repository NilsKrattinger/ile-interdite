package ileinterdite.model;

import ileinterdite.util.State;
import ileinterdite.model.adventurers.Adventurer;

import java.util.*;

public class Grid {

	private Cell[][] cells;
	public final static int WIDTH = 6;
	public final static int HEIGTH = 6;
	private Treasure[] treasures;
	Collection<Adventurer> pawns;

	public Grid(Cell[][] cells, Collection<Adventurer> pawns, Treasure[] treasures) {
		this.cells = cells;
		this.pawns = pawns;
		this.treasures = treasures;
	}

	/**
	 * Methode qui renvoie un tableau un 6x6 contenant l'etat de chaque case.
	 * @return State[][]
	 */
	public  State[][] getStateOfCells() {
		State[][] cellsState;
		cellsState = new State[Grid.WIDTH][Grid.HEIGTH];
		for (int i = 0; i < Grid.HEIGTH ; i++) {
			for (int j = 0; j < Grid.WIDTH ; j++) {
				if (cells[i][j] != null){
					cellsState[i][j] = cells[i][j].getState();
				}
			}

		}
		return cellsState;
}


	/**
	 *
	 * @param x int
	 * @param y int
	 * @param x_old int
	 * @param y_old int
	 * @param adv int
	 *
	 *            enleve l'aventurier de son ancienne pos et l'ajoute sur la nouvelle.
	 */
	public void move(int x, int y, int x_old, int y_old, Adventurer adv) {
		//get the actual cell of the adventurer
		cells[x_old][y_old].removeAdventurer(adv);
		 //get the new cell of the adventurer
		cells[x][y].addAdventurer(adv);
	}

	/**
	 * Asseche la tuile en X U
	 * @param x X pos on grid
	 * @param y Y pos on grid
	 */
	public void dry(int x, int y){
		cells[x][y].setState(State.NORMAL);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */

}