package ileinterdite.util.helper;

import ileinterdite.model.Card;
import ileinterdite.model.Cell;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
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
    
    public static String[] splitAdventurerClassName(String message) {
        String[] strings;
        ArrayList<String> finalString = new ArrayList<>();
        strings = message.split("/");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].replaceAll("\\s", "");
            if(!strings[i].isEmpty()){
                finalString.add(strings[i]);
            }
            
        }
        String[] adventurerClass = new String[finalString.size()];
        return  finalString.toArray(adventurerClass);
    }

    public static Card getTreasureCard(Adventurer adv, int index) {
        return adv.getHand().getCards().remove(index);
    }

    public static boolean checkVictory(Grid grid, Adventurer adv, ArrayList<Adventurer> adventurers) {
        Cell cell = grid.getCell(adv.getX(), adv.getY());
        // And all the adventurers are on the cell
        return cell.getName().equalsIgnoreCase("Heliport")         // Current adventurer is on the winning cell
                && grid.getTreasures().size() == 0                              // And all treasures has been found
                && cell.getAdventurers().size() == adventurers.size();          // And all the adventurers are on the cell
    }

    public static void getTreasureCardCells(Utils.State[][] states, ArrayList<Utils.State> acceptableStates) {
        for (int j = 0; j < states.length; j++) {
            for (int i = 0; i < states[j].length; i++) {
                states[j][i] = (acceptableStates.contains(states[j][i])) ? Utils.State.ACCESSIBLE : Utils.State.INACCESSIBLE;
            }
        }
    }
}
