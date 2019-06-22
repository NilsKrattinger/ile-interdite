package ileinterdite.view;

import ileinterdite.model.Card;
import ileinterdite.util.Parameters;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawView implements ActionListener {

    private ArrayList<BufferedImage> cardsToShow;
    private JFrame window;
    private JPanel mainPanel;
    private JPanel cardPanel;
    private Timer updater;

    public DrawView() {
        cardsToShow = new ArrayList<>();

        SwingUtilities.invokeLater(() -> {
            window = new JFrame();
            window.setTitle("Cartes piochées");
            window.setSize(495, 689);
            window.setLocationRelativeTo(null);
            window.setUndecorated(true);

            remakeWindow();

            updater = new Timer(1500, this);
        });
    }

    private void remakeWindow() {
        window.getContentPane().removeAll();
        mainPanel = new JPanel(new BorderLayout());
        window.add(mainPanel, BorderLayout.CENTER);
        window.getContentPane().revalidate();
    }

    public void setCardsToShow(ArrayList<Card> newCards) {
        for (Card card : newCards) {
            BufferedImage img = Utils.loadImage("cartes/" + card.getCardName() + ".png");
            if (img != null) {
                this.cardsToShow.add(img);
            } else if (Parameters.LOGS) {
                System.out.println("cartes/" + card.getCardName().replaceAll("[\\s']", "") + ".png non trouvé");
            }
        }

        SwingUtilities.invokeLater(() -> {
            if (!window.isVisible()) {
                remakeWindow();
                window.setVisible(true);
                updateCard();
                updater.start();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateCard();
    }

    private void updateCard() {
        if (cardsToShow.size() > 0) {
            if (cardPanel != null) {
                mainPanel.remove(cardPanel);
            }
            final BufferedImage img = cardsToShow.get(0);
            cardPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    g.drawImage(img, 0, 0, g.getClipBounds().width, g.getClipBounds().height, this);
                }
            };
            mainPanel.add(cardPanel, BorderLayout.CENTER);
            mainPanel.revalidate();
            cardsToShow.remove(0);
        } else {
            mainPanel.removeAll();
            mainPanel.revalidate();
            updater.stop();
            window.setVisible(false);
        }
    }
}
