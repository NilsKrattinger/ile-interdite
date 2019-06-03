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
    private static BufferedReader reader;


    public static Cell[] cellsFactory(String filepath, ArrayList<Adventurer> adventurers, Treasure[] treasures) {
        Cell[] cells = new Cell[24];
        String[][] cellAtributs = new String[24][3];

        String line;
        try {
            reader = Utils.bufferInit(filepath);
            for (int i = 0; i < 24; i++) {
                cellAtributs[i][0] = reader.readLine();
                cellAtributs[i][1] = reader.readLine();
                cellAtributs[i][2] = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 24; i++) {
            switch (cellAtributs[i][0]) {
                case "SPAWN":
                    Adventurer adventurer = adventurers.get(0);
                    int j = 1;
                    while (i < adventurers.size() && adventurer.getPawn().toString().equalsIgnoreCase(cellAtributs[i][2])) {
                        adventurer = adventurers.get(j);
                        j++;
                    }
                    cells[i] = new SpawnCell(adventurers.get(i), cellAtributs[i][1]);
                    break;
                case "TREASURE":
                    cells[i] = new TreasureCell(treasures[i - 4], cellAtributs[i][1]);
                    break;
                case "SIMPLE":
                    cells[i] = new Cell(cellAtributs[i][1]);
                    break;
                default:
                    //TODO Implementer Erreur
            }

        }
        return cells;
    }

}
