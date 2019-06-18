package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;

public class Explorer extends Adventurer {

    public static final Pawn PAWN = Pawn.GREEN;
    public static final String CLASS_NAME = "Explorateur";

    public Explorer() {
        super();
    }

    public Explorer(Grid grid) {
        super(grid);
    }

    public Explorer(int x, int y) {
        super(x, y);
    }

    public Explorer(Grid grid, int x, int y) {
        super(grid, x, y);
    }

    @Override
    public Pawn getPawn() {
        return PAWN;
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'explorateur
     * @param tab
     */
    @Override
    public void cellChoiceMoving(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL)
                        && this.getX() >= i-1 && this.getX() <= i+1
                        && this.getY() >= j-1 && this.getY() <= j+1
                        && (this.getX() != i || this.getY() != j)) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est assechable ou non par l'explorateur
     * @param tab
     */
    @Override
    public void cellChoiceDrying(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if (state == Utils.State.FLOODED
                        && this.getX() >= i-1 && this.getX() <= i+1
                        && this.getY() >= j-1 && this.getY() <= j+1) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    public Utils.State[][] getRescuCells() {
        Utils.State[][] cellsState = grid.getStateOfCells();
        super.cellChoiceMoving(cellsState);

        return cellsState;
    }

}