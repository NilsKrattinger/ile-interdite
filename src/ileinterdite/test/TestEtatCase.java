package ileinterdite.test;

import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

public class TestEtatCase {
    private static Cell[][] board;

    public static void main(String[] args) {
        init();
    }


    public static void init(){

        Utils.State[] cellState;

        board = new Cell[Grid.WIDTH][Grid.HEIGTH];
        DemoBoardGenarator dbg = new DemoBoardGenarator();
        cellState = dbg.boardBuilder("../../Case.txt");
        for (int j = 0; j < Grid.WIDTH; j++) {
            for (int i = 0; i < Grid.HEIGTH; i++) {

                board[j][i] = new Cell();
                board[j][i].setState(cellState[i + (j * Grid.WIDTH)]);
            }
        }

        Grid grid= new Grid(board,null,null);

        Utils.State[][] retrievedStates = grid.getStateOfCells();

        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 6; i++) {
                System.out.println("Case " + "x : " +(i+1) + " y :" + (j+1) + " " +retrievedStates[j][i].toString() );
                // TODO : Use Utils to log only if parameter is set to true
            }
        }
    }
}
