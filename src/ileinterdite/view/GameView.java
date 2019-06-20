package ileinterdite.view;

import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;

public class GameView {
    private final JFrame window;

    private final JPanel bottomPanel;
    private final JPanel advViewPanel;

    private final JPanel gridPanel;

    public GameView(int width, int height) {

        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width, height);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        window.add(bottomPanel, BorderLayout.SOUTH);

        advViewPanel = new JPanel();
        bottomPanel.add(advViewPanel);

        /* We put the gridPanel into a panel with a GridBagLayout to take the "GetPreferredSize" in account
           and have a squared grid */
        JPanel gridContenant = new JPanel(new GridBagLayout());
        gridPanel = new JPanel(new GridLayout(1,1)) {
            // Code inspired from https://coderanch.com/t/629253/java/create-JPanel-maintains-aspect-ratio#2880512
            // By Louis Lewis

            /**
             * Override the preferred size to return the largest it can, in a square shape.
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize;
                Component c = getParent();
                if (c != null && c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();

                int s = Math.min(w, h);
                return new Dimension(s,s);
            }
        };

        gridPanel.setLayout(new GridLayout(1, 1));
        gridContenant.add(gridPanel);
        window.add(gridContenant, BorderLayout.CENTER);
    }

    public void setVisible() {
        window.setVisible(true);
    }

    public void setAdventurerView(AdventurerView view) {
        advViewPanel.removeAll();
        advViewPanel.add(view.getMainPanel());
        advViewPanel.repaint();
    }

    public void setGridView(GridView view) {
        gridPanel.add(view.getMainPanel());
    }

    public void showEndGame(JPanel panel) {
        window.getContentPane().removeAll();
        window.getContentPane().add(panel);
        window.repaint();
        panel.repaint();
    }
}
