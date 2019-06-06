/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ileinterdite.util;

import ileinterdite.model.adventurers.Adventurer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Eric
 */
public class Utils {

    public enum Action {
        NAVIGATOR_CHOICE,
        MOVE,
        DRY,
        GIVE_CARD,
        GET_TREASURE,
        VALIDATE_ACTION,
        END_TURN,
        CANCEL_ACTION
    }
 
    public enum State {
        NORMAL("Asséchée"),
        FLOODED("Inondée"),
        SUNKEN("Coulée"),
        NON_EXISTENT("  "),
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


    public enum Pawn {
        RED("Rouge", new Color(209, 45, 42), Color.LIGHT_GRAY),
        GREEN("Vert", new Color(65, 138, 71), Color.LIGHT_GRAY),
        BLUE("Bleu", new Color(66, 100, 173), Color.LIGHT_GRAY),
        WHITE("Blanc", new Color(220, 215, 219), Color.BLACK),
        BLACK("Noir", new Color(9, 18, 22), Color.LIGHT_GRAY),
        YELLOW("Jaune", new Color(255, 243, 83), Color.BLACK) ;

        private final String label;
        private final Color color;
        private final Color textColor;


        Pawn(String label, Color color, Color textColor) {
            this.label = label;
            this.color = color;
            this.textColor = textColor;
        }

        @Override
        public String toString() {
            return this.label;
        }

        public Color getColor() {
            return this.color;
        }

        public Color getTextColor() {
            return textColor;
        }

        static Pawn getFromName(String name) {
            if (RED.name().equals(name)) return RED;
            if (GREEN.name().equals(name)) return GREEN;
            if (BLUE.name().equals(name)) return BLUE;
            if (WHITE.name().equals(name)) return WHITE;
            if (BLACK.name().equals(name)) return BLACK;
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

    /**
     * Permet d'initialiser un buffer avec le fichier passer et paametres et retoun un buffer
     * @param filepath
     * @throws FileNotFoundException
     */
    public static BufferedReader bufferInit(String filepath) throws FileNotFoundException {

        return new BufferedReader(new FileReader(filepath));

    }

    /**
     * Copies a BufferedImage and returns the copy
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Changes the opacity of an image
     * @param img The image to change
     * @param opacity The opacity to set, from 0 (0%) to 255 (100%)
     */
    public static void setOpacity(BufferedImage img, int opacity) {
        WritableRaster raster = img.getRaster();
        for (int i = 0; i < raster.getWidth(); i++) {
            for (int j = 0; j < raster.getHeight(); j++) {
                int[] pixel = raster.getPixel(i, j, (int[]) null);
                pixel[3] = opacity;
                raster.setPixel(i, j, pixel);
            }
        }
    }
}
