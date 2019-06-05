package ileinterdite.components;

import ileinterdite.util.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CellComponent extends JPanel {

    private BufferedImage image;
    private BufferedImage flooded_image;
    private Utils.State cellState;

    public CellComponent(String path, Utils.State state) {
        this.cellState = state;
        if (path != null) {
            path = path.replaceAll("[\\s']", "");
            try {
                path = "res/images/tuiles/" + path;
                image = ImageIO.read(new File(path + ".png"));
                flooded_image = ImageIO.read(new File(path + "_Inonde.png"));
            } catch (IOException ex) {
                System.out.println(path);
                ex.printStackTrace();
            }
        }
    }

    public void setState(Utils.State cellState) {
        this.cellState = cellState;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cellState == Utils.State.NORMAL || cellState == Utils.State.FLOODED) {
            if (!g.drawImage(((cellState == Utils.State.NORMAL) ? image : flooded_image), 0, 0, g.getClipBounds().width, g.getClipBounds().height, this)) {
                System.out.println("err");
            };
        }
    }

}