package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

public class Pilot extends Adventurer {
    private boolean powerAvailable;

    public Pilot() {
        this(0, 0);
    }

    public Pilot(int x, int y) {
        super(x, y);
        this.powerAvailable = true;
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par le pilote
     * @param tab
     */
    @Override
    public void cellChoiceMoving(Utils.State[][] tab) {
        if (this.isPowerAvailable()) {
            for (int j = 0; j < Grid.HEIGHT; j++) {
                for (int i = 0; i < Grid.WIDTH; i++) {
                    Utils.State state = tab[j][i];
                    if (state == Utils.State.FLOODED || state == Utils.State.NORMAL) {
                        tab[j][i] = Utils.State.ACCESSIBLE;
                    } else {
                        tab[j][i] = Utils.State.INACCESSIBLE;
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