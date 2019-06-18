package ileinterdite.view;

import ileinterdite.components.CellComponent;
import ileinterdite.components.PawnComponent;
import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.*;
import ileinterdite.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GridView implements IObservable<Message>, IObserver<Tuple<Integer, Integer>> {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private JPanel gridPanel;
    private CellComponent[][] cellComponents;
    private HashMap<String, CellComponent> cellMap;
    private HashMap<Utils.Pawn, PawnComponent> pawns;

    public GridView() {
        observers = new CopyOnWriteArrayList<>();
        pawns = new HashMap<>();
        cellMap = new HashMap<>();

        gridPanel = new JPanel(new GridLayout(Grid.WIDTH, Grid.HEIGHT));
    }

    public JPanel getMainPanel() {
        return gridPanel;
    }

    public void showGrid(Cell[][] cells) {
        cellComponents = new CellComponent[cells.length][cells.length];

        for (int j = 0; j < cells.length; j++) {
            for (int i = 0; i < cells[j].length; i++) {
                Cell cell = cells[j][i];

                CellComponent comp = new CellComponent(cell.getName(), cell.getState(), i + 1, j + 1);
                gridPanel.add(comp);

                comp.addObserver(this);
                cellComponents[j][i] = comp;
                cellMap.put(cell.getName(), comp);
            }
        }
        gridPanel.updateUI();
    }

    public void showAdventurers(ArrayList<Adventurer> adventurers) {
        for (Adventurer a : adventurers) {
            int x = a.getX();
            int y = a.getY();

            PawnComponent comp = new PawnComponent(a.getPawn(), x, y);
            pawns.put(a.getPawn(), comp);
            cellComponents[y][x].addPawn(comp);
        }
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
        PawnComponent pawn = pawns.get(adv.getPawn());
        cellComponents[pawn.getY()][pawn.getX()].removePawn(pawn);
        pawn.setX(adv.getX());
        pawn.setY(adv.getY());

        cellComponents[pawn.getY()][pawn.getX()].addPawn(pawn);
        resetCells();
    }

    public void updateCell(int x, int y, Utils.State state) {
        cellComponents[y][x].setState(state);
        resetCells();
    }

    public void updateCell(String name, Utils.State state) {
        if (cellMap.containsKey(name)) {
            cellMap.get(name).setState(state);
            resetCells();
        }
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
