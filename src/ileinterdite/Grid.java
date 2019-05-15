package ileinterdite;

import java.util.*;

public class Grid {

	Cell[][] cells;
	Collection<Adventurer> pawn;
	Collection<Treasure> treasures;

	public Grid(Cell[][] cells, Collection<Adventurer> pawn, Collection<Treasure> treasures) {
		this.cells = cells;
		this.pawn = pawn;
		this.treasures = treasures;
	}

	/**
	 * Methode qui renvoie un tableau un 6x6 contenant l'etat de chaque case.
	 * @return State[][]
	 */



	public  State[][] getStatCells() {
		// TODO - implement ileinterdite.Grid.getEtatCases
		State[][] StatCells;
		StatCells = new State[6][6];
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 5; j++) {
				Cell cellTmp;
				cellTmp = this.getCell(i,j);
				if (cellTmp != null){
					StatCells[i][j] = cellTmp.getState();
				}
			}

		}
		return StatCells;
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

}