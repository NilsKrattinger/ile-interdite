package ileinterdite.view;

import ileinterdite.components.CellComponent;
import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Parameters;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
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

    private static JFrame window;
    private static JPanel gridPanel;
    private static CellComponent[][] cellComponents;

    public GridView() {
        window = new JFrame("Grid");
        window.setSize(500, 500);

        gridPanel = new JPanel(new GridBagLayout());
        window.add(gridPanel);
    }

    public void setVisible() {
        window.setVisible(true);
    }

    public void showGrid(Cell[][] cells) {
        cellComponents = new CellComponent[cells.length][cells.length];
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 2;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        for (int j = 0; j < cells.length; j++) {
            for (int i = 0; i < cells[j].length; i++) {
                c.gridx = i;
                c.gridy = j;
                Cell cell = cells[j][i];
                CellComponent comp = new CellComponent(cell.getName(), cell.getState());
                gridPanel.add(comp, c);
                cellComponents[j][i] = comp;
            }
        }
        gridPanel.updateUI();
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

    public void updateCell(int x, int y, Utils.State state) {
        cellComponents[y][x].setState(state);
    }

}
