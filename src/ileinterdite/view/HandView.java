package ileinterdite.view;

import ileinterdite.model.Card;
import ileinterdite.model.Hand;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandView implements IObservable<CardMessage> {

    private final CopyOnWriteArrayList<IObserver<CardMessage>> observers;

    private JFrame windo;
    private JPanel mainPanel;
    private JPanel cardPanel;
    private ArrayList<JLabel> cards;
    private Hand hand;

    public HandView() {
        observers = new CopyOnWriteArrayList<>();

        initFram();
        cards = new ArrayList<>();
        initcards();

        mainPanel = new JPanel(new BorderLayout());

        cardPanel = new JPanel(new GridLayout(1, 5));
        for (int i = 0; i < 5; i++) {
            cardPanel.add(cards.get(i));
        }
        mainPanel.add(cardPanel);
        windo.add(mainPanel);

    }


    private void initFram() {
        windo = new JFrame();
        windo.setTitle("HandView");
        windo.setSize(420, 150);
        windo.setLocationRelativeTo(null);
        windo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windo.setVisible(true);

    }

    private void initcards() {
        for (int i = 0; i < 5; i++) {
            cards.add(new JLabel());
            cards.get(i).setIcon(null);
            cards.get(i).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    CardMessage m = new CardMessage();
                   if( hand.getCard(cards.indexOf((mouseEvent.getComponent()))).getCardName().equalsIgnoreCase("Sacs de sable")){

                      m.action = Utils.Action.SANDBAGUSAGE;
                      }
                   else {
                       m.action = Utils.Action.HELICOPTERUSAGE;
                   }
                   notifyObservers(m);
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
    }


    public void update(Adventurer currentAdventurer) {
        int i = 0;
        hand = currentAdventurer.getHand();
        for (Card card: hand.getCards()){
            BufferedImage img = Utils.loadImage("cartes/" + card.getCardName() + ".png");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 6, img.getHeight() / 6, Image.SCALE_SMOOTH));
                cards.get(i).setIcon(icon);
            }
            i++;
        }

        this.cardPanel.repaint();
    }

    @Override
    public void addObserver(IObserver<CardMessage> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<CardMessage> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(CardMessage message) {
        for (IObserver<CardMessage> o : observers) {
            o.update(this, message);
        }
    }

}
