package ileinterdite.test;

import ileinterdite.State;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DemoBoardGenarator {
    private static BufferedReader reader;

    /**
     * Genere une liste d'etat (enum) selon un .txt avec le non de l'etat par ligne.
     * @return State[]
     */
    public static State[] boardBuilder(String filepath) {
        State[] cellState = new State[36];
        String line;


        try{
            fileInit(filepath);

            for (int i = 0; i < 36 ; i++) {

                line = reader.readLine();
                switch (line) {
                    case "NORMAL":
                        cellState[i] = State.NORMAL;
                        break;

                    case "FLOODED":
                        cellState[i] = State.FLOODED;
                        break;

                    case "SUNKEN":
                        cellState[i] = State.SUNKEN;
                        System.out.println("sunken");

                        break;

                    case "NON_EXISTENT":
                        cellState[i] = State.NON_EXISTENT;
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
