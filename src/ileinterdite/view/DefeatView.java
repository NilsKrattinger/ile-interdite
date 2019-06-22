package ileinterdite.view;

import javax.swing.*;
import java.awt.*;

public class DefeatView {

    private JPanel mainPanel;

    public DefeatView(boolean waterScale, boolean treasure, boolean heliport, boolean drown) {
        SwingUtilities.invokeLater(() -> {
            mainPanel = new JPanel(new BorderLayout());

            JLabel label = new JLabel("Vous avez perdu !");
            label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 24));
            label.setHorizontalAlignment(SwingConstants.CENTER);

            String deathMessage = "";
            if (waterScale) {
                deathMessage = "L'eau est montée trop vite, et vous vous êtes noyé.";
            } else if (treasure) {
                deathMessage = "Un trésor a sombré au fond de l'océan et n'est plus récupérable.";
            } else if (heliport) {
                deathMessage = "Votre hélicoptère a coulé et vous êtes condamnés à sombrer avec l'Ile Interdite.";
            } else if (drown) {
                deathMessage = "L'un de vos aventuriers a sombré au fond de l'océan, et le chagrin vous a tous emportés.";
            }

            JLabel deathLabel = new JLabel(deathMessage);
            deathLabel.setHorizontalAlignment(SwingConstants.CENTER);
            deathLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            mainPanel.add(label, BorderLayout.CENTER);
            mainPanel.add(deathLabel, BorderLayout.SOUTH);
            mainPanel.revalidate();
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
