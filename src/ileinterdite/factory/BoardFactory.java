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
    private static final String DEFAULTCELLFILE = "res/cell.txt";
    private static ArrayList<Adventurer> adventurers;
    private static Cell[][] cells;
    private static ArrayList<Treasure> treasures;

    /**
     * initialise le plateau de jeu et créée :
     * les tuilles
     * les aventurier
     *
     * @return
     */
    public static void initBoardFactory() {
        ArrayList<Cell> boardCellList = new ArrayList<>();
        Cell[] builtCells;

        Cell emptyCell = new Cell();
        emptyCell.setState(Utils.State.NON_EXISTENT);
        treasures = TreasureFactory.treasureFactory();
        adventurers = AdventurersFactory.adventurerFactory();

        String filepath;
        if (!Parameters.DEMOMAP) {
            filepath = DEFAULTCELLFILE;
        } else {
            filepath = "res/DEMOMAP.txt";
        }

        builtCells = CellsFactory.cellsFactory(filepath,adventurers,treasures.toArray(new Treasure[treasures.size()]));
        boardCellList.addAll(Arrays.asList(builtCells));

        if (!Parameters.DEMOMAP) {
            Collections.shuffle(boardCellList);
        }
        // On place les cases "vide"
        for (int i : EMPTYCELLINDEX) {
            boardCellList.add(i, emptyCell);
        }

        cells = BoardFactory.convertToArray(boardCellList);
        treasures = TreasureFactory.treasureFactory();
    }

    public static ArrayList<Adventurer> getAdventurers() {
        return adventurers;
    }

    public static Cell[][] getCells() {
        return cells;
    }

    public static ArrayList<Treasure> getTreasures() {
        return treasures;
    }

    public static void setAdventurers(ArrayList<Adventurer> adventurers) {
        BoardFactory.adventurers = adventurers;
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