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

public class HandView implements IObservable<Message> {

    private final CopyOnWriteArrayList<IObserver<Message>> observers;


    private JPanel mainPanel;
    private JPanel cardPanel;

    private JPanel minimizedMainPanel;
    private JPanel minimizedCardPanel;

    private ArrayList<JLabel> cards;
    private ArrayList<JLabel> littleCards;

    private Hand hand;

    public HandView(Adventurer adventurer) {
        observers = new CopyOnWriteArrayList<>();

        cards = new ArrayList<>();
        littleCards = new ArrayList<>();

        initCards(cards, false);
        initCards(littleCards, true);

        mainPanel = new JPanel(new BorderLayout());
        cardPanel = new JPanel(new GridLayout(1, Hand.NB_MAX_CARDS));

        minimizedMainPanel = new JPanel(new BorderLayout());
        minimizedCardPanel = new JPanel(new GridLayout(1, Hand.NB_MAX_CARDS));

        for (int i = 0; i < Hand.NB_MAX_CARDS; i++) {
            cardPanel.add(cards.get(i));
            minimizedCardPanel.add(littleCards.get(i));
        }

        mainPanel.add(cardPanel);
        minimizedMainPanel.add(minimizedCardPanel);

        this.update(adventurer);
    }

    private void initCards(ArrayList<JLabel> cards, boolean rightToLeft) {
        for (int i = 0; i < Hand.NB_MAX_CARDS; i++) {
            cards.add(new JLabel());
            cards.get(i).setIcon(null);
            cards.get(i).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    int index = cards.indexOf((mouseEvent.getComponent()));
                    index = (rightToLeft) ? Hand.NB_MAX_CARDS - index - 1 : index;
                    if (index < hand.getSize()){
                         if (hand.getCard(index) != null) {
                            Message m = new Message(Utils.Action.USE_TREASURE_CARD, Integer.toString(index));

                            if (Parameters.LOGS){
                                System.out.println(m.action);
                            }
                            notifyObservers(m);

                         }
                    }
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
        updateLabels(currentAdventurer, cards, 6, false);
        updateLabels(currentAdventurer, littleCards, 10, true);
        this.cardPanel.repaint();
        this.minimizedCardPanel.repaint();
    }

    private void updateLabels(Adventurer currentAdventurer, ArrayList<JLabel> cards, int reducedSize, boolean rightToLeft) {
        int i = 0;
        String path;
        hand = currentAdventurer.getHand();
        for (Card card : hand.getCards()) {
            path = card.getCardName();
            path = path.replaceAll("[\\s']", "");
            path = "cartes/" + path;
            BufferedImage img = Utils.loadImage(path + ".png");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / reducedSize, img.getHeight() / reducedSize, Image.SCALE_SMOOTH));
                int index = rightToLeft ? cards.size() - i - 1 : i;
                cards.get(index).setIcon(icon);
            }
            i++;
        }
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

    public JPanel getMainPanel(){
        return this.mainPanel;
    }

    public JPanel getMinimizedPanel(){
        return this.minimizedMainPanel;
    }

}
