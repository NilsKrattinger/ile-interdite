package ileinterdite.factory;

import ileinterdite.model.Cell;
import ileinterdite.model.SpawnCell;
import ileinterdite.model.Treasure;
import ileinterdite.model.TreasureCell;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CellsFactory {
    public static Cell[] cellsFactory(String filepath, ArrayList<Adventurer> adventurers, Treasure[] treasures) {
        Cell[] cells = new Cell[24];
        Utils.State cellState;
        String[][] cellAttributes = new String[24][5];

        try {
            BufferedReader reader = Utils.bufferInit(filepath);
            for (int i = 0; i < 24; i++) {
                cellAttributes[i][0] = reader.readLine();
                cellAttributes[i][1] = reader.readLine();
                cellAttributes[i][2] = reader.readLine();
                cellAttributes[i][3] = reader.readLine();
                cellAttributes[i][4] = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 24; i++) {
            switch (cellAttributes[i][0]) {
                case "SPAWN":
                    Adventurer adventurer;
                    int j = 0;
                    do {
                        adventurer = adventurers.get(j);
                        j++;
                    } while (j < adventurers.size() && !adventurer.getPawn().toString().equalsIgnoreCase(cellAttributes[i][2]));
                    cells[i] = new SpawnCell(adventurer, cellAttributes[i][1]);
                    break;
                case "TREASURE":
                    cells[i] = new TreasureCell(getTreasureFromName(treasures, cellAttributes[i][4]), cellAttributes[i][1]);
                    break;
                case "SIMPLE":
                    cells[i] = new Cell(cellAttributes[i][1]);
                    break;
                default:
                    throw new RuntimeException();
            }

            switch (cellAttributes[i][3]) {
                case "SUNKEN":
                    cellState = Utils.State.SUNKEN;
                    break;

                case "FLOODED":
                    cellState = Utils.State.FLOODED;
                    break;
                default:
                    cellState = Utils.State.NORMAL;
                    break;
            }
            cells[i].setState(cellState);
        }
        return cells;
    }

    private static Treasure getTreasureFromName(Treasure[] treasures, String name) {
        Treasure result = null;
        // We only have 4 elements (at max) so we can go through the whole array each time for readability improvement
        for (Treasure t : treasures) {
            if (t.getName().equalsIgnoreCase(name)) {
                result = t;
            }
        }

        return result;
    }

}
