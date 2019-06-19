package ileinterdite.util.helper;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

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

    public static boolean isMovementPossible(Utils.State[][] cellStates) {
        boolean availability = false;
        int j = 0;
        while (!availability && j < cellStates.length) {
            int i = 0;
            while (!availability && i < cellStates[j].length) {
                availability = cellStates[j][i] == Utils.State.ACCESSIBLE;
                i++;
            }
            j++;
        }

        return availability;
    }

}
