package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils.Pawn;

public class Engineer extends Adventurer {

    public static final Pawn PAWN = Pawn.RED;

    public Engineer() {
        super();
    }

    public Engineer(Grid grid) {
        super(grid);
    }

    public Engineer(int x, int y) {
        super(x, y);
    }

    public Engineer(Grid grid, int x, int y) {
        super(grid, x, y);
    }

    @Override
    public Pawn getPawn() {
        return PAWN;
    }
}