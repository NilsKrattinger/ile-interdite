package ileinterdite.model;

public class Treasure {
    private String name;

    public Treasure(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final String[] TREASURE_NAMES = {"La Pierre sacrée", "La Statue du zéphyr", "Le Cristal ardent", "Le Calice de l'onde"};
}