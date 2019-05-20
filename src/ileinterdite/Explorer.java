package ileinterdite;

import java.util.Collection;

public class Explorer extends Adventurer {

	public Collection<State> getAvailableCells() {
		// TODO - implement ileinterdite.Explorer.getAvailableCells
		throw new UnsupportedOperationException();
	}

	public Collection<State> getDryableCells() {
		// TODO - implement ileinterdite.Explorer.getDryableCells
		throw new UnsupportedOperationException();
	}

	/**
	 * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'explorateur
	 * @param tab
	 */
	@Override
	public void cellChoiceMoving(State[][] tab) {
		for (int i=0; i<=5; i++) {
			for (int j=0; j<=5; j++) {
				State state = tab[i][j];
				if ((state == State.FLOODED || state == State.NORMAL)
						&& this.getX() >= i-1 && this.getX() <= i+1
						&& this.getY() >= j-1 && this.getY() <= j+1
						&& (this.getX() != i || this.getY() != j)) {
					tab[i][j] = State.ACCESSIBLE;
				} else {
					tab[i][j] = State.INACCESSIBLE;
				}
			}
		}
	}

	@Override
	public void cellChoiceDrying(State[][] tab) {
		for (int i=0; i<=5; i++) {
			for (int j=0; j<=5; j++) {
				State state = tab[i][j];
				if (state == State.FLOODED
						&& this.getX() >= i-1 && this.getX() <= i+1
						&& this.getY() >= j-1 && this.getY() <= j+1) {
					tab[i][j] = State.ACCESSIBLE;
				} else {
					tab[i][j] = State.INACCESSIBLE;
				}
			}
		}
	}
}