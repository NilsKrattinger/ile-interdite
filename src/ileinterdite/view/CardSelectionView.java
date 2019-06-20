package ileinterdite.view;

import ileinterdite.model.TreasureCard;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class CardSelectionView {

    private JFrame window;
    private JPanel mainPanel;
    private JPanel upperPanel;
    private JPanel cardPanel;
    private JLabel cardLabel;
    private ArrayList<TreasureCard> cardToDisplay;
    private ArrayList<JLabel> cardsIco;

    public CardSelectionView() {
        initFrame();
        cardToDisplay = new ArrayList<>();
        cardsIco = new ArrayList<>();
        mainPanel = new JPanel(new BorderLayout());
        upperPanel= new JPanel();
        cardPanel = new JPanel();
        cardLabel = new JLabel();
        mainPanel.add(upperPanel,BorderLayout.SOUTH);
        window.add(mainPanel);
    }

    /**
     * Update the window
     * @param cards
     * @return
     */
    public void update(ArrayList<TreasureCard> cards, int nbCards, String action){
        cardToDisplay = cards;
        this.updateLabel(cards.size(),nbCards,action);
        cardPanel = new JPanel(new GridLayout(1,cards.size()));

        for (TreasureCard card:cards) {
            BufferedImage img = Utils.loadImage("cartes/" + card.getCardName() + ".png");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 2, img.getHeight() / 2, Image.SCALE_SMOOTH));
                JLabel cardIco = new JLabel();
                cardIco.setIcon(icon);
                cardsIco.add(cardIco);
                cardPanel.add(cardIco);
            }
        }
        mainPanel.add(cardLabel,BorderLayout.CENTER);
    }

    private void updateLabel(int nbAcualCard, int nbCards,String action){
        cardLabel.setText(this.textbuilder(nbAcualCard,nbCards,action));
        cardLabel.repaint();
    }

    private String textbuilder(int size, int nbCards, String action) {
        boolean singular;
        int nbCardtodeff = 0;

        nbCardtodeff = size - nbCards;
        singular = nbCardtodeff == 1;

        String string = "";
        string = "Veuillez choisir ";
        string =  string + nbCardtodeff;

        if (!singular){
            string = string + " cartes";

        } else {
            string = string + " carte";
        }
        string = string + " à " + action;

        return string;
    }

    /**
     * initialise the window set name , look, ect...
     */
    private void initFrame() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 200);
        window.setTitle("Selection de cartes");
        window.setLocationRelativeTo(null);
        window.setUndecorated(true);


    }

    private void loadWindow(){
        cardsIco.clear();
        cardToDisplay.clear();

    }
}
