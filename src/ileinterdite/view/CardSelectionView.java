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
    private ArrayList<BufferedImage> selectedIco;


    public CardSelectionView() {
        selectedIco = new ArrayList<>();
        normalIco = new ArrayList<>();
        cardSelected = new ArrayList<>();
        cards = new ArrayList<>();
        observers = new CopyOnWriteArrayList<>();
        mainPanel = new JPanel(new BorderLayout());
        cardLabel = new ArrayList<>();

        this.initFrame();
        window.add(mainPanel);

    }

    /**
     * draw each card of the array list permit to select one
     * @param playerCards
     * @param nbCard nb card to remove
     */
    public void update(ArrayList<Card> playerCards, int nbCard) {
        windowLoad();


        cards = playerCards;
        labelPanel = new JPanel();
        labelPanel.add(new JLabel(textbuilder(nbCard, "deffausser"), SwingConstants.CENTER));
        choicePanel = new JPanel(new GridLayout(1, cards.size() - 1));

        for (Card card : cards) {

            String path = "cartes/" + card.getCardName().replaceAll("[\\s']", "");

            BufferedImage img = Utils.loadImage(path + ".png");
            if (img != null) {

                normalIco.add(img);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 4, img.getHeight() / 4, Image.SCALE_SMOOTH));
                JLabel cardlLabel = new JLabel("", SwingConstants.CENTER);
                cardlLabel.setIcon(icon);
                cardlLabel.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        int cardIndex;

                        cardIndex = cardLabel.indexOf(mouseEvent.getComponent());
                        if (cardIndex != -1) {
                            JLabel selectedCard = cardLabel.get(cardIndex);

                            if (cardSelected.contains(cardIndex)) {
                                BufferedImage cardimg = normalIco.get(cardIndex);
                                selectedCard.setIcon(new ImageIcon(cardimg.getScaledInstance(cardimg.getWidth() / 4, cardimg.getHeight() / 4, Image.SCALE_SMOOTH)));

                                cardSelected.remove(cardSelected.indexOf(cardIndex));
                                selectedCard.repaint();
                            } else {
                                BufferedImage cardimg = selectedIco.get(cardIndex);
                                selectedCard.setIcon(new ImageIcon(cardimg.getScaledInstance(cardimg.getWidth() / 4, cardimg.getHeight() / 4, Image.SCALE_SMOOTH)));

                                cardSelected.add(cardIndex);
                                selectedCard.repaint();
                            }
                        }

                        if (cardSelected.size() == nbCard) {
                            Message m = new Message(Utils.Action.ADVENTURER_CHOICE, buildStringMessage(cardSelected));

                            mainPanel.remove(choicePanel);
                            windowClose();

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

                choicePanel.add(cardlLabel);

                cardLabel.add(cardlLabel);
                mainPanel.add(choicePanel, BorderLayout.CENTER);

                choicePanel.repaint();
                window.setVisible(true);
            }
        }
        creaateSelectedIcons();
    }

    /**
     * window Initalisation set the look, the size, ect
     */
    private void initFrame() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 200);
        window.setTitle("Selection de Pions");
        window.setLocationRelativeTo(null);
        window.setUndecorated(true);


    }

    private String textbuilder(int nbCards, String action) {
        boolean singular;
        int nbCardtodeff = 0;

        nbCardtodeff = nbCards;
        singular = nbCardtodeff == 1;

        String string = "";
        string = "Veuillez choisir ";
        string = string + nbCardtodeff;

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
     *
     * @param cardSelected
     * @return
     */
    private String buildStringMessage(ArrayList<Integer> cardSelected) {
        String stringMessage = "";
        for (Integer i : cardSelected) {
            stringMessage = stringMessage + "/" + cards.get(i).getCardName();
        }
        return stringMessage;
    }


    private void creaateSelectedIcons() {
        for (BufferedImage img : normalIco) {
            selectedIco.add(Utils.deepCopy(img));
            Utils.setOpacity(selectedIco.get(selectedIco.size() - 1), 128);

        }
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
        selectedIco.clear();
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


