package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.model.Hand;
import ileinterdite.model.Treasure;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Pawn;
import ileinterdite.model.Card;

public abstract class Adventurer {

	Grid grid;
	private Hand hand;
	String name;
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
    }

    /**
     * Gets the pawn of the current adventurer, for the bg color and text color
     * @return
     */
    public abstract Pawn getPawn();

	/**
	 * Methode qui retourne un tableau avec les case  accessible par l'aventurier
	 * @return State[][] avec une marque accesible ou non
	 */
	public Utils.State[][] getAccessibleCells() {
        Utils.State[][] cellsState = grid.getStateOfCells();
		cellChoiceMoving(cellsState);

		return cellsState;
	}

	/**
	 * Methode qui retourne un tableau avec les case assechables par l'aventurier
	 * @return State[][] avec une marque assechable ou non
	 */
	public Utils.State[][] getDryableCells() {
        Utils.State[][] cellsState = grid.getStateOfCells();
		cellChoiceDrying(cellsState);

		return cellsState;
	}

	/**
	 * Deplace l'aventurier sur la nouvelle case et le supprime de son ancien emplacement
	 * @param newX int
	 * @param newY int
	 */
	public void move(int newX, int newY) {
		int currX = this.getX();
		int currY = this.getY();
		grid.move(newX,newY,currX,currY,this);
		this.x = newX;
		this.y = newY;
	}

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'aventurier
     * @param tab
     */
    public void cellChoiceMoving(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL)
                        && (this.getY() == j && (this.getX() == i-1
                        || this.getX() == i+1) || this.getX() == i
                        && (this.getY() == j-1 || this.getY() == j+1))) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est assechable ou non par l'aventurier
     * @param tab
     */
    public void cellChoiceDrying(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if ((state == Utils.State.FLOODED)
                        && ((this.getY() == j && (this.getX() >= i-1 && this.getX() <= i+1))
                        || (this.getX() == i && (this.getY() >= j-1 && this.getY() <= j+1)))) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    public void newTurn(){

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public int getNumberOfCards() {
        return this.getHand().getCards().size();
    }
}