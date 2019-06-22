package ileinterdite.model.adventurers;

import ileinterdite.util.Utils.Pawn;

public class Engineer extends Adventurer {

    private static final Pawn PAWN = Pawn.RED;
    private static final String CLASS_NAME = "Ingénieur";

    public Engineer() {
        super();
    }

    public Engineer(int x, int y) {
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