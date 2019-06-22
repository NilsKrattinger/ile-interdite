package ileinterdite.view;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerSelectionView implements IObservable<StartMessage> {


    private final CopyOnWriteArrayList<IObserver<StartMessage>>observers;

    private JFrame window;
    private JPanel mainPanel;
    private JPanel choicePanel;
    private ArrayList<JLabel> adventurerCardLabels;
    private ArrayList<Integer> adventurerSelected;
    private ArrayList<Adventurer> adventurers;
    private ArrayList<BufferedImage> normalIco;
    private ArrayList<BufferedImage> selectedIco;


    public PlayerSelectionView(ArrayList<Adventurer> adv, int nbAdv) {
        observers = new CopyOnWriteArrayList<>();


        adventurers = adv;

        normalIco = new ArrayList<>();
        selectedIco = new ArrayList<>();
        adventurerSelected = new ArrayList<>();
        adventurerCardLabels = new ArrayList<>();

        final int adventurerSize = adventurers.size();
        SwingUtilities.invokeLater(() -> {
            this.initFrame();
            mainPanel = new JPanel(new BorderLayout());
            choicePanel = new JPanel(new GridLayout(1, adventurerSize-1));
        });


        for (Adventurer adventurer : adventurers) {
            BufferedImage img = Utils.loadImage("personnages/" + adventurer.getClassName().toLowerCase() + ".png");
            if (img != null) {
                selectedIco.add(img);
                final ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() , img.getHeight() , Image.SCALE_SMOOTH));
                SwingUtilities.invokeLater(() -> {
                    JLabel advImgLabel = new JLabel("", SwingConstants.CENTER);
                    advImgLabel.setIcon(icon);
                    adventurerCardLabels.add(advImgLabel);
                    advImgLabel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent mouseEvent) {
                            int cardIndex;
                            cardIndex = adventurerCardLabels.indexOf(mouseEvent.getComponent());
                            if (cardIndex != -1) {
                                JLabel selectedCard = adventurerCardLabels.get(cardIndex);
                                if (adventurerSelected.contains(cardIndex)) {
                                    BufferedImage cardimg = normalIco.get(cardIndex);
                                    selectedCard.setIcon(new ImageIcon(cardimg.getScaledInstance(cardimg.getWidth(), cardimg.getHeight(), Image.SCALE_SMOOTH)));
                                    adventurerSelected.remove(adventurerSelected.indexOf(cardIndex));
                                    selectedCard.repaint();
                                } else {
                                    BufferedImage cardimg = selectedIco.get(cardIndex);
                                    selectedCard.setIcon(new ImageIcon(cardimg.getScaledInstance(cardimg.getWidth(), cardimg.getHeight(), Image.SCALE_SMOOTH)));
                                    adventurerSelected.add(cardIndex);
                                    selectedCard.repaint();
                                }
                            }
                            if (adventurerSelected.size() == nbAdv) {
                                StartMessage m = new StartMessage();
                                for (Integer advIndex : adventurerSelected) {
                                    m.adventurers.add(adventurers.get(advIndex));
                                }
                                window.setVisible(false);
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
                    choicePanel.add(advImgLabel);
                });
            }
        }
        this.createIcons();

        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < adventurerCardLabels.size(); i++) {
                adventurerCardLabels.get(i).setIcon(new ImageIcon(normalIco.get(i).getScaledInstance(normalIco.get(i).getWidth(), normalIco.get(i).getHeight(), Image.SCALE_SMOOTH)));

            }
            choicePanel.repaint();
            JLabel text = new JLabel(textbuilder(nbAdv), SwingConstants.CENTER);
            text.setFont(new Font(text.getFont().getFamily(), text.getFont().getStyle(), 25));
            mainPanel.add(text, BorderLayout.NORTH);
            mainPanel.add(choicePanel, BorderLayout.CENTER);
            window.add(mainPanel);

            this.window.setVisible(true);
        });
    }


    /**
     * window Initalisation set the look, the size, ect
     */
    private void initFrame() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 350);
        window.setTitle("Selection de personnages");
        window.setLocationRelativeTo(null);
        window.setUndecorated(true);
    }

    private String textbuilder(int nbCards) {
        boolean singular;
        int nbCardtodeff = 0;

        nbCardtodeff = nbCards;
        singular = nbCardtodeff == 1;

        String string = "";
        string = "Veuillez choisir ";
        string = string + nbCardtodeff;
        string = string + " aventuriers";

        return string;
    }


    private void createIcons() {
        for (BufferedImage img : selectedIco) {
            normalIco.add(Utils.deepCopy(img));
            Utils.setOpacity(normalIco.get(normalIco.size() - 1), 128);

        }
    }

    @Override
    public void addObserver(IObserver<StartMessage> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<StartMessage> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(StartMessage message) {
        for (IObserver<StartMessage> o : observers) {
            o.update(this, message);
        }
    }
}




