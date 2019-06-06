package ileinterdite.model;

public class FloodCard extends Card {
    private Cell linkedCell;
    private String cellName;

    public FloodCard(Cell linkedCell, Grid board) {
        super(board);
        this.linkedCell = linkedCell;
    }

    public Cell getLinkedCell() {
        return linkedCell;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
}