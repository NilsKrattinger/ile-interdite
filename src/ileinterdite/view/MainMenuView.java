package ileinterdite.view;

import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.StartMessage;
import ileinterdite.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;


public class MainMenuView implements IObservable<StartMessage> {
    // We use CopyOnWriteArrayList to avoid ConcurrentModificationException if the observer unregisters while notifications are being sent
    private final CopyOnWriteArrayList<IObserver<StartMessage>> observers;

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private JFrame window;
    private JPanel playersNamePanel;
    private JTextField nbPlayersTextField;
    private ArrayList<JTextField> playerNameFields;
    private JCheckBox demoOption;
    private JCheckBox logOption;
    private JCheckBox randomOption;

    public MainMenuView() {
        this.observers = new CopyOnWriteArrayList<>();
        SwingUtilities.invokeLater(this::initView);
    }

    private void initView() {
        this.initWindow();

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel upperPanel = new JPanel();
        JPanel lowerPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel player = new JPanel(new BorderLayout());

        playersNamePanel = new JPanel();
        playersNamePanel.setLayout(new BoxLayout(playersNamePanel, BoxLayout.Y_AXIS));
        playerNameFields = new ArrayList<>();

        ////////////////OPTION PANEL///////////////////
        JPanel optionPanel = new JPanel(new GridLayout(4, 1));
        JLabel optionLabel = new JLabel("Options : ");
        logOption = new JCheckBox("Afficher les log");
        demoOption = new JCheckBox("Jouer avec la partie de démonstartion");
        randomOption = new JCheckBox("Jouer avec l'aléatoire");
        randomOption.setSelected(true);

        optionPanel.add(optionLabel);
        optionPanel.add(logOption);
        optionPanel.add(demoOption);
        optionPanel.add(randomOption);

        centerPanel.add(optionPanel, BorderLayout.EAST);
        //////////////////////////////////////////////

        ////////////////Choice Panel///////////////////

        JPanel nbPlayersPanel = new JPanel();
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

        player.add(nbPlayersPanel, BorderLayout.NORTH);
        player.add(playersNamePanel, BorderLayout.CENTER);

        centerPanel.add(player);

        //////////////////////////////////////////////

        JSlider difficulty = new JSlider(JSlider.HORIZONTAL, 1, 4, 1);

        difficulty.setMajorTickSpacing(1);
        difficulty.setPaintTicks(true);
        difficulty.setSnapToTicks(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("Novice"));
        labelTable.put(2, new JLabel("Normal"));
        labelTable.put(3, new JLabel("Elite"));
        labelTable.put(4, new JLabel("Légendaire"));
        difficulty.setLabelTable(labelTable);

        difficulty.setPaintLabels(true);

        JPanel difficultyPanel = new JPanel(new BorderLayout());
        JLabel difficultyLabel = new JLabel("Difficulté", SwingConstants.CENTER);


        difficultyPanel.add(difficultyLabel, BorderLayout.NORTH);
        difficultyPanel.add(difficulty);
        centerPanel.add(difficultyPanel, BorderLayout.SOUTH);

        //////////////////////////////////////////////

        JButton validateButton = new JButton("Démarrer la partie");
        validateButton.addActionListener(actionEvent -> {
            boolean canStart = true;

            ArrayList<String> playerNames = new ArrayList<>();
            for (JTextField field : playerNameFields) {
                String playerName = field.getText().replaceAll("^\\s+$", "");
                if (playerName.isEmpty()) {
                    canStart = false;
                }
                playerNames.add(playerName);
            }

            if (canStart) {
                StartMessage m = new StartMessage();
                m.playerName = playerNames;
                m.demoOption = demoOption.isSelected();
                m.logOption = logOption.isSelected();
                m.randomOption = randomOption.isSelected();
                m.difficulty = difficulty.getValue();
                notifyObservers(m);
                window.setVisible(false);
            } else {
                Utils.showError("Les noms de joueurs ne peuvent pas être vides !");
            }
        });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        lowerPanel.add(new JLabel(" "), c);
        c.gridy = 1;
        lowerPanel.add(validateButton, c);

        //////////////////////////////////////////////
        upperPanel.add(new JLabel("Ile Interdite"));

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);
        
        window.add(mainPanel);

        updatePlayerNames();
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
        SwingUtilities.invokeLater(() -> this.window.setVisible(true));
    }

    @Override
    public void addObserver(IObserver<StartMessage> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver<StartMessage> o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(StartMessage m) {
        for (IObserver<StartMessage> observer : observers) {
            observer.update(this, m);
        }
    }

    private void initWindow() {
        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 400);
        window.setLocationRelativeTo(null);
    }
}

