package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;

public class Pilot extends Adventurer {

    public static final Pawn PAWN = Pawn.BLUE;
    public static final String CLASS_NAME = "Pilote";

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
     *
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

    /**
     * Si la case n'est pas une case adjacente passe la disponibilité du pouvoir à faux.
     * @param newX int
     * @param newY int
     */
    @Override
    public void move(int newX, int newY) {

        if (!(this.getY() == newY && (this.getX() == newX - 1
                || this.getX() == newX + 1) || this.getX() == newX
                && (this.getY() == newY - 1 || this.getY() == newY + 1))) {
            this.setPowerAvailable(false);
        }
        super.move(newX, newY);
    }

    /**
     * rend le pouvoire utilisable
     */
    @Override
    public void newTurn() {
        super.newTurn();
        setPowerAvailable(true);
    }

    @Override
    public Utils.State[][] getRescueCells() {
        setPowerAvailable(true);
        return super.getRescueCells();
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    public boolean isPowerAvailable() {
        return this.powerAvailable;
    }

    public void setPowerAvailable(boolean powerAvailable) {
        this.powerAvailable = powerAvailable;
    }

}