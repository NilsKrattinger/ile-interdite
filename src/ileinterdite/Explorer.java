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
	 * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'aventurier
	 * @param tab
	 */

	public void cellChoiceMoving(State[][] tab) {
		for (i=0; i<=5; i++) {
			for (j=0; j<=5; j++) {
				state = tab[i][j];
				if ((state ==State.FLOODED || state ==State.NORMAL)
						&& (this.y==j && (this.state ==i-1
						|| this.state ==i+1) || this.state ==i
						&& (this.y==j-1 || this.y==j+1))) {
					tab[i][j]=State.ACCESSIBLE;
				} else {
					tab[i][j]=State.INACCESSIBLE;
				}
			}
		}
	}

}