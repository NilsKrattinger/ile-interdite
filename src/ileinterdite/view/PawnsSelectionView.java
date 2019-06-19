package ileinterdite.view;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PawnsSelectionView  implements IObservable<Message> {

    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private JFrame window;
    private JPanel mainPanel;
    private JPanel choicePanel;
    private JButton annulerButton;
    private ArrayList<JLabel> pawnsIco;


    public PawnsSelectionView() {
        observers = new CopyOnWriteArrayList<>();
        pawnsIco = new ArrayList<>();
        mainPanel = new JPanel(new BorderLayout());

        annulerButton = new JButton("Annuler");
        choicePanel = new JPanel();



        mainPanel.add(annulerButton,BorderLayout.SOUTH);
        mainPanel.add(choicePanel,BorderLayout.CENTER);


        this.initFrame();
        this.window.setVisible(false);
    }

    public void update(ArrayList<Adventurer> adventurers) {

        choicePanel = new JPanel(new GridLayout(1, adventurers.size() - 1));


        for (Adventurer adventuer : adventurers) {
            JPanel tmp = new JPanel(new GridLayout(2, 1));

            String path = adventuer.getPawn().toString();
            path = "pion" + path;
            path = "pions/" + path;

            BufferedImage img = Utils.loadImage(path + ".png");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 2, img.getHeight() / 2, Image.SCALE_SMOOTH));
                JLabel pawnIco = new JLabel("", SwingConstants.CENTER);
                pawnIco.setIcon(icon);
                pawnIco.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        int index;
                        index = PawnsSelectionView.this.pawnsIco.indexOf(mouseEvent.getComponent());
                        if (index != -1) {
                            Message m = new Message(Utils.Action.ADVENTURER_CHOICE, adventurers.get(index).getClassName());
                            pawnsIco.clear();
                            choicePanel = null;
                            window.setVisible(false);
                            notifyObservers(m);
                        }

                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {

                    }
                });
                pawnsIco.add(pawnIco);
                tmp.add(pawnIco);
                tmp.add(new JLabel(adventuer.getName(), SwingConstants.CENTER));
                choicePanel.add(tmp);
                mainPanel.add(choicePanel,BorderLayout.CENTER);
                window.add(mainPanel);
                window.repaint();
                window.setVisible(true);

            }

        }
    }

    private void initFrame() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 200);
        window.setTitle("Selection de Pions");
        window.setLocationRelativeTo(null);
        window.setUndecorated(true);


    }

    @Override
    public void addObserver(IObserver<Message> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<Message> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Message message) {
        for (IObserver<Message> o : observers) {
            o.update(this, message);
        }
    }
}
