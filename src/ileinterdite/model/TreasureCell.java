package ileinterdite.model;

import ileinterdite.model.adventurers.Adventurer;

public class TreasureCell extends Cell {
    private Treasure treasure;


    public TreasureCell(Treasure treasure, String name) {
        super(name);
        this.treasure = treasure;

    }


}
