package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

public class Navigator extends Adventurer {

    public Navigator(Grid grid) {
        super(grid);
    }

    @Override
    public void cellChoiceMoving(Utils.State[][] tab) {
        super.cellChoiceMoving(tab);
    }
}