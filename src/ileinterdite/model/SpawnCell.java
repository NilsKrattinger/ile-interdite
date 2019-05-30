package ileinterdite.model;

import ileinterdite.model.adventurers.Adventurer;

public class SpawnCell extends Cell {
    private Adventurer adventurerSpawn;

    public SpawnCell(Adventurer adventurer, String name) {
        super(name);
        this.adventurerSpawn = adventurer;

    }

    @Override
    public Adventurer getAdventurerSpawn() {
        return this.adventurerSpawn;
    }

    @Override
    public void spawnAdventurer(int x, int y) {
        super.spawnAdventurer(x,y);
        this.adventurerSpawn.move(x , y);
    }
}
