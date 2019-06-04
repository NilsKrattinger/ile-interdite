package ileinterdite.factory;

import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.Treasure;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Parameters;
import ileinterdite.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BoardFactory {
    private static final int[] EMPTYCELLINDEX = {0, 1, 4, 5, 6, 11, 24, 29, 30, 31, 34, 35};
    private static final String DEFAULTCELLFILE = "res/cell.txt"; //TODO FIND THE DEFAULT PATH


    /**
     * initialise le plateau de jeu et créée :
     * les tuilles
     * les aventurier
     *
     * @return
     */
    public static Object[] boardFactory(String filepath) {
        Object[] buildedStuff = new Object[3];
        ArrayList<Cell> boardCellList = new ArrayList<>();
        ArrayList<Adventurer> adventurers;
        Cell[] buildedCells;

        Cell emptyCell = new Cell();
        emptyCell.setState(Utils.State.NON_EXISTENT);

        adventurers = AdventurersFactory.adventurerFactory();

        if (!Parameters.DEMOMAP) {
            filepath = DEFAULTCELLFILE;
        } else {
            filepath = "res/DEMOMAP.txt";
        }

        buildedCells = CellsFactory.cellsFactory(filepath, adventurers, null);
        boardCellList.addAll(Arrays.asList(buildedCells));

        if (!Parameters.DEMOMAP) {
            Collections.shuffle(boardCellList);
        }
        // On place les cases "vide"
        for (int i : EMPTYCELLINDEX) {
            boardCellList.add(i, emptyCell);
        }

        buildedStuff[0] = adventurers;
        buildedStuff[1] = BoardFactory.convertToArray(boardCellList);
        Treasure[] treasures = new Treasure[1];
        treasures[0] = new Treasure();
        buildedStuff[2] = treasures; //TODO add treasures

        return buildedStuff;
    }

    /**
     * Convertie une liste de Cell de taille 36 en
     * tableau 6 x 6 de cell
     *
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
