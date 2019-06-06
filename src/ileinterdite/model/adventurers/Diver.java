package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;

public class Diver extends Adventurer {

    public static final Pawn PAWN = Pawn.BLACK;
    public static final String CLASS_NAME = "Plongeur";

    public Diver() {
        super();
    }

    public Diver(Grid grid) {
        super(grid);
    }

    public Diver(int x, int y) {
        super(x, y);
    }

    public Diver(Grid grid, int x, int y) {
        super(grid, x, y);
    }

    @Override
    public Pawn getPawn() {
        return PAWN;
    }

    public void cellChoiceMoving(Utils.State[][] tab) {
        treatBoard(this.getX(), this.getY(), tab);
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                if (tab[j][i] != Utils.State.ACCESSIBLE) {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    public void treatBoard(int x, int y, Utils.State[][] tab) {
        if (x >= 0 && x < Grid.WIDTH && y >= 0 && y < Grid.WIDTH) {
            Utils.State state = tab[y][x];
            if ((x != this.getX() || y != this.getY()) && (
                        state == Utils.State.FLOODED || state == Utils.State.NORMAL)) {
                tab[y][x] = Utils.State.ACCESSIBLE;
            } else if (state != Utils.State.ACCESSIBLE) {
                tab[y][x] = Utils.State.INACCESSIBLE;
            }

            if ((x == this.getX() && y == this.getY()) || state == Utils.State.FLOODED || state == Utils.State.SUNKEN) {
                treatBoard(x - 1, y, tab);
                treatBoard(x + 1, y, tab);
                treatBoard(x, y - 1, tab);
                treatBoard(x, y + 1, tab);
            }
        }
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}