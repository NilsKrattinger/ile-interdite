package ileinterdite.view;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PawnsSelectionView implements IObservable<Message> {

    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private JFrame window;
    private JPanel mainPanel;
    private JButton annulerButton;
    private JButton validerButton;
    private JPanel buttonPanel;
    private ArrayList<JLabel> pawnsIco;
    private ArrayList<Integer> pawnsSelected;
    private ArrayList<Adventurer> adventurers;
    private JPanel choicePanel;


    public PawnsSelectionView() {
        pawnsSelected = new ArrayList<>();
        adventurers = new ArrayList<>();
        observers = new CopyOnWriteArrayList<>();
        mainPanel = new JPanel(new BorderLayout());
        pawnsIco = new ArrayList<>();

        this.initFrame();
        validerButton = new JButton("Valider");
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Message m = new Message(Utils.Action.ADVENTURER_CHOICE, buildStringMessage(pawnsSelected));

                mainPanel.remove(choicePanel);
                notifyObservers(m);
                windowClose();


            }
        });

        annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Message m = new Message(Utils.Action.CANCEL_ACTION, null);
                window.setVisible(false);
                notifyObservers(m);
            }
        });

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(validerButton,c);
        c.gridx = 2;
        c.gridy = 0;
        buttonPanel.add(annulerButton,c);

        //mainPanel.add(annulerButton, BorderLayout.SOUTH);
        window.add(mainPanel);

    }

    public void update(ArrayList<Adventurer> avalibleAdventurers, int nbAdveturer) {
        windowLoad();

        if(nbAdveturer > 1){
            validerButton.setEnabled(true);
            validerButton.setVisible(true);
        } else {
            validerButton.setEnabled(false);
            validerButton.setVisible(false);
        }


        adventurers = avalibleAdventurers;

        choicePanel = new JPanel(new GridLayout(1, adventurers.size() - 1));

        for (Adventurer adventuer : adventurers) {

            String path = "pions/" + "pion" + adventuer.getPawn().toString();

            JPanel pawnPanel = new JPanel(new GridLayout(2, 1));

            BufferedImage img = Utils.loadImage(path + ".png");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 2, img.getHeight() / 2, Image.SCALE_SMOOTH));
                JLabel pawnIco = new JLabel("", SwingConstants.CENTER);
                pawnIco.setIcon(icon);
                pawnIco.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        int pawnIndex;

                        pawnIndex = pawnsIco.indexOf(mouseEvent.getComponent());
                        if (pawnIndex != -1) {
                            JLabel pawn = pawnsIco.get(pawnIndex);
                            if (pawnsSelected.contains(pawnIndex)){
                                pawn.getParent().setBackground(Color.lightGray);
                                pawnsSelected.remove(pawnsSelected.indexOf(pawnIndex));
                            } else {
                                pawn.getParent().setBackground(Color.WHITE);
                                pawnsSelected.add(pawnIndex);
                            }
                        }

                        if (pawnsSelected.size() == nbAdveturer) {
                            Message m = new Message(Utils.Action.ADVENTURER_CHOICE, buildStringMessage(pawnsSelected));

                            mainPanel.remove(choicePanel);
                            notifyObservers(m);
                            windowClose();

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

                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                pawnsIco.add(pawnIco);

                pawnPanel.add(pawnIco);
                pawnPanel.add(new JLabel(adventuer.getName(), SwingConstants.CENTER));
                pawnPanel.setBackground(Color.lightGray);

                choicePanel.add(pawnPanel);
                mainPanel.add(choicePanel, BorderLayout.CENTER);

                choicePanel.repaint();
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

    private String buildStringMessage(ArrayList<Integer> pawnsSelected) {
        String stringMessage = "";
        for (Integer i : pawnsSelected) {
            stringMessage = stringMessage + "/" + adventurers.get(i).getClassName();
        }
        return stringMessage;
    }

    private void windowClose() {

        window.setVisible(false);

    }

    private void  windowLoad(){
        pawnsIco.clear();
        pawnsSelected.clear();
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
