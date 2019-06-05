package ileinterdite.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;

public class MainMenuView extends Observable {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    private final JFrame window;
    private JPanel nbPlayersPanel;
    private JTextField nbPlayersTextField;
    private JPanel playersNamePanel;
    private ArrayList<JTextField> playerNameFields;

    public MainMenuView() {
        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(350, 200);
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.window.add(mainPanel);

        nbPlayersPanel = new JPanel();
        nbPlayersTextField = new JTextField("2");
        nbPlayersTextField.setDisabledTextColor(Color.BLACK);
        nbPlayersTextField.setEnabled(false);

        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nbPlayersTextField.setText(Integer.toString(Math.max(MIN_PLAYERS, Integer.valueOf(nbPlayersTextField.getText()) - 1)));
                updatePlayerNames();
            }
        });
        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nbPlayersTextField.setText(Integer.toString(Math.min(MAX_PLAYERS, Integer.valueOf(nbPlayersTextField.getText()) + 1)));
                updatePlayerNames();
            }
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
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<String> playerNames = new ArrayList<>();
                for (JTextField field : playerNameFields) {
                    playerNames.add(field.getText());
                }

                setChanged();
                notifyObservers(playerNames);
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

    public static void main(String[] args) {
        MainMenuView v = new MainMenuView();
        v.setVisible();
    }
}
