package ileinterdite.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treasure treasure = (Treasure) o;
        return name.equals(treasure.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}