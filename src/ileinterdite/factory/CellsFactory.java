package ileinterdite.factory;

import ileinterdite.model.Cell;
import ileinterdite.model.SpawnCell;
import ileinterdite.model.Treasure;
import ileinterdite.model.TreasureCell;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class CellsFactory {
    private static BufferedReader reader;


    public static Cell[] cellsFactory(String filepath, Adventurer[] adventurers, Treasure[] treasures) {
        Cell[] cells = new Cell[24];
        Utils.CellType[] CellType = new Utils.CellType[24];
        String[][] cellAtributs = new String[24][3];

        String line;
        try {
            reader = Utils.bufferInit(filepath);
            for (int i = 0; i < 24; i++) { //TODO gerner tout avec des string ca evite les double case verfi si enum toujours utiles après
                cellAtributs[i][0] = reader.readLine();
                cellAtributs[i][1] = reader.readLine();
                cellAtributs[i][3] = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 24; i++) {
            switch (cellAtributs[i][0]) {
                case "SPAWN":
                    cells[i] = new SpawnCell(adventurers[i], cellAtributs[i][1]);
                    break;
                case "TREASURE":
                    cells[i] = new TreasureCell(treasures[i % 6], cellAtributs[i][1]);
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
