package ileinterdite.view;

import ileinterdite.components.WaterScaleComponent;

import javax.swing.*;

public class WaterScaleView {

    private JPanel mainPanel;
    private WaterScaleComponent scale;

    public WaterScaleView() {
        SwingUtilities.invokeLater(() -> {
            mainPanel = new JPanel();
            scale = new WaterScaleComponent();
            mainPanel.add(scale);
        });
    }

    public void setScale(int level) {
        SwingUtilities.invokeLater(() -> scale.setLevel(level));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
