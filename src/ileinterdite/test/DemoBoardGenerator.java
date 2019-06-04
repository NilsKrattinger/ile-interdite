//package ileinterdite.test;
//
//import ileinterdite.model.Cell;
//import ileinterdite.model.Grid;
//import ileinterdite.model.SpawnCell;
//import ileinterdite.model.adventurers.Adventurer;
//import ileinterdite.util.Utils;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class DemoBoardGenerator {
//    private static BufferedReader reader;
//
//    /**
//     * Genere une liste d'etat (enum) selon un .txt avec le non de l'etat par ligne.
//     *
//     * @return State[]
//     */
//    public static Cell[][] boardBuilder(String filepath,ArrayList<Adventurer> players) {
//        Utils.State[] cellState = new Utils.State[36];
//        String[] cellName = new String[36];
//        int[] spawnAdbventurerIndex = new int[6];
//        String line;
//
//
//        try {
//            fileInit(filepath);
//
//            for (int i = 0; i < 36; i++) {
//
//                line = reader.readLine();
//                switch (line) {
//                    case "NORMAL":
//                        cellState[i] = Utils.State.NORMAL;
//                        break;
//
//                    case "FLOODED":
//                        cellState[i] = Utils.State.FLOODED;
//                        break;
//
//                    case "SUNKEN":
//                        cellState[i] = Utils.State.SUNKEN;
//                        break;
//
//                    case "NON_EXISTENT":
//                        cellState[i] = Utils.State.NON_EXISTENT;
//                        break;
//                    default:
//                        cellState[i] = null;
//                        break;
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Cell[][] board = new Cell[6][6];
//
//        for (int j = 0; j < Grid.WIDTH; j++) {
//            for (int i = 0; i < Grid.HEIGHT; i++) {
//                board[j][i] = new Cell();
//                board[j][i].setState(cellState[i + (j * Grid.WIDTH)]);
//                board[j][i].setName(cellName[i + (j * Grid.WIDTH)]);
//            }
//        }
//
//
//        return board;
//    }
//
//    private static void fileInit(String filepath) throws FileNotFoundException {
//
//        reader = new BufferedReader(new FileReader(filepath));
//
//
//    }
//
//    private static void placeAdventurer(ArrayList<Adventurer> players, Cell[][] board){
//        String DIVERCELL = "La Porte de Fer";
//        String ENGINEERCELL = "La Porte de Bronze";
//        String EXPLORERCELL = "La Porte de Cuivre";
//        String MESSAGER = "La Porte d’Argent";
//        String NAVIGATOR = "La Porte d’Or";
//        String PILOT = "Heliport";
//        Cell cellTmp;
//        String name;
//
//        for (int j = 0; j < Grid.HEIGHT; j++) {
//            for (int i = 0; i < Grid.WIDTH; i++) {
//                 name = board[j][i].getName();
//
//                switch (name){
//                    case "La Porte de Fer":
//                        if
//
//                }
//
//            }
//
//        }
//
//
//
//    }
//}