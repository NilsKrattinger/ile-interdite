/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ileinterdite.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

import ileinterdite.model.adventurers.Adventurer;

/**
 *
 * @author Eric
 */
public class Utils {

    public static enum Action {
        MOVE,
        DRY,
        GIVE_CARD,
        GET_TREASURE,
        VALIDATE_CELL,
        END_TURN,
        CANCEL_ACTION
    }
 
    public static enum State {
        NORMAL("Asséchée"),
        FLOODED("Inondée"),
        SUNKEN("Coulée"),
        NON_EXISTENT(""),
        ACCESSIBLE("Accessible"),
        INACCESSIBLE("Inaccessible");

        String label;
        
        State(String label) {
            this.label = label ;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    public static enum Pawn {
        RED("Rouge", new Color(193, 34, 44)),
        GREEN("Vert", new Color(0, 195, 0)),
        BLUE("Bleu", new Color(55,194,198)),
        ORANGE("Orange", new Color(235, 122, 34)),
        PURPLE("Violet", new Color(204, 94, 255)),
        YELLOW("Jaune", new Color(255, 255, 0)) ;

        private final String label;
        private final Color color;


        Pawn(String label, Color color) {
            this.label = label;
            this.color = color;
        }

        @Override
        public String toString() {
            return this.label;
        }

        public Color getColor() {
            return this.color;
        }

        static Pawn getFromName(String name) {
            if (RED.name().equals(name)) return RED;
            if (GREEN.name().equals(name)) return GREEN;
            if (BLUE.name().equals(name)) return BLUE;
            if (ORANGE.name().equals(name)) return ORANGE ;
            if (PURPLE.name().equals(name)) return PURPLE;
            if (YELLOW.name().equals(name)) return YELLOW;
            return null;
        }
    }

    public static ArrayList<Adventurer> shuffleAdventurers(ArrayList<Adventurer> arrayList) {
        if (Parameters.RANDOM) {
            Collections.shuffle(arrayList);
        }
        return arrayList ;
    }
    
    /**
     * Permet de poser une question à laquelle l'utilisateur répond par oui ou non
     * @param question texte à afficher
     * @return true si l'utilisateur répond oui, false sinon
     */
    public static Boolean askQuestion(String question) {
        System.out.println("Divers.askQuestion(" + question + ")");
        int answer = JOptionPane.showConfirmDialog (null, question, "", JOptionPane.YES_NO_OPTION) ;
        System.out.println("\tréponse : " + (answer == JOptionPane.YES_OPTION ? "Oui" : "Non"));
        return answer == JOptionPane.YES_OPTION;
    }    
    
    /**
     * Permet d'afficher un message d'information avec un bouton OK
     * @param message Message à afficher 
     */
    public static void showInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.OK_OPTION);
    }
}
