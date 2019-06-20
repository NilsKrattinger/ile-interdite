package ileinterdite.view;

import ileinterdite.model.TreasureCard;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class CardSelectionView implements IObservable<Message> {

    private JFrame window;
    private JPanel mainPanel;
    private JPanel upperPanel;
    private JPanel cardPanel;
    private JLabel cardLabel;
    private ArrayList<TreasureCard> cardToDisplay;
    private ArrayList<JLabel> cardsIco;
    private ArrayList<Integer> cardsIndex;

    private final CopyOnWriteArrayList<IObserver<Message>> observers;


    public CardSelectionView() {
        observers = new CopyOnWriteArrayList<>();

        initFrame();
        cardsIndex = new ArrayList<>();
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

        for (JLabel cardico:cardsIco) {
            cardico.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    int cardIndex;
                    cardIndex = cardsIco.indexOf(mouseEvent.getComponent());
                    JLabel card = cardsIco.get(cardIndex);
                    int r = card.getBackground().getRed();
                    int g = card.getBackground().getGreen();
                    int b = card.getBackground().getBlue();
                    if (cardsIndex.contains(cardIndex)) {
                        cardsIndex.remove(cardsIndex.get(cardIndex));
                        card.setBackground(new Color(r, g, b, 0));
                    } else {
                        cardsIndex.add(cardIndex);
                        card.setBackground(new Color(r, g, b, 65));
                    }

                    if (cardsIndex.size() == nbCards){
                        for (Integer i : cardsIndex) {
                            cardToDisplay.remove(i);
                        }
                        Message m = new Message(Utils.Action.CARD_CHOICE,buildStringMessage(cardToDisplay));

                    };



                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                }
                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                }
                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                }
                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                }
            });

        }
        mainPanel.add(cardLabel,BorderLayout.CENTER);


    }

    private String buildStringMessage(ArrayList<TreasureCard> cardsSelected) {
        String stringMessage = "";
        for (TreasureCard card : cardsSelected) {
            stringMessage = stringMessage + "/" + card.getCardName();
        }
        return stringMessage;
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

    @Override
    public void addObserver(IObserver<Message> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<Message> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Message message) {
        for (IObserver<Message> o : observers) {
            o.update(this, message);
        }
    }
}
