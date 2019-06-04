package ileinterdite.view;

import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Parameters;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;

import java.util.Collection;
import java.util.Observable;

public class GridView extends Observable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public void showActions() {
        // TODO - implement ileinterdite.view.GridView.showActions
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param tab
     */
    public void showMovements(Collection<Utils.State> tab) {
        // TODO - implement ileinterdite.view.GridView.showMovements
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param x
     * @param y
     * @param adv
     */
    public void updateAdvPosition(int x, int y, Adventurer adv) {
        // TODO - implement ileinterdite.view.GridView.updateAdvPosition
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param tab
     */
    public void showDryOptions(Collection<Utils.State> tab) {
        // TODO - implement ileinterdite.view.GridView.showDryOptions
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param x
     * @param y
     */
    public void updateDriedCells(int x, int y) {
        // TODO - implement ileinterdite.view.GridView.updateDriedCells
        throw new UnsupportedOperationException();
    }

    /**
     * Shows which cells are selectable
     * @param states The list of cells with states either ACCESSIBLE or INACCESSIBLE
     */
    public void showSelectableCells(Utils.State[][] states, Grid grid, Tuple<Integer, Integer> adventurerCoordinates) {

        System.out.println("Grille actuelle :");
        System.out.println("  1  2  3  4  5  6");
        for (int j = 0; j < states.length; j++) {
            System.out.print(j + 1);
            for (int i = 0; i < states[j].length; i++) {
                Utils.State st = grid.getCell(i, j).getState();
                String color = (states[j][i] == Utils.State.ACCESSIBLE) ? ANSI_GREEN : ((i == adventurerCoordinates.x && j == adventurerCoordinates.y) ? ANSI_YELLOW : ANSI_RED);
                System.out.print(" " + color + st.toString().charAt(0) + st.toString().charAt(1) + ANSI_RESET);
            }
            System.out.println();
        }

        System.out.println("As = Asséchée, In = Inondée, Co = Coulée");
        System.out.println(ANSI_RED + "Rouge = Inaccessible" + ANSI_RESET + " ; " + ANSI_GREEN + "Vert = Accessible" + ANSI_RESET + " ; " +
                ANSI_YELLOW + "Jaune = position de l'aventurier" + ANSI_RESET);

        System.out.println("Ces tuiles sont accessibles :");
        for (int j = 0; j < states.length; j++) {
            for (int i = 0; i < states[j].length; i++) {
                if (states[j][i] == Utils.State.ACCESSIBLE) {
                    System.out.print("(" + (i + 1) + "," + (j + 1) + ") ");
                }
            }
        }

        System.out.println();
    }

    /**
     * Update the position of an adventurer
     * @param adv The adventurer to update
     */
    public void updateAdventurer(Adventurer adv) {
        if (Parameters.LOGS) {
            System.out.println("Adventurer moved to (" + (adv.getX() + 1) + ',' + (adv.getY() + 1) + ")");
        }
    }

    public void updateDriedCell(int x, int y) {
        if (Parameters.LOGS) {
            System.out.println("Cell at (" + (x + 1) + ',' + (y + 1) + ") is now dry.");
        }
    }

}
