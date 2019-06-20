package ileinterdite.view;

import javax.swing.*;

public class DefeatView {

    private JPanel mainPanel;

    public DefeatView() {
        mainPanel = new JPanel();
        JLabel label = new JLabel("DÉFAiTE !");
        mainPanel.add(label);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
