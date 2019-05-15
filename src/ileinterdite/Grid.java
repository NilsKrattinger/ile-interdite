package ileinterdite;

import java.util.*;

public class Grid {

	private Cell[][] cells;
	private final int WIDTH = 6;
	private final int HEIGTH = 6;
	Collection<Adventurer> pawns;
	Collection<Treasure> treasures;


	public Grid(Cell[][] cells, Collection<Adventurer> pawns, Collection<Treasure> treasures) {
		this.cells = cells;
		this.pawns = pawns;
		this.treasures = treasures;
	}

	/**
	 * Methode qui renvoie un tableau un 6x6 contenant l'etat de chaque case.
	 * @return State[][]
	 */
	public  State[][] getStateOfCells() {
		// TODO - implement ileinterdite.Grid.getEtatCases
		State[][] statCells;
		statCells = new State[getWIDTH()][getHEIGTH()];
		for (int i = 0; i <= getHEIGTH() - 1; i++) {
			for (int j = 0; j <= getWIDTH() - 1; j++) {
				Cell cellTmp;
				cellTmp = this.getCell(i,j);
				if (cellTmp != null){
					statCells[i][j] = cellTmp.getState();
				}
			}

		}
		return statCells;
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
		Cell tmpCell;
		tmpCell = getCell(x_old,y_old); //get the actual cell of the adventurer
		tmpCell.removeAdventurer(adv);
		tmpCell = getCell(x,y); //get the new cell of the adventurer
		tmpCell.addAdventurer(adv);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void dry(int x, int y) {
		// TODO - implement ileinterdite.Grid.dry
		throw new UnsupportedOperationException();
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGTH() {
		return HEIGTH;
	}
}