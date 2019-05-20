package ileinterdite.test;

import ileinterdite.util.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DemoBoardGenerator {
    private static BufferedReader reader;

    /**
     * Genere une liste d'etat (enum) selon un .txt avec le non de l'etat par ligne.
     * @return State[]
     */
    public static Utils.State[] boardBuilder(String filepath) {
        Utils.State[] cellState = new Utils.State[36];
        String line;


        try{
            fileInit(filepath);

            for (int i = 0; i < 36 ; i++) {

                line = reader.readLine();
                switch (line) {
                    case "NORMAL":
                        cellState[i] = Utils.State.NORMAL;
                        break;

                    case "FLOODED":
                        cellState[i] = Utils.State.FLOODED;
                        break;

                    case "SUNKEN":
                        cellState[i] = Utils.State.SUNKEN;
                        break;

                    case "NON_EXISTENT":
                        cellState[i] = Utils.State.NON_EXISTENT;
                        break;
                    default:
                        cellState[i] = null;
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
         }
        return cellState;
    }

    private static void fileInit(String filepath) throws FileNotFoundException {

            reader = new BufferedReader(new FileReader(filepath));


    }
}
