package ileinterdite.view;

import ileinterdite.model.adventurers.Adventurer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GameView {
    private JFrame window;

    private JPanel bottomPanel;
    private JPanel advViewPanel;

    private JPanel gridPanel;
    private JPanel handsPanel;
    private JPanel pilesPanel;
    private JPanel waterScalePanel;

    public GameView() {
        SwingUtilities.invokeLater(this::initView);
    }

    private void initView() {
        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
        window.setSize(width - screenInsets.left - screenInsets.right, height - screenInsets.top - screenInsets.bottom);
        window.setUndecorated(true);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        window.add(bottomPanel, BorderLayout.SOUTH);

        advViewPanel = new JPanel();
        bottomPanel.add(advViewPanel);

        /* We put the gridPanel into a panel with a GridBagLayout to take the "GetPreferredSize" in account
           and have a squared grid */
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridPanel = new JPanel(new GridLayout(1, 1)) {
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
                return new Dimension(s, s);
            }
        };

        gridPanel.setLayout(new GridLayout(1, 1));
        gridContainer.add(gridPanel);
        window.add(gridContainer, BorderLayout.CENTER);

        handsPanel = new JPanel();
        window.add(handsPanel, BorderLayout.EAST);

        JPanel leftPanel = new JPanel(new BorderLayout());

        pilesPanel = new JPanel(new GridLayout(1, 1));
        leftPanel.add(pilesPanel, BorderLayout.NORTH);

        waterScalePanel = new JPanel(new GridLayout(1, 1));
        leftPanel.add(waterScalePanel, BorderLayout.CENTER);

        window.add(leftPanel, BorderLayout.WEST);
    }

    public void setVisible() {
        SwingUtilities.invokeLater(() -> window.setVisible(true));
    }

    public void setAdventurerView(AdventurerView view) {
        SwingUtilities.invokeLater(() -> {
            advViewPanel.removeAll();
            advViewPanel.add(view.getMainPanel());
            advViewPanel.revalidate();
            advViewPanel.repaint();
        });
    }

    public void setGridView(GridView view) {
        SwingUtilities.invokeLater(() -> {
            gridPanel.add(view.getMainPanel());
            gridPanel.revalidate();
        });
    }

    public void setTreasureView(TreasureView treasureView) {
        SwingUtilities.invokeLater(() -> {
            bottomPanel.add(treasureView.getMainPanel());
            bottomPanel.revalidate();
        });
    }

    public void setHandViews(HashMap<Adventurer, HandView> handViews) {
        SwingUtilities.invokeLater(() -> {
            handsPanel.setLayout(new GridLayout(handViews.size(), 1));
            for (Adventurer adv : handViews.keySet()) {
                JPanel tempPanel = new JPanel();
                tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
                tempPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                tempPanel.add(new JLabel(adv.getName() + " (" + adv.getClassName() + ")"));
                tempPanel.add(handViews.get(adv).getMinimizedPanel());
                handsPanel.add(tempPanel);
            }
        });
    }

    public void setWaterScaleView(WaterScaleView view) {
        SwingUtilities.invokeLater(() -> {
            waterScalePanel.add(view.getMainPanel());
            waterScalePanel.revalidate();
        });
    }

    public void setPilesView(PilesView pilesView) {
        SwingUtilities.invokeLater(() -> {
            pilesPanel.add(pilesView.getMiniaturesPanel());
            pilesPanel.revalidate();
        });
    }

    public void showVictory(VictoryView view) {
        SwingUtilities.invokeLater(() -> showEndGame(view.getMainPanel()));
    }

    public void showDefeat(DefeatView view) {
        SwingUtilities.invokeLater(() -> showEndGame(view.getMainPanel()));
    }

    private void showEndGame(JPanel panel) {
        window.getContentPane().removeAll();
        window.getContentPane().add(panel);
        window.revalidate();
        panel.repaint();
    }
}
