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

        DemoBoardGenerator dbg = new DemoBoardGenerator();
        board = dbg.boardBuilder("../../Case.txt");

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
