package ileinterdite.components;

import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class CellComponent extends JPanel implements IObservable<Tuple<Integer, Integer>>, MouseListener {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<Tuple<Integer, Integer>>> observers;
    private final ArrayList<PawnComponent> pawns;

    private int x; private int y;
    private BufferedImage image;
    private BufferedImage translucentImage;
    private BufferedImage floodedImage;
    private BufferedImage translucentFloodedImage;
    private Utils.State cellState;
    private Utils.State accessible;
    private Rectangle positionInWindow;

    public CellComponent(String path, Utils.State state, int x, int y) {
        setOpaque(false);
        observers = new CopyOnWriteArrayList<>();
        pawns = new ArrayList<>();

        this.x = x;
        this.y = y;
        this.cellState = state;
        if (path != null) {
            path = path.replaceAll("[\\s']", "");
            path = "tuiles/" + path;
            image = Utils.loadImage(path + ".png");
            if (image != null) {
                translucentImage = Utils.deepCopy(image);
                Utils.setOpacity(translucentImage, 100);
            }

            floodedImage = Utils.loadImage(path + "_Inonde.png");
            if (floodedImage != null) {
                translucentFloodedImage = Utils.deepCopy(floodedImage);
                Utils.setOpacity(translucentFloodedImage, 100);
            }
        }

        addMouseListener(this);
    }

    public void addPawn(PawnComponent pawn) {
        this.pawns.add(pawn);
        repaint();
    }

    public void removePawn(PawnComponent pawn) {
        this.pawns.remove(pawn);
        repaint();
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
            for (int i = 0; i < pawns.size(); i++) {
                int width = g.getClipBounds().width / 3;
                int height = g.getClipBounds().height / 3;
                int x = (i % 2 == 1) ? width + (int) (width / 1.5) + 5 : (int) (width / 1.5);
                int y = (i > 1) ? height + height / 3 + 5 : height / 3;

                PawnComponent pawn = pawns.get(i);
                pawn.paintComponent(g.create(x, y, width, height));
            }
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
        if (accessible == Utils.State.ACCESSIBLE && positionInWindow != null && positionInWindow.contains(e.getX(), e.getY())) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (accessible != Utils.State.ACCESSIBLE || positionInWindow == null || !positionInWindow.contains(e.getX(), e.getY())) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
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