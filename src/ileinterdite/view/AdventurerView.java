package ileinterdite.view;

import ileinterdite.model.Hand;
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

    private final JPanel mainPanel;
    private final JLabel nbActionsLabel;
    private  JPanel adventurerPanel;

    public AdventurerView(Adventurer ad, HandView handView) {
        observers = new CopyOnWriteArrayList<>();
        mainPanel = new JPanel(new BorderLayout()) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(getParent().getWidth(), (int) (d.getHeight()));

            }
        };



        adventurerPanel = new JPanel(new BorderLayout());
        adventurerPanel.setLayout(new BoxLayout(adventurerPanel, BoxLayout.X_AXIS));

        JPanel handMain = handView.getMainPanel();
        handMain.setAlignmentX(Component.LEFT_ALIGNMENT);
        adventurerPanel.add(handMain);


        mainPanel.add(handMain,BorderLayout.WEST);



        mainPanel.add(adventurerPanel, BorderLayout.CENTER);

        mainPanel.add(new JLabel(""),BorderLayout.EAST);






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
        adventurerPanel.add(actionButtonPanel);

        BufferedImage img = Utils.loadImage("personnages/" + ad.getClassName().toLowerCase() + ".png");
        if (img != null) {
            ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 2, img.getHeight() / 2, Image.SCALE_SMOOTH));
            JLabel adventurerCard = new JLabel();
            adventurerCard.setIcon(icon);
            adventurerPanel.add(adventurerCard);
        }

        JPanel nbActionsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        JPanel nbTextPanel = new JPanel();
        nbTextPanel.setAlignmentX(SwingConstants.CENTER);
        JLabel nbTextLabel = new JLabel("NB ACTIONS");
        nbTextLabel.setFont(new Font(nbActionsPanel.getFont().getName(), nbActionsPanel.getFont().getStyle(), 18));
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

        adventurerPanel.add(nbActionsPanel);
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

    public JPanel getMainPanel() {
        return mainPanel;
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