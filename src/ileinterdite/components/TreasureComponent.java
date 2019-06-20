package ileinterdite.components;

import ileinterdite.model.Treasure;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TreasureComponent extends JPanel {

    private static final float resize = 1.5f;

    private boolean collected;
    private BufferedImage transparentImage;
    private BufferedImage image;

    public TreasureComponent(Treasure treasure) {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        collected = false;
        String path = treasure.getName().replaceAll("[\\s']", "");
        path = "tresors/" + path + ".png";
        image = Utils.loadImage(path);

        if (image != null) {
            transparentImage = Utils.deepCopy(image);
            Utils.setOpacity(transparentImage, 100);
        }
    }

    public int getWidth() {
        return (int) (image.getWidth() / resize);
    }

    public int getHeight() {
        return (int) (image.getHeight() / resize);
    }

    public void setCollected() {
        collected = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(((collected) ? image : transparentImage), 0, 0, (int) (image.getWidth() / resize), (int) (image.getHeight() / resize), this);
    }
}
