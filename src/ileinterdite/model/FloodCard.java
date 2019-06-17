package ileinterdite.model;

public class FloodCard extends Card {
    private Cell linkedCell;


    public FloodCard(Cell linkedCell, Grid board) {
        super(board);
        this.linkedCell = linkedCell;
    }

    public Cell getLinkedCell() {
        return linkedCell;
    }

    public String getCellName() {
        return linkedCell.getName();
    }
}