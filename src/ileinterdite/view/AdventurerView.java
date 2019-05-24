package ileinterdite.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;

import javax.swing.border.MatteBorder;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.Message;
import ileinterdite.util.Parameters;
import ileinterdite.util.Utils;


public class AdventurerView extends Observable {

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
            setChanged();
            notifyObservers(new Message(Utils.Action.MOVE));
        });
        this.dryButton = new JButton( "Assecher");
        dryButton.addActionListener(e -> {
            setChanged();
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
                setChanged();
                notifyObservers(new Message(Utils.Action.VALIDATE_ACTION, pos));
                position.setText("");
            }
        });
        this.endTurnButton = new JButton("Terminer Tour");
        endTurnButton.addActionListener(e -> {
            setChanged();
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
        advName.setText(adventurerName);
    }

    /**
     * Shows which cells are selectable
     * @param states The list of cells with states either ACCESSIBLE or INACCESSIBLE
     */
    public void showSelectableCells(Utils.State[][] states) {
        System.out.println("Ces tuiles sont accessibles :");
        for (int j = 0; j < states.length; j++) {
            for (int i = 0; i < states[j].length; i++) {
                if (states[i][j] == Utils.State.ACCESSIBLE) {
                    System.out.print("(" + (i + 1) + "," + (j + 1) + ") ");
                }
            }
        }

        System.out.println();
    }

    /**
     * Update the position of an adventurer
     * @param adv The adventurer to update
     */
    public void updateAdventurer(Adventurer adv) {
        if (Parameters.LOGS) {
            System.out.println("Adventurer moved to (" + (adv.getX() + 1) + ',' + (adv.getY() + 1) + ")");
        }
    }

    public void updateDriedCell(int x, int y) {
        if (Parameters.LOGS) {
            System.out.println("Cell at (" + x + ',' + y + ") is now dry.");
        }
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
}

 

