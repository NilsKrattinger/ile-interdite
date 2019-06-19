package ileinterdite.util.helper;

import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Parameters;
import ileinterdite.view.GridView;

import java.util.ArrayList;

public class GridControllerHelper {

    public static void initView(GridView view, Grid grid, ArrayList<Adventurer> adventurers, IObserver<Message> observer) {

        view.addObserver(observer);
        view.showGrid(grid.getCells());
        view.showAdventurers(adventurers);
    }

    /**
     * Spawn adventurers on the grid
     * @param adventurers The list of adventurers to spawn
     * @param grid The grid where they must spawn
     */
    public static void spawnAdventurers(ArrayList<Adventurer> adventurers, Grid grid) {
        for (Adventurer adventurer : adventurers) {
            adventurer.setGrid(grid);
        }

        Cell[][] cells = grid.getCells();
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                if (adventurers.contains(cells[j][i].getAdventurerSpawn())) {
                    cells[j][i].spawnAdventurer(i, j);
                }
            }
        }
    }
}
