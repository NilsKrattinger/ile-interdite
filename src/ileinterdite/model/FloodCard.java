package ileinterdite.model;

public class FloodCard extends Card {
    private Cell linkedCell;

    public FloodCard(Cell linkedCell, Grid board) {
        super(board, linkedCell.getName());
        this.linkedCell = linkedCell;
    }

    public Cell getLinkedCell() {
        return linkedCell;
    }
}