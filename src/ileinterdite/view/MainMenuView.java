package ileinterdite.view;

import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainMenuView implements IObservable<ArrayList<String>> {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<ArrayList<String>>> observers;

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    private final JFrame window;
    private JPanel nbPlayersPanel;
    private JTextField nbPlayersTextField;
    private JPanel playersNamePanel;
    private ArrayList<JTextField> playerNameFields;

    public MainMenuView() {
        this.observers = new CopyOnWriteArrayList<>();

        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(350, 250);
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.window.add(mainPanel);

        nbPlayersPanel = new JPanel();
        nbPlayersTextField = new JTextField("2");
        nbPlayersTextField.setDisabledTextColor(Color.BLACK);
        nbPlayersTextField.setEnabled(false);

        JButton minus = new JButton("-");
        minus.addActionListener(actionEvent -> {
            nbPlayersTextField.setText(Integer.toString(Math.max(MIN_PLAYERS, Integer.valueOf(nbPlayersTextField.getText()) - 1)));
            updatePlayerNames();
        });
        JButton plus = new JButton("+");
        plus.addActionListener(actionEvent -> {
            nbPlayersTextField.setText(Integer.toString(Math.min(MAX_PLAYERS, Integer.valueOf(nbPlayersTextField.getText()) + 1)));
            updatePlayerNames();
        });

        nbPlayersPanel.add(minus);
        nbPlayersPanel.add(nbPlayersTextField);
        nbPlayersPanel.add(plus);
        mainPanel.add(nbPlayersPanel, BorderLayout.NORTH);

        playersNamePanel = new JPanel();
        playersNamePanel.setLayout(new BoxLayout(playersNamePanel, BoxLayout.Y_AXIS));
        mainPanel.add(playersNamePanel);
        playerNameFields = new ArrayList<>();

        updatePlayerNames();

        JButton validateButton = new JButton("Démarrer la partie");
        validateButton.addActionListener(actionEvent -> {
            boolean canStart = true;

            ArrayList<String> playerNames = new ArrayList<>();
            for (JTextField field : playerNameFields) {
                String playerName = field.getText();
                playerNames.add(playerName);

                //if (playerName.isEmpty()) { canStart = false; }
            }

            if (canStart) {
                notifyObservers(playerNames);
                window.setVisible(false);
            } else {
                Utils.showInformation("Les noms de joueurs ne peuvent pas être vides !");
            }
        });
        mainPanel.add(validateButton, BorderLayout.SOUTH);
    }

    private void updatePlayerNames() {
        int nbPlayers = Integer.valueOf(nbPlayersTextField.getText());
        while (playerNameFields.size() < nbPlayers) {
            JTextField nameField = new JTextField();
            nameField.setColumns(10);
            playerNameFields.add(nameField);
        }

        while (playerNameFields.size() > nbPlayers) {
            playerNameFields.remove(playerNameFields.size() - 1);
        }

        playersNamePanel.removeAll();
        for (int i = 0; i < playerNameFields.size(); i++) {
            JTextField field = playerNameFields.get(i);
            JLabel label = new JLabel("Joueur " + (i + 1));

            JPanel panel = new JPanel();
            panel.add(label);
            panel.add(field);

            playersNamePanel.add(panel);
        }

        playersNamePanel.updateUI();
    }


    public void setVisible() {
        this.window.setVisible(true);
    }

    @Override
    public void addObserver(IObserver<ArrayList<String>> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<ArrayList<String>> o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(ArrayList<String> message) {
        for (IObserver<ArrayList<String>> observer : observers) {
            observer.update(this, message);
        }
    }
}
