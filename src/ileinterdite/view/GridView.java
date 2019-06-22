package ileinterdite.view;

import ileinterdite.components.CellComponent;
import ileinterdite.components.PawnComponent;
import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
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

        SwingUtilities.invokeLater(() -> gridPanel = new JPanel(new GridLayout(Grid.WIDTH, Grid.HEIGHT)));
    }

    public JPanel getMainPanel() {
        return gridPanel;
    }

    public void showGrid(Cell[][] cells) {
        cellComponents = new CellComponent[cells.length][cells.length];

        SwingUtilities.invokeLater(() -> {
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
        });
    }

    public void showAdventurers(ArrayList<Adventurer> adventurers) {
        for (Adventurer a : adventurers) {
            final Adventurer adv = a;
            final int x = a.getX();
            final int y = a.getY();

            SwingUtilities.invokeLater(() -> {
                PawnComponent comp = new PawnComponent(adv.getPawn(), x, y);
                pawns.put(adv.getPawn(), comp);
                cellComponents[y][x].addPawn(comp);
            });
        }
    }

    /**
     * Shows which cells are selectable
     *
     * @param states The list of cells with states either ACCESSIBLE or INACCESSIBLE
     */
    public void showSelectableCells(Utils.State[][] states) {
        for (int j = 0; j < states.length; j++) {
            for (int i = 0; i < states[j].length; i++) {
                final Utils.State state = states[j][i];
                final CellComponent comp = cellComponents[j][i];
                SwingUtilities.invokeLater(() -> comp.setAccessible(state));
            }
        }
    }

    /**
     * Update the position of an adventurer
     *
     * @param adv The adventurer to update
     */
    public void updateAdventurer(Adventurer adv) {
        SwingUtilities.invokeLater(() -> {
            PawnComponent pawn = pawns.get(adv.getPawn());
            cellComponents[pawn.getY()][pawn.getX()].removePawn(pawn);
            pawn.setX(adv.getX());
            pawn.setY(adv.getY());

            cellComponents[pawn.getY()][pawn.getX()].addPawn(pawn);
        });
        resetCells();
    }

    public void updateCell(int x, int y, Utils.State state) {
        SwingUtilities.invokeLater(() -> cellComponents[y][x].setState(state));
        resetCells();
    }

    public void updateCell(String name, Utils.State state) {
        if (cellMap.containsKey(name)) {
            final CellComponent comp = cellMap.get(name);
            SwingUtilities.invokeLater(() -> comp.setState(state));
            resetCells();
        }
    }

    public void newTurn() {
        resetCells();
    }

    public void resetCells() {
        SwingUtilities.invokeLater(() -> {
            for (CellComponent[] cellComponent : cellComponents) {
                for (CellComponent component : cellComponent) {
                    component.resetAccessible();
                }
            }
        });
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
