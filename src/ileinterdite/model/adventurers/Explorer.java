package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

public class Explorer extends Adventurer {

    public Explorer() {
        super();
    }

    public Explorer(int x, int y) {
        super(x, y);
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'explorateur
     * @param tab
     */
    @Override
    public void cellChoiceMoving(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL)
                        && this.getX() >= i-1 && this.getX() <= i+1
                        && this.getY() >= j-1 && this.getY() <= j+1
                        && (this.getX() != i || this.getY() != j)) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est assechable ou non par l'explorateur
     * @param tab
     */
    @Override
    public void cellChoiceDrying(Utils.State[][] tab) {
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                Utils.State state = tab[j][i];
                if (state == Utils.State.FLOODED
                        && this.getX() >= i-1 && this.getX() <= i+1
                        && this.getY() >= j-1 && this.getY() <= j+1) {
                    tab[j][i] = Utils.State.ACCESSIBLE;
                } else {
                    tab[j][i] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

}