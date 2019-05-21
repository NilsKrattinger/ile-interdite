package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.model.Hand;
import ileinterdite.util.Utils;

public class Adventurer {

	Grid grid;
	Hand hand;
	private int x;
	private int y;

	/**
	 * Methode qui retourne un tableau avec les case  accessible par l'aventurier
	 * @return State[][] avec une marque accesible ou non
	 */
	public Utils.State[][] getAccessibleCells() {
        Utils.State[][] cellsState;
		cellsState = grid.getStateOfCells();
		//TODO ADD Choix Tuile Deplacment sur StatCells.

		return cellsState;
	}

	/**
	 * Methode qui retourne un tableau avec les case assechables par l'aventurier
	 * @return State[][] avec une marque assechable ou non
	 */
	public Utils.State[][] getDryableCells() {
        Utils.State[][] cellsState;
		cellsState = grid.getStateOfCells();
		//TODO ADD Choix Tuile Assechement sur StatCells.

		return cellsState;
	}

	/**
	 * Deplace l'aventurier sur la nouvelle case et le supprime de son ancien emplacement
	 * @param newX int
	 * @param newY int
	 */
	public void move(int newX, int newY) {
		int currX = this.getX();
		int currY = this.getY();
		grid.move(newX,newY,currX,currY,this);
	}

	public boolean isPowerAvailable() {
		// TODO - implement ileinterdite.model.adventurers.Adventurer.isPowerAvailable
		throw new UnsupportedOperationException();
	}

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'aventurier
     * @param tab
     */
    public void cellChoiceMoving(Utils.State[][] tab) {
        for (int i = 0; i < Grid.WIDTH; i++) {
            for (int j = 0; j< Grid.HEIGHT; j++) {
                Utils.State state = tab[i][j];
                if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL)
                        && (this.getY() == j && (this.getX() == i-1
                        || this.getX() == i+1) || this.getX() == i
                        && (this.getY() == j-1 || this.getY() == j+1))) {
                    tab[i][j] = Utils.State.ACCESSIBLE;
                } else {
                    tab[i][j] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est assechable ou non par l'aventurier
     * @param tab
     */
    public void cellChoiceDrying(Utils.State[][] tab) {
        for (int i = 0; i < Grid.WIDTH; i++) {
            for (int j = 0; j < Grid.HEIGHT; j++) {
                Utils.State state = tab[i][j];
                if ((state == Utils.State.FLOODED)
                        && (this.getY() == j
                        && (this.getX() >= i-1 || this.getX() <= i+1)
                        || this.getX() == i
                        && (this.getY() >= j-1 || this.getY() <= j+1))) {
                    tab[i][j] = Utils.State.ACCESSIBLE;
                } else {
                    tab[i][j] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}