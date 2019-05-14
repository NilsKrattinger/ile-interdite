package ileinterdite;

import java.util.*;

public class Grid {

	Collection<Cell> cells;
	Collection<Adventurer> pawn;
	Collection<Treasure> treasures;

	/**
	 * Methode qui renvoie un tableau un 6x6 contenant l'etat de chaque case.
	 * @return
	 */
	public  State[][] getStatCells() {
		// TODO - implement ileinterdite.Grid.getEtatCases
		State[][] StatCells;
		StatCells = new State[6][6];
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 5; j++) {
				Cell cellTmp;
				cellTmp = this.getTuile(i,j);
				if (cellTmp != null){
					StatCells[i][j] = cellTmp.getState();
				}
			}

		}
		return StatCells;
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
		// TODO - implement ileinterdite.Grid.move
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Cell getTuile(int x, int y) {
		// TODO - implement ileinterdite.Grid.getTuile
		throw new UnsupportedOperationException();
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