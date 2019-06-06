package ileinterdite.components;

import ileinterdite.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class CellComponent extends JPanel implements IObservable<Tuple<Integer, Integer>>, MouseListener {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<Tuple<Integer, Integer>>> observers;

    private int x; private int y;
    private BufferedImage image;
    private BufferedImage translucentImage;
    private BufferedImage floodedImage;
    private BufferedImage translucentFloodedImage;
    private Utils.State cellState;
    private Utils.State accessible;
    private Rectangle positionInWindow;

    public CellComponent(String path, Utils.State state, int x, int y) {
        observers = new CopyOnWriteArrayList<>();

        this.x = x;
        this.y = y;
        this.cellState = state;
        if (path != null) {
            path = path.replaceAll("[\\s']", "");
            try {
                path = "res/images/tuiles/" + path;
                image = ImageIO.read(new File(path + ".png"));
                floodedImage = ImageIO.read(new File(path + "_Inonde.png"));
                translucentImage = Utils.deepCopy(image);
                translucentFloodedImage = Utils.deepCopy(floodedImage);
                Utils.setOpacity(translucentImage, 100);
                Utils.setOpacity(translucentFloodedImage, 100);
            } catch (IOException ex) {
                System.out.println(path);
                ex.printStackTrace();
            }
        }

        addMouseListener(this);
    }

    public void setState(Utils.State cellState) {
        this.cellState = cellState;
        repaint();
    }

    public void setAccessible(Utils.State state) {
        this.accessible = state;
        repaint();
    }

    public void resetAccessible() {
        this.accessible = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cellState == Utils.State.NORMAL || cellState == Utils.State.FLOODED) {
            BufferedImage img;
            if (cellState == Utils.State.NORMAL) {
                img = (accessible != Utils.State.INACCESSIBLE) ? image : translucentImage;
            } else {
                img = (accessible != Utils.State.INACCESSIBLE) ? floodedImage : translucentFloodedImage;
            }

            g.drawImage(img, 0, 0, g.getClipBounds().width, g.getClipBounds().height, this);
        }

        positionInWindow = g.getClipBounds();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int xClick = e.getX();
        int yClick = e.getY();
        if (positionInWindow != null && positionInWindow.contains(xClick, yClick)) {
            notifyObservers(new Tuple<>(x, y));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void addObserver(IObserver<Tuple<Integer, Integer>> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<Tuple<Integer, Integer>> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Tuple<Integer, Integer> message) {
        for (IObserver<Tuple<Integer, Integer>> observer : observers) {
            observer.update(this, message);
        }
    }
}