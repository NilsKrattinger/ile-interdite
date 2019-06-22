package ileinterdite.model;

public class FloodCard extends Card {
    private Cell linkedCell;

    public FloodCard(Cell linkedCell) {
        super(linkedCell.getName());
        this.linkedCell = linkedCell;
    }

    public Cell getLinkedCell() {
        return linkedCell;
    }
}