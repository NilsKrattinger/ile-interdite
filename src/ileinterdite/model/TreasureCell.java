package ileinterdite.model;

public class TreasureCell extends Cell {
    private Treasure treasure;


    public TreasureCell(Treasure treasure, String name) {
        super(name);
        this.setTreasure(treasure);

    }

    public Treasure getTreasure() {
        return this.treasure;
    }

    public void setTreasure(Treasure treasure) {
        this.treasure = treasure;
    }
}
