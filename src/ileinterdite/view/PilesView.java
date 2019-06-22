package ileinterdite.view;

import ileinterdite.components.CardComponent;
import ileinterdite.model.Card;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class PilesView {

    private JFrame window;
    private JPanel mainPanel;
    private JPanel overviewPanel;
    private JPanel bigPicturePanel;
    private JPanel miniaturePanel;

    private HashMap<Utils.CardType, ArrayList<Card>> piles;
    private CardComponent treasureCard;
    private CardComponent floodCard;

    public PilesView() {
        piles = new HashMap<>();
        BufferedImage redBackImg = Utils.loadImage("cartes/Fondrouge.png");
        BufferedImage blueBackImg = Utils.loadImage("cartes/Fondbleu.png");

        SwingUtilities.invokeLater(() -> {
            window = new JFrame("Affichage de la défausse");
            window.setSize(720, 720);
            window.setUndecorated(true);
            window.setLocationRelativeTo(null);

            mainPanel = new JPanel(new BorderLayout());
            overviewPanel = new JPanel(new GridBagLayout());
            mainPanel.add(overviewPanel, BorderLayout.CENTER);
            window.add(mainPanel, BorderLayout.CENTER);

            JButton close = new JButton("Fermer");
            close.addActionListener((e) -> {
                showGrid();
                window.setVisible(false);
            });
            window.add(close, BorderLayout.SOUTH);

            bigPicturePanel = new JPanel(new BorderLayout());

            treasureCard = new CardComponent(null);
            treasureCard.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPile(Utils.CardType.TREASURE);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            floodCard = new CardComponent(null);
            floodCard.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPile(Utils.CardType.FLOOD);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            miniaturePanel = new JPanel(new GridLayout(1, 4));
            miniaturePanel.add(new CardComponent(redBackImg));
            miniaturePanel.add(treasureCard);
            miniaturePanel.add(floodCard);
            miniaturePanel.add(new CardComponent(blueBackImg));
        });
    }

    public JPanel getMiniaturesPanel() {
        return miniaturePanel;
    }

    public void updatePile(Utils.CardType type, ArrayList<Card> pile) {
        piles.put(type, pile);
        final BufferedImage img = (pile.size() > 0) ? Utils.loadImage("cartes/" + pile.get(pile.size() - 1).getCardName() + ".png") : null;
        if (type == Utils.CardType.TREASURE) {
            SwingUtilities.invokeLater(() -> treasureCard.setImg(img));
        } else {
            SwingUtilities.invokeLater(() -> floodCard.setImg(img));
        }
    }

    private void showPile(Utils.CardType type) {
        ArrayList<Card> pile = piles.get(type);
        if (pile == null || pile.size() == 0) {
            return;
        }
        int cardsPerLine = (int) (Math.ceil(Math.sqrt(pile.size())));

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;

        SwingUtilities.invokeLater(overviewPanel::removeAll);

        for (Card card : pile) {
            final BufferedImage cardImg = Utils.loadImage("cartes/" + card.getCardName() + ".png");
            final GridBagConstraints constraint = new GridBagConstraints(c.gridx, c.gridy, c.gridwidth, c.gridheight,
                    c.weightx, c.weighty, c.anchor, c.fill, c.insets, c.ipadx, c.ipady); // Save a final copy to use in invoke later

            SwingUtilities.invokeLater(() -> {
                CardComponent cardComp = new CardComponent(cardImg);
                cardComp.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showOneCard(cardImg);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                cardComp.repaint();
                overviewPanel.add(cardComp, constraint);
            });

            c.gridx++;
            if (c.gridx == cardsPerLine) {
                c.gridy++;
                c.gridx = 0;
            }
        }

        SwingUtilities.invokeLater(() -> {
            overviewPanel.revalidate();
            overviewPanel.repaint();
            window.setVisible(true);
        });
    }

    private void showOneCard(BufferedImage img) {
        SwingUtilities.invokeLater(() -> {
            CardComponent cardComp = new CardComponent(img);
            cardComp.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showGrid();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            bigPicturePanel.removeAll();
            bigPicturePanel.add(cardComp, BorderLayout.CENTER);
            bigPicturePanel.revalidate();
            mainPanel.removeAll();
            mainPanel.add(bigPicturePanel);
            mainPanel.revalidate();
        });
    }

    private void showGrid() {
        SwingUtilities.invokeLater(() -> {
            mainPanel.removeAll();
            mainPanel.add(overviewPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }
}
