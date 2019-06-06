package ileinterdite.view;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Utils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static javax.swing.SwingConstants.CENTER;


public class AdventurerView implements IObservable<Message> {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<Message>> observers;

    private final JPanel buttonsPanel;
    private final JPanel centeredPanel;
    private final JFrame window;
    private final JPanel adventurerPanel;
    private final JPanel mainPanel;
    private final JButton moveButton;
    private final JButton dryButton;
    private final JButton validateCellButton;
    private final JButton endTurnButton;
    private final JLabel advName;
    private JTextField position;

    public AdventurerView() {
        observers = new CopyOnWriteArrayList<>();

        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(350, 200);
        mainPanel = new JPanel(new BorderLayout());
        this.window.add(mainPanel);

        mainPanel.setBackground(new Color(230, 230, 230));

        // =================================================================================
        // NORD : le titre = nom de l'aventurier sur la couleurActive du pion

        this.adventurerPanel = new JPanel();
        advName = new JLabel("", SwingConstants.CENTER);
        adventurerPanel.add(advName);
        mainPanel.add(adventurerPanel, BorderLayout.NORTH);

        // =================================================================================
        // CENTRE : 1 ligne pour position courante
        this.centeredPanel = new JPanel(new GridLayout(2, 1));
        this.centeredPanel.setOpaque(false);
        mainPanel.add(this.centeredPanel, BorderLayout.CENTER);

        centeredPanel.add(new JLabel ("Position", SwingConstants.CENTER));
        position = new  JTextField(30);
        position.setHorizontalAlignment(CENTER);
        centeredPanel.add(position);


        // =================================================================================
        // SUD : les boutons
        this.buttonsPanel = new JPanel(new GridLayout(2,2));
        this.buttonsPanel.setOpaque(false);
        mainPanel.add(this.buttonsPanel, BorderLayout.SOUTH);

        this.moveButton = new JButton("Bouger") ;
        moveButton.addActionListener(e -> {
            notifyObservers(new Message(Utils.Action.MOVE));
        });
        this.dryButton = new JButton( "Assecher");
        dryButton.addActionListener(e -> {
            notifyObservers(new Message(Utils.Action.DRY));
        });
        this.validateCellButton = new JButton("Valider tuile");
        validateCellButton.addActionListener(e -> {
            String pos = position.getText();
            if (pos.length() == 0) {
                position.setBorder(BorderFactory.createLineBorder(Color.RED));
                position.grabFocus();
            } else {
                position.setBorder(null);
                notifyObservers(new Message(Utils.Action.VALIDATE_ACTION, pos));
                position.setText("");
            }
        });
        this.endTurnButton = new JButton("Terminer Tour");
        endTurnButton.addActionListener(e -> {
            notifyObservers(new Message(Utils.Action.END_TURN));
        });

        this.buttonsPanel.add(moveButton);
        this.buttonsPanel.add(dryButton);
        this.buttonsPanel.add(validateCellButton);
        this.buttonsPanel.add(endTurnButton);
    }

    public void setColor(Color color, Color textColor) {
        mainPanel.setBorder(BorderFactory.createLineBorder(color, 2)) ;
        adventurerPanel.setBackground(color);
        centeredPanel.setBorder(new MatteBorder(0, 0, 2, 0, color));
        advName.setForeground(textColor);
    }

    public void setText(String playerName, String adventurerName) {
        //le titre = nom du joueur
        window.setTitle(playerName);
        advName.setText(adventurerName + " (" + playerName + ")" );
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

    public void setVisible() {
        this.window.setVisible(true);
    }

    public void setPosition(String pos) {
        this.position.setText(pos);
    }

    public JButton getValidateCellButton() {
        return validateCellButton;
    }

    public String getPosition() {
        return position.getText();
    }

    public JButton getMoveButton() {
        return moveButton;
    }

    public JButton getDryButton() {
        return dryButton;
    }

    public JButton getEndTurnButton() {
        return endTurnButton;
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