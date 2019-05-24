package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

public class Navigator extends Adventurer {

    public Navigator() {
        super();
    }

    public Navigator(Grid grid) {
        super(grid);
    }

    public Navigator(int x, int y) {
        super(x, y);
    }

    @Override
    public void cellChoiceMoving(Utils.State[][] tab) {
        super.cellChoiceMoving(tab);
    }
}