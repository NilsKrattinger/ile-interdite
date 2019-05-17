package ileinterdite;

import java.util.Collection;

public class Adventurer {

	private Grid grid;
	private Hand hand;
	private int x;
	private int y;

	public Collection<State> getAvailableCells() {
		// TODO - implement ileinterdite.Adventurer.getAvailableCells
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void movement(int x, int y) {
		// TODO - implement ileinterdite.Adventurer.movement
		throw new UnsupportedOperationException();
	}

	public void getPosition() {
		// TODO - implement ileinterdite.Adventurer.getPosition
		throw new UnsupportedOperationException();
	}

	public Collection<State> getDryableCells() {
		// TODO - implement ileinterdite.Adventurer.getDryableCells
		throw new UnsupportedOperationException();
	}

	/**
	 * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'aventurier
	 * @param tab
	 */
	public void cellChoiceMoving(State[][] tab) {
		for (int i=0; i<=5; i++) {
			for (int j=0; j<=5; j++) {
				State state = tab[i][j];
				if ((state == State.FLOODED || state == State.NORMAL)
						&& (this.getY() == j && (this.getX() == i-1
						|| this.getX() == i+1) || this.getX() == i
						&& (this.getY() == j-1 || this.getY() == j+1))) {
					tab[i][j] = State.ACCESSIBLE;
				} else {
					tab[i][j] = State.INACCESSIBLE;
				}
			}
		}
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}
}