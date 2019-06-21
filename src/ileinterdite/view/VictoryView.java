package ileinterdite.view;

import javax.swing.*;
import java.awt.*;

public class VictoryView {

    private JPanel mainPanel;

    public VictoryView() {
        mainPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Félicitations, vous avez gagné !");
        label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel deathLabel = new JLabel("Vous avez su déjouer l'Ile Interdite et lui subtiliser ses précieux trésors !");
        deathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deathLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        mainPanel.add(label, BorderLayout.CENTER);
        mainPanel.add(deathLabel, BorderLayout.SOUTH);
        mainPanel.revalidate();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
