package ileinterdite.test;

import ileinterdite.*;

public class TestEtatCase {
    private static Cell[][] board;

    public static void main(String[] args) {
        init();
        //print();

    }


    public static void init(){

        State[] cellState;

        board = new Cell[6][6];
        DemoBoardGenarator DBG = new DemoBoardGenarator();
        cellState = DBG.boardBuilder("../../Case.txt");
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 6; i++) {

                board[j][i] = new Cell();
                board[j][i].setState(cellState[i + (j * 6)]);
            }
        }

        Grid grid= new Grid(board,null,null);

        State[][] gettedState = grid.getStatCells();

        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 6; i++) {
                System.out.println("Case " + "x : " +(i+1) + " y :" + (j+1) + " " +gettedState[j][i].toString() );

            }
        }
    }
}
