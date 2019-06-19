package ileinterdite.util.helper;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

import java.util.ArrayList;

public class InterruptionControllerHelper {

    public static boolean isSavePossible(Utils.State[][] cellStates) {
        boolean availableCellFound = false;
        int i = 0;
        while (i < Grid.HEIGHT && !availableCellFound) {
            int j = 0;
            while (j < Grid.WIDTH && !availableCellFound) {
                availableCellFound = cellStates[i][j] == Utils.State.ACCESSIBLE;
                j++;
            }
            i++;
        }
        return availableCellFound;
    }

    public static String[] splitAdventurerClassName(String message){
        String[] strings;
       ArrayList<String> finalString = new ArrayList<>();
        strings = message.split("/");
        for (int i = 0; i < strings.length; i++) {
            if( !strings[i].isBlank() && !strings[i].isEmpty()){
                finalString.add(strings[i]);
            }
            
        }
        String[] adventurerClass = new String[finalString.size()];
       return  finalString.toArray(adventurerClass);

    }

}
