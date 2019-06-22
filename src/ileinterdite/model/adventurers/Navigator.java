package ileinterdite.model.adventurers;

import ileinterdite.util.Utils.Pawn;

public class Navigator extends Adventurer {

    private static final Pawn PAWN = Pawn.YELLOW;
    private static final String CLASS_NAME = "Navigateur";

    public Navigator() {
        super();
    }

    public Navigator(int x, int y) {
        super(x, y);
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