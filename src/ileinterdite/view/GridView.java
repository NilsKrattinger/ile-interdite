package ileinterdite.view;

import ileinterdite.components.CellComponent;
import ileinterdite.model.Cell;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GridView implements IObservable<Message>, IObserver<Tuple<Integer, Integer>> {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private JFrame window;
    private JPanel gridPanel;
    private CellComponent[][] cellComponents;

    public GridView() {
        observers = new CopyOnWriteArrayList<>();

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
                Cell cell = cells[j][i];

                CellComponent comp = new CellComponent(cell.getName(), cell.getState(), i + 1, j + 1);

                c.gridx = i;
                c.gridy = j;
                gridPanel.add(comp, c);

                comp.addObserver(this);
                cellComponents[j][i] = comp;
            }
        }
        gridPanel.updateUI();
    }

    /**
     * Shows which cells are selectable
     * @param states The list of cells with states either ACCESSIBLE or INACCESSIBLE
     */
    public void showSelectableCells(Utils.State[][] states) {
        for (int j = 0; j < states.length; j++) {
            for (int i = 0; i < states[j].length; i++) {
                cellComponents[j][i].setAccessible(states[j][i]);
            }
        }
    }

    /**
     * Update the position of an adventurer
     * @param adv The adventurer to update
     */
    public void updateAdventurer(Adventurer adv) {
        if (Parameters.LOGS) {
            System.out.println("Adventurer moved to (" + (adv.getX() + 1) + ',' + (adv.getY() + 1) + ")");
        }
        resetCells();
    }

    public void updateCell(int x, int y, Utils.State state) {
        cellComponents[y][x].setState(state);
        resetCells();
    }

    public void newTurn() {
        resetCells();
    }

    public void resetCells() {
        for (int j = 0; j < cellComponents.length; j++) {
            for (int i = 0; i < cellComponents[j].length; i++) {
                cellComponents[j][i].resetAccessible();
            }
        }
    }

    @Override
    public void addObserver(IObserver<Message> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<Message> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Message message) {
        for (IObserver<Message> o : observers) {
            o.update(this, message);
        }
    }

    @Override
    public void update(IObservable<Tuple<Integer, Integer>> o, Tuple<Integer, Integer> message) {
        notifyObservers(new Message(Utils.Action.VALIDATE_ACTION, message.x + " " + message.y));
    }
}
