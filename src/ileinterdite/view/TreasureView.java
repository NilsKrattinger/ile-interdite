package ileinterdite.view;

import ileinterdite.components.TreasureComponent;
import ileinterdite.model.Treasure;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TreasureView {

    private JPanel mainPanel;
    private HashMap<Treasure, TreasureComponent> components;

    public TreasureView(ArrayList<Treasure> treasures) {
        components = new HashMap<>();

        int totalWidth = 0;
        int maxHeight = 0;
        for (Treasure t : treasures) {
            TreasureComponent comp = new TreasureComponent(t);
            components.put(t, comp);
            totalWidth += comp.getWidth();
            maxHeight = Math.max(maxHeight, comp.getHeight());
        }

        final int width = totalWidth + treasures.size() * 10; // Add a little space between treasures
        final int height = maxHeight;
        mainPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(width, height);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        JPanel componentPanel = new JPanel(new GridLayout(1, 4));
        for (TreasureComponent comp : components.values()) {
            componentPanel.add(comp);
        }

        mainPanel.add(componentPanel);
    }

    public void collectTreasure(Treasure t) {
        components.get(t).setCollected();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
