package ileinterdite.view;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class AdventurerView implements IObservable<Message> {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private final JFrame window;
    private final JPanel mainPanel;
    private final JLabel nbActionsLabel;

    public AdventurerView(Adventurer ad) {
        observers = new CopyOnWriteArrayList<>();

        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(720, 220);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        this.window.add(mainPanel);

        JPanel cardPanel = new JPanel();

        JPanel actionButtonPanel = new JPanel(new GridLayout(2, 2));
        JButton moveButton = new JButton("Bouger") ;
        moveButton.addActionListener(e -> notifyObservers(new Message(Utils.Action.MOVE)));

        JButton dryButton = new JButton( "Assécher");
        dryButton.addActionListener(e -> notifyObservers(new Message(Utils.Action.DRY)));

        JButton giveCardButton = new JButton("Donner carte");
        giveCardButton.addActionListener(e -> notifyObservers(new Message(Utils.Action.GIVE_CARD)));

        JButton getTreasureButton = new JButton("Récupérer Trésor");
        getTreasureButton.addActionListener(e -> notifyObservers(new Message(Utils.Action.GET_TREASURE)));

        actionButtonPanel.add(moveButton);
        actionButtonPanel.add(dryButton);
        actionButtonPanel.add(giveCardButton);
        actionButtonPanel.add(getTreasureButton);
        mainPanel.add(actionButtonPanel);

        BufferedImage img = Utils.loadImage("personnages/" + ad.getClassName().toLowerCase() + ".png");
        if (img != null) {
            ImageIcon icon = new ImageIcon(img);
            JLabel adventurerCard = new JLabel();
            adventurerCard.setIcon(icon);
            mainPanel.add(adventurerCard);
        }

        JPanel nbActionsPanel = new JPanel(new GridBagLayout());
        //nbActionsPanel.setLayout(new BoxLayout(nbActionsPanel, BoxLayout.Y_AXIS));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        JPanel nbTextPanel = new JPanel();
        nbTextPanel.setAlignmentX(SwingConstants.CENTER);
        JLabel nbTextLabel = new JLabel("NB ACTIONS");
        nbTextLabel.setFont(new Font(nbActionsPanel.getFont().getName(), nbActionsPanel.getFont().getStyle(), 20));
        nbTextPanel.add(nbTextLabel);
        nbActionsPanel.add(nbTextPanel, c);

        c.gridy = 1;
        JPanel nbPanel = new JPanel();
        nbPanel.setAlignmentX(SwingConstants.CENTER);
        nbActionsLabel = new JLabel("3");
        nbActionsLabel.setFont(new Font(nbActionsPanel.getFont().getName(), nbActionsPanel.getFont().getStyle(), 26));
        nbPanel.add(nbActionsLabel);
        nbActionsPanel.add(nbPanel, c);

        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton finishTurnButton = new JButton("Fin tour");
        finishTurnButton.addActionListener(e -> notifyObservers(new Message(Utils.Action.END_TURN)));
        nbActionsPanel.add(finishTurnButton, c);

        mainPanel.add(nbActionsPanel);
    }

    public void showAdventurers(ArrayList<Adventurer> adventurers) {
        JFrame advChoice = new JFrame("Choix aventurier à déplacer");
        advChoice.setSize(300, 150);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        final JComboBox<String> advList = new JComboBox<>();
        for (Adventurer a : adventurers) {
            advList.addItem(a.getClassName() + " (" + a.getName() + ")");
        }
        main.add(advList);

        JButton validate = new JButton("Valider");
        validate.addActionListener(e -> {
            Message m = new Message(Utils.Action.NAVIGATOR_CHOICE, advList.getSelectedItem().toString());

            notifyObservers(m);
            advChoice.setVisible(false);
        });
        main.add(validate);

        advChoice.add(main);
        advChoice.setVisible(true);
    }

    public void show() {
        this.window.setVisible(true);
    }

    public void hide() {
        this.window.setVisible(false);
    }

    public void setNbActions(int nbActions) {
        nbActionsLabel.setText(Integer.toString(nbActions));
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