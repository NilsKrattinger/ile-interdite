package ileinterdite.model;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Utils;

import java.util.*;

public class Grid {

	private Cell[][] cells;
	public final static int WIDTH = 6;
	public final static int HEIGHT = 6;
	private Treasure[] treasures;

	public Grid(Cell[][] cells, Treasure[] treasures) {
		this.cells = cells;
		this.treasures = treasures;
	}

	/**
	 * Methode qui renvoie un tableau un 6x6 contenant l'etat de chaque case.
	 * @return State[][]
	 */
	public Utils.State[][] getStateOfCells() {
        Utils.State[][] cellsState = new Utils.State[Grid.WIDTH][Grid.HEIGHT];
		for (int j = 0; j < Grid.HEIGHT; j++) {
			for (int i = 0; i < Grid.WIDTH ; i++) {
				Cell cellTmp;
				cellTmp = this.getCell(i,j);
				if (cellTmp != null){
					cellsState[j][i] = cellTmp.getState();
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
		cells[y_old][x_old].removeAdventurer(adv);
		 //get the new cell of the adventurer
		cells[y][x].addAdventurer(adv);
	}

	/**
	 * Asseche la tuile en X U
	 * @param x X pos on grid
	 * @param y Y pos on grid
	 */
	public Cell getCell(int x, int y) {
        return cells[y][x];
    }

	public void dry(int x, int y){
		cells[y][x].setState(Utils.State.NORMAL);
	}

}