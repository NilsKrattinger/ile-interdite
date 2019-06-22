package ileinterdite.model.adventurers;

import ileinterdite.model.*;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;

import java.util.ArrayList;

public abstract class Adventurer {

    private Grid grid;
    private Hand hand;
    private String name;
    private int x;
    private int y;

    public Adventurer() {
        this(0, 0);
    }

    public Adventurer(Grid grid) {
        this(grid, 0, 0);
    }

    public Adventurer(int x, int y) {
        this(null, x, y);
    }

    public Adventurer(Grid grid, int x, int y) {
        this.grid = grid;
        this.x = x;
        this.y = y;
        this.hand = new Hand();
    }

    /**
     * Gets the pawn of the current adventurer, for the bg color and text color
     */
    public abstract Pawn getPawn();

    /**
     * Methode qui retourne un tableau avec les case  accessible par l'aventurier
     *
     * @return State[][] avec une marque accesible ou non
     */
    public Utils.State[][] getAccessibleCells() {
        Utils.State[][] cellsState = grid.getStateOfCells();
        cellChoiceMoving(cellsState);

        return cellsState;
    }

    /**
     * Methode qui retourne un tableau avec les case  accessible par l'aventurier déplacer par le navigateur
     *
     * @return State[][] avec une marque accesible ou non
     */
    public Utils.State[][] getPowerNavigatorAccessibleCells() {
        Utils.State[][] cellsState = grid.getStateOfCells();
        cellPowerNavigatorChoiceMoving(cellsState, x, y, 2);

        for (int j = 0; j < cellsState.length; j++) {
            for (int i = 0; i < cellsState[j].length; i++) {
                if (cellsState[j][i] != Utils.State.ACCESSIBLE) {
                    cellsState[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }

        return cellsState;
    }

    /**
     * Methode qui retourne un tableau avec les case assechables par l'aventurier
     *
     * @return State[][] avec une marque assechable ou non
     */
    public Utils.State[][] getDryableCells() {
        Utils.State[][] cellsState = grid.getStateOfCells();
        cellChoiceDrying(cellsState);

        return cellsState;
    }

    /**
     * Deplace l'aventurier sur la nouvelle case et le supprime de son ancien emplacement
     *
     * @param newX int
     * @param newY int
     */
    public void move(int newX, int newY) {
        int currX = this.getX();
        int currY = this.getY();
        grid.move(newX, newY, currX, currY, this);
        this.x = newX;
        this.y = newY;
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'aventurier
     */
    public void cellChoiceMoving(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL)
                        && (this.getY() == j && (this.getX() == i - 1
                        || this.getX() == i + 1) || this.getX() == i
                        && (this.getY() == j - 1 || this.getY() == j + 1))) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile,
     * si elle est accessible ou non par l'aventurier déplacé par le Navigateur
     */
    private void cellPowerNavigatorChoiceMoving(Utils.State[][] tab, int x, int y, int distance) {
        if (x < 0 || y < 0 || x >= Grid.WIDTH || y >= Grid.HEIGHT) {
            return;
        }

        Utils.State currState = tab[y][x];
        if (currState == Utils.State.NORMAL || currState == Utils.State.FLOODED) {
            if ((x != this.getX() || y != this.getY())) {
                distance--;
                tab[y][x] = Utils.State.ACCESSIBLE;
            } else {
                tab[y][x] = Utils.State.INACCESSIBLE;
            }

            if (distance > 0) {
                cellPowerNavigatorChoiceMoving(tab, x - 1, y, distance);
                cellPowerNavigatorChoiceMoving(tab, x + 1, y, distance);
                cellPowerNavigatorChoiceMoving(tab, x, y - 1, distance);
                cellPowerNavigatorChoiceMoving(tab, x, y + 1, distance);
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est assechable ou non par l'aventurier
     */
    public void cellChoiceDrying(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if ((state == Utils.State.FLOODED)
                        && ((this.getY() == j && (this.getX() >= i - 1 && this.getX() <= i + 1))
                        || (this.getX() == i && (this.getY() >= j - 1 && this.getY() <= j + 1)))) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    public void newTurn() {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        grid.move(x, y, 0, 0, this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getClassName();

    public Hand getHand() {
        return hand;
    }

    public ArrayList<Card> getCards() {
        return this.getHand().getCards();
    }

    public int getNumberOfCards() {
        return this.getHand().getCards().size();
    }

    public Utils.State[][] getRescueCells() {
        return getAccessibleCells();
    }

    /**
     * Vérifie que l'aventurier dispose d'au moins 4 cartes du même du trésor et qu'il est bien sur la tuile associée
     * au trésor dont il a les 4 cartes
     *
     * @return le trésor qu'il peut récupérer ou null s'il ne peut en récupérer aucun (moins de 4 cartes, pas 4 cartes
     * du même trésor ou pas sur la tuile associée à ses 4 cartes
     */
    public Treasure getAvailableTreasure() {
        if (this.getNumberOfCards() < 4) {
            return null;
        } else {
            int nbTreasureCards;
            for (String treasureName : Treasure.TREASURE_NAMES) {
                nbTreasureCards = 0;
                for (Card card : this.getCards()) {
                    if (card.getCardName().equals(treasureName)) {
                        nbTreasureCards++;
                    }
                }
                if (nbTreasureCards >= 4 && grid.getCell(this.getX(), this.getY()) instanceof TreasureCell) {
                    TreasureCell adventurerCell = (TreasureCell) this.grid.getCell(this.getX(), this.getY());
                    if (adventurerCell.getTreasure().getName().equalsIgnoreCase(treasureName)) {
                        return grid.getTreasure(treasureName);
                    }
                }
            }
            return null;
        }
    }
}