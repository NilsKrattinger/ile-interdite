package ileinterdite.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CardComponent extends JPanel {

    private BufferedImage img;

    public CardComponent(BufferedImage img) {
        this.img = img;
        setOpaque(false);
    }

    public void setImg(BufferedImage img) {
        this.img = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (img != null) {
            int availableWidth = g.getClipBounds().width;
            int availableHeight = g.getClipBounds().height;
            Dimension d = getDimension(img, availableWidth, availableHeight);
            g.drawImage(img, (availableWidth - d.width) / 2, (availableHeight - d.height) / 2, d.width, d.height, this);
        }
    }

    private Dimension getDimension(BufferedImage img, int availableWidth, int availableHeight) {
        Dimension result = new Dimension();

        float ratio = (float) img.getWidth() / img.getHeight();

        if (availableWidth / ratio > availableHeight) {
            result.height = availableHeight;
            result.width = (int) (result.height * ratio);
        } else {
            result.width = availableWidth;
            result.height = (int) (result.width / ratio);
        }

        return result;
    }

    @Override
    public Dimension getPreferredSize() {
        if (img != null) {
            float ratio = (float) (img.getWidth()) / img.getHeight();
            Component c = getParent();
            int height;
            do {
                height = c.getSize().height;
                c = c.getParent();
            } while (height <= 100 && c != null);
            height = (int) (height * 0.1f);
            int width = (int) (height * ratio);
            return new Dimension(width, height);
        } else {
            return new Dimension(0, 0);
        }
    }
}
