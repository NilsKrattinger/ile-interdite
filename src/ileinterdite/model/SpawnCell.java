package ileinterdite.model;

import ileinterdite.model.adventurers.Adventurer;

public class SpawnCell extends Cell {
    private Adventurer adventurer;

    public SpawnCell(Adventurer adventurer, String name) {
        super(name);
        this.adventurer = adventurer;

    }

    @Override
    public void spawnAdventurer(int x, int y) {
        super.spawnAdventurer(x,y);
        this.adventurer.move(x , y);
    }
}
