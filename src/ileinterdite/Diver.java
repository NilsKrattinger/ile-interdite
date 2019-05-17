package ileinterdite;

import java.util.Collection;

public class Diver extends Adventurer {

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void isCellReachable(int x, int y) {
		// TODO - implement ileinterdite.Diver.isCellReachable
		throw new UnsupportedOperationException();
	}

	public Collection<State> getAvailableCells() {
		// TODO - implement ileinterdite.Diver.getAvailableCells
		throw new UnsupportedOperationException();
	}

	public void cellChoiceMoving(State[][] tab) {
		treatBoard(this.getX(),this.getY(),tab);
		for (int i=0; i<=5; i++) {
			for (int j=0; j<=5; j++) {
				if (tab[i][j] != State.ACCESSIBLE) {
					tab[i][j] = State.INACCESSIBLE;
				}
			}
		}
	}

	public void treatBoard(int x, int y, State[][] tab) {
		if (x >= 0 && x <= 5 && y >= 0 && y <= 5) {
			State state = this.getGrid().getCell(x, y).getState();
			if ((x != this.getX() || y != this.getY()) && state != State.SUNKEN) {
				tab[x][y] = State.ACCESSIBLE;
			} else {
				tab[x][y] = State.INACCESSIBLE;
			}

			if ((x == this.getX() && y == this.getY()) || state == State.FLOODED || state == State.SUNKEN) {
				treatBoard(x - 1, y, tab);
				treatBoard(x + 1, y, tab);
				treatBoard(x, y - 1, tab);
				treatBoard(x, y + 1, tab);
			}
		}
	}

}