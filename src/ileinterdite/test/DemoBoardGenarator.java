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
        State[] cellStat = new State[36];
        String line;


        try{
            fileInit(filepath);

            for (int i = 0; i < 36 ; i++) {

                line = reader.readLine();
                switch (line) {
                    case "NORMAL":
                        cellStat[i] = State.NORMAL;
                        break;

                    case "FLOODED":
                        cellStat[i] = State.FLOODED;
                        break;

                    case "SUNKEN":
                        cellStat[i] = State.SUNKEN;
                        System.out.println("sunken");

                        break;

                    case "NON_EXISTENT":
                        cellStat[i] = State.NON_EXISTENT;
                        break;
                    default:
                        cellStat[i] = null;
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
         }
        return cellStat;
    }

    private static void fileInit(String filepath) throws FileNotFoundException {

            reader = new BufferedReader(new FileReader(filepath));


    }
}
