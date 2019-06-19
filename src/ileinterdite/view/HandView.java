package ileinterdite.view;

import ileinterdite.model.Card;
import ileinterdite.model.Hand;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Message;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandView implements IObservable<Message> {

    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private JFrame windo;
    private JPanel mainPanel;
    private JPanel cardPanel;
    private ArrayList<JLabel> cards;
    private Hand hand;

    public HandView(Adventurer adventurer) {
        observers = new CopyOnWriteArrayList<>();

        cards = new ArrayList<>();
        initFram(adventurer);
        initcards();

        mainPanel = new JPanel(new BorderLayout());
        cardPanel = new JPanel(new GridLayout(1, 5));

        for (int i = 0; i < 5; i++) {
            cardPanel.add(cards.get(i));
        }

        mainPanel.add(cardPanel);
        windo.add(mainPanel);

        this.update(adventurer);

    }


    private void initFram(Adventurer adv) {
        windo = new JFrame();
        windo.setTitle(adv.getName());
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
                    int index = cards.indexOf((mouseEvent.getComponent()));
                    if (index < hand.getSize()){
                         if (hand.getCard(index) != null) {
                            Message m = new Message(Utils.Action.USE_TREASURE_CARD, Integer.toString(index));

                            System.out.println(m.action);
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
        int i = 0;
        String path;
        hand = currentAdventurer.getHand();
        for (Card card : hand.getCards()) {
            path = card.getCardName();
            path = path.replaceAll("[\\s']", "");
            path = "cartes/" + path;
            BufferedImage img = Utils.loadImage(path + ".png");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 6, img.getHeight() / 6, Image.SCALE_SMOOTH));
                cards.get(i).setIcon(icon);
            }
            i++;
        }

        this.cardPanel.repaint();
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
