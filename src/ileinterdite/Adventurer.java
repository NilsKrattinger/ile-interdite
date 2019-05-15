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
		State[][] StatCells;
		StatCells = new State[6][6];
		StatCells = grid.getStatCells();
		//TODO ADD Choix Tuile sur StatCells.

		return StatCells;
	}

	/**
	 * Deplace l'aventureir sur la nouvelle case et le suprime de sont ancient emplacement
	 * @param xNew int
	 * @param yNew int
	 */
	public void movement(int xNew, int yNew) {
		int x = this.getX();
		int y = this.getY();
		grid.move(xNew,yNew,x,y,this);
	}

	public void getPosition() {
		// TODO - implement ileinterdite.Adventurer.getPosition
		throw new UnsupportedOperationException();
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