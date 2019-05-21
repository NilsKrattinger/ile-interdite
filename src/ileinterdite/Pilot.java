package ileinterdite;

import java.util.Collection;

public class Pilot extends Adventurer {
	private boolean powerAvailable;

	public Collection<State> getAvailableCells() {
		// TODO - implement ileinterdite.Pilot.getAvailableCells
		throw new UnsupportedOperationException();
	}

	/**
	 * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par le pilote
	 * @param tab
	 */
	@Override
	public void cellChoiceMoving(State[][] tab) {
        if (this.isPowerAvailable()) {
            for (int i = 0; i <= 5; i++) {
                for (int j = 0; j <= 5; j++) {
                    State state = tab[i][j];
                    if (state == State.FLOODED || state == State.NORMAL) {
                        tab[i][j] = State.ACCESSIBLE;
                    } else {
                        tab[i][j] = State.INACCESSIBLE;
                    }
                }
            }
        } else {
		    super.cellChoiceMoving(tab);
        }
	}

    public boolean isPowerAvailable() {
        return this.powerAvailable;
    }

    public void setPowerAvailable(boolean powerAvailable) {
        this.powerAvailable = powerAvailable;
    }
}