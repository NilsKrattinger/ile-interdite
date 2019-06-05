package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils.Pawn;

public class Messager extends Adventurer {

    public static final Pawn PAWN = Pawn.WHITE;
    public static final String CLASS_NAME = "Messager";

    public Messager() {
        super();
    }

    public Messager(Grid grid) {
        super(grid);
    }

    public Messager(int x, int y) {
        super(x, y);
    }

    public Messager(Grid grid, int x, int y) {
        super(grid, x, y);
    }

    @Override
    public Pawn getPawn() {
        return PAWN;
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}