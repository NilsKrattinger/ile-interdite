package ileinterdite.components;

import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PawnComponent extends JPanel {

    private BufferedImage pawnImage;
    private int x;
    private int y;

    public PawnComponent(Utils.Pawn pawn, int x, int y) {
        this.x = x;
        this.y = y;

        String path = "pions/pion" + pawn.toString() + ".png";
        pawnImage = Utils.loadImage(path);
        setOpaque(false);
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(pawnImage, 0, 0, g.getClipBounds().width, g.getClipBounds().height, this);
    }
}
