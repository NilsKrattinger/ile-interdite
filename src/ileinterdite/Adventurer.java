package ileinterdite;

import java.util.Collection;

public class Adventurer {

	Grid grid;
	Hand hand;
	private int x;
	private int y;

	/**
	 * Methode qui retourne un tableau avec les case  accessible par l'aventurier
	 * @return State[][] avec un marque accesible ou non
	 */
	public State[][] getAccessibleCells() {
		State[][] cellsState;
		cellsState = grid.getStateOfCells();
		//TODO ADD Choix Tuile sur StatCells.

		return cellsState;
	}

	/**
	 * Deplace l'aventurier sur la nouvelle case et le supprime de son ancien emplacement
	 * @param newX int
	 * @param newY int
	 */
	public void movement(int newX, int newY) {
		int currX = this.getX();
		int currY = this.getY();
		grid.move(newX,newY,currX,currY,this);
	}
	
	public boolean isPowerAvailable() {
		// TODO - implement ileinterdite.Adventurer.isPowerAvailable
		throw new UnsupportedOperationException();
	}

	public Collection<State> getDryableCells() {
		// TODO - implement ileinterdite.Adventurer.getDryableCells
		throw new UnsupportedOperationException();
	}



	public int getX() {

		return x;
	}

	public int getY() {

		return y;
	}

}