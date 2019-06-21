package ileinterdite.view;

import ileinterdite.components.WaterScaleComponent;

import javax.swing.*;

public class WaterScaleView {

    private JPanel mainPanel;
    private WaterScaleComponent scale;

    public WaterScaleView() {
        mainPanel = new JPanel();
        scale = new WaterScaleComponent();
        mainPanel.add(scale);
    }

    public void setScale(int level) {
        scale.setLevel(level);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
