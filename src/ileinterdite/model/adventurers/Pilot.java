package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;

public class Pilot extends Adventurer {

    public static final Pawn PAWN = Pawn.BLUE;
    private boolean powerAvailable = true;

    public Pilot() {
        super(0, 0);
    }

    public Pilot(Grid grid) {
        super(grid);
    }

    public Pilot(int x, int y) {
        super(x, y);
    }

    public Pilot(Grid grid, int x, int y) {
        super(grid, x, y);
    }

    @Override
    public Pawn getPawn() {
        return PAWN;
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
                    if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL) && (i != getX() || j != getY())) {
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