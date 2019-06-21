package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;

import java.util.ArrayList;
import java.util.HashMap;

public class Diver extends Adventurer {

    public static final Pawn PAWN = Pawn.BLACK;
    public static final String CLASS_NAME = "Plongeur";

    public Diver() {
        super();
    }

    public Diver(Grid grid) {
        super(grid);
    }

    public Diver(int x, int y) {
        super(x, y);
    }

    public Diver(Grid grid, int x, int y) {
        super(grid, x, y);
    }

    @Override
    public Pawn getPawn() {
        return PAWN;
    }

    public void cellChoiceMoving(Utils.State[][] tab) {
        treatBoard(this.getX(), this.getY(), tab);
        setRemainingCellsUnavailable(tab);
    }

    public void treatBoard(int x, int y, Utils.State[][] tab) {
        if (x >= 0 && x < Grid.WIDTH && y >= 0 && y < Grid.WIDTH) {
            Utils.State state = tab[y][x];
            if ((x != this.getX() || y != this.getY()) && (
                        state == Utils.State.FLOODED || state == Utils.State.NORMAL)) {
                tab[y][x] = Utils.State.ACCESSIBLE;
            } else if (state != Utils.State.ACCESSIBLE) {
                tab[y][x] = Utils.State.INACCESSIBLE;
            }

            if ((x == this.getX() && y == this.getY()) || state == Utils.State.FLOODED || state == Utils.State.SUNKEN) {
                treatBoard(x - 1, y, tab);
                treatBoard(x + 1, y, tab);
                treatBoard(x, y - 1, tab);
                treatBoard(x, y + 1, tab);
            }
        }
    }

    public Utils.State[][] getRescueCells() {
        Utils.State[][] cellStates = grid.getStateOfCells();

        setClosestCellAvailable(cellStates, getX(), getY(), new ArrayList<>(), 0, -1);
        setRemainingCellsUnavailable(cellStates);
        return cellStates;
    }

    private void setRemainingCellsUnavailable(Utils.State[][] cellStates) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                if (cellStates[j][i] != Utils.State.ACCESSIBLE) {
                    cellStates[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    private void setClosestCellAvailable(Utils.State[][] states, int x, int y, ArrayList<Tuple<Integer, Tuple<Integer, Integer>>> nextPos, int distance, int maxDistance) {
        if ((distance <= maxDistance || maxDistance == -1) && x >= 0 && y >= 0 && x < Grid.WIDTH && y < Grid.HEIGHT) {
            Utils.State cellState = states[y][x];
            if (cellState == Utils.State.NORMAL || cellState == Utils.State.FLOODED) {
                states[y][x] = Utils.State.ACCESSIBLE;
                maxDistance = distance;
            } else if (cellState == Utils.State.SUNKEN) {
                states[y][x] = Utils.State.INACCESSIBLE;
                distance++;
                nextPos.add(new Tuple<>(distance, new Tuple<>(x + 1, y)));
                nextPos.add(new Tuple<>(distance, new Tuple<>(x - 1, y)));
                nextPos.add(new Tuple<>(distance, new Tuple<>(x, y + 1)));
                nextPos.add(new Tuple<>(distance, new Tuple<>(x, y - 1)));

            }
        }

        if (!nextPos.isEmpty()) {
            Tuple<Integer, Tuple<Integer, Integer>> nextAction = nextPos.remove(0);
            distance = nextAction.x;
            Tuple<Integer, Integer> pos = nextAction.y;
            setClosestCellAvailable(states, pos.x, pos.y, nextPos, distance, maxDistance);
        }
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}