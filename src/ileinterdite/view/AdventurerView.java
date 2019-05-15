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

import ileinterdite.util.Utils;


public class AdventurerView extends Observable {

    private final JPanel panelBoutons ;
    private final JPanel panelCentre ;
    private final JFrame window;
    private final JPanel panelAventurier;
    private final JPanel mainPanel;
    private final JButton btnBouger  ;
    private final JButton btnAssecher;
    private final JButton btnValiderTuile;
    private final JButton btnTerminerTour;
    private JTextField position;

    public AdventurerView(String nomJoueur, String nomAventurier, Color couleur) {

        this.window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(350, 200);
        //le titre = nom du joueur 
        window.setTitle(nomJoueur);
        mainPanel = new JPanel(new BorderLayout());
        this.window.add(mainPanel);

        mainPanel.setBackground(new Color(230, 230, 230));
        mainPanel.setBorder(BorderFactory.createLineBorder(couleur, 2)) ;

        // =================================================================================
        // NORD : le titre = nom de l'aventurier sur la couleurActive du pion

        this.panelAventurier = new JPanel();
        panelAventurier.setBackground(couleur);
        panelAventurier.add(new JLabel(nomAventurier,SwingConstants.CENTER ));
        mainPanel.add(panelAventurier, BorderLayout.NORTH);

        // =================================================================================
        // CENTRE : 1 ligne pour position courante
        this.panelCentre = new JPanel(new GridLayout(2, 1));
        this.panelCentre.setOpaque(false);
        this.panelCentre.setBorder(new MatteBorder(0, 0, 2, 0, couleur));
        mainPanel.add(this.panelCentre, BorderLayout.CENTER);

        panelCentre.add(new JLabel ("Position", SwingConstants.CENTER));
        position = new  JTextField(30);
        position.setHorizontalAlignment(CENTER);
        panelCentre.add(position);


        // =================================================================================
        // SUD : les boutons
        this.panelBoutons = new JPanel(new GridLayout(2,2));
        this.panelBoutons.setOpaque(false);
        mainPanel.add(this.panelBoutons, BorderLayout.SOUTH);

        this.btnBouger = new JButton("Bouger") ;
        btnBouger.addActionListener(e -> {
            setChanged();
            notifyObservers(Utils.Action.MOVE);
        });
        this.btnAssecher = new JButton( "Assecher");
        btnAssecher.addActionListener(e -> {
            setChanged();
            notifyObservers(Utils.Action.DRY);
        });
        this.btnValiderTuile = new JButton("Valider tuile");
        btnValiderTuile.addActionListener(e -> {
            String pos = position.getText();
            if (pos.length() == 0) {
                position.setBorder(BorderFactory.createLineBorder(Color.RED));
                position.grabFocus();
            } else {
                position.setBorder(null);
                setChanged();
                notifyObservers(pos);
                position.setText("");
            }
        });
        this.btnTerminerTour = new JButton("Terminer Tour");
        btnTerminerTour.addActionListener(e -> {
            setChanged();
            notifyObservers(Utils.Action.END_TURN);
        });

        this.panelBoutons.add(btnBouger);
        this.panelBoutons.add(btnAssecher);
        this.panelBoutons.add(btnValiderTuile);
        this.panelBoutons.add(btnTerminerTour);
    }

    public void setVisible() {
        this.window.setVisible(true);
    }

    public void setPosition(String pos) {
        this.position.setText(pos);
    }

    public JButton getBtnValiderTuile() {
        return btnValiderTuile;
    }

    public String getPosition() {
        return position.getText();
    }

    public JButton getBtnBouger() {
        return btnBouger;
    }

    public JButton getBtnAssecher() {
        return btnAssecher;
    }

    public JButton getBtnTerminerTour() {
        return btnTerminerTour;
    }
}

 

