package ileinterdite.components;

import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WaterScaleComponent extends JPanel {

    private static final float SPACE_PERCENTAGE = 7.86f;
    private static final float START_PERCENTAGE = 82.21f;
    private static final float STICK_WIDTH_PERCENTAGE = 33.64f;

    private BufferedImage scaleImage;
    private BufferedImage stickImage;
    private int level;

    public WaterScaleComponent() {
        scaleImage = Utils.loadImage("Niveau.png");
        stickImage = Utils.loadImage("stick.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int height = g.getClipBounds().height;
        int width = g.getClipBounds().width;
        g.drawImage(scaleImage, 0, 0, width, height, this);

        float stickRatio = (float) (stickImage.getWidth()) / stickImage.getHeight();
        int stickWidth = (int) (width * STICK_WIDTH_PERCENTAGE / 100);
        int stickHeight = (int) (stickWidth / stickRatio);
        int y = (int) (height * START_PERCENTAGE / 100 - (level - 1) * SPACE_PERCENTAGE / 100 * height - stickHeight / 2);
        g.drawImage(stickImage, 0, y, stickWidth, stickHeight, this);
    }

    public void setLevel(int level) {
        this.level = level;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        float ratio = (float) (scaleImage.getWidth()) / scaleImage.getHeight();
        int height = getParent().getSize().height;
        int width = (int) (height * ratio);
        return new Dimension(width, height);
    }
}
