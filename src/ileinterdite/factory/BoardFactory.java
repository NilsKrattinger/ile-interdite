package ileinterdite.factory;

import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Parameters;
import ileinterdite.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardFactory {
    private static final int[] EMPTYCELLINDEX = {0, 1, 4, 5, 6, 11, 24, 29, 30, 31, 34, 35};
    private static final String DEFAULTCELLFILE = "res/cell.txt"; //TODO FIND THE DEFAULT PATH


    /**
     * initialise le plateau de jeu et créée :
     *  les tuilles
     *  les aventurier
     * @return
     */
    public static Cell[][] boardFactory(String filepath) {
        ArrayList<Cell> boardCellList = new ArrayList<>();
        Adventurer[] adventurers = new Adventurer[6];
        Cell[] buildedCells;

        Cell emptyCell = new Cell();
        emptyCell.setState(Utils.State.NON_EXISTENT);

        adventurers = AdventurersFactory.adventurerFactory();

        if(!Parameters.DEMOMAP){
            filepath = DEFAULTCELLFILE;
        }

        buildedCells = CellsFactory.cellsFactory(filepath,adventurers,null);
        boardCellList.addAll(Arrays.asList(buildedCells));
        // On place les cases "vide"
        for (int i : EMPTYCELLINDEX) {
            boardCellList.add(i, emptyCell);
        }

        return BoardFactory.convertToArray(boardCellList);
    }

    /**
     * Convertie une liste de Cell de taille 36 en
     * tableau 6 x 6 de cell
     * @param boardCellList Cell list
     * @return cell array
     */
    private static Cell[][] convertToArray(ArrayList<Cell> boardCellList) {
        Cell[][] boardCellArray = new Cell[Grid.HEIGHT][Grid.WIDTH];
        for (int i = 0; i < boardCellList.size(); i++) {
            boardCellArray[i / Grid.HEIGHT][i % Grid.WIDTH] = boardCellList.get(i);
        }
        return boardCellArray;
    }

}
