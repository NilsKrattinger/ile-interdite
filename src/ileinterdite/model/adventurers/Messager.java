package ileinterdite.model.adventurers;

import ileinterdite.util.Utils.Pawn;

public class Messager extends Adventurer {

    private static final Pawn PAWN = Pawn.WHITE;
    private static final String CLASS_NAME = "Messager";

    public Messager() {
        super();
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