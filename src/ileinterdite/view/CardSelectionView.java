package ileinterdite.view;

import ileinterdite.model.Card;
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

    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private JFrame window;
    private JPanel mainPanel;
    private JPanel choicePanel;
    private JPanel labelPanel;
    private ArrayList<JLabel> cardLabel;
    private ArrayList<Integer> cardSelected;
    private ArrayList<Card> cards;
    private ArrayList<BufferedImage> normalIco;
    private ArrayList<BufferedImage> unselectedIco;


    public CardSelectionView() {
        unselectedIco = new ArrayList<>();
        normalIco = new ArrayList<>();
        cardSelected = new ArrayList<>();
        cards = new ArrayList<>();
        observers = new CopyOnWriteArrayList<>();
        cardLabel = new ArrayList<>();

        SwingUtilities.invokeLater(() -> {
            this.initFrame();
            mainPanel = new JPanel(new BorderLayout());
            window.add(mainPanel);
        });

    }

    /**
     * draw each card of the array list permit to select one
     *
     * @param nbCard nb card to remove
     */
    public void update(ArrayList<Card> playerCards, int nbCard, String action) {
        SwingUtilities.invokeLater(() -> {
            windowLoad();

            mainPanel.removeAll();
            mainPanel.revalidate();
        });

        cards = playerCards;
        final int cardsSize = cards.size();
        SwingUtilities.invokeLater(() -> {
            labelPanel = new JPanel();
            labelPanel.add(new JLabel(textBuilder(nbCard, action), SwingConstants.CENTER));
            choicePanel = new JPanel(new GridLayout(1, cardsSize - 1));
        });

        for (Card card : cards) {

            String path = "cartes/" + card.getCardName();

            BufferedImage img = Utils.loadImage(path + ".png");
            if (img != null) {

                normalIco.add(img);
                unselectedIco.add(Utils.deepCopy(img));
                Utils.setOpacity(unselectedIco.get(unselectedIco.size() - 1), 128);

                img = unselectedIco.get(unselectedIco.size() - 1);
                final ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 4, img.getHeight() / 4, Image.SCALE_SMOOTH));
                final ArrayList<BufferedImage> normalImg = new ArrayList<>(normalIco);
                final ArrayList<BufferedImage> unselectedImg = new ArrayList<>(unselectedIco);
                SwingUtilities.invokeLater(() -> {
                    JLabel cardLabel = new JLabel("", SwingConstants.CENTER);
                    cardLabel.setIcon(icon);
                    cardLabel.addMouseListener(new MouseListener() {

                        @Override
                        public void mouseClicked(MouseEvent mouseEvent) {
                            int cardIndex;

                            JLabel component = (JLabel) mouseEvent.getComponent();
                            cardIndex = CardSelectionView.this.cardLabel.indexOf(component);
                            if (cardIndex != -1) {
                                JLabel selectedCard = CardSelectionView.this.cardLabel.get(cardIndex);

                                if (cardSelected.contains(cardIndex)) {
                                    BufferedImage cardImg = unselectedImg.get(cardIndex);
                                    selectedCard.setIcon(new ImageIcon(cardImg.getScaledInstance(cardImg.getWidth() / 4, cardImg.getHeight() / 4, Image.SCALE_SMOOTH)));

                                    cardSelected.remove((Integer) cardIndex);
                                    selectedCard.repaint();
                                } else {
                                    BufferedImage cardImg = normalImg.get(cardIndex);
                                    selectedCard.setIcon(new ImageIcon(cardImg.getScaledInstance(cardImg.getWidth() / 4, cardImg.getHeight() / 4, Image.SCALE_SMOOTH)));

                                    cardSelected.add(cardIndex);
                                    selectedCard.repaint();
                                }
                            }

                            if (cardSelected.size() == nbCard) {
                                Message m = new Message(Utils.Action.CARD_CHOICE, buildStringMessage(cardSelected));

                                mainPanel.remove(choicePanel);
                                windowClose();
                                notifyObservers(m);

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

                    mainPanel.add(labelPanel, BorderLayout.SOUTH);

                    choicePanel.add(cardLabel);

                    this.cardLabel.add(cardLabel);
                    mainPanel.add(choicePanel, BorderLayout.CENTER);

                    choicePanel.repaint();
                    window.setVisible(true);
                });
            }
        }
    }

    /**
     * window Initialisation set the look, the size, ect
     */
    private void initFrame() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 200);
        window.setTitle("Selection de Pions");
        window.setLocationRelativeTo(null);
        window.setUndecorated(true);


    }

    private String textBuilder(int nbCards, String action) {
        boolean singular;
        singular = nbCards == 1;

        String string = "Veuillez choisir ";
        string = string + nbCards;

        if (!singular) {
            string = string + " cartes";

        } else {
            string = string + " carte";
        }
        string = string + " à " + action;

        return string;
    }


    /**
     * Convert a index ArrayList to String with adventurer separated by /
     */
    private String buildStringMessage(ArrayList<Integer> cardSelected) {
        StringBuilder stringMessage = new StringBuilder();
        for (Integer i : cardSelected) {
            stringMessage.append("/").append(cards.get(i).getCardName());
        }
        return stringMessage.toString();
    }

    /**
     * Hide the window
     */
    private void windowClose() {
        window.setVisible(false);
    }

    /**
     * clear window last usage
     */
    private void windowLoad() {
        cardLabel.clear();
        cardSelected.clear();
        unselectedIco.clear();
        normalIco.clear();
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


