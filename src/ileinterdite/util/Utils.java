package ileinterdite.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * @author Eric
 */
public class Utils {

    public enum Action {
        // Common actions for the player
        MOVE,
        DRY,
        GET_TREASURE,

        // More actions initialized by the player
        GIVE_CARD,
        CANCEL_ACTION,
        END_TURN,
        USE_TREASURE_CARD,

        // Actions for the system only
        RESCUE,

        // Interruptions
        NAVIGATOR_CHOICE,
        ADVENTURER_CHOICE,
        CARD_CHOICE,
        DISCARD,
        VALIDATE_ACTION,

        SAND_CARD_ACTION,
        HELICOPTER_CARD_CELL_CHOICE,
        HELICOPTER_CARD_ADVENTURER_CHOICE
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
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }


    public enum CardType {
        FLOOD("Inondation"),
        TREASURE("Trésor");

        String label;

        CardType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    public enum Pawn {
        RED("Rouge"),
        GREEN("Vert"),
        BLUE("Bleu"),
        WHITE("Blanc"),
        BLACK("Noir"),
        YELLOW("Jaune");

        private final String label;

        Pawn(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Permet d'afficher un message d'information avec un bouton OK
     *
     * @param message Message à afficher
     */
    public static void showInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Permet d'afficher un message d'alerte avec un bouton OK
     *
     * @param message Message à afficher
     */
    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Permet d'afficher un message d'erreur avec un bouton OK
     *
     * @param message Message à afficher
     */
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Permet d'initialiser un buffer avec le fichier passé et parametres et retourne un buffer
     *
     * @throws FileNotFoundException Si le fichier n'existe pas
     */
    public static BufferedReader bufferInit(String filepath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filepath));
    }

    /**
     * Loads an image from path
     *
     * @param path The path where the image is located, without the res/images/ at the beginning of the path
     * @return A BufferedImage containing the loaded image
     */
    public static BufferedImage loadImage(String path) {
        path = "res/images/" + path;
        path = path.replaceAll("[\\s']", "");
        try {
            BufferedImage newImg = ImageUtils.getCachedImage(path);

            if (newImg == null) {
                newImg = ImageIO.read(new File(path));
                ImageUtils.cacheImage(path, newImg);
            }

            return newImg;
        } catch (IOException ex) {
            if (Parameters.LOGS) {
                System.err.println("File " + path + " could not be opened. More information below.");
                ex.printStackTrace();
            }
        }

        return null;
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
     *
     * @param img     The image to change
     * @param opacity The opacity to set, from 0 (0%) to 255 (100%)
     */
    public static void setOpacity(BufferedImage img, int opacity) {
        WritableRaster raster = img.getRaster();
        for (int i = 0; i < raster.getWidth(); i++) {
            for (int j = 0; j < raster.getHeight(); j++) {
                int[] pixel = raster.getPixel(i, j, (int[]) null);
                if (pixel[3] != 0) {
                    pixel[3] = opacity;
                }
                raster.setPixel(i, j, pixel);
            }
        }
    }
}
