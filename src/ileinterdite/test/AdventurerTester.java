package ileinterdite.test;

import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.*;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils.State;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class AdventurerTester {
    private static final State[] INACCESSIBLE_ROW = new State[] {
            State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE
    };

    /**
     * Retrieve an array of states (6x6) containing a board template for testing
     * @return State[6][6]
     */
    private static State[][] getBeginState() {
        return new State[][] {
                { State.NON_EXISTENT,   State.NON_EXISTENT, State.NORMAL,   State.SUNKEN,   State.NON_EXISTENT, State.NON_EXISTENT },
                { State.NON_EXISTENT,   State.NORMAL,       State.SUNKEN,   State.FLOODED,  State.FLOODED,      State.NON_EXISTENT },
                { State.NORMAL,         State.FLOODED,      State.SUNKEN,   State.NORMAL,   State.NORMAL,       State.SUNKEN },
                { State.NORMAL,         State.FLOODED,      State.FLOODED,  State.NORMAL,   State.SUNKEN,       State.NORMAL },
                { State.NON_EXISTENT,   State.NORMAL,       State.FLOODED,  State.NORMAL,   State.NORMAL,       State.NON_EXISTENT },
                { State.NON_EXISTENT,   State.NON_EXISTENT, State.NORMAL,   State.NORMAL,   State.NON_EXISTENT, State.NON_EXISTENT }
        };
    }

    /**
     * Retrieve an array of cells created using <code>getBeginState</code> for testing
     * @return Cell[6][6]
     */
    private static Cell[][] getBeginCells() {
        Cell[][] result = new Cell[6][6];
        State[][] beginStates = getBeginState();

        for (int i = 0; i < beginStates.length; i++) {
            for (int j = 0; j < beginStates[i].length; j++) {
                State s = beginStates[i][j];
                Cell c = new Cell();
                c.setState(s);
                result[i][j] = c;
            }
        }

        return result;
    }

    /**
     * Create a new grid with an Explorer, a Pilot, a Diver and an Engineer at START_POS coordinated.
     * This may be used to get quickly started for a test.
     * @param adventurers An empty ArrayList that will be filled with the adventurers created.
     * @return Grid
     */
    private static Grid initializeGrid(ArrayList<Adventurer> adventurers) {
        Explorer exp = new Explorer(START_POS.x, START_POS.y);
        Pilot pilot = new Pilot(START_POS.x, START_POS.y);
        Diver diver = new Diver(START_POS.x, START_POS.y);
        Engineer engineer = new Engineer(START_POS.x, START_POS.y);

        adventurers.add(exp);
        adventurers.add(pilot);
        adventurers.add(diver);
        adventurers.add(engineer);

        Grid grid = new Grid(getBeginCells(), null);
        exp.setGrid(grid);
        pilot.setGrid(grid);
        diver.setGrid(grid);
        engineer.setGrid(grid);

        return grid;
    }

    @Test
    public void testDryAccess() {
        Adventurer adv = new Navigator(START_POS.x, START_POS.y);

        State[][] state = getBeginState();
        adv.cellChoiceDrying(state);
        Assert.assertEquals(DRY_ADV, state);

        Explorer exp = new Explorer(START_POS.x, START_POS.y);
        state = getBeginState();
        exp.cellChoiceDrying(state);
        Assert.assertEquals(DRY_EXPLORER, state);

        Pilot pilot = new Pilot(START_POS.x, START_POS.y);
        state = getBeginState();
        pilot.cellChoiceDrying(state);
        Assert.assertEquals(DRY_ADV, state);
    }

    @Test
    public void testMoveAccess() {
        Adventurer adv = new Navigator(START_POS.x, START_POS.y);

        State[][] state = getBeginState();
        adv.cellChoiceMoving(state);
        Assert.assertEquals(MOVE_ADV, state);

        Pilot pilot = new Pilot(START_POS.x, START_POS.y);
        state = getBeginState();
        pilot.cellChoiceMoving(state);
        Assert.assertEquals(MOVE_PILOT, state);

        Explorer exp = new Explorer(START_POS.x, START_POS.y);
        state = getBeginState();
        exp.cellChoiceMoving(state);
        Assert.assertEquals(MOVE_EXPLORER, state);

        Diver diver = new Diver(START_POS.x, START_POS.y);
        state = getBeginState();
        diver.cellChoiceMoving(state);
        Assert.assertEquals(MOVE_DIVER, state);
    }

    @Test
    public void testCellChoiceMoving() {
        /*
         * 0 => Explorer
         * 1 => Pilot
         * 2 => Diver
         * 3 => Engineer
         */
        ArrayList<Adventurer> adventurers = new ArrayList<>();
        initializeGrid(adventurers);

        Assert.assertEquals(MOVE_EXPLORER, adventurers.get(0).getAccessibleCells());
        Assert.assertEquals(MOVE_PILOT, adventurers.get(1).getAccessibleCells());
        Assert.assertEquals(MOVE_DIVER, adventurers.get(2).getAccessibleCells());
        Assert.assertEquals(MOVE_ADV, adventurers.get(3).getAccessibleCells());
    }

    @Test
    public void testMove() {
        /*
         * 0 => Explorer
         * 1 => Pilot
         * 2 => Diver
         * 3 => Engineer
         */
        ArrayList<Adventurer> adventurers = new ArrayList<>();
        Grid grid = initializeGrid(adventurers);

        Explorer exp = (Explorer) adventurers.get(0);
        Pilot pilot = (Pilot) adventurers.get(1);

        exp.move(START_POS.x + 1, START_POS.y);

        Cell startCell = grid.getCell(START_POS.x, START_POS.y);
        Cell endCell = grid.getCell(START_POS.x + 1, START_POS.y);

        Assert.assertFalse(startCell.isAdventurerOnCell(exp));
        Assert.assertTrue(startCell.isAdventurerOnCell(pilot));
        Assert.assertFalse(endCell.isAdventurerOnCell(pilot));
        Assert.assertTrue(endCell.isAdventurerOnCell(exp));
    }

    // Starts at (0, 0)
    private static final Tuple<Integer, Integer> START_POS = new Tuple<>(2, 3);

    private static final State[][] DRY_ADV = new State[][] {
            INACCESSIBLE_ROW,
            INACCESSIBLE_ROW,
            INACCESSIBLE_ROW,
            { State.INACCESSIBLE, State.ACCESSIBLE,     State.ACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE, State.INACCESSIBLE,   State.ACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            INACCESSIBLE_ROW
    };

    private static final State[][] DRY_EXPLORER = new State[][] {
            INACCESSIBLE_ROW,
            INACCESSIBLE_ROW,
            { State.INACCESSIBLE, State.ACCESSIBLE,     State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE, State.ACCESSIBLE,     State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE, State.INACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            INACCESSIBLE_ROW
    };

    private static final State[][] MOVE_ADV = new State[][] {
            INACCESSIBLE_ROW,
            INACCESSIBLE_ROW,
            INACCESSIBLE_ROW,
            { State.INACCESSIBLE, State.ACCESSIBLE,     State.INACCESSIBLE, State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE, State.INACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            INACCESSIBLE_ROW
    };

    private static final State[][] MOVE_EXPLORER = new State[][] {
            INACCESSIBLE_ROW,
            INACCESSIBLE_ROW,
            { State.INACCESSIBLE, State.ACCESSIBLE, State.INACCESSIBLE, State.ACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE, State.ACCESSIBLE, State.INACCESSIBLE, State.ACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE, State.ACCESSIBLE, State.ACCESSIBLE,   State.ACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            INACCESSIBLE_ROW
    };

    private static final State[][] MOVE_PILOT = new State[][] {
            { State.INACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE},
            { State.ACCESSIBLE,     State.ACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE},
            { State.ACCESSIBLE,     State.ACCESSIBLE,   State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE},
            { State.INACCESSIBLE,   State.ACCESSIBLE,   State.ACCESSIBLE,   State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE},
            { State.INACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE},
    };

    private static final State[][] MOVE_DIVER = new State[][] {
            { State.INACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE},
            { State.ACCESSIBLE,     State.ACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE},
            { State.ACCESSIBLE,     State.ACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE,   State.ACCESSIBLE,   State.ACCESSIBLE,   State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE},
            { State.INACCESSIBLE,   State.INACCESSIBLE, State.ACCESSIBLE,   State.INACCESSIBLE, State.INACCESSIBLE, State.INACCESSIBLE},
    };
}
