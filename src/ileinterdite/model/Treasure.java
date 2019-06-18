package ileinterdite.model;

public class Treasure {
    private String nom;

    public Treasure(String nom) {
        this.setNom(nom);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public static final String[] TREASURE_NAMES = {"La Pierre sacrée", "La Statue du zéphyr", "Le Cristal ardent", "Le Calice de l'onde"};
}