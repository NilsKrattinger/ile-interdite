package ileinterdite.view;

import javax.swing.*;

public class VictoryView {

    private JPanel mainPanel;

    public VictoryView() {
        mainPanel = new JPanel();
        mainPanel.add(new JLabel("ViCTOiRE !"));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
